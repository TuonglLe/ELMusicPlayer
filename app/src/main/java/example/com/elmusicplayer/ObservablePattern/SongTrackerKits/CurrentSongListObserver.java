package example.com.elmusicplayer.ObservablePattern.SongTrackerKits;

import java.util.List;

import example.com.elmusicplayer.ObservablePattern.Observer;
import example.com.elmusicplayer.SongKits.Song;



public interface CurrentSongListObserver extends Observer {
    void recieveCurrentSongListStatus(List<Song> currentSongList);
}
