package example.com.elmusicplayer.MusicService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.session.IMediaControllerCallback;
import android.util.Log;

import example.com.elmusicplayer.ObservablePattern.PlaybackKits.PlaybackModePublisher;
import example.com.elmusicplayer.SongKits.Song;
import example.com.elmusicplayer.Utils.Constants;
import example.com.elmusicplayer.Utils.MediaConstants;
import example.com.elmusicplayer.Utils.MessageConstants;



public class MediaActionReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = MediaActionReceiver.class.getSimpleName();
    private MusicService musicService;

    public MediaActionReceiver(MusicService musicService) {
        Log.d(LOG_TAG, "MediaReceiver is created");
        this.musicService = musicService;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String mediaAction = intent.getAction();
        Log.d(LOG_TAG, mediaAction);

        if(mediaAction.equals(MediaConstants.ACTION_PLAY_FROM_MEDIA_ID)) {
            Bundle bundle = intent.getExtras();
            Song chosenSong = (Song) bundle.getSerializable(MediaConstants.SONG_KEY);
            int chosenSOngIndex = bundle.getInt(MediaConstants.MEDIA_INDEX_KEY, -1);
            musicService.getPlayback().playNewSong(chosenSong, chosenSOngIndex);
        }else if(mediaAction.equals(MediaConstants.ACTION_PLAY_CURRENT_SONG)){
            musicService.getPlayback().replayCurrentSong();
        }else if(mediaAction.equals(MediaConstants.ACTION_PAUSED)){
            musicService.getPlayback().pause();
        }else if(mediaAction.equals(MediaConstants.ACTION_RESUMED)){
            musicService.getPlayback().resume();
        }else if(mediaAction.equals(MediaConstants.ACTION_STOPPED)){
            musicService.getPlayback().stop();
        }else if(mediaAction.equals(MediaConstants.ACTION_SKIP_TO_NEXT)){
            musicService.getPlayback().skipToNext();
        }else if(mediaAction.equals(MediaConstants.ACTION_SKIP_TO_PREV)){
            musicService.getPlayback().skipToPrev();
        }else if(mediaAction.equals(MediaConstants.ACTION_LOOP_MODE)){
            int loopMode = intent.getIntExtra(MediaConstants.MEDIA_LOOP_MODE_KEY, MediaConstants.LOOP_MODE_NONE);
            musicService.getSongsTracker().setCurrentLoopMode(loopMode);
        }else if(mediaAction.equals(MediaConstants.ACTION_NOTIFY_CURRENT_SONG)){
            musicService.getSongsTracker().notifyCurrentSong();
        }else if(mediaAction.equals(MediaConstants.ACTION_UPDATE_SONG_POSITION)){
            int songPosition = intent.getIntExtra(MediaConstants.SONG_POSITION_KEY, musicService.getPlayback().getMediaPlayer().getCurrentPosition());
            musicService.getPlayback().updateCurrentPosition(songPosition);
        }else if(mediaAction == MediaConstants.ACTION_START_NOTIFY_PLAYBACK) {
            musicService.getPlayback().startNotifyPlayback();
        }else if(mediaAction == MediaConstants.ACTION_STOP_NOTIFY_PLAYBACK) {
            musicService.getPlayback().stopNotifyPlayback();
        }else if(mediaAction == MediaConstants.ACTION_REASKED_CURRENT_MODE){
            PlaybackModePublisher.getInstance().notifyPlaybackModeObservers(musicService.getPlayback().getCurrentMode());
        }else {

        }

    }
}
