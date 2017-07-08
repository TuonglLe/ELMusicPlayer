package example.com.elmusicplayer.Utils;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;


public final class SongDurationConverter {
    private SongDurationConverter(){

    }
    private static final DecimalFormat decimalFormat = new DecimalFormat("#00");
    public static String toString(int duration){
        if(duration <= 0){
            return "00 : 00";
        }

        long songMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);

        long songSenconds = TimeUnit.MILLISECONDS.toSeconds(
                duration - TimeUnit.MINUTES.toMillis(songMinutes)
        );
        return decimalFormat.format(songMinutes) + " : " + decimalFormat.format(songSenconds);
    }

}
