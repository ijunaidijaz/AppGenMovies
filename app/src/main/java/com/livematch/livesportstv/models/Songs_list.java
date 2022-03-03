package com.livematch.livesportstv.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Songs_list implements Serializable {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Url")
    @Expose
    private String url;
    @SerializedName("Active")
    @Expose
    private Boolean active;
    @SerializedName("SortNumber")
    @Expose
    private Integer sortNumber;
    @SerializedName("SubcategoryId")
    @Expose
    private Integer subcategoryId;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("RedirectApp")
    @Expose
    private String redirectApp;
    @SerializedName("WebUrl")
    @Expose
    private String webUrl;

    public Songs_list(Integer id, String title, String url, Boolean active, Integer sortNumber, Integer subcategoryId, String type, String redirectApp, String webUrl) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.active = active;
        this.sortNumber = sortNumber;
        this.subcategoryId = subcategoryId;
        this.type = type;
        this.redirectApp = redirectApp;
        this.webUrl = webUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }

    public Integer getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(Integer subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRedirectApp() {
        return redirectApp;
    }

    public void setRedirectApp(String redirectApp) {
        this.redirectApp = redirectApp;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}
