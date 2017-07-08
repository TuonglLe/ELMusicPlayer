package example.com.elmusicplayer.SongKits;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import example.com.elmusicplayer.Utils.PermissionHelper;



public class DeviceSongsFinder {
    public static List<Song> findDeviceSongs(Context context, String query){
        List<Song> songs = new ArrayList<>();
        if(PermissionHelper.permissionIsGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE)){
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
            String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
            Cursor cursor = context.getContentResolver().query(uri, null, selection, null, sortOrder);

            while (cursor.moveToNext()){
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String path= cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                Song song = new Song.Builder()
                        .setTitle(title)
                        .setPath(path)
                        .setArtist(artist)
                        .setDuration(duration)
                        .build();
                songs.add(song);
            }
            cursor.close();
        }
        return songs;
    }
}
