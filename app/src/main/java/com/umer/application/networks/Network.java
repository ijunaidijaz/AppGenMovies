package com.umer.application.networks;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.umer.application.utils.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by fah33m on 21/05/16.
 */
public class Network {
    /*Without token*/
    static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    private static Network object;
    private static ApiServices services;
    private static Retrofit retrofit = null;


    public Network() {
        object = this;
        OkHttpClient.Builder httpClient = HeaderToken();
        OkHttpClient clientWithToken = httpClient.build();
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);
        okHttpClient.addNetworkInterceptor(new StethoInterceptor());
        OkHttpClient clientWithoutToken = httpClient.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL).client(clientWithoutToken)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        services = retrofit.create(ApiServices.class);

    }

    public static Retrofit getClientWithToken() {

        return retrofit;
    }


    public static OkHttpClient.Builder HeaderToken() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(1, TimeUnit.MINUTES);
        httpClient.readTimeout(1, TimeUnit.MINUTES);
        httpClient.addNetworkInterceptor(new StethoInterceptor());
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            Request request = original.newBuilder()
                    .method(original.method(), original.body())
                    .build();

            return chain.proceed(request);
        });
        return httpClient;
    }

    public synchronized static Network getInstance() {
        if (object == null) {
            object = new Network();
        }
        return object;
    }


    public static ApiServices apis() {
        return Network.getInstance().getApiServices();
    }

    public static String getBaseUrl() {
        return getNetworkClient().baseUrl().toString();
    }

    private static Retrofit getNetworkClient() {
        if (object == null)
            getInstance();
        return getInstance().retrofit;
    }

    public ApiServices getApiServices() {
        return object.services;
    }

}