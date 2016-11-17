package player.andre.awareplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.android.gms.awareness.fence.FenceState;

/**
 * Created by andre on 25/08/16.
 */
public class FenceBroadcastReceiver extends BroadcastReceiver {

    public static final String FENCE_RECEIVER_ACTION = "FENCE_RECEIVER_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
        FenceState fenceState = FenceState.extract(intent);
        if (TextUtils.equals(fenceState.getFenceKey(), FENCE_RECEIVER_ACTION)) {
            if (fenceState.getCurrentState() == FenceState.TRUE) {
                new SpotifyStarter(context);
            }
        }
    }
}

