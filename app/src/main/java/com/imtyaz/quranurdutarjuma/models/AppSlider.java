package com.imtyaz.quranurdutarjuma.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AppSlider implements Serializable {

    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Url")
    @Expose
    private String url;
    @SerializedName("Active")
    @Expose
    private boolean active;
    @SerializedName("SortNumber")
    @Expose
    private int sortNumber;
    @SerializedName("SubcategoryId")
    @Expose
    private int subcategoryId;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("RedirectApp")
    @Expose
    private String redirectApp;
    @SerializedName("WebUrl")
    @Expose
    private String webUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(int sortNumber) {
        this.sortNumber = sortNumber;
    }

    public int getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(int subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRedirectApp() {
        if (redirectApp == null) {
            redirectApp = "";
        }
        return redirectApp;
    }

    public void setRedirectApp(String redirectApp) {
        this.redirectApp = redirectApp;
    }

    public String getWebUrl() {
        if (webUrl == null) {
            webUrl = "";
        }
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}
