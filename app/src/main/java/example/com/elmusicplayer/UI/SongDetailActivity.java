package example.com.elmusicplayer.UI;

import android.support.annotation.Nullable;
import android.os.Bundle;

import example.com.elmusicplayer.R;

public class SongDetailActivity extends BaseActivity{
//        implements PlaybackObserver,
//        Runnable,
//        CurrentSongAndIndexObserver,
//        PlaybackModeObserver,
//        View.OnClickListener {
    private static final String LOG_TAG = SongDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected int fetchLayoutResource() {
        return R.layout.activity_song_detail;
    }

    @Override
    protected void doSomethingWhenServiceIsConnectd() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.media_control_fragment_holder, new SongDetailFragment())
                .commit();
    }
}
