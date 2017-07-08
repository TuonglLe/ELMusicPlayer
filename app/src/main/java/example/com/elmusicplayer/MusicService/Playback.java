package example.com.elmusicplayer.MusicService;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;
import android.util.Log;

import java.io.IOException;

import example.com.elmusicplayer.ObservablePattern.PlaybackKits.PlaybackModePublisher;
import example.com.elmusicplayer.SongKits.RemoteSongsFinder;
import example.com.elmusicplayer.SongKits.Song;
import example.com.elmusicplayer.UI.BasicMediaActions;
import example.com.elmusicplayer.Utils.MediaConstants;



public class Playback implements AudioManager.OnAudioFocusChangeListener,
        BasicMediaActions{

    private static final String LOG_TAG = Playback.class.getSimpleName();
    private static final float VOLUME_CAN_DUCK = 0.2f;
    private static final float VOLUME_FULL = 1f;

    public static  final int MODE_PREPARING_NEW_SONG = 134;
    public static  final int MODE_BUFFERING = 135;
    public static  final int MODE_ERROR = 136;
    public static  final int MODE_PLAYING = 137;
    public static  final int MODE_STOPPING = 138;
    public static  final int MODE_PAUSING = 139;

    private int currentMode = MODE_STOPPING;
    private MusicService musicService;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    private boolean isAskedForNotifying = true;

    private final MediaPlaybackhandler mediaPlaybackhandler = new MediaPlaybackhandler();

    public Playback(MusicService musicService) {
        synchronized (this){
        }
        this.musicService = musicService;
        audioManager = (AudioManager) musicService.getSystemService(Context.AUDIO_SERVICE);

    }


    public void replayCurrentSong(){
        Log.d(LOG_TAG, "replay current song");
        Song currentSong = musicService.getSongsTracker().getCurrentSong();
        if(currentSong == null){
            Log.d(LOG_TAG, "currentSong == null");
            return;
        }
        Log.d(LOG_TAG, currentSong.getTitle());
        relaxResource();
        setUpMediaPlayer(musicService.getSongsTracker().getCurrentSong().getPath());

    }

    @Override
    public void playNewSong(Song chosenSong, int chosenSongIndex){
        Log.d(LOG_TAG, "playFromMediaID");
        Log.d(LOG_TAG, chosenSong == null? "chosenSong == null": chosenSong.getTitle());
        Log.d(LOG_TAG, chosenSongIndex + "");

        musicService.startService(new Intent(musicService, MusicService.class));
        musicService.getSongsTracker().setCurrentSongAndIndex(chosenSong, chosenSongIndex);

        relaxResource();
        setUpMediaPlayer(
                musicService.getSongsTracker().getCurrentSong().getPath()
        );
        Log.d(LOG_TAG, RemoteSongsFinder.BASE_URL +  musicService.getSongsTracker().getCurrentSong().getPath());

    }

    @Override
    public void pause(){
        Log.d(LOG_TAG, "pause");
        if(isPlaying()){
            mediaPlayer.pause();
            setCurrentMode(MODE_PAUSING);
        }
    }

    public void resume(){
        Log.d(LOG_TAG, "resume");
        if(!isPlaying()){
            mediaPlayer.start();
            setCurrentMode(MODE_PLAYING);
        }
    }

    @Override
    public void stop(){
        Log.d(LOG_TAG, "stop");
        relaxResource();
        musicService.stopSelf();
    }

    @Override
    public void updateCurrentPosition(int position) {
        Log.d(LOG_TAG, "updateCurrentPosition");
        if(currentMode == MODE_PLAYING || currentMode == MODE_PAUSING){
            mediaPlayer.seekTo(position);
        }
        Intent intent = new Intent(MediaConstants.ACTION_UPDATE_PLAYBACK_CURRENT_POSITION_COMPLETE);
        musicService.sendBroadcast(intent);
    }



    @Override
    public void startNotifyPlayback() {
//        excuService.execute(this);
    }


    @Override
    public void stopNotifyPlayback() {
//        excuService.shutdownNow();
    }

    @Override
    public void skipToNext() {
        skipTo(1);
    }

    @Override
    public void skipToPrev() {
        skipTo(-1);
    }

    @Override
    public void setLoopMode(int loopMode) {
    }

    public void skipTo(int amount){
        Log.d(LOG_TAG, "skipTo " + amount);
        int nextSOngIndex = amount + musicService.getSongsTracker().getCurrentSongIndex();
        if(isValidIndex(nextSOngIndex)){
            playNewSong(
                    musicService.getSongsTracker().getCurrentSongList().get(nextSOngIndex),
                    nextSOngIndex
            );
        }else{
            if(musicService.getSongsTracker().getCurrentLoopMode() == MediaConstants.LOOP_MODE_ALL){
                nextSOngIndex = 0;
                playNewSong(
                        musicService.getSongsTracker().getCurrentSongList().get(nextSOngIndex),
                        nextSOngIndex
                );
            }else{
                stop();
            }
        }
    }

    private void relaxResource(){
        setCurrentMode(MODE_STOPPING);
        audioManager.abandonAudioFocus(this);
        if(mediaPlayer != null){
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void setUpMediaPlayer(String songPath){
        audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setWakeMode(musicService, PowerManager.PARTIAL_WAKE_LOCK);
            mediaPlayer.setOnCompletionListener(mediaPlaybackhandler);
            mediaPlayer.setOnBufferingUpdateListener(mediaPlaybackhandler);
            mediaPlayer.setOnErrorListener(mediaPlaybackhandler);
            mediaPlayer.setOnPreparedListener(mediaPlaybackhandler);
            try{
                if(songPath.startsWith("http")){
                    Log.d(LOG_TAG, "http");
                    mediaPlayer.setDataSource(songPath);
                }else{
                    mediaPlayer.setDataSource(musicService, Uri.parse(songPath));
                }
                mediaPlayer.prepareAsync();
                setCurrentMode(MODE_PREPARING_NEW_SONG);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private boolean isValidIndex(int songIndex){
        if(musicService.getSongsTracker().getCurrentSongList() == null){
            return  false;
        }
        return songIndex >= 0 && songIndex < musicService.getSongsTracker().getCurrentSongList().size();
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange){
            case AudioManager.AUDIOFOCUS_LOSS:
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                if(isPlaying()){
                    pause();
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.setVolume(VOLUME_CAN_DUCK, VOLUME_CAN_DUCK);
                }
                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                mediaPlayer.setVolume(VOLUME_FULL, VOLUME_FULL);
                if(!isPlaying()){
                    resume();
                }
                break;
            default:
                break;
        }
    }


    public boolean isPlaying(){
        return currentMode == MODE_PLAYING;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public int getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(int currentMode) {
        this.currentMode = currentMode;
        PlaybackModePublisher.getInstance().notifyPlaybackModeObservers(currentMode);
    }

    public MusicService getMusicService() {
        return musicService;
    }


    /**
     * @MediaPlaybackHandler
     */
     private class MediaPlaybackhandler implements MediaPlayer.OnErrorListener,
            MediaPlayer.OnBufferingUpdateListener,
            MediaPlayer.OnPreparedListener,
            MediaPlayer.OnCompletionListener{

        @Override
        public void onCompletion(MediaPlayer mp) {
            int loopMode = musicService.getSongsTracker().getCurrentLoopMode();
            switch (loopMode){
                case MediaConstants.LOOP_MODE_ONE:
                    replayCurrentSong();
                    break;
                case MediaConstants.LOOP_MODE_ALL:
                    skipTo(1);
                    break;
                default:
                    stop();
                    break;
            }
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
            if(musicService.getSongsTracker().getCurrentSong().isGoogleSong()){
                setCurrentMode(MODE_BUFFERING);
            }else{
                setCurrentMode(MODE_PLAYING);
            }
        }
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            Log.d(LOG_TAG, percent + "");
            if(mediaPlayer.isPlaying()){
                setCurrentMode(MODE_PLAYING);
            }else{
                setCurrentMode(MODE_PAUSING);
            }
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.e(LOG_TAG, String.format("Error(%s%s)", what, extra));
            relaxResource();
            setCurrentMode(MODE_ERROR);
            return true;
        }
    }
}
