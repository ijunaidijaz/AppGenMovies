package com.free.newhdmovies.app;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.free.newhdmovies.BuildConfig;
import com.free.newhdmovies.R;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.startapp.sdk.adsbase.StartAppAd;

public class MainApp extends Application {
    public static MainApp INSTANCE;
    private Activity activity;
    public static MainApp getAppContext() {
        return INSTANCE;
    }
    SharedPreferences prefs = null;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        StartAppAd.disableAutoInterstitial();
        StartAppAd.disableSplash();
//        StartAppSDK.init(getAppContext(), getResources().getString(R.string.STARTAPP_APP_ID), false);
        MobileAds.initialize(getApplicationContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
//                Toast.makeText(getAppContext(), initializationStatus.toString(), Toast.LENGTH_SHORT).show();
                //Showing a simple Toast Message to the user when The Google AdMob Sdk Initialization is Completed

            }
        });
        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration) {
                // AppLovin SDK is initialized, start loading ads
            }
        });
        FirebaseMessaging.getInstance().subscribeToTopic("myTopic");

    }
    public Activity getCurrentActivity() {
        return activity;
    }

    public void setCurrentActivity(Activity activity) {
        this.activity = activity;
    }
}
