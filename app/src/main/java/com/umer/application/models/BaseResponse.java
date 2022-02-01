package com.umer.application.models;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.umer.application.app.BaseClass;
import com.umer.application.networks.ResponseCode;

import java.io.Serializable;

import retrofit2.Response;

public class BaseResponse extends BaseClass implements Serializable {

    @SerializedName("statusCode")
    @Expose
    public String statusCode;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("code")
    @Expose
    private int code;
    public BaseResponse() {
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public BaseResponse(String message) {
        this.message = message;
    }

    public static boolean isSuccess(Response response) {
        try {
            return ResponseCode.isBetweenSuccessRange(response.code());
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isUnAuthorized(Response response) {
        try {
            return response.code() == ResponseCode.FORBIDDEN
                    || response.code() == ResponseCode.UN_AUTHORIZED;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isSessionExpired(Response response) {
        try {
            return response.code() == ResponseCode.FORBIDDEN;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean appNotUpdated(Response response) {
        try {
            return ((BaseResponse) response.body()).getCode() == ResponseCode.APP_NOT_UPDATED;
        } catch (Exception e) {
            return false;
        }
    }
}
