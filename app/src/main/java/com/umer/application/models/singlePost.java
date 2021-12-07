package com.umer.application.models;

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
