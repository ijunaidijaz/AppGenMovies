package com.livematch.livesportstv.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class YoutubeVideoItem implements Serializable {

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("thumbnail_url")
    @Expose
    public String thumbnail_url;

    @SerializedName("title")
    @Expose
    public String title;


    public YoutubeVideoItem(String id, String thumbnail_url, String title) {
        this.id = id;
        this.thumbnail_url = thumbnail_url;
        this.title = title;
    }

//    public YoutubeVideoItem(String id, String title) {
//        this.id = id;
//        this.title = title;
//    }
}
