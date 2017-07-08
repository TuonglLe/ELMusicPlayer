package example.com.elmusicplayer.ObservablePattern.PlaybackKits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import example.com.elmusicplayer.MusicService.Playback;
import example.com.elmusicplayer.ObservablePattern.Observer;



public class PlaybackPublisher {
    private static volatile  PlaybackPublisher instance;
    public static PlaybackPublisher getInstance(){
        if(instance == null){
            synchronized (PlaybackPublisher.class){
                instance = new PlaybackPublisher();
            }
        }
        return instance;
    };


    private List<PlaybackObserver> playbackObservers;
    private List<PlaybackModeObserver> playbackModeObservers;

    private PlaybackPublisher(){
        playbackObservers = Collections.synchronizedList(new ArrayList<PlaybackObserver>());
        playbackModeObservers = Collections.synchronizedList(new ArrayList<PlaybackModeObserver>());
    }

    public void registerObserver(Observer observer){
        if(observer instanceof PlaybackObserver){
            playbackObservers.add((PlaybackObserver) observer);
            return;
        }
        if(observer instanceof PlaybackModeObserver){
            playbackModeObservers.add((PlaybackModeObserver) observer);
            return;
        }
    }

    public void unregisterObserver(Observer observer){
        if(observer instanceof PlaybackObserver){
            playbackObservers.remove(observer);
            return;
        }
        if(observer instanceof PlaybackModeObserver){
            playbackModeObservers.remove(observer);
            return;
        }
    }

    public void notifyPlaybackObservers(Playback playback){
        for (PlaybackObserver playbackObserver: playbackObservers){
            if(playbackObservers != null) {
                playbackObserver.receiverPlaybackStatus(playback);
            }
//            }else{
//                unregisterObserver(playbackObserver);
//            }
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
