package com.free.newhdmovies.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class singlePost implements Serializable {

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Keyword")
    @Expose
    private String keyword;
    @SerializedName("RedirectApp")
    @Expose
    private String redirectApp;
    @SerializedName("PlayList")
    @Expose
    private Boolean playList;
    @SerializedName("Limit")
    @Expose
    private Integer limit;
    @SerializedName("RedirectLink")
    @Expose
    private boolean redirectLink;
    @SerializedName("IsYoutube")
    @Expose
    private Boolean isYoutube;
    @SerializedName("SubCateGeoryId")
    @Expose
    private Integer subCategoryId;
    @SerializedName("BaseApi")
    @Expose
    private BaseApi baseApi;
    @SerializedName("ServerLink1")
    @Expose
    private String ServerLink1;
    @SerializedName("ServerLink2")
    @Expose
    private String ServerLink2;
    @SerializedName("ServerLink3")
    @Expose
    private String ServerLink3;

    public String getServerLink1() {
        return ServerLink1;
    }

    public void setServerLink1(String serverLink1) {
        ServerLink1 = serverLink1;
    }

    public String getServerLink2() {
        return ServerLink2;
    }

    public void setServerLink2(String serverLink2) {
        ServerLink2 = serverLink2;
    }

    public String getServerLink3() {
        return ServerLink3;
    }

    public void setServerLink3(String serverLink3) {
        ServerLink3 = serverLink3;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getRedirectApp() {
        return redirectApp;
    }

    public void setRedirectApp(String redirectApp) {
        this.redirectApp = redirectApp;
    }

    public Boolean getPlayList() {
        return playList;
    }

    public void setPlayList(Boolean playList) {
        this.playList = playList;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Boolean getYoutube() {
        return isYoutube;
    }

    public void setYoutube(Boolean youtube) {
        isYoutube = youtube;
    }

    public Integer getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Integer subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public BaseApi getBaseApi() {
        return baseApi;
    }

    public void setBaseApi(BaseApi baseApi) {
        this.baseApi = baseApi;
    }

    public boolean isRedirectLink() {
        return redirectLink;
    }

    public void setRedirectLink(boolean redirectLink) {
        this.redirectLink = redirectLink;
    }
}
