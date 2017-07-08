package example.com.elmusicplayer.MusicService;

import android.util.Log;

import java.util.List;

import example.com.elmusicplayer.ObservablePattern.SongTrackerKits.SongsTrackerPublisher;
import example.com.elmusicplayer.SongKits.Song;
import example.com.elmusicplayer.Utils.MediaConstants;



public class SongsTracker {
    private static final String LOG_TAG = SongsTracker.class.getSimpleName();

    private Song currentSong;
    private int currentSongIndex = -1;
    private List<Song> currentSongList;
    private int currentLoopMode = MediaConstants.LOOP_MODE_NONE;

    public int getCurrentLoopMode() {
        return currentLoopMode;
    }

    public void setCurrentLoopMode(int currentLoopMode) {
        this.currentLoopMode = currentLoopMode;
    }

    /**
     * @SETTER
     */
    public void setCurrentSongAndIndex(Song currentSong, int currentSongIndex) {
        this.currentSong = currentSong;
        this.currentSongIndex = currentSongIndex;
        Log.d(LOG_TAG, currentSong == null ? "currentSong == null" : this.getCurrentSong().getTitle());
        Log.d(LOG_TAG, currentSongIndex + "");
        notifyCurrentSong();

    }

    public void setCurrentSongList(List<Song> currentSongList) {
        this.currentSongList = currentSongList;
        SongsTrackerPublisher.getInstance().notifyCurrentSongListObserver(currentSongList);
    }

    public void notifyCurrentSong(){
        SongsTrackerPublisher.getInstance().notifyCurrentSongAndIndexObservers(currentSong, currentSongIndex);
    }

    /**
     * @GETTER
     */
    public Song getCurrentSong() {
        return currentSong;
    }

    public int getCurrentSongIndex() {
        return currentSongIndex;
    }

    public List<Song> getCurrentSongList() {
        return currentSongList;
    }
}
