package com.umer.application.app;

import android.app.Application;
import android.os.StrictMode;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainApp extends Application {
    public static MainApp INSTANCE;

    public static MainApp getAppContext() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE=this;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        MobileAds.initialize(getApplicationContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
Toast.makeText(getAppContext(),initializationStatus.toString(),Toast.LENGTH_SHORT).show();
                //Showing a simple Toast Message to the user when The Google AdMob Sdk Initialization is Completed

            }
        });


    }
}
