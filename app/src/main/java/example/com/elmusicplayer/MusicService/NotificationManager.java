package example.com.elmusicplayer.MusicService;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.squareup.picasso.Picasso;

import example.com.elmusicplayer.ObservablePattern.PlaybackKits.PlaybackModeObserver;
import example.com.elmusicplayer.ObservablePattern.PlaybackKits.PlaybackModePublisher;
import example.com.elmusicplayer.ObservablePattern.PlaybackKits.PlaybackPublisher;
import example.com.elmusicplayer.R;
import example.com.elmusicplayer.UI.SongDetailActivity;
import example.com.elmusicplayer.Utils.MediaConstants;



public class NotificationManager implements PlaybackModeObserver {
    private static final String LOG_TAG = NotificationManager.class.getSimpleName();
    public static final int NOTIFICATION_ID = 321;
    public static final int NOTIFICATION_REQUEST_CODE = 321;
    public static final int PLAYING_MODE = 13;
    public static final int PAUSING_MODE = 14;

    private MusicService musicService;

    private PendingIntent skipToNextPendingIntent,
                          skipToPrevPendingIntent,
                          pausePendingIntent,
                          stopPendingIntent,
                          playCurrentSongPendingIntent,
                          resumePendingIntent;
    private NotificationCompat.Action skipToNextAction,
                                      skipToPrevAction,
                                      pauseAction,
                                      stopAction,
                                      resumeAction,
                                      playCurrentSongAction;

    private Notification preparingTypeNotfication, resumeTypeNotification, pauseTypeNotification, replayCurrentSongTypeNotifcation;


    private android.support.v7.app.NotificationCompat.MediaStyle mediaStyle;

    private TaskStackBuilder songDetailTaskStackBuilder;
    private PendingIntent returnedPendingIntent;

    public NotificationManager(MusicService musicService) {

        this.musicService = musicService;
        PlaybackModePublisher.getInstance().registerObserver(this);

        setUpBasicAction();

        setUpContentIntent();

        mediaStyle = new android.support.v7.app.NotificationCompat.MediaStyle();



    }

    public void startForeground(Notification musicServiceNotification){
//        Notification musicServiceNotification = null;
//        Log.d(LOG_TAG, "" + musicService.getPlayback().getCurrentMode());
//
//        switch (musicService.getPlayback().getCurrentMode()){
//            case Playback.MODE_PLAYING:
//                pauseTypeNotification = buildNotiicationBuilder(resumeAction).build();
//                musicServiceNotification = pauseTypeNotification;
//                break;
//            case Playback.MODE_PAUSING:
//                resumeTypeNotification = buildNotiicationBuilder(pauseAction).build();
//
//                musicServiceNotification = resumeTypeNotification;
//                break;
//            case Playback.MODE_PREPARING_NEW_SONG:
//                setUpPrearingNotification();
//                musicServiceNotification = preparingTypeNotfication;
//                break;
//            case Playback.MODE_STOPPING:
//                replayCurrentSongTypeNotifcation = buildNotiicationBuilder(playCurrentSongAction).build();
//                musicServiceNotification = replayCurrentSongTypeNotifcation;
//                break;
//            default:
//        }

        if(musicServiceNotification != null){
            musicService.startForeground(
                    NOTIFICATION_ID,
                    musicServiceNotification
            );
        }
    }

    private void setUpBasicAction(){
        skipToNextPendingIntent = buildMediaActionPendingItent(MediaConstants.ACTION_SKIP_TO_NEXT);
        skipToNextAction = buildMediaAction(
                R.drawable.ic_skip_next_white_24dp,
                "",
                skipToNextPendingIntent
        );

        skipToPrevPendingIntent = buildMediaActionPendingItent(MediaConstants.ACTION_SKIP_TO_PREV);
        skipToPrevAction = buildMediaAction(
                R.drawable.ic_skip_previous_white_24dp,
                "",
                skipToPrevPendingIntent
        );

        playCurrentSongPendingIntent = buildMediaActionPendingItent(MediaConstants.ACTION_PLAY_CURRENT_SONG);
        playCurrentSongAction = buildMediaAction(
                R.drawable.uamp_ic_play_arrow_white_24dp,
                "",
                playCurrentSongPendingIntent
        );

        resumePendingIntent = buildMediaActionPendingItent(MediaConstants.ACTION_RESUMED);
        resumeAction = buildMediaAction(
                R.drawable.uamp_ic_play_arrow_white_24dp,
                "",
                resumePendingIntent
        );

        pausePendingIntent = buildMediaActionPendingItent(MediaConstants.ACTION_PAUSED);
        pauseAction = buildMediaAction(
                R.drawable.uamp_ic_pause_white_24dp,
                "",
                pausePendingIntent
        );

        stopPendingIntent = buildMediaActionPendingItent(MediaConstants.ACTION_STOPPED);
        stopAction = buildMediaAction(
                R.drawable.ic_stop,
                "",
                stopPendingIntent
        );
    }

    private void setUpPrearingNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(musicService);
        builder.setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(musicService.getSongsTracker().getCurrentSong().getTitle())
                .setContentText(musicService.getSongsTracker().getCurrentSong().getArtist())
                .setContentIntent(returnedPendingIntent)
                .setColor(ContextCompat.getColor(musicService, R.color.colorPrimary))
                .setStyle(mediaStyle)
                .addAction(skipToPrevAction)
                .addAction(stopAction)
                .addAction(skipToNextAction);
        preparingTypeNotfication = builder.build();
    }

    private PendingIntent buildMediaActionPendingItent(String mediaAction){
        Intent intent = new Intent(mediaAction).setPackage(musicService.getPackageName());
        return PendingIntent.getBroadcast(
                musicService,
                NOTIFICATION_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );
    }

    private NotificationCompat.Action buildMediaAction(int iconResource, String title, PendingIntent mediaActionPendingIntent){
        NotificationCompat.Action.Builder builder = new NotificationCompat.Action.Builder(iconResource, title, mediaActionPendingIntent);
        return builder.build();
    }


    private void setUpContentIntent() {
        songDetailTaskStackBuilder = TaskStackBuilder.create(musicService);
        songDetailTaskStackBuilder.addNextIntentWithParentStack(new Intent(musicService, SongDetailActivity.class));
        returnedPendingIntent = songDetailTaskStackBuilder.getPendingIntent(NOTIFICATION_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT, null);
    }

    private NotificationCompat.Builder buildNotiicationBuilder(NotificationCompat.Action alternativeAction){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(musicService);
        builder.setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(musicService.getSongsTracker().getCurrentSong().getTitle())
                .setContentText(musicService.getSongsTracker().getCurrentSong().getArtist())
                .setContentIntent(returnedPendingIntent)
//                .setLargeIcon(BitmapFactory.decodeResource(musicService.getResources(), R.drawable.ic_launcher))
                .setColor(ContextCompat.getColor(musicService, R.color.colorPrimary))
                .setStyle(mediaStyle)
                .addAction(skipToPrevAction)
                .addAction(alternativeAction)
                .addAction(stopAction)
                .addAction(skipToNextAction);
        return builder;
    }

    @Override
    public void receviePlaybackMode(int playbackMode) {
        Notification notification = null;
        switch (playbackMode){
            case Playback.MODE_PLAYING:
                pauseTypeNotification = buildNotiicationBuilder(pauseAction).build();
                notification = pauseTypeNotification;
                break;
            case Playback.MODE_PAUSING: case Playback.MODE_PREPARING_NEW_SONG:
                resumeTypeNotification = buildNotiicationBuilder(resumeAction).build();
                notification = resumeTypeNotification;
                break;
            case Playback.MODE_STOPPING:
                replayCurrentSongTypeNotifcation = buildNotiicationBuilder(playCurrentSongAction).build();
                notification = replayCurrentSongTypeNotifcation;
                break;
            default:
                break;
        }
        if(notification != null){
            startForeground(notification);
        }
    }
}
