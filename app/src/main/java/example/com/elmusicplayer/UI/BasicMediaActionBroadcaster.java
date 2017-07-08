package example.com.elmusicplayer.UI;

import android.content.Context;
import android.content.Intent;

import example.com.elmusicplayer.SongKits.Song;
import example.com.elmusicplayer.Utils.MediaConstants;


public class BasicMediaActionBroadcaster implements BasicMediaActions {
    private Context context;

    public BasicMediaActionBroadcaster(Context context) {
        this.context = context;
    }

    @Override
    public void playNewSong(Song newSOng, int newSongIndex) {

    }

    @Override
    public void replayCurrentSong() {
        Intent intent = new Intent(MediaConstants.ACTION_PLAY_CURRENT_SONG);
        context.sendBroadcast(intent);
    }

    @Override
    public void pause() {
        Intent actionIntent = new Intent(MediaConstants.ACTION_PAUSED);
        context.sendBroadcast(actionIntent);
    }

    @Override
    public void stop() {
        Intent actionIntent = new Intent(MediaConstants.ACTION_STOPPED);
        context.sendBroadcast(actionIntent);
    }

    @Override
    public void skipToNext() {
        Intent actionIntent = new Intent(MediaConstants.ACTION_SKIP_TO_NEXT);
        context.sendBroadcast(actionIntent);
    }

    @Override
    public void skipToPrev() {
        Intent actionIntent = new Intent(MediaConstants.ACTION_SKIP_TO_PREV);
        context.sendBroadcast(actionIntent);
    }

    @Override
    public void setLoopMode(int loopMode) {
        Intent actionIntent = new Intent(MediaConstants.ACTION_LOOP_MODE);
        actionIntent.putExtra(MediaConstants.MEDIA_LOOP_MODE_KEY, loopMode);
        context.sendBroadcast(actionIntent);
    }

    @Override
    public void startNotifyPlayback() {
        Intent intent = new Intent(MediaConstants.ACTION_START_NOTIFY_PLAYBACK);
        context.sendBroadcast(intent);
    }

    @Override
    public void stopNotifyPlayback() {
        Intent intent = new Intent(MediaConstants.ACTION_STOP_NOTIFY_PLAYBACK);
        context.sendBroadcast(intent);
    }

    @Override
    public void updateCurrentPosition(int position) {
        Intent songPositionIntent = new Intent(MediaConstants.ACTION_UPDATE_SONG_POSITION);
        songPositionIntent.putExtra(MediaConstants.SONG_POSITION_KEY, position);
        context.sendBroadcast(songPositionIntent);
    }


    @Override
    public void resume() {
        Intent intent = new Intent();
        intent.setAction(MediaConstants.ACTION_RESUMED);
        context.sendBroadcast(intent);
    }

}
