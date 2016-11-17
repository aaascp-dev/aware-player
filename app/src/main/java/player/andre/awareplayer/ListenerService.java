package player.andre.awareplayer;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.HeadphoneFence;
import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by andre on 25/08/16.
 */

public class ListenerService extends Service {

    public static final String STATUS = "LISTENER_SERVICE_STATUS";
    public static final String RUNNING_STATUS = "RUNNING_STATUS";
    private PendingIntent pendingIntent;
    private FenceBroadcastReceiver fenceReceiver;
    private GoogleApiClient googleApiClient;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        setRunningStatus(true);
        setupFenceReceiver();
        return START_STICKY;
    }

    private void setupFenceReceiver() {

        Intent intent = new Intent(FenceBroadcastReceiver.FENCE_RECEIVER_ACTION);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        fenceReceiver = new FenceBroadcastReceiver();
        registerReceiver(
                fenceReceiver,
                new IntentFilter(FenceBroadcastReceiver.FENCE_RECEIVER_ACTION));

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .build();
        googleApiClient.connect();

        registerHeadphoneFence();
    }

    private void registerHeadphoneFence() {
        AwarenessFence headphoneFence = HeadphoneFence.during(HeadphoneState.PLUGGED_IN);
        Awareness.FenceApi.updateFences(
                googleApiClient,
                new FenceUpdateRequest.Builder()
                        .addFence(FenceBroadcastReceiver.FENCE_RECEIVER_ACTION,
                                headphoneFence,
                                pendingIntent)
                        .build());
    }

    private void unregisterHeadphoneFence() {
        Awareness.FenceApi.updateFences(
                googleApiClient,
                new FenceUpdateRequest.Builder()
                        .removeFence(FenceBroadcastReceiver.FENCE_RECEIVER_ACTION)
                        .build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setRunningStatus(false);
        unregisterHeadphoneFence();
        unregisterReceiver(fenceReceiver);
    }

    private void setRunningStatus(boolean status) {
        SharedPreferences settings = getSharedPreferences(STATUS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(RUNNING_STATUS, status);
        editor.commit();
    }
}
