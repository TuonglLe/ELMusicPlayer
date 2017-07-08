package example.com.elmusicplayer.UI;


import android.support.v4.app.Fragment;

import example.com.elmusicplayer.R;
import example.com.elmusicplayer.UI.Fragment.AdvancedMediaControllerFragment;

public class SongDetailFragment extends AdvancedMediaControllerFragment {


    @Override
    protected int rootViewLayout() {
        return R.layout.fragment_song_detail;
    }


}
