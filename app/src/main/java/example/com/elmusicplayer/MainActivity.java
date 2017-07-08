package example.com.elmusicplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import example.com.elmusicplayer.ObservablePattern.SongTrackerKits.CurrentSongListObserver;
import example.com.elmusicplayer.ObservablePattern.SongTrackerKits.SongsTrackerPublisher;
import example.com.elmusicplayer.SongKits.Song;
import example.com.elmusicplayer.UI.BaseActivity;
import example.com.elmusicplayer.UI.Fragment.DeviceSongListFragment;
import example.com.elmusicplayer.UI.Fragment.RemoteSongListFragment;
import example.com.elmusicplayer.UI.Fragment.SongListFragment;
import example.com.elmusicplayer.UI.MediaControllersFragment_MainActivity;
import example.com.elmusicplayer.Utils.MessageConstants;

public class MainActivity extends BaseActivity implements CurrentSongListObserver {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private FragmentManager fragmentManager;
    private SongListFragment currentSongListFragment;
    private MediaControllersFragment_MainActivity basicMedoaControllerFragment;
    private DeviceSongListFragment deviceSongListFragment;
    private RemoteSongListFragment remoteSongListFragment;

    private ViewPager viewPager;
    private SongListFragmentPagerAdapter songListFragmentPagerAdapter;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SongsTrackerPublisher.getInstance().registerObserver(this);

//        currentSongListFragment = new SongListFragment();
        basicMedoaControllerFragment = new MediaControllersFragment_MainActivity();


        fragmentManager = getSupportFragmentManager();
//        setupViewPager();
        fragmentManager.beginTransaction()
//                .replace(R.id.song_list_fragment_holder, currentSongListFragment, SongListFragment.FRAGMENT_TAG)
                .replace(R.id.media_control_fragment_holder, basicMedoaControllerFragment, MediaControllersFragment_MainActivity.TAG)
                .commit();

        hideMediaOCntrollerFragment();



    }



    public void  hideMediaOCntrollerFragment(){
        fragmentManager.beginTransaction()
                .hide(basicMedoaControllerFragment)
                .commit();
    }
    public void  showMediaOCntrollerFragment(){
        fragmentManager.beginTransaction()
                .show(basicMedoaControllerFragment)
                .commit();
    }

    public boolean mediaCOntrollerIsHiden(){
        return basicMedoaControllerFragment.isHidden();
    }

    @Override
    protected int fetchLayoutResource(){
        return R.layout.activity_main;
    }

    @Override
    protected void doSomethingWhenServiceIsConnectd() {
//        currentSongListFragment.setMessageType(MessageConstants.MESSAGE_TYPE_REMOTE_JSON_SONGS);
//        currentSongListFragment.sendMessageToMusicService();
        setupViewPager();
    }

    @Override
    public void recieveCurrentSongListStatus(List<Song> currentSongList) {
        if(currentSongList != null){
            getCurrentSongListFragment().getSongListRVAdapter().setSongList(currentSongList);
        }
    }

    private SongListFragment getVisibleSOngListFragment(){
        return (SongListFragment) fragmentManager.findFragmentByTag(SongListFragment.FRAGMENT_TAG);
    }

    private void setupViewPager(){
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        songListFragmentPagerAdapter = new SongListFragmentPagerAdapter(fragmentManager);
        viewPager.setAdapter(songListFragmentPagerAdapter);
        viewPager.setCurrentItem(0);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                getCurrentSongListFragment().sendMessageToMusicService();
                switch (tab.getPosition()){
                    case 0:
                        remoteSongListFragment.sendMessageToMusicService();
                        break;
                    case 1:
                        deviceSongListFragment.sendMessageToMusicService();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if( viewPager.getCurrentItem() == 0 ) {
            remoteSongListFragment.sendMessageToMusicService();
        }  else {
            deviceSongListFragment.sendMessageToMusicService();
        }
    }


    private SongListFragment getCurrentSongListFragment(){
        if(songListFragmentPagerAdapter.songListFragments.size() > 0){
            return songListFragmentPagerAdapter.songListFragments.get(viewPager.getCurrentItem());
        } else {
            return null;
        }
    }

     class SongListFragmentPagerAdapter extends FragmentPagerAdapter{

        List<SongListFragment> songListFragments;
        public SongListFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            songListFragments = new ArrayList<>();
        }

         @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    Log.d(LOG_TAG, "remoteSongListFragment");
                    if(remoteSongListFragment == null){
                        remoteSongListFragment = new RemoteSongListFragment();
                        songListFragments.add(remoteSongListFragment);
                    }
                    return remoteSongListFragment;
                case 1:
                    Log.d(LOG_TAG, "deviceSongListFragment");
                    if(deviceSongListFragment == null){
                        deviceSongListFragment = new DeviceSongListFragment();
                        songListFragments.add(deviceSongListFragment);
                    }
                    return deviceSongListFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "JSON SONG";
                case 1:
                    return "DEVICE";
                default:
                    return super.getPageTitle(position);
            }
        }
    }

}
