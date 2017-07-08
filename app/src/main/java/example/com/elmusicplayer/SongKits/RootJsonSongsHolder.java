package example.com.elmusicplayer.SongKits;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;



public class RootJsonSongsHolder {
    @JsonProperty("music") private List<Song> songs;

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}
