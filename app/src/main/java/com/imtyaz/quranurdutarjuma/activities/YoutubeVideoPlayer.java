package com.imtyaz.quranurdutarjuma.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.applovin.mediation.ads.MaxInterstitialAd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.imtyaz.quranurdutarjuma.R;
import com.imtyaz.quranurdutarjuma.models.ApplicationSettings;
import com.imtyaz.quranurdutarjuma.utils.AdsTypes;
import com.startapp.sdk.adsbase.StartAppAd;

public class YoutubeVideoPlayer extends YouTubeBaseActivity {
    YouTubePlayerView youtubeVideoView;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    String videoId;
    ApplicationSettings applicationSettings = new ApplicationSettings();
    MaxInterstitialAd maxinterstitialAd;
    Dialog dialog;
    private com.google.android.gms.ads.AdView admobAdView;
    private InterstitialAd admobInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_youtube_video_player);
        applicationSettings = applicationSettings.retrieveApplicationSettings(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        videoId = bundle.getString("VIDEO_URL");
        initViews();
        loadAds();
        new Handler().postDelayed(() -> {
            showAd();
        }, 3000);


        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                youTubePlayer.loadVideo(videoId);
                youTubePlayer.play();
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

//        youtubeVideoView.initialize(getResources().getString(R.string.YOUTUBE_API_KEY), onInitializedListener);
        youtubeVideoView.initialize(applicationSettings.getYouTubeApiKey(), onInitializedListener);


    }

    private void initViews() {

        youtubeVideoView = findViewById(R.id.youtubeVideoView);
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    public void loadAds() {
        if (applicationSettings.getAdds() == AdsTypes.admobAds) {
            admobInterstitialAds();
        } else if (applicationSettings.getAdds() == AdsTypes.facebooksAds) {
            maxInterstitialAd();
        }
    }

    public void showAd() {
        if (MainActivity.clickCount >= applicationSettings.getAdMobLimit()) {
            if (applicationSettings.getAdds() == AdsTypes.admobAds) {
                if (admobInterstitialAd.isLoaded()) {
                    showingAdDialog();
                    new Handler().postDelayed(() -> {
                        cancelShowingAdDialog();
                        admobInterstitialAd.show();
                    }, 2000);

                }
                MainActivity.clickCount = 0;
            } else if (applicationSettings.getAdds() == AdsTypes.facebooksAds) {
                if (maxinterstitialAd.isReady()) {
                    showingAdDialog();
                    new Handler().postDelayed(() -> {
                        cancelShowingAdDialog();
                        maxinterstitialAd.showAd();
                    }, 2000);
                }
                MainActivity.clickCount = 0;

            } else if (applicationSettings.getAdds() == AdsTypes.startAppAds) {
                showingAdDialog();
                new Handler().postDelayed(() -> {
                    cancelShowingAdDialog();
                    StartAppAd.showAd(this);
                }, 2000);

                MainActivity.clickCount = 0;
            }
            loadAds();
        }
    }

    public void admobInterstitialAds() {
        admobInterstitialAd = new InterstitialAd(this);
        admobInterstitialAd.setAdUnitId(getResources().getString(R.string.ADMOB_INTER_ID));
        admobInterstitialAd.loadAd(new AdRequest.Builder().build());
        AdRequest adRequest = new AdRequest.Builder().build();
    }

    void maxInterstitialAd() {
        maxinterstitialAd = new MaxInterstitialAd(getResources().getString(R.string.APPLOVIN_INTER_ID), this);
        // Load the first ad
        maxinterstitialAd.loadAd();
    }

    public void showingAdDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.showing_ad);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public void cancelShowingAdDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}