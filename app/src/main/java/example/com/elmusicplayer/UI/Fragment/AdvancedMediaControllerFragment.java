package example.com.elmusicplayer.UI.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import example.com.elmusicplayer.MusicService.Playback;
import example.com.elmusicplayer.R;
import example.com.elmusicplayer.SongKits.Song;
import example.com.elmusicplayer.Utils.MediaConstants;
import example.com.elmusicplayer.Utils.SongDurationConverter;



public abstract class AdvancedMediaControllerFragment extends BasicMediaControllerFragment implements Runnable{
    protected final SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

                @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(currentPlaybackMode == Playback.MODE_ERROR || currentPlaybackMode== Playback.MODE_STOPPING
                        || currentPlaybackMode == Playback.MODE_PREPARING_NEW_SONG){
                    return;
                }
                setCurrentPosition(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            if(currentPlaybackMode == Playback.MODE_ERROR || currentPlaybackMode== Playback.MODE_STOPPING
                    || currentPlaybackMode == Playback.MODE_PREPARING_NEW_SONG){
                setCurrentPosition(0);
            }
            userIsTouchingSongSLider = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if(currentPlaybackMode == Playback.MODE_ERROR || currentPlaybackMode== Playback.MODE_STOPPING
                    || currentPlaybackMode == Playback.MODE_PREPARING_NEW_SONG){
                setCurrentPosition(0);
                userIsTouchingSongSLider = false;
            }else{
                int currentSOngSliderPosition = seekBar.getProgress();
                getBasicMediaActionBroadcaster().updateCurrentPosition(currentSOngSliderPosition);
            }
        }
    };

    protected final PlaybackCurrentPositionRecevier playbackBroadcastReceiver = new PlaybackCurrentPositionRecevier();
    protected final IntentFilter playbackCurrentPosIntentFilter = new IntentFilter(MediaConstants.ACTION_HANDLE_PLAYBACK_CURRENT_POSITION);

    protected SeekBar songSlider;
    protected ProgressBar progressBar;

    protected final PlaybackUPdateCurrentPosCOmpleteRecevier updateCurrentPosCOmpleteRecevier = new PlaybackUPdateCurrentPosCOmpleteRecevier();
    protected  final IntentFilter updateCurrentPosComPleteIntentFilter = new IntentFilter(MediaConstants.ACTION_UPDATE_PLAYBACK_CURRENT_POSITION_COMPLETE);

    protected TextView songCurrentPositionView, songDurationOrReverseView;
    protected boolean isReverse;

    protected Handler mainLoopHandler;
    protected int currentPosition;
    protected boolean userIsTouchingSongSLider = false;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void setUpAdviceMediaControlsViews() {
        songCurrentPositionView = (TextView) rootView.findViewById(R.id.song_currentt_position);
        songDurationOrReverseView = (TextView) rootView.findViewById(R.id.song_duration_or_reverse);
        songDurationOrReverseView.setOnClickListener(this);
        songSlider = (SeekBar) rootView.findViewById(R.id.song_slider);
        songSlider.setOnSeekBarChangeListener(seekBarChangeListener);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);

        mainLoopHandler = new Handler(getContext().getMainLooper());

    }

    @Override
    public void run() {
        songCurrentPositionView.setText(SongDurationConverter.toString(currentPosition));
        songSlider.setProgress(currentPosition);

        if(isReverse){
            songDurationOrReverseView.setText(
                   "- " + SongDurationConverter.toString(songSlider.getMax()  - songSlider.getProgress())
            );
        }else{
            songDurationOrReverseView.setText(
                    SongDurationConverter.toString(songSlider.getMax())
            );
        }
    }

    @Override
    public void receivingCurrentSongAndIndexStatus(Song currentSong, int currentSOngIndex) {
        super.receivingCurrentSongAndIndexStatus(currentSong, currentSOngIndex);
        if(currentSong != null){
            songCurrentPositionView.setText("00 : 00");
            songSlider.setProgress(0);
            songSlider.setMax((int) (currentSong.getDuration()));
        }
    }

    @Override
    public void receviePlaybackMode(int playbackMode) {
        super.receviePlaybackMode(playbackMode);
        handleCurrentPostion();
        handleProgressBar();
    }

    private void handleProgressBar(){
        Log.d(LOG_TAG, currentPlaybackMode == Playback.MODE_BUFFERING? "BUFFERIGN" : "NOT ");
        if(allViewsAreInitialized){
            progressBar.setVisibility(
                    currentPlaybackMode == Playback.MODE_BUFFERING || currentPlaybackMode == Playback.MODE_PREPARING_NEW_SONG? View.VISIBLE: View.GONE
            );
        }
    }

    private void handleCurrentPostion(){
        if(currentPlaybackMode == Playback.MODE_PREPARING_NEW_SONG
                || currentPlaybackMode == Playback.MODE_STOPPING){
            if(allViewsAreInitialized){
                Log.d(LOG_TAG, "allViewsAreInitialized");
                setCurrentPosition(0);
            }else{
                Log.d(LOG_TAG, "NOT allViewsAreInitialized");
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        getContext().registerReceiver(playbackBroadcastReceiver, playbackCurrentPosIntentFilter);
        getContext().registerReceiver(updateCurrentPosCOmpleteRecevier, updateCurrentPosComPleteIntentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(playbackBroadcastReceiver);
        getContext().unregisterReceiver(updateCurrentPosCOmpleteRecevier);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v.getId() == R.id.song_duration_or_reverse){
            isReverse = !isReverse;
        }
    }

    private class PlaybackCurrentPositionRecevier extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(allViewsAreInitialized){
                if(!userIsTouchingSongSLider){
                    int receviedCurrentPosition = intent.getIntExtra(MediaConstants.PLAYBACK_CURRENT_POSITION_KEY, 0);
                    setCurrentPosition(receviedCurrentPosition);
                }
            }
        }
    }

    private class PlaybackUPdateCurrentPosCOmpleteRecevier extends  BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            userIsTouchingSongSLider = false;
        }
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        mainLoopHandler.post(this);
    }

}
