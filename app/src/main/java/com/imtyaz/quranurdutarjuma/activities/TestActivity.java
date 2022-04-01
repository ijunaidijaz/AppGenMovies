package com.imtyaz.quranurdutarjuma.activities;

import static com.applovin.mediation.AppLovinUtils.ServerParameterKeys.ZONE_ID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyZone;
import com.imtyaz.quranurdutarjuma.R;
import com.yodo1.mas.Yodo1Mas;
import com.yodo1.mas.error.Yodo1MasError;
import com.yodo1.mas.event.Yodo1MasAdEvent;

import java.lang.reflect.Method;

public class TestActivity extends AppCompatActivity {
    private final String APP_ID = "app6ff0e0a799244a0ebd";
    private final String ZONE_ID = "vzdebea15f3c9947c686";
    private final String TAG = "AdColonyDemo";

    private Button showButton;
    private ProgressBar progress;
    private AdColonyInterstitial ad;
    private AdColonyInterstitialListener listener;
    private AdColonyAdOptions adOptions;
    private final Yodo1Mas.RewardListener rewardListener = new Yodo1Mas.RewardListener() {
        @Override
        public void onAdOpened(@NonNull Yodo1MasAdEvent event) {

        }

        @Override
        public void onAdvertRewardEarned(@NonNull Yodo1MasAdEvent event) {

        }

        @Override
        public void onAdError(@NonNull Yodo1MasAdEvent event, @NonNull Yodo1MasError error) {
            Toast.makeText(TestActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    private final Yodo1Mas.InterstitialListener interstitialListener = new Yodo1Mas.InterstitialListener() {
        @Override
        public void onAdOpened(@NonNull Yodo1MasAdEvent event) {
        }

        @Override
        public void onAdError(@NonNull Yodo1MasAdEvent event, @NonNull Yodo1MasError error) {
            Toast.makeText(TestActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            Log.d("TAG", "onAdError: "+error.toString());
        }

        @Override
        public void onAdClosed(@NonNull Yodo1MasAdEvent event) {
        }
    };

    private final Yodo1Mas.BannerListener bannerListener = new Yodo1Mas.BannerListener() {
        @Override
        public void onAdOpened(@NonNull Yodo1MasAdEvent event) {

        }

        @Override
        public void onAdError(@NonNull Yodo1MasAdEvent event, @NonNull Yodo1MasError error) {
            Toast.makeText(TestActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdClosed(@NonNull Yodo1MasAdEvent event) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        progress = findViewById(R.id.progress);
        setColonyAds();
//        initializeYodo1Ads();
//        findViewById(R.id.yodo1_demo_video).setOnClickListener(this::showVideo);
//        findViewById(R.id.yodo1_demo_interstitial).setOnClickListener(this::showInterstitial);
//        findViewById(R.id.yodo1_demo_banner).setOnClickListener(this::showBanner);
        showButton = findViewById(R.id.yodo1_demo_interstitial);
        showButton.setOnClickListener(v -> ad.show());
    }
    public void initializeYodo1Ads() {
        Yodo1Mas.getInstance().init(this, "7P5zgkTJun", new Yodo1Mas.InitListener() {
            @Override
            public void onMasInitSuccessful() {
                Toast.makeText(TestActivity.this, "sdk init successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMasInitFailed(@NonNull Yodo1MasError error) {
                Toast.makeText(TestActivity.this, "init failed", Toast.LENGTH_SHORT).show();
            }
        });
        Yodo1Mas.getInstance().setInterstitialListener(interstitialListener);
        Yodo1Mas.getInstance().setBannerListener(bannerListener);
        Yodo1Mas.getInstance().showBannerAd(TestActivity.this, "mas_test");

    }
    private void showVideo(View v) {
        if (!Yodo1Mas.getInstance().isRewardedAdLoaded()) {
            Toast.makeText(this, "Rewarded video ad has not been cached.", Toast.LENGTH_SHORT).show();
            return;
        }
        Yodo1Mas.getInstance().showRewardedAd(this);
    }

    private void showInterstitial(View v) {
        if (!Yodo1Mas.getInstance().isInterstitialAdLoaded()) {
            Toast.makeText(this, "Interstitial ad has not been cached.", Toast.LENGTH_SHORT).show();
            return;
        }
        Yodo1Mas.getInstance().showInterstitialAd(this);
    }

    private void showBanner(View v) {
        int align = Yodo1Mas.BannerTop | Yodo1Mas.BannerHorizontalCenter;
        Yodo1Mas.getInstance().showBannerAd(TestActivity.this, align);
//        if (!Yodo1Mas.getInstance().isBannerAdLoaded()) {
//            Toast.makeText(this, "Banner ad has not been cached.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        String placement = "placementId";
//
//        /**
//         * 'align' will determine the general position of the banner, such as:
//         *       - top horizontal center
//         *       - bottom horizontal center
//         *       - left vertical center
//         *       - right vertical center
//         *       - horizontal vertical center
//         *        The above 5 positions can basically meet most of the needs
//         *
//         * align = vertical | horizontal
//         *              vertical:
//         *              Yodo1Mas.BannerTop
//         *              Yodo1Mas.BannerBottom
//         *              Yodo1Mas.BannerVerticalCenter
//         *              horizontal:
//         *              Yodo1Mas.BannerLeft
//         *              Yodo1Mas.BannerRight
//         */
//        int align = Yodo1Mas.BannerBottom | Yodo1Mas.BannerHorizontalCenter;
//
//        /**
//         * 'offset' will adjust the position of the banner on the basis of 'align'
//         *  If 'align' cannot meet the demand, you can adjust it by 'offset'
//         *
//         *  horizontal offset:
//         *  offsetX > 0, the banner will move to the right.
//         *  offsetX < 0, the banner will move to the left.
//         *  if align = Yodo1Mas.BannerLeft, offsetX < 0 is invalid
//         *
//         *  vertical offset:
//         *  offsetY > 0, the banner will move to the bottom.
//         *  offsetY < 0, the banner will move to the top.
//         *  if align = Yodo1Mas.BannerTop, offsetY < 0 is invalid
//         *
//         *  Click here to see more details: https://developers.yodo1.com/knowledge-base/android-banner-configuration/
//         */
//        int offsetX = 0;
//        int offsetY = 0;
//        Yodo1Mas.getInstance().showBannerAd(this, placement, align, offsetX, offsetY);
//    }
//
//    private void showAppLovinMediationDebugger(View v) {
//        try {
//            Class<?> applovinSdkClass = Class.forName("com.applovin.sdk.AppLovinSdk");
//            Method instanceMethod = applovinSdkClass.getDeclaredMethod("getInstance", Context.class);
//            Object obj = instanceMethod.invoke(applovinSdkClass, TestActivity.this);
//            Method debuggerMethod = applovinSdkClass.getMethod("showMediationDebugger");
//            debuggerMethod.invoke(obj);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void showAdMobMediationTestSuite(View v) {
//        MediationTestSuite.launch(this);
    }
    private  void setColonyAds(){
        AdColonyAppOptions appOptions = new AdColonyAppOptions()
                .setUserID("unique_user_id")
                .setKeepScreenOn(true);
        AdColony.configure(this, appOptions, APP_ID);
        adOptions = new AdColonyAdOptions();
        listener = new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(AdColonyInterstitial ad) {
                // Ad passed back in request filled callback, ad can now be shown
                TestActivity.this.ad = ad;
                showButton.setEnabled(true);
                progress.setVisibility(View.INVISIBLE);
                Log.d(TAG, "onRequestFilled");
            }

            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
                // Ad request was not filled
                progress.setVisibility(View.INVISIBLE);
                Log.d(TAG, "onRequestNotFilled");
            }

            @Override
            public void onOpened(AdColonyInterstitial ad) {
                // Ad opened, reset UI to reflect state change
                showButton.setEnabled(false);
                progress.setVisibility(View.VISIBLE);
                Log.d(TAG, "onOpened");
            }

            @Override
            public void onExpiring(AdColonyInterstitial ad) {
                // Request a new ad if ad is expiring
                showButton.setEnabled(false);
                progress.setVisibility(View.VISIBLE);
                AdColony.requestInterstitial(ZONE_ID, this, adOptions);
                Log.d(TAG, "onExpiring");
            }
        };

        // Set up button to show an ad when clicked


    }
    @Override
    protected void onResume() {
        super.onResume();

        // It's somewhat arbitrary when your ad request should be made. Here we are simply making
        // a request if there is no valid ad available onResume, but really this can be done at any
        // reasonable time before you plan on showing an ad.
        if (ad == null || ad.isExpired()) {
            // Optionally update location info in the ad options for each request:
            // LocationManager locationManager =
            //     (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            // Location location =
            //     locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            // adOptions.setUserMetadata(adOptions.getUserMetadata().setUserLocation(location));
            progress.setVisibility(View.VISIBLE);
            AdColony.requestInterstitial(ZONE_ID, listener, adOptions);
        }
    }

}