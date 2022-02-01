package com.umer.application.networks;

import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.google.gson.stream.MalformedJsonException;
import com.umer.application.R;
import com.umer.application.app.BaseClass;
import com.umer.application.app.MainApp;
import com.umer.application.models.BaseResponse;
import com.umer.application.utils.Configurations;
import com.umer.application.utils.Constants;
import com.umer.application.utils.Loading;
import com.umer.application.utils.Utils;
import com.umer.application.utils.ViewDialog;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.ParseException;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by fah33m on 21/05/16.
 */
public class NetworkCall extends BaseClass {


    public ViewDialog viewDialog;
    private Object taggedObject;
    private OnNetworkResponse callback;
    private Call request;
    Loading loading;


    private NetworkCall() {
    }

    public static synchronized NetworkCall make() {
        return new NetworkCall();
    }

    public NetworkCall setCallback(OnNetworkResponse callback) {
        this.callback = callback;
        return this;
    }


    public NetworkCall enque(Call call) {
        this.request = call;
        return this;
    }

    public NetworkCall setTag(Object tag) {
        this.taggedObject = tag;
        return this;
    }

    public NetworkCall autoLoadingCancel(Loading loading) {
        this.loading = loading;
        return this;
    }

//    public NetworkCall autoLoading(FragmentManager manager) {
//        this.viewDialog = new ViewDialog();
//        if (viewDialog.isVisible() && viewDialog.isResumed()) {
//            viewDialog.dismiss();
//        } else {
//            viewDialog.show(manager, Constants.tagViewDialog);
//        }
//        return this;
//    }

    public NetworkCall execute() {
        request.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                cancelLoading();
                if (BaseResponse.isSuccess(response)) {
                    callback.onSuccess(call, response, taggedObject);
                } else if (BaseResponse.isSessionExpired(response)) {
//                    MainApp.resetApplication();
//                    Notify.ToastLong("Your session has been expired, Please login again");
                } else if (response.body() == null || !BaseResponse.isSuccess(response)) {
                    try {
                        callback.onFailure(call, Network.parseErrorResponse(response), taggedObject);
                    } catch (Exception e) {
                        callback.onFailure(call, new BaseResponse(Configurations.isProduction() ? string(R.string.exceptionInErrorResponse) : string(R.string.invalid_data)), taggedObject);
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                cancelLoading();
                BaseResponse response;
                if (t != null) {
                    if (Utils.isCause(SocketTimeoutException.class, t)) {
                        response = new BaseResponse(string(R.string.timeout));
                    } else if (Utils.isCause(ConnectException.class, t)) {
                        response = new BaseResponse(string(R.string.connectException));
                    } else if (Utils.isCause(MalformedJsonException.class, t)) {
                        response = new BaseResponse(string(R.string.invalid_data));
                    } else if (t instanceof SSLHandshakeException || t instanceof SSLException) {
                        response = new BaseResponse(string(R.string.ssl));
                    } else if (t instanceof IOException) {
                        response = new BaseResponse(string(R.string.no_internet));
                    } else {
                        response = new BaseResponse(Configurations.isDevelopment() ? t.getMessage() : string(R.string.text_somethingwentwrong));
                    }
                    callback.onFailure(call, response, taggedObject);
                } else {
                    response = new BaseResponse(string(R.string.text_somethingwentwrong));
                    callback.onFailure(call, response, taggedObject);
                }
            }
        });
        return this;
    }

    public void cancelLoading() {
//        if (viewDialog != null && viewDialog.isVisible() && viewDialog.isResumed()) {
//            viewDialog.dismiss();
//        }
        if (loading != null && loading.isVisible()) {
            loading.cancel();
        }
    }

}

