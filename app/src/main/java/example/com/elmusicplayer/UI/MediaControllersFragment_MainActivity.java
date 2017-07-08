package example.com.elmusicplayer.UI;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.animation.AnimatorCompatHelper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.Duration;

import example.com.elmusicplayer.R;
import example.com.elmusicplayer.UI.Fragment.BasicMediaControllerFragment;

public class MediaControllersFragment_MainActivity extends BasicMediaControllerFragment {
    public static final String TAG = MediaControllersFragment_MainActivity.class.getSimpleName();
    @Override
    protected int rootViewLayout() {
        return R.layout.fragment_media_controllers_main_activty;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void setUpAdviceMediaControlsViews() {
        canRotateSongImage = true;
        rootView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == rootView){
            startActivity(
                    new Intent(getContext(), SongDetailActivity.class)
            );
        }
    }
}
