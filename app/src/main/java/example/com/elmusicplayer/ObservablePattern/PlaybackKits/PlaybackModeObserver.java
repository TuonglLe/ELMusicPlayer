package example.com.elmusicplayer.ObservablePattern.PlaybackKits;

import example.com.elmusicplayer.ObservablePattern.Observer;



public interface PlaybackModeObserver extends Observer {
    void receviePlaybackMode(int playbackMode);
}
