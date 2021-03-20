package umn.ac.id.uts;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Song implements Serializable {
    private String title;
    private String artist;
    private String album;
    private String path;
    private String duration;
//    private Bitmap cover;

    public Song(String title, String artist, String album, String path, String duration){
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.album = album;
//        this.filename = filename;
        this.path = path;
    }

    public String getPath() {
        return path;
    }
    public void setFilename(String filename) {
        this.path = filename;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSinger() {
        return artist;
    }
    public void setSinger(String singer) {
        this.artist = singer;
    }

    public String getAlbum(){
        return album;
    }
    public void setAlbum(String album){
        this.album = album;
    }
//
//    public Bitmap getCover(){
//        return cover;
//    }
//    public void setCover(Bitmap cover){
//        this.cover = cover;
//    }
}
