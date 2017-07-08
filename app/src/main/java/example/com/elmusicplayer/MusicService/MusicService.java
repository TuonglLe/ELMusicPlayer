package example.com.elmusicplayer.MusicService;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.transition.TransitionPropagation;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import example.com.elmusicplayer.ObservablePattern.PlaybackKits.PlaybackPublisher;
import example.com.elmusicplayer.ObservablePattern.SongTrackerKits.SongsTrackerPublisher;
import example.com.elmusicplayer.SongKits.DeviceSongsFinder;
import example.com.elmusicplayer.SongKits.RemoteSongsFinder;
import example.com.elmusicplayer.SongKits.Song;
import example.com.elmusicplayer.Utils.Constants;
import example.com.elmusicplayer.Utils.MediaConstants;
import example.com.elmusicplayer.Utils.MessageConstants;



public class MusicService extends Service {
    private static final String LOG_TAG = MusicService.class.getSimpleName();

    private class MusicHandler extends Handler{
        private List<Song> remoteSongs;
        public MusicHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MessageConstants.MESSAGE_TYPE_DEVICE_SONGS:
                    remoteSongs = DeviceSongsFinder.findDeviceSongs(MusicService.this, null);
                    break;
                case MessageConstants.MESSAGE_TYPE_REMOTE_JSON_SONGS:
                    remoteSongs = RemoteSongsFinder.fetRemoteSongs(RemoteSongsFinder.REMOTE_SONGS_URL);
                    break;
                default:
                    break;

            }
            if(remoteSongs != null){
                new Handler(getMainLooper()).post(
                        new Runnable() {
                            @Override
                            public void run() {
                                songsTracker.setCurrentSongList(remoteSongs);
                            }
                        }
                );
            }

        }
    }

    private HandlerThread musicActionHandlerThread;
    private MusicHandler musicHandler;
    private Messenger messenger;

    private Playback playback;

    private SongsTracker songsTracker;

    private MediaActionReceiver mediaActionReceiver;
    private IntentFilter mediaActionIntentFilter;

    private NotificationManager notificationManager;

    private ExecutorService excuService;
//    private boolean needToStartPlaybackThread = true;
    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "service is created");
        notificationManager = new NotificationManager(this);

        playback = new Playback(this);
        songsTracker = new SongsTracker();

        setUpMediaRecevier();

        musicActionHandlerThread = new HandlerThread(LOG_TAG, Process.THREAD_PRIORITY_BACKGROUND);
        musicActionHandlerThread.start();
        musicHandler = new MusicHandler(musicActionHandlerThread.getLooper());
        messenger = new Messenger(musicHandler);

        excuService = Executors.newSingleThreadExecutor();
        excuService.execute(new Runnable() {
            @Override
            public void run() {
                while (true ){
                    if(playback.isPlaying()){
                        Intent intent = new Intent(MediaConstants.ACTION_HANDLE_PLAYBACK_CURRENT_POSITION);
                        intent.putExtra(MediaConstants.PLAYBACK_CURRENT_POSITION_KEY, playback.getMediaPlayer().getCurrentPosition());
                        sendBroadcast(intent);
                    }
                }
            }
        });
    }
//
//    public void startPlaybackThread(){
//        needToStartPlaybackThread = true;
//    }
//
//    public void stopPlaybackThread(){
//        needToStartPlaybackThread = false;
//    }


    private void setUpMediaRecevier(){
        mediaActionIntentFilter = new IntentFilter();
        mediaActionIntentFilter.addAction(MediaConstants.ACTION_PLAY_FROM_MEDIA_ID);
        mediaActionIntentFilter.addAction(MediaConstants.ACTION_PLAY_CURRENT_SONG);
        mediaActionIntentFilter.addAction(MediaConstants.ACTION_RESUMED);
        mediaActionIntentFilter.addAction(MediaConstants.ACTION_PAUSED);
        mediaActionIntentFilter.addAction(MediaConstants.ACTION_STOPPED);
        mediaActionIntentFilter.addAction(MediaConstants.ACTION_SKIP_TO_NEXT);
        mediaActionIntentFilter.addAction(MediaConstants.ACTION_SKIP_TO_PREV);
        mediaActionIntentFilter.addAction(MediaConstants.ACTION_LOOP_MODE);
        mediaActionIntentFilter.addAction(MediaConstants.ACTION_NOTIFY_CURRENT_SONG);
        mediaActionIntentFilter.addAction(MediaConstants.ACTION_UPDATE_SONG_POSITION);
        mediaActionIntentFilter.addAction(MediaConstants.ACTION_START_NOTIFY_PLAYBACK);
        mediaActionIntentFilter.addAction(MediaConstants.ACTION_STOP_NOTIFY_PLAYBACK);
        mediaActionIntentFilter.addAction(MediaConstants.ACTION_REASKED_CURRENT_MODE);

        mediaActionReceiver = new MediaActionReceiver(this);
        registerReceiver(mediaActionReceiver, mediaActionIntentFilter);

    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "onDestroy");

        unregisterReceiver(mediaActionReceiver);
//        playback.killThread();
        excuService.shutdownNow();
//        thread.interrupt();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger == null? null: messenger.getBinder();
    }

    public SongsTracker getSongsTracker() {
        return songsTracker;
    }

    public Playback getPlayback() {
        return playback;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }


}
