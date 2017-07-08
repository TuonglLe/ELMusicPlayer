package example.com.elmusicplayer.Utils;


public final class MediaConstants {
    private MediaConstants(){
    }

    public static  final String ACTION_PLAY_FROM_MEDIA_ID = MediaConstants.class.getCanonicalName() + "_ACTION_PLAY_FROM_MEDIA_ID";
    public static  final String ACTION_PLAY_CURRENT_SONG = MediaConstants.class.getCanonicalName() + "_ACTION_PLAY_CURRENT_SONG";
    public static  final String ACTION_PAUSED = MediaConstants.class.getCanonicalName() + "_" + "ACTION_PAUSED";
    public static  final String ACTION_RESUMED = MediaConstants.class.getCanonicalName() + "_" + "ACTION_RESUMED";
    public static  final String ACTION_STOPPED = MediaConstants.class.getCanonicalName() + "_" + "ACTION_STOPPED";
    public static  final String ACTION_SKIP_TO_NEXT = MediaConstants.class.getCanonicalName() + "ACTION_SKIP_TO_NEXT";
    public static  final String ACTION_SKIP_TO_PREV = MediaConstants.class.getCanonicalName() + "ACTION_SKIP_TO_PREV";
    public static  final String ACTION_LOOP_MODE = MediaConstants.class.getCanonicalName() + "ACTION_LOOP_MODE";
    public static  final String ACTION_UPDATE_SONG_POSITION = MediaConstants.class.getCanonicalName() + "ACTION_UPDATE_SONG_POSITION";
    public static  final String ACTION_NOTIFY_CURRENT_SONG = MediaConstants.class.getCanonicalName() + "_" + "ACTION_NOTIFY_CURRENT_SONG";
    public static  final String ACTION_START_NOTIFY_PLAYBACK = MediaConstants.class.getCanonicalName() + "_" + "ACTION_START_NOTIFY_PLAYBACK";
    public static  final String ACTION_STOP_NOTIFY_PLAYBACK = MediaConstants.class.getCanonicalName() + "_" + "ACTION_STOP_NOTIFY_PLAYBACK";

    public static  final String ACTION_HANDLE_PLAYBACK_CURRENT_POSITION = MediaConstants.class.getCanonicalName() + "_" + "ACTION_HANDLE_PLAYBACK_CURRENT_POSITION";
    public static  final String ACTION_REASKED_CURRENT_MODE = MediaConstants.class.getCanonicalName() + "_" + "ACTION_REASKED_CURRENT_MODE";
    public static  final String ACTION_UPDATE_PLAYBACK_CURRENT_POSITION_COMPLETE = MediaConstants.class.getCanonicalName() + "_" + "ACTION_UPDATE_PLAYBACK_CURRENT_POSITION_COMPLETE";



    public static  final String IS_TOUCHING_SONG_SEEKBAR_KEY = MediaConstants.class.getCanonicalName() + "_" + "IS_TOUCHING_ACTION_BAR_KEY";

    public static final String MEDIA_INDEX_KEY = "MEDIA_INDEX_KEY";
    public static final String SONG_POSITION_KEY = "MEDIA_INDEX_KEY";
    public static final String PLAYBACK_CURRENT_POSITION_KEY = "PLAYBACK_CURRENT_POSITION_KEY";
    public static final String SONG_KEY = "MEDIA_SONG_KEY";
    public static final String MEDIA_LOOP_MODE_KEY = "MEDIA_LOOP_MODE_KEY";

    public static final int LOOP_MODE_ALL = 32;
    public static final int LOOP_MODE_ONE = 543;
    public static final int LOOP_MODE_NONE = 553;
}
