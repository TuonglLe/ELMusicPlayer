package example.com.elmusicplayer.SongKits;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import example.com.elmusicplayer.Utils.JsonHelper;



public final class RemoteSongsFinder {
    public static final String BASE_URL = "http://storage.googleapis.com/automotive-media/";
    public static final String REMOTE_SONGS_URL = "http://storage.googleapis.com/automotive-media/music.json";
    public static List<Song> fetRemoteSongs(String urlString){
        List<Song> songs = null;
        String jsonResponse = JsonHelper.getJsonResponse(urlString);
        if(jsonResponse != null){
            try {
                RootJsonSongsHolder rootJsonSongsHolder = new ObjectMapper().readValue(jsonResponse, RootJsonSongsHolder.class);
                songs = rootJsonSongsHolder.getSongs();
                for(Song song: songs){
                    song.setIsGoogleSong(true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return songs;
    }
}
