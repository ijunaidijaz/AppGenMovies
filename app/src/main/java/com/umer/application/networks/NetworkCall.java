package com.umer.application.networks;

import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.umer.application.models.BaseResponse;
import com.umer.application.utils.Constants;
import com.umer.application.utils.ViewDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by fah33m on 21/05/16.
 */
public class NetworkCall {


    public ViewDialog viewDialog;
    private Object taggedObject;
    private OnNetworkResponse callback;
    private Call request;


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

    public NetworkCall autoLoading(FragmentManager manager) {
        this.viewDialog = new ViewDialog();
        if (viewDialog.isVisible() && viewDialog.isResumed()) {
            viewDialog.dismiss();
        } else {
            viewDialog.show(manager, Constants.tagViewDialog);
        }
        return this;
    }

    public NetworkCall execute() {
        request.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                cancelLoading();
                if (BaseResponse.isSuccess(response)) {
                    Log.e("CallBack Method", "=" + call.request().url().toString());
                    callback.onSuccess(call, response, taggedObject);

                } else if (response.body() == null || !BaseResponse.isSuccess(response)) {
                    Log.e("CallBack Method", "=" + call.request().url().toString());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("CallBack Method Failure", "=" + t.getMessage());
            }
        });
        return this;
    }

    public void cancelLoading() {
        if (viewDialog != null && viewDialog.isVisible() && viewDialog.isResumed()) {
            viewDialog.dismiss();
        }
    }

}

