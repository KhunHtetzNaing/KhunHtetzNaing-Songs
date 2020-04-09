package com.htetznaing.songs.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Songs implements Serializable {

    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("audio")
    @Expose
    private String audio;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("artists")
    @Expose
    private String artists;
    @SerializedName("info")
    @Expose
    private String info;
    @SerializedName("youtube")
    @Expose
    private String youtube;
    @SerializedName("joox")
    @Expose
    private String joox;
    @SerializedName("video")
    @Expose
    private String video;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getJoox() {
        return joox;
    }

    public void setJoox(String joox) {
        this.joox = joox;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

}