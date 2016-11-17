package player.andre.awareplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.view.KeyEvent;

/**
 * Created by andre on 25/08/16.
 */
public class SpotifyStarter implements SpotifyBroadcastReceiver.Listener {

    public static final String PLAYBACK_RECEIVER = "com.spotify.music.playbackstatechanged";
    public static final String METADATA_RECEIVER = "com.spotify.music.metadatachanged";
    public static final String BR_PLAYLIST = "spotify:user:aaascp:playlist:6BYtpWiHFxXvzMFB6GzQQI";

    private Context context;
    private SpotifyBroadcastReceiver receiver;


    public SpotifyStarter(Context context) {
        this.context = context;
        setupReceiver();
        start();
    }

    private void setupReceiver() {
        receiver = new SpotifyBroadcastReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(SpotifyStarter.METADATA_RECEIVER);
        filter.addAction(SpotifyStarter.PLAYBACK_RECEIVER);
        context.registerReceiver(receiver, filter);
    }

    private void start() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(BR_PLAYLIST));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onOpen() {
        context.sendOrderedBroadcast(
                intentPlay(KeyEvent.ACTION_DOWN),
                null);

        context.sendOrderedBroadcast(
                intentPlay(KeyEvent.ACTION_UP),
                null);
    }

    public static Intent intentPlay(int action) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        intent.setComponent(getButtonReceiver());
        intent.putExtra(
                Intent.EXTRA_KEY_EVENT,
                new KeyEvent(
                        action,
                        KeyEvent.KEYCODE_MEDIA_PLAY));

        return intent;
    }

    private static ComponentName getButtonReceiver() {
        return new ComponentName(
                "com.spotify.music",
                "com.spotify.music.internal.receiver.MediaButtonReceiver");
    }

    @Override
    public void onStarted() {
        context.unregisterReceiver(receiver);
    }
}
