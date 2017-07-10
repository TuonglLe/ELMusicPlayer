package example.com.elmusicplayer.UI.Fragment;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;

import example.com.elmusicplayer.Utils.MessageConstants;
import example.com.elmusicplayer.Utils.PermissionHelper;


public class DeviceSongListFragment extends SongListFragment {
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }



    @Override
    public int setMessageType() {
        return MessageConstants.MESSAGE_TYPE_DEVICE_SONGS;
    }
}
