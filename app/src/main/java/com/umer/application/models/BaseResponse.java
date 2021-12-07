package com.umer.application.models;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.umer.application.networks.ResponseCode;

import java.io.Serializable;

import retrofit2.Response;

public class BaseResponse implements Serializable {

    @SerializedName("statusCode")
    @Expose
    public String statusCode;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("url")
    @Expose
    public String url;

    public BaseResponse() {
    }

    public BaseResponse(String message) {
        this.message = message;
    }

    public static boolean isSuccess(Response response) {
        boolean check = false;
        try {

            return ResponseCode.isBetweenSuccessRange(response.code());
            /*if (response.code() == ResponseCode.SUCCESS) {
                check = true;
            }*/
        } catch (Exception e) {
            check = false;
            Log.e("Base Response check=" + check, "Exception" + e.toString());

        }
        return check;
    }
}
