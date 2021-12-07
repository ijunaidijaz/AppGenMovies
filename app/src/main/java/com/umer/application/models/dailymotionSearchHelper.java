package com.umer.application.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class dailymotionSearchHelper implements Serializable {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("limit")
    @Expose
    private Integer limit;
    @SerializedName("explicit")
    @Expose
    private Boolean explicit;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("has_more")
    @Expose
    private Boolean hasMore;
    @SerializedName("list")
    @Expose
    private ArrayList<YoutubeVideoItem> songs_List = null;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Boolean getExplicit() {
        return explicit;
    }

    public void setExplicit(Boolean explicit) {
        this.explicit = explicit;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public ArrayList<YoutubeVideoItem> getSongs_List() {
        return songs_List;
    }

    public void setSongs_List(ArrayList<YoutubeVideoItem> songs_List) {
        this.songs_List = songs_List;
    }
}
