package com.livematch.livesportstv.networks;


import com.livematch.livesportstv.models.BaseResponse;

import retrofit2.Call;
import retrofit2.Response;


public interface OnNetworkResponse {

    void onSuccess(Call call, Response response, Object tag);

    void onFailure(Call call, BaseResponse response, Object tag);

}
