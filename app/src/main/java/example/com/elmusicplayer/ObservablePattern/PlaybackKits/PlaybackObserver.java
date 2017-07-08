package example.com.elmusicplayer.ObservablePattern.PlaybackKits;

import example.com.elmusicplayer.MusicService.Playback;
import example.com.elmusicplayer.ObservablePattern.Observer;


public interface PlaybackObserver extends Observer{
    void receiverPlaybackStatus(Playback playback);
}
