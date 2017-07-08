package example.com.elmusicplayer.ObservablePattern.SongTrackerKits;

import example.com.elmusicplayer.ObservablePattern.Observer;
import example.com.elmusicplayer.SongKits.Song;



public interface CurrentSongAndIndexObserver extends Observer {
    void receivingCurrentSongAndIndexStatus(Song currentSong, int currentSOngIndex);
}
