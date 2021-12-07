package com.umer.application.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.umer.application.utils.functions;

import java.io.Serializable;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ApplicationSettings implements Serializable {
    @SerializedName("IsYoutubePost")
    @Expose
    private boolean isYoutubePost;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Discraption")
    @Expose
    private String discraption;
    @SerializedName("Log")
    @Expose
    private String log;
    @SerializedName("LayoutBackGround")
    @Expose
    private String layoutBackGround;
    @SerializedName("ReplacePkg")
    @Expose
    private String replacePkg;
    @SerializedName("SplashScreen")
    @Expose
    private String splashScreen;
    @SerializedName("NotificationOneSignalId")
    @Expose
    private String notificationOneSignalId;
    @SerializedName("ActionBarColor")
    @Expose
    private String actionBarColor;
    @SerializedName("ListingItemBackgroundColor")
    @Expose
    private String listingItemBackgroundColor;
    @SerializedName("AdMob")
    @Expose
    private Boolean adMob;
    @SerializedName("AdMobLimit")
    @Expose
    private int adMobLimit;
    @SerializedName("AdMobBannerId")
    @Expose
    private String adMobBannerId;
    @SerializedName("AdMobInterstitialId")
    @Expose
    private String adMobInterstitialId;
    @SerializedName("StartAppId")
    @Expose
    private Object startAppId;
    @SerializedName("RowDisplay")
    @Expose
    private Integer rowDisplay;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("Category")
    @Expose
    private ArrayList<String> category = null;
    @SerializedName("YouTubeApiKey")
    @Expose
    private String youTubeApiKey;
    @SerializedName("Featured")
    @Expose
    private Boolean featured;
    @SerializedName("IncludeHeader")
    @Expose
    private Boolean includeHeader;
    @SerializedName("RedirectLink")
    @Expose
    private String redirectLink;
    @SerializedName("Logo")
    @Expose
    private String logo;
    @SerializedName("GrideViewPost")
    @Expose
    private int grideViewPost;
    @SerializedName("AdmobAppId")
    @Expose
    private String admobAppId;

    @SerializedName("Adds")
    @Expose
    private int adds;
    @SerializedName("FacebookBannerId")
    @Expose
    private String facebookBannerId;
    @SerializedName("FacebookInterstialAdId")
    @Expose
    private String facebookInterstialAdId;
    @SerializedName("FacebookRewardedVideo")
    @Expose
    private String facebookRewardedVideo;

    public int getAdds() {
        return adds;
    }

    public void setAdds(int adds) {
        this.adds = adds;
    }

    public String getFacebookBannerId() {
        return facebookBannerId;
    }

    public void setFacebookBannerId(String facebookBannerId) {
        this.facebookBannerId = facebookBannerId;
    }

    public String getFacebookInterstialAdId() {
        return facebookInterstialAdId;
    }

    public void setFacebookInterstialAdId(String facebookInterstialAdId) {
        this.facebookInterstialAdId = facebookInterstialAdId;
    }

    public String getFacebookRewardedVideo() {
        return facebookRewardedVideo;
    }

    public void setFacebookRewardedVideo(String facebookRewardedVideo) {
        this.facebookRewardedVideo = facebookRewardedVideo;
    }

    public boolean isYoutubePost() {
        return isYoutubePost;
    }

    public void setYoutubePost(boolean youtubePost) {
        isYoutubePost = youtubePost;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscraption() {
        return discraption;
    }

    public void setDiscraption(String discraption) {
        this.discraption = discraption;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getLayoutBackGround() {
        return layoutBackGround;
    }

    public void setLayoutBackGround(String layoutBackGround) {
        this.layoutBackGround = layoutBackGround;
    }

    public String getReplacePkg() {
        return replacePkg;
    }

    public void setReplacePkg(String replacePkg) {
        this.replacePkg = replacePkg;
    }

    public String getSplashScreen() {
        return splashScreen;
    }

    public void setSplashScreen(String splashScreen) {
        this.splashScreen = splashScreen;
    }

    public String getNotificationOneSignalId() {
        return notificationOneSignalId;
    }

    public void setNotificationOneSignalId(String notificationOneSignalId) {
        this.notificationOneSignalId = notificationOneSignalId;
    }

    public String getActionBarColor() {
        return actionBarColor;
    }

    public void setActionBarColor(String actionBarColor) {
        this.actionBarColor = actionBarColor;
    }

    public String getListingItemBackgroundColor() {
        return listingItemBackgroundColor;
    }

    public void setListingItemBackgroundColor(String listingItemBackgroundColor) {
        this.listingItemBackgroundColor = listingItemBackgroundColor;
    }

    public Boolean getAdMob() {
        return adMob;
    }

    public void setAdMob(Boolean adMob) {
        this.adMob = adMob;
    }

    public int getAdMobLimit() {
        return adMobLimit;
    }

    public void setAdMobLimit(int adMobLimit) {
        this.adMobLimit = adMobLimit;
    }

    public String getAdMobBannerId() {
        return adMobBannerId;
    }

    public void setAdMobBannerId(String adMobBannerId) {
        this.adMobBannerId = adMobBannerId;
    }

    public String getAdMobInterstitialId() {
        return adMobInterstitialId;
    }

    public void setAdMobInterstitialId(String adMobInterstitialId) {
        this.adMobInterstitialId = adMobInterstitialId;
    }

    public Object getStartAppId() {
        return startAppId;
    }

    public void setStartAppId(Object startAppId) {
        this.startAppId = startAppId;
    }

    public int getRowDisplay() {
        return rowDisplay;
    }

    public void setRowDisplay(int rowDisplay) {
        this.rowDisplay = rowDisplay;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public ArrayList<String> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
    }

    public String getYouTubeApiKey() {
        return youTubeApiKey;
    }

    public void setYouTubeApiKey(String youTubeApiKey) {
        this.youTubeApiKey = youTubeApiKey;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Boolean getIncludeHeader() {
        return includeHeader;
    }

    public void setIncludeHeader(Boolean includeHeader) {
        this.includeHeader = includeHeader;
    }

    public String getRedirectLink() {
        return redirectLink;
    }

    public void setRedirectLink(String redirectLink) {
        this.redirectLink = redirectLink;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getGrideViewPost() {
        return grideViewPost;
    }

    public void setGrideViewPost(int grideViewPost) {
        this.grideViewPost = grideViewPost;
    }

    public String getAdmobAppId() {
        return admobAppId;
    }

    public void setAdmobAppId(String admobAppId) {
        this.admobAppId = admobAppId;
    }

    public void saveApplicationSettings(Context context , ApplicationSettings obj){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Application_Settings" , MODE_PRIVATE);
        //set variables of 'myObject', etc.

        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        prefsEditor.apply();
    }

    public ApplicationSettings retrieveApplicationSettings(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Application_Settings" , MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("MyObject", "");
        ApplicationSettings obj = gson.fromJson(json, ApplicationSettings.class);
        return obj;
    }


}
