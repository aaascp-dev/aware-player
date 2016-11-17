package player.andre.awareplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * Created by andre on 25/08/16.
 */

public class SpotifyBroadcastReceiver extends BroadcastReceiver {

    private static final String SPOTIFY_PACKAGE = "com.spotify.music";
    private static final String PLAYBACK_STATE_CHANGED = SPOTIFY_PACKAGE + ".playbackstatechanged";
    private static final String METADATA_CHANGED = SPOTIFY_PACKAGE + ".metadatachanged";
    private Listener listener;

    public SpotifyBroadcastReceiver(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(METADATA_CHANGED)) {
            listener.onOpen();
        } else if (action.equals(PLAYBACK_STATE_CHANGED)) {
            boolean playing = intent.getBooleanExtra("playing", false);
            if (playing) {
                listener.onStarted();
            }
        }

    }



    public interface Listener {
        void onOpen();

        void onStarted();
    }
}
