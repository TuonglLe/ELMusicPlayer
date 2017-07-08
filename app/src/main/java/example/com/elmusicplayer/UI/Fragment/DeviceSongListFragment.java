package example.com.elmusicplayer.UI.Fragment;

import example.com.elmusicplayer.Utils.MessageConstants;


public class DeviceSongListFragment extends SongListFragment {
    @Override
    public int setMessageType() {
        return MessageConstants.MESSAGE_TYPE_DEVICE_SONGS;
    }
}
