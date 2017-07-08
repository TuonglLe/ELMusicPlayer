package example.com.elmusicplayer.ObservablePattern.PlaybackKits;

import java.util.ArrayList;
import java.util.List;

import example.com.elmusicplayer.MusicService.Playback;
import example.com.elmusicplayer.ObservablePattern.Observer;



public class PlaybackModePublisher {
    private static volatile  PlaybackModePublisher instance;
    public static PlaybackModePublisher getInstance(){
        if(instance == null){
            synchronized (PlaybackModePublisher.class){
                instance = new PlaybackModePublisher();
            }
        }
        return instance;
    };


    private List<PlaybackModeObserver> playbackModeObservers;

    private PlaybackModePublisher(){
        playbackModeObservers = new ArrayList<>();
    }

    public void registerObserver(Observer observer){
        if(observer instanceof PlaybackModeObserver){
            playbackModeObservers.add((PlaybackModeObserver) observer);
        }
    }

    public void unregisterObserver(Observer observer){
        if(observer instanceof PlaybackModeObserver){
            playbackModeObservers.remove(observer);
            return;
        }
    }


    public void notifyPlaybackModeObservers(int playbackMode){
        for(PlaybackModeObserver observer: playbackModeObservers){
            if(observer == null){
                unregisterObserver(observer);
            }else{
                observer.receviePlaybackMode(playbackMode);
            }
        }
    }
}
