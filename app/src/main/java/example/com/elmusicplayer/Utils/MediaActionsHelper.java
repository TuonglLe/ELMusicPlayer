package example.com.elmusicplayer.Utils;

import android.content.Intent;


public final class MediaActionsHelper {
    private MediaActionsHelper(){
    }

    public static Intent createMediaActionIntent(String mediaAction){
        Intent mediaActionIntent = new Intent(mediaAction);
        return mediaActionIntent;
    }
}
