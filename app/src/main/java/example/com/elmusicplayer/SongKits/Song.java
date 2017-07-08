package example.com.elmusicplayer.SongKits;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Song implements Serializable {
    public static final String BASE_URL = "http://storage.googleapis.com/automotive-media/";
    public static final String BASE_IMAGE_URL = "http://storage.googleapis.com/automotive-media/album_art_2.jpg";
    private String title,
            artist,
            image;
    private boolean isGoogleSong;
    @JsonProperty("source") private String path;
    private long duration;

    public Song(){

    }
    private Song(Song.Builder builder){
        setArtist(builder.artist);
        setTitle(builder.title);
        setPath(builder.path);
        setImage(builder.image);
        setDuration(builder.duration);
        setIsGoogleSong(builder.isGoogleSong);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        if(image == null){
            this.image = BASE_IMAGE_URL;
        }
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isGoogleSong() {
        return isGoogleSong;
    }

    public void setIsGoogleSong(boolean googleSong) {
        isGoogleSong = googleSong;
        if (isGoogleSong){
            setPath(BASE_URL + path);
            setImage(BASE_URL + image);
            setDuration(duration * 1000);
        }
    }

    /**
     * @SongBuilder
     */
    public static class Builder{
        String title,
                artist,
                path,
                image;
        long duration;
        boolean isGoogleSong;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setArtist(String artist) {
            this.artist = artist;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Builder setImage(String iamge){
            this.image = image;
            return this;
        }

        public Builder setDuration(long duration) {
            this.duration = duration;
            return this;
        }

        public Builder setIsGoogleSong(boolean isGoogleSong){
            this.isGoogleSong = isGoogleSong;
            return this;
        }

        public Song build(){
            return new Song(this);
        }
    }
}
