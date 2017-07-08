package example.com.elmusicplayer.ObservablePattern.SongTrackerKits;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import example.com.elmusicplayer.ObservablePattern.Observer;
import example.com.elmusicplayer.SongKits.Song;



public class SongsTrackerPublisher {

    private static final String LOG_TAG = SongsTrackerPublisher.class.getSimpleName();

    private static volatile SongsTrackerPublisher instance;
    public static SongsTrackerPublisher getInstance(){
        if(instance == null){
            synchronized (SongsTrackerPublisher.class){
                instance = new SongsTrackerPublisher();
            }
        }
        return instance;
    };

    private List<CurrentSongAndIndexObserver> currentSongAndIndexObservers;
    private List<CurrentSongListObserver> currentSongListObservers;

    private SongsTrackerPublisher(){
        currentSongListObservers = new ArrayList<>();
        currentSongAndIndexObservers = new ArrayList<>();
    }

    public void registerObserver(Observer observer){
        if(observer instanceof CurrentSongAndIndexObserver){
            currentSongAndIndexObservers.add((CurrentSongAndIndexObserver) observer);
            Log.d(LOG_TAG, "register CurrentSongAndIndexObserver");
        }else if(observer instanceof  CurrentSongListObserver){
            currentSongListObservers.add((CurrentSongListObserver) observer);
            Log.d(LOG_TAG, "register CurrentSongListObserver");
        }else{
        }
    }

    public void unregisterObserver(Observer observer){
        if(observer instanceof CurrentSongAndIndexObserver){
            currentSongAndIndexObservers.remove(observer);
        }else if(observer instanceof  CurrentSongListObserver){
            currentSongListObservers.remove(observer);
        }else{
        }
    }

    public void notifyCurrentSongAndIndexObservers(Song currentSong, int currentSongIndex){
        for(CurrentSongAndIndexObserver observer: currentSongAndIndexObservers){
            Log.d(LOG_TAG, "notify CurrentSongAndIndexObserver");
            observer.receivingCurrentSongAndIndexStatus(currentSong, currentSongIndex);
        }
    }

    public void notifyCurrentSongListObserver(List<Song> currentSOngList){
        for (CurrentSongListObserver observer: currentSongListObservers){
            Log.d(LOG_TAG, "notify CurrentSongAndIndexObserver");
            observer.recieveCurrentSongListStatus(currentSOngList);
        }
    }


}
