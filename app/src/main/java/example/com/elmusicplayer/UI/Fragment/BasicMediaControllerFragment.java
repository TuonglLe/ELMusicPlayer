package example.com.elmusicplayer.UI.Fragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import example.com.elmusicplayer.MusicService.Playback;
import example.com.elmusicplayer.ObservablePattern.PlaybackKits.PlaybackModeObserver;
import example.com.elmusicplayer.ObservablePattern.PlaybackKits.PlaybackModePublisher;
import example.com.elmusicplayer.ObservablePattern.SongTrackerKits.CurrentSongAndIndexObserver;
import example.com.elmusicplayer.ObservablePattern.SongTrackerKits.SongsTrackerPublisher;
import example.com.elmusicplayer.R;
import example.com.elmusicplayer.SongKits.RemoteSongsFinder;
import example.com.elmusicplayer.SongKits.Song;
import example.com.elmusicplayer.UI.BaseActivity;
import example.com.elmusicplayer.UI.BasicMediaActionBroadcaster;
import example.com.elmusicplayer.Utils.MediaConstants;


public abstract class BasicMediaControllerFragment extends BaseFragment implements
        PlaybackModeObserver,
        CurrentSongAndIndexObserver,
        View.OnClickListener{

    protected   final String LOG_TAG ;

    protected TextView songTitleView,
            songArtistView;
    protected ImageView songImageView;

    protected ImageButton playPauseButton;

    protected int currentPlaybackMode = -1;

    protected boolean canRotateSongImage;

    protected  Animator songImaAnimator;


    public BasicMediaControllerFragment(){
        LOG_TAG = this.getClass().getSimpleName();
        PlaybackModePublisher.getInstance().registerObserver(this);
        SongsTrackerPublisher.getInstance().registerObserver(this);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        askForCurrentSong();
        askForCurrentMode();
    }

    protected void askForCurrentMode(){
        Intent intent= new Intent(MediaConstants.ACTION_REASKED_CURRENT_MODE);
        getContext().sendBroadcast(intent);
    }

    protected  void askForCurrentSong(){
        Intent askeToUPdateCurrentSongIntent= new Intent(MediaConstants.ACTION_NOTIFY_CURRENT_SONG);
        getContext().sendBroadcast(askeToUPdateCurrentSongIntent);
    }

    @Override
    protected void setUpViews() {
        ViewGroup mediaCOntrollersHolder = (ViewGroup) rootView.findViewById(R.id.media_controlers_holder);
        Log.d(LOG_TAG , rootView.getChildCount() + "");
        for(int i = 0; i < mediaCOntrollersHolder.getChildCount(); i++){
            View childView = mediaCOntrollersHolder.getChildAt(i);
            if(childView instanceof Button || childView instanceof ImageButton){
                childView.setOnClickListener(this);
            }
        }
        for(int i = 0; i < rootView.getChildCount(); i++){
            View childView = rootView.getChildAt(i);
            if(childView instanceof Button || childView instanceof ImageButton){
                childView.setOnClickListener(this);
            }
        }
        songTitleView = (TextView) rootView.findViewById(R.id.song_title);

        songArtistView = (TextView) rootView.findViewById(R.id.song_artist);
        songImageView = (ImageView) rootView.findViewById(R.id.song_image);
        songImaAnimator = AnimatorInflater.loadAnimator(getContext(), R.animator.rotation);
        songImaAnimator.setTarget(songImageView);

        playPauseButton = (ImageButton) rootView.findViewById(R.id.play);

        setUpAdviceMediaControlsViews();

    }
    protected abstract void setUpAdviceMediaControlsViews();

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play:
                handlePlayPauseActions();
                break;
            case R.id.skipNext:
                getBasicMediaActionBroadcaster().skipToNext();
                break;
            case R.id.skipPre:
                getBasicMediaActionBroadcaster().skipToPrev();
                break;
            default:
                break;
        }
    }

    @Override
    public void receivingCurrentSongAndIndexStatus(Song currentSong, int currentSOngIndex) {
        setUpCurrentSOng(currentSong);
    }

    @Override
    public void receviePlaybackMode(int playbackMode) {
        setCurrentPlaybackMode(playbackMode);
    }

    private void setUpCurrentSOng(Song currentSong){
        if(currentSong != null ){
            Picasso.with(getContext())
                    .load(currentSong.getImage())
                    .into(songImageView);
            songTitleView.setText(currentSong.getTitle());
            songArtistView.setText(currentSong.getArtist());
        }
    }

    protected void handlePlayPauseActions() {
        switch (currentPlaybackMode){
            case Playback.MODE_STOPPING:
                getBasicMediaActionBroadcaster().replayCurrentSong();
                break;
//            case Playback.MODE_PLAYING: case Playback.MODE_BUFFERING:
            case Playback.MODE_PLAYING:
                getBasicMediaActionBroadcaster().pause();
                break;
            case Playback.MODE_PAUSING:
                getBasicMediaActionBroadcaster().resume();
                break;
            default:
                break;
        }
    }

    private void setCurrentPlaybackMode(int currentPlaybackMode) {
        this.currentPlaybackMode = currentPlaybackMode;
        handleAnimation();
        handlePlayAndPauseButton();
    }

    private void handlePlayAndPauseButton(){
        if(allViewsAreInitialized){
            if(currentPlaybackMode == Playback.MODE_PLAYING ){
                playPauseButton.setImageResource(R.drawable.uamp_ic_pause_white_48dp);
            }else {
                playPauseButton.setImageResource(R.drawable.uamp_ic_play_arrow_white_48dp);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleAnimation(){
        if(!canRotateSongImage){
            return;
        }
        switch (currentPlaybackMode){
            case Playback.MODE_PLAYING :
                if(songImaAnimator.isPaused()){
                    songImaAnimator.resume();
                }else{
                    songImaAnimator.start();
                }
                break;
            case Playback.MODE_STOPPING:
                songImaAnimator.end();
                break;
            default:
                songImaAnimator.pause();
                break;
        }
    }

    protected BaseActivity getBaseActivity(){
        if(getContext() instanceof  BaseActivity){
            return (BaseActivity) getContext();
        }
        return null;
    }

    protected BasicMediaActionBroadcaster getBasicMediaActionBroadcaster(){
        if(contextIsBaseActivity()){
            return getBaseActivity().getMediaActionBroadcaster();
        }
        return null;
    }

    protected boolean contextIsBaseActivity(){
        return  getContext() instanceof BaseActivity;
    }

}
