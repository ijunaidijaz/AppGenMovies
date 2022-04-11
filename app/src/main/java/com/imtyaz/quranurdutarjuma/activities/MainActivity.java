package com.imtyaz.quranurdutarjuma.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyZone;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinSdkUtils;
import com.imtyaz.quranurdutarjuma.R;
import com.imtyaz.quranurdutarjuma.databinding.MainActivityBinding;
import com.imtyaz.quranurdutarjuma.fragments.HomeFragment;
import com.imtyaz.quranurdutarjuma.fragments.MovieListFragment;
import com.imtyaz.quranurdutarjuma.fragments.ServerLinksFragment;
import com.imtyaz.quranurdutarjuma.fragments.VideoListFragment;
import com.imtyaz.quranurdutarjuma.fragments.WatchNowFirstFragment;
import com.imtyaz.quranurdutarjuma.fragments.WatchNowSecondFragment;
import com.imtyaz.quranurdutarjuma.models.ApplicationSettings;
import com.imtyaz.quranurdutarjuma.models.BaseResponse;
import com.imtyaz.quranurdutarjuma.models.Songs_list;
import com.imtyaz.quranurdutarjuma.models.singlePost;
import com.imtyaz.quranurdutarjuma.networks.Network;
import com.imtyaz.quranurdutarjuma.networks.NetworkCall;
import com.imtyaz.quranurdutarjuma.networks.OnNetworkResponse;
import com.imtyaz.quranurdutarjuma.utils.AdsTypes;
import com.imtyaz.quranurdutarjuma.utils.RequestCodes;
import com.imtyaz.quranurdutarjuma.utils.Utils;
import com.yodo1.mas.Yodo1Mas;
import com.yodo1.mas.banner.Yodo1MasBannerAdListener;
import com.yodo1.mas.banner.Yodo1MasBannerAdSize;
import com.yodo1.mas.banner.Yodo1MasBannerAdView;
import com.yodo1.mas.error.Yodo1MasError;
import com.yodo1.mas.event.Yodo1MasAdEvent;
import com.yodo1.mas.helper.model.Yodo1MasAdBuildConfig;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnNetworkResponse {

    public static int clickCount = 0;
    public static ApplicationSettings applicationSettings;
    public boolean isSingleVideoFrag = false, isCategory = false;
    ArrayList<Songs_list> songsList;
    ArrayList<Songs_list> categoryList, categoryTwoList = new ArrayList<>();
    int itemId = 0;
    int itemPosition;
    //    static GridViewActivity instance;
    String inFragment = "";
    MainActivityBinding binding;
    MaxAdView maxAdView;
    String categoryName;
    MaxInterstitialAd maxinterstitialAd;
    Dialog dialog;
    private com.google.android.gms.ads.AdView admobAdView;
    private final String TAG = "AdColonyDemo";

    private Button showButton;
    private ProgressBar progress;
    AdColonyInterstitial colonyInterstitial;
    private AdColonyInterstitialListener listener;
    boolean isColonyAdLoaded = false;
    AdColonyAdOptions adOptions;
    AdColonyAdViewListener adColonyAdViewListener;
    AdColonyAdView adView;
    //    private InterstitialAd admobInterstitialAd;

    public final Yodo1Mas.InterstitialListener interstitialListener = new Yodo1Mas.InterstitialListener() {
        @Override
        public void onAdOpened(@NonNull Yodo1MasAdEvent event) {
            Log.d("TAG", "onAdOpened: interstitial");
        }

        @Override
        public void onAdError(@NonNull Yodo1MasAdEvent event, @NonNull Yodo1MasError error) {
            Log.d("TAG", "onAdError: interstitial");
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
//            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdClosed(@NonNull Yodo1MasAdEvent event) {
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainActivityBinding.inflate(getLayoutInflater());
        applicationSettings = (ApplicationSettings) getIntent().getSerializableExtra("applicationSettings");
        binding.setSettings(applicationSettings);
        setContentView(binding.getRoot());
        fragmentTrx(new HomeFragment(), null, "HomeFragment");
        Yodo1MasAdBuildConfig config = new Yodo1MasAdBuildConfig.Builder()
                .enableAdaptiveBanner(true)
                .enableUserPrivacyDialog(true)
                .build();
        Yodo1Mas.getInstance().setAdBuildConfig(config);
        initializeYodo1Ads();
        setColonyAds();
        loadAds();
        loadBanners();
    }

    public void initializeYodo1Ads() {
        Yodo1Mas.getInstance().init(this, applicationSettings.getYodo1AppId(), new Yodo1Mas.InitListener() {
            @Override
            public void onMasInitSuccessful() {
//                Toast.makeText(MainActivity.this, "sdk init successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMasInitFailed(@NonNull Yodo1MasError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
//        Yodo1Mas.getInstance().setRewardListener(rewardListener);
        Yodo1Mas.getInstance().setInterstitialListener(interstitialListener);
        Yodo1Mas.getInstance().setBannerListener(bannerListener);

    }

    private void setColonyAds() {
        AdColonyAppOptions appOptions = new AdColonyAppOptions()
                .setUserID("unique_user_id")
                .setKeepScreenOn(true);
        AdColony.configure(this, appOptions, applicationSettings.getAdmobAppId());
        AdColonyAdOptions adOptions = new AdColonyAdOptions();

    }

    public void getSinglePost(int id) {
        showAd();
        NetworkCall.make()
                .setCallback(this)
                .autoLoadingCancel(Utils.getLoading(this, "Loading..."))
                .setTag(RequestCodes.API.GET_SINGLE_POST)
                .enque(new Network().apis().getSinglePost(id))
                .execute();

    }


    @Override
    public void onSuccess(Call call, Response response, Object tag) {
        switch ((int) tag) {
            case RequestCodes.API.GET_SINGLE_POST:
                if (response.body() != null) {
                    singlePost singlePost1 = (singlePost) response.body();
                    singlePostResponseHandling(singlePost1);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(Call call, BaseResponse response, Object tag) {
        switch ((int) tag) {
            case RequestCodes.API.GET_ALL_POSTS:
            case RequestCodes.API.GET_SINGLE_POST:
        }
    }

    public void openAppOnPlayStore(String appPackageName) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void requestColonyBannerAd() {
        // Optional Ad specific options to be sent with request
        adColonyAdViewListener = new AdColonyAdViewListener() {
            @Override
            public void onRequestFilled(AdColonyAdView adColonyAdView) {
                Log.d(TAG, "onRequestFilled");
//                resetUI();
                binding.maxBanner.setVisibility(View.VISIBLE);
                binding.maxBanner.addView(adColonyAdView);
                adView = adColonyAdView;
            }

            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
                super.onRequestNotFilled(zone);
                Log.d(TAG, "onRequestNotFilled");
//                resetUI();
            }

            @Override
            public void onOpened(AdColonyAdView ad) {
                super.onOpened(ad);
                Log.d(TAG, "onOpened");
            }

            @Override
            public void onClosed(AdColonyAdView ad) {
                super.onClosed(ad);
                Log.d(TAG, "onClosed");
            }

            @Override
            public void onClicked(AdColonyAdView ad) {
                super.onClicked(ad);
                Log.d(TAG, "onClicked");
            }

            @Override
            public void onLeftApplication(AdColonyAdView ad) {
                super.onLeftApplication(ad);
                Log.d(TAG, "onLeftApplication");
            }
        };
        adOptions = new AdColonyAdOptions();

        //Request Ad
        AdColony.requestAdView(applicationSettings.getAdMobBannerId(), adColonyAdViewListener, AdColonyAdSize.BANNER, adOptions);
    }


    public void fragmentTrx(Fragment fragment, Bundle bundle, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_video_fragment, fragment, tag);
        fragment.setArguments(bundle);
        transaction.addToBackStack(fragment.getTag());
        transaction.detach(fragment);
        transaction.attach(fragment);
        transaction.commitAllowingStateLoss();

    }

    public void popBackStack() {
        FragmentManager fm = getLastFragmentManagerWithBack(getSupportFragmentManager());
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();

        }
    }

    public void clearAllFragmentStack() {
        FragmentManager fm = getLastFragmentManagerWithBack(getSupportFragmentManager());
        if (fm.getBackStackEntryCount() > 0) {
            for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
                fm.popBackStack();
            }
        }
    }

    private FragmentManager getLastFragmentManagerWithBack(FragmentManager fm) {
        FragmentManager fmLast = fm;
        List<Fragment> fragments = fm.getFragments();
        for (Fragment f : fragments) {
            if ((f.getChildFragmentManager() != null) && (f.getChildFragmentManager().getBackStackEntryCount() > 0)) {
                fmLast = f.getFragmentManager();
                FragmentManager fmChild = getLastFragmentManagerWithBack(f.getChildFragmentManager());
                if (fmChild != fmLast)
                    fmLast = fmChild;
            }
        }
        return fmLast;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.container_video_fragment);
            if (f instanceof MovieListFragment || f instanceof WatchNowFirstFragment || f instanceof WatchNowSecondFragment
                    || f instanceof VideoListFragment || f instanceof ServerLinksFragment) {
                popBackStack();
            } else {
                onButtonShowPopupWindowClick();
            }
        } else {
            onBackPressed();
        }
    }

    public void openVideoFragment(String keyword, String playListId, boolean isPlayList,
                                  int limit, boolean isYoutube, int Adds, String color, String imageUrl,
                                  String admob_interID, String faebook_interID) {
        Bundle bundle = new Bundle();
        bundle.putString("KEYWORD", keyword);
        bundle.putString("PLAYLIST_ID", playListId);
        bundle.putBoolean("isPLAYLIST", isPlayList);
        bundle.putInt("LIMIT", limit);
        bundle.putBoolean("isYoutube", isYoutube);
        bundle.putInt("ADDS", Adds);
        bundle.putString("Color", color);
        bundle.putString("appIcon", imageUrl);
        bundle.putString("ADMOB_INTER_ID", admob_interID);
        bundle.putString("FACEBOOK_INTER_ID", faebook_interID);
        VideoListFragment fragment = new VideoListFragment();
        fragmentTrx(fragment, bundle, "VideoListFragment");
    }

    public void openMovieListFragment(String keyword, String playListId, boolean isPlayList,
                                      int limit, boolean isYoutube, int Adds, String color, String imageUrl,
                                      String admob_interID, String faebook_interID) {
        Bundle bundle = new Bundle();
        bundle.putString("KEYWORD", keyword);
        bundle.putString("TITLE", categoryName);
        bundle.putString("PLAYLIST_ID", playListId);
        bundle.putBoolean("isPLAYLIST", isPlayList);
        bundle.putInt("LIMIT", limit);
        bundle.putBoolean("isYoutube", isYoutube);
        bundle.putInt("ADDS", Adds);
        bundle.putString("Color", color);
        bundle.putString("appIcon", imageUrl);
        bundle.putString("ADMOB_INTER_ID", admob_interID);
        bundle.putString("FACEBOOK_INTER_ID", faebook_interID);
        bundle.putSerializable("VideosList", songsList);
        bundle.putSerializable("applicationSettings", applicationSettings);
        MovieListFragment fragment = new MovieListFragment();
        fragmentTrx(fragment, bundle, "MovieListFragment");
    }

    public void openWebUrl(singlePost singlePost) {
        Intent i = new Intent(this, WebViewActivity.class);
        i.putExtra("WEBURL", singlePost.getBaseApi().getUrl());
        startActivity(i);
    }

    public void openWebUrl(String url) {
        Intent i = new Intent(this, WebViewActivity.class);
        i.putExtra("WEBURL", url);
        startActivity(i);
    }

    public void singlePostResponseHandling(singlePost singlePost1) {
        if (isSingleVideoFrag) {
            openVideoFragment(singlePost1.getKeyword(), singlePost1.getBaseApi().getCode(),
                    singlePost1.getPlayList(), singlePost1.getLimit(), applicationSettings.getIsYoutubePost(),
                    applicationSettings.getAdds(), applicationSettings.getActionBarColor(), applicationSettings.getLog(),
                    getResources().getString(R.string.ADMOB_INTER_ID), getResources().getString(R.string.FACEBOOK_INTER_ID));
        } else if (singlePost1.getRedirectApp().isEmpty() && !singlePost1.isRedirectLink()) {
            if (singlePost1.getPlayList()) {
                //playlist should be in string
                if (isSingleVideoFrag) {
                    openVideoFragment(singlePost1.getKeyword(), singlePost1.getBaseApi().getCode(),
                            singlePost1.getPlayList(), singlePost1.getLimit(), applicationSettings.getIsYoutubePost(),
                            applicationSettings.getAdds(), applicationSettings.getActionBarColor(), applicationSettings.getLog(),
                            getResources().getString(R.string.ADMOB_INTER_ID), getResources().getString(R.string.FACEBOOK_INTER_ID));
                } else {
                    if (isCategory) {
                        openMovieListFragment(singlePost1.getKeyword(), "",
                                singlePost1.getPlayList(), singlePost1.getLimit(), applicationSettings.getIsYoutubePost(),
                                applicationSettings.getAdds(), applicationSettings.getActionBarColor(), applicationSettings.getLog(),
                                getResources().getString(R.string.ADMOB_INTER_ID), getResources().getString(R.string.FACEBOOK_INTER_ID));

                    } else {
                        openWatchNowFirstFragment(songsList);
                    }
                }
            } else {
                if (isSingleVideoFrag) {
                    openVideoFragment(singlePost1.getKeyword(), "",
                            singlePost1.getPlayList(), singlePost1.getLimit(), applicationSettings.getIsYoutubePost(),
                            applicationSettings.getAdds(), applicationSettings.getActionBarColor(), applicationSettings.getLog(),
                            getResources().getString(R.string.ADMOB_INTER_ID), getResources().getString(R.string.FACEBOOK_INTER_ID));
                } else {
                    if (isCategory) {
                        openMovieListFragment(singlePost1.getKeyword(), "",
                                singlePost1.getPlayList(), singlePost1.getLimit(), applicationSettings.getIsYoutubePost(),
                                applicationSettings.getAdds(), applicationSettings.getActionBarColor(), applicationSettings.getLog(),
                                getResources().getString(R.string.ADMOB_INTER_ID), getResources().getString(R.string.FACEBOOK_INTER_ID));

                    } else {
                        openWatchNowFirstFragment(songsList);
                    }
                }
            }
        } else if (!singlePost1.getRedirectApp().isEmpty()) {
            openAppOnPlayStore(singlePost1.getRedirectApp());
        } else if (singlePost1.isRedirectLink()) {
            if (isSingleVideoFrag) {
                openWebUrl(singlePost1);
            } else {
                if (isCategory) {
                    openMovieListFragment(singlePost1.getKeyword(), "",
                            singlePost1.getPlayList(), singlePost1.getLimit(), applicationSettings.getIsYoutubePost(),
                            applicationSettings.getAdds(), applicationSettings.getActionBarColor(), applicationSettings.getLog(),
                            getResources().getString(R.string.ADMOB_INTER_ID), getResources().getString(R.string.FACEBOOK_INTER_ID));

                } else {
                    openWatchNowFirstFragment(categoryList);
                }

            }
        } else if (applicationSettings.getAdds() == AdsTypes.facebooksAds) {
            if (isSingleVideoFrag) {
                openWebUrl(singlePost1);
            } else {
                if (isCategory) {
                    openMovieListFragment(singlePost1.getKeyword(), "",
                            singlePost1.getPlayList(), singlePost1.getLimit(), applicationSettings.getIsYoutubePost(),
                            applicationSettings.getAdds(), applicationSettings.getActionBarColor(), applicationSettings.getLog(),
                            getResources().getString(R.string.ADMOB_INTER_ID), getResources().getString(R.string.FACEBOOK_INTER_ID));

                } else {
                    openWatchNowFirstFragment(categoryList);
                }
            }
        } else {
            if (isSingleVideoFrag) {
                openWebUrl(singlePost1);
            } else {
                if (isCategory) {
                    openMovieListFragment(singlePost1.getKeyword(), "",
                            singlePost1.getPlayList(), singlePost1.getLimit(), applicationSettings.getIsYoutubePost(),
                            applicationSettings.getAdds(), applicationSettings.getActionBarColor(), applicationSettings.getLog(),
                            getResources().getString(R.string.ADMOB_INTER_ID), getResources().getString(R.string.FACEBOOK_INTER_ID));

                } else {
                    openWatchNowFirstFragment(categoryList);
                }
            }
        }
    }
//
//    public void singlePostResponseHandling(singlePost singlePost1) {
//
//    }

    public void openSinglePost(int position, int clickCount) {
        isSingleVideoFrag = false;
        itemId = position;
        getSinglePost(position);
    }

    public void openSinglePost(int position, int clickCount, boolean isVideoFrag) {
        isSingleVideoFrag = true;
        itemId = position;
        getSinglePost(position);
    }

    public void openSinglePostWithoutAdd(int position) {
        getSinglePost(position);
    }

    public void onButtonShowPopupWindowClick() {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.exit_popup_window, null);
        TextView appName = popupView.findViewById(R.id.appName);
        Button exit = popupView.findViewById(R.id.exit_btn);
        Button cancel = popupView.findViewById(R.id.cancel_btn);
        ImageView ratingImage = popupView.findViewById(R.id.rateAppIcon);
        TextView ratingText = popupView.findViewById(R.id.rateAppText);
        View line = popupView.findViewById(R.id.line);
        line.setBackgroundColor(Color.parseColor(applicationSettings.getActionBarColor()));
        appName.setText(applicationSettings.getTitle());
        appName.setTextColor(Color.parseColor(applicationSettings.getActionBarColor()));

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        View v = popupWindow.getContentView();

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(v, Gravity.TOP, 130, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

        exit.setOnClickListener(v1 -> {
            finish();
        });
        cancel.setOnClickListener(v12 -> {
            popupWindow.dismiss();
        });

        ratingImage.setOnClickListener(v13 -> {
            openAppOnPlayStore(getResources().getString(R.string.PACKAGE_NAME));
        });
        ratingText.setOnClickListener(v13 -> {
            openAppOnPlayStore(getResources().getString(R.string.PACKAGE_NAME));
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResultReceived(String result) {
        inFragment = result;
    }

    public void scrollToTop() {
//        binding.nestedScrollView.fullScroll(View.FOCUS_UP);
    }

    public void openWatchNowFirstFragment(List<Songs_list> songs_list) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("VideosList", (Serializable) songsList);
        bundle.putSerializable("selectedVideo", songs_list.get(itemPosition));
        bundle.putSerializable("applicationSettings", applicationSettings);
        WatchNowFirstFragment fragment = new WatchNowFirstFragment();
        fragmentTrx(fragment, bundle, "OpenWatchNow");
    }

    public void loadBanners() {
        if (applicationSettings.getAdds() == AdsTypes.admobAds) {
            requestColonyBannerAd();
//            binding.maxBanner.setVisibility(View.GONE);
//            binding.adView.setVisibility(View.VISIBLE);

//            admobBannerAds();
        } else if (applicationSettings.getAdds() == AdsTypes.facebooksAds) {
            binding.maxBanner.setVisibility(View.VISIBLE);
            loadMaxBannerAd();
        } else if (applicationSettings.getAdds() == AdsTypes.startAppAds) {

            loadYodoBanner();
//            binding.startAppBannerAd.setVisibility(View.VISIBLE);
//            binding.maxBanner.setVisibility(View.GONE);
//            binding.adView.setVisibility(View.GONE);
        }
    }

    private void loadYodoBanner() {
        Yodo1Mas.getInstance().showBannerAd(MainActivity.this, "mas_test");
    }

    public void loadAds() {
        if (applicationSettings.getAdds() == AdsTypes.admobAds) {
            loadColonyInterstitial();
        } else if (applicationSettings.getAdds() == AdsTypes.facebooksAds) {
            maxInterstitialAd();
        } else if (applicationSettings.getAdds() == AdsTypes.startAppAds) {
//           initializeYodo1Ads();
        }
    }

    private void loadColonyInterstitial() {
        listener = new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(AdColonyInterstitial ad) {
                // Ad passed back in request filled callback, ad can now be shown
                colonyInterstitial = ad;
                isColonyAdLoaded = true;
//                showButton.setEnabled(true);
//                progress.setVisibility(View.INVISIBLE);
                Log.d(TAG, "onRequestFilled");
            }

            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
                // Ad request was not filled
                isColonyAdLoaded = false;
                Log.d(TAG, "onRequestNotFilled: ");
            }

            @Override
            public void onOpened(AdColonyInterstitial ad) {
                // Ad opened, reset UI to reflect state change
                Log.d(TAG, "onOpened");
            }

            @Override
            public void onExpiring(AdColonyInterstitial ad) {
                // Request a new ad if ad is expiring
                isColonyAdLoaded = false;
                AdColony.requestInterstitial(applicationSettings.getAdMobInterstitialId(), this, adOptions);
                Log.d(TAG, "onExpiring");
            }

            @Override
            public void onClosed(AdColonyInterstitial ad) {
                super.onClosed(ad);
                isColonyAdLoaded = false;
                AdColony.requestInterstitial(applicationSettings.getAdMobInterstitialId(), this, adOptions);
            }
        };
        // Set up button to show an ad when clicked
        AdColony.requestInterstitial(applicationSettings.getAdMobInterstitialId(), listener, adOptions);

    }

    public void showAd() {
        if (clickCount >= applicationSettings.getAdMobLimit()) {
            if (applicationSettings.getAdds() == AdsTypes.admobAds) {
                showColonyInterstitial();
                clickCount = 0;
            } else if (applicationSettings.getAdds() == AdsTypes.facebooksAds) {
                if (maxinterstitialAd.isReady()) {
                    showingAdDialog();
                    new Handler().postDelayed(() -> {
                        cancelShowingAdDialog();
                        maxinterstitialAd.showAd();
                    }, 2000);
                }
                clickCount = 0;

            } else if (applicationSettings.getAdds() == AdsTypes.startAppAds) {
                showingAdDialog();
                new Handler().postDelayed(() -> {
                    cancelShowingAdDialog();
                    showYodoInterstitial();
                }, 2000);

                clickCount = 0;
            }
            loadAds();
        }
    }

    private void loadMaxBannerAd() {
        maxAdView = new MaxAdView(applicationSettings.getApplovinBannerId(), this);
        // Stretch to the width of the screen for banners to be fully functional
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        // Get the adaptive banner height.
        int heightDp = MaxAdFormat.BANNER.getAdaptiveSize(this).getHeight();
        int heightPx = AppLovinSdkUtils.dpToPx(this, heightDp);
        RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightPx);
        lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        maxAdView.setLayoutParams(lay);
        maxAdView.setExtraParameter("adaptive_banner", "true");
        // Set background or background color for banners to be fully functional
//        maxAdView.setBackgroundColor( R.color.background_color );
//        ViewGroup rootView = findViewById(android.R.id.contentMax);
        binding.maxBanner.addView(maxAdView);

        maxAdView.setListener(new MaxAdViewAdListener() {
            @Override
            public void onAdExpanded(MaxAd ad) {

            }

            @Override
            public void onAdCollapsed(MaxAd ad) {

            }

            @Override
            public void onAdLoaded(MaxAd ad) {
//                binding.adSpace.setVisibility(View.GONE);
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {

            }

            @Override
            public void onAdHidden(MaxAd ad) {

            }

            @Override
            public void onAdClicked(MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {

            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {

            }
        });
        maxAdView.loadAd();
    }

    void maxInterstitialAd() {
        maxinterstitialAd = new MaxInterstitialAd(applicationSettings.getApplovinInterstialAdId(), this);
        // Load the first ad
        maxinterstitialAd.loadAd();
        maxinterstitialAd.setListener(new MaxAdViewAdListener() {
            @Override
            public void onAdExpanded(MaxAd ad) {

            }

            @Override
            public void onAdCollapsed(MaxAd ad) {

            }

            @Override
            public void onAdLoaded(MaxAd ad) {
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {

            }

            @Override
            public void onAdHidden(MaxAd ad) {
                clickCount = 0;
            }

            @Override
            public void onAdClicked(MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                clickCount = 0;
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                clickCount = 0;
            }
        });
    }

    private void showYodoInterstitial() {
        if (!Yodo1Mas.getInstance().isInterstitialAdLoaded()) {
            Toast.makeText(this, "Interstitial ad has not been cached.", Toast.LENGTH_SHORT).show();
            return;
        }
        Yodo1Mas.getInstance().showInterstitialAd(this);
    }

    private void showYodoBanner(View v) {
        if (!Yodo1Mas.getInstance().isBannerAdLoaded()) {
            Toast.makeText(this, "Banner ad has not been cached.", Toast.LENGTH_SHORT).show();
            return;
        }
        String placement = "placementId";
        int align = Yodo1Mas.BannerBottom | Yodo1Mas.BannerHorizontalCenter;
        int offsetX = 0;
        int offsetY = 0;
        Yodo1Mas.getInstance().showBannerAd(this, placement, align, offsetX, offsetY);
    }

    private void showColonyInterstitial() {
        if (!isColonyAdLoaded) {
//            setColonyAds();
            return;
        }
        showingAdDialog();
        new Handler().postDelayed(() -> {
            cancelShowingAdDialog();
            colonyInterstitial.show();
        }, 2000);
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
