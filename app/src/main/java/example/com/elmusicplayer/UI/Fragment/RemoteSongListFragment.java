package example.com.elmusicplayer.UI.Fragment;

import example.com.elmusicplayer.Utils.MessageConstants;


public class RemoteSongListFragment extends SongListFragment {
    @Override
    public int setMessageType() {
        return MessageConstants.MESSAGE_TYPE_REMOTE_JSON_SONGS;
    }
}
