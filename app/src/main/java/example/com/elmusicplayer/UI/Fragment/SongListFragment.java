package example.com.elmusicplayer.UI.Fragment;

import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import example.com.elmusicplayer.MainActivity;
import example.com.elmusicplayer.R;
import example.com.elmusicplayer.UI.SongListRVKits.SongListRVAdapter;


public abstract class SongListFragment extends BaseFragment {
    public static  final String FRAGMENT_TAG = SongListFragment.class.getSimpleName();

    protected  int layoutResource = R.layout.fragment_song_list;
    protected  int messageType;
    protected RecyclerView recyclerView;
    protected SongListRVAdapter songListRVAdapter;

    public SongListFragment(){
        super();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected int rootViewLayout() {
        return R.layout.fragment_song_list;
    }

    @Override
    protected void setUpViews() {
        setUPRecyclerView();
    }

    protected void setUPRecyclerView(){
        recyclerView = (RecyclerView) rootView.findViewById(R.id.song_list_recycler_view);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        );

        songListRVAdapter = new SongListRVAdapter(getContext());
        recyclerView.setAdapter(songListRVAdapter);
    }

    public SongListRVAdapter getSongListRVAdapter() {
        return songListRVAdapter;
    }

    public void sendMessageToMusicService() {
        if (!(getContext() instanceof MainActivity)) {
            return;
        }

        Message message = Message.obtain();
        message.what = setMessageType();

        MainActivity mainActivity = (MainActivity) getContext();
        try {
            if(mainActivity.getMessenger() != null){
                mainActivity.getMessenger().send(message);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setLayoutResource(){};

    public abstract int setMessageType();
}
