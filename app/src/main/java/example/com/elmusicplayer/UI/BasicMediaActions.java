package example.com.elmusicplayer.UI;

import example.com.elmusicplayer.SongKits.Song;


public interface BasicMediaActions {
    void playNewSong(Song newSOng, int newSongIndex);
    void replayCurrentSong();
    void pause();
    void resume();
    void stop();
    void skipToNext();
    void skipToPrev();
    void setLoopMode(int loopMode);
    void startNotifyPlayback();
    void stopNotifyPlayback();
    void updateCurrentPosition(int position);
}
