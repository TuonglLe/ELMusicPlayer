package example.com.elmusicplayer.UI.SongListRVKits;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import example.com.elmusicplayer.MainActivity;
import example.com.elmusicplayer.R;
import example.com.elmusicplayer.SongKits.Song;
import example.com.elmusicplayer.UI.BaseActivity;
import example.com.elmusicplayer.UI.MediaControllersFragment_MainActivity;
import example.com.elmusicplayer.UI.SongDetailActivity;
import example.com.elmusicplayer.Utils.MediaConstants;
import example.com.elmusicplayer.Utils.MessageConstants;


public class SongListRVAdapter extends RecyclerView.Adapter<SongListRVAdapter.SongViewHolder> {
    private Context context;
    private List<Song> songList;

    public SongListRVAdapter(Context context) {
        this.context = context;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SongViewHolder(
                LayoutInflater.from(context).inflate(R.layout.view_holder_song, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        if(songList == null || songList.isEmpty()){
            return;
        }

        holder.bindData(songList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return songList == null? 0 :  songList.size();
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
        notifyDataSetChanged();
    }

    /**
     * @SongViewHolder
     */
    class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        Song vhSong;
        TextView songTitleView, songArtistView;
        int vhSongIndex;

        public SongViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            songTitleView = (TextView) itemView.findViewById(R.id.song_title);
            songArtistView = (TextView) itemView.findViewById(R.id.song_artist);
        }

        void bindData(Song song, int vhSongIndex){
            this.vhSong = song;
            this.vhSongIndex = vhSongIndex;
            songTitleView.setText(song.getTitle());
            songArtistView.setText(song.getArtist());
        }

        @Override
        public void onClick(View v) {
            Intent mediaActionBroadCstIntent = new Intent(MediaConstants.ACTION_PLAY_FROM_MEDIA_ID);
            Bundle bundle = new Bundle();
            bundle.putSerializable(MediaConstants.SONG_KEY, vhSong);
            bundle.putInt(MediaConstants.MEDIA_INDEX_KEY, vhSongIndex);
            mediaActionBroadCstIntent.putExtras(bundle);
            context.sendBroadcast(mediaActionBroadCstIntent);

            if(context instanceof MainActivity){

                MainActivity mainActivity = (MainActivity) context;
               if(mainActivity.mediaCOntrollerIsHiden()){
                   mainActivity.showMediaOCntrollerFragment();
               }
            }
        }
    }
}
