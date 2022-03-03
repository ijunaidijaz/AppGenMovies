package com.livematch.livesportstv.models;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.livematch.livesportstv.app.MainApp;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ApplicationSettings implements Serializable {

    @SerializedName("IsYoutubePost")
    @Expose
    public Boolean isYoutubePost;
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
    private String adMobLimit;
    @SerializedName("AdMobBannerId")
    @Expose
    private String adMobBannerId;
    @SerializedName("AdMobInterstitialId")
    @Expose
    private String adMobInterstitialId;
    @SerializedName("StartAppId")
    @Expose
    private String startAppId;
    @SerializedName("RowDisplay")
    @Expose
    private Integer rowDisplay;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("Category")
    @Expose
    private String category;
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
    private String grideViewPost;
    @SerializedName("AdmobAppId")
    @Expose
    private String admobAppId;
    @SerializedName("Adds")
    @Expose
    private Integer adds;
    @SerializedName("ApplovinBannerId")
    @Expose
    private String applovinBannerId;
    @SerializedName("ApplovinInterstialAdId")
    @Expose
    private String applovinInterstialAdId;
    @SerializedName("FyberBannerAdId")
    @Expose
    private String fyberBannerAdId;
    @SerializedName("FacebookRewardedVideo")
    @Expose
    private String facebookRewardedVideo;
    @SerializedName("PostCategory")
    @Expose
    private PostCategory postCategory;
    @SerializedName("PostCategoryId")
    @Expose
    private String postCategoryId;
    @SerializedName("AppSubCategories")
    @Expose
    private List<AppSubCategory> appSubCategories = null;

    public static void saveApplicationSettings(Context context, ApplicationSettings obj) {
        SharedPreferences mPrefs = MainApp.getAppContext().getSharedPreferences("Application_Settings", MODE_PRIVATE);
        //set variables of 'myObject', etc.

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        prefsEditor.putString("MyObject", json);
        prefsEditor.commit();
        prefsEditor.apply();
    }

    public Boolean getIsYoutubePost() {
        return isYoutubePost;
    }

    public void setIsYoutubePost(Boolean isYoutubePost) {
        this.isYoutubePost = isYoutubePost;
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
        return Integer.parseInt(adMobLimit);
    }

    public void setAdMobLimit(String adMobLimit) {
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

    public Integer getRowDisplay() {
        return rowDisplay;
    }

    public void setRowDisplay(Integer rowDisplay) {
        this.rowDisplay = rowDisplay;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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

    public String getGrideViewPost() {
        return grideViewPost;
    }

    public void setGrideViewPost(String grideViewPost) {
        this.grideViewPost = grideViewPost;
    }

    public String getAdmobAppId() {
        return admobAppId;
    }

    public void setAdmobAppId(String admobAppId) {
        this.admobAppId = admobAppId;
    }

    public Integer getAdds() {
        return adds;
    }

    public void setAdds(Integer adds) {
        this.adds = adds;
    }

    public String getApplovinBannerId() {
        return applovinBannerId;
    }

    public void setApplovinBannerId(String applovinBannerId) {
        this.applovinBannerId = applovinBannerId;
    }

    public String getApplovinInterstialAdId() {
        return applovinInterstialAdId;
    }

    public void setApplovinInterstialAdId(String applovinInterstialAdId) {
        this.applovinInterstialAdId = applovinInterstialAdId;
    }

    public String getFyberBannerAdId() {
        return fyberBannerAdId;
    }

    public void setFyberBannerAdId(String fyberBannerAdId) {
        this.fyberBannerAdId = fyberBannerAdId;
    }

    public Boolean getYoutubePost() {
        return isYoutubePost;
    }

    public void setYoutubePost(Boolean youtubePost) {
        isYoutubePost = youtubePost;
    }

    public String getStartAppId() {
        return startAppId;
    }

    public void setStartAppId(String startAppId) {
        this.startAppId = startAppId;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getFacebookRewardedVideo() {
        return facebookRewardedVideo;
    }

    public void setFacebookRewardedVideo(String facebookRewardedVideo) {
        this.facebookRewardedVideo = facebookRewardedVideo;
    }

    public String getPostCategoryId() {
        return postCategoryId;
    }

    public void setPostCategoryId(String postCategoryId) {
        this.postCategoryId = postCategoryId;
    }

    public PostCategory getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(PostCategory postCategory) {
        this.postCategory = postCategory;
    }

    public List<AppSubCategory> getAppSubCategories() {
        return appSubCategories;
    }

    public void setAppSubCategories(List<AppSubCategory> appSubCategories) {
        this.appSubCategories = appSubCategories;

    }

    public ApplicationSettings retrieveApplicationSettings(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Application_Settings", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("MyObject", "");
        ApplicationSettings obj = gson.fromJson(json, ApplicationSettings.class);
        return obj;
    }

    public List<AppSlider> getSlider(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ApplicationSlider", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("ApplicationSlider", "");
        Type type = new TypeToken<List<AppSlider>>() {
        }.getType();
        List<AppSlider> list = gson.fromJson(json, type);
        return list;
    }

    public void saveSlider(ArrayList<AppSlider> appSlider) {
        SharedPreferences mPrefs = MainApp.getAppContext().getSharedPreferences("ApplicationSlider", MODE_PRIVATE);
        //set variables of 'myObject', etc.

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(appSlider);
        prefsEditor.putString("ApplicationSlider", json);
        prefsEditor.commit();
        prefsEditor.apply();
    }

    public class PostCategory implements Serializable {

        @SerializedName("Id")
        @Expose
        private Integer id;
        @SerializedName("Name")
        @Expose
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


    }
}
