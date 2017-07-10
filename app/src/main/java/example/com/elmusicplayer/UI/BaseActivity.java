package example.com.elmusicplayer.UI;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import example.com.elmusicplayer.MusicService.MusicService;
import example.com.elmusicplayer.R;
import example.com.elmusicplayer.Utils.PermissionHelper;


public abstract class BaseActivity extends AppCompatActivity  {
    private static final String LOG_TAG = BaseActivity.class.getSimpleName();
    protected boolean serviceIsAlive;
    protected Toolbar toolbar;
    protected Messenger messenger;
    protected BasicMediaActionBroadcaster mediaActionBroadcaster;
    protected final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(LOG_TAG, "onServiceConnected");
            serviceIsAlive = true;
            messenger = new Messenger(service);
            doSomethingWhenServiceIsConnectd();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceIsAlive = false;
        }
    };

    public BasicMediaActionBroadcaster getMediaActionBroadcaster() {
        return mediaActionBroadcaster;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //handlePermission();
        super.onCreate(savedInstanceState);
        setContentView(fetchLayoutResource());
        setUpToolbar();

        mediaActionBroadcaster = new BasicMediaActionBroadcaster(this);
    }

    protected void handlePermission(){
        if(!PermissionHelper.permissionIsGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            PermissionHelper.requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, 99);
        }
        if(!PermissionHelper.permissionIsGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            PermissionHelper.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, 99);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindMusicService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
    }

    private void setUpToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar == null){
            return;
        }
        setSupportActionBar(toolbar);
        if(getSupportParentActivityIntent() != null){
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    protected abstract int fetchLayoutResource();

    protected abstract void doSomethingWhenServiceIsConnectd();

    protected void bindMusicService(){
        bindService(
                new Intent(this, MusicService.class),
                serviceConnection,
                BIND_AUTO_CREATE
        );
    }
    public Messenger getMessenger() {
        return messenger;
    }
}
