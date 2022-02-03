package com.free.newhdmovies.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinSdkUtils;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.startapp.sdk.adsbase.StartAppAd;
import com.free.newhdmovies.R;
import com.free.newhdmovies.databinding.TestingGridViewBinding;
import com.free.newhdmovies.fragments.HomeFragment;
import com.free.newhdmovies.fragments.MovieListFragment;
import com.free.newhdmovies.fragments.ServerLinksFragment;
import com.free.newhdmovies.fragments.VideoListFragment;
import com.free.newhdmovies.fragments.WatchNowFirstFragment;
import com.free.newhdmovies.fragments.WatchNowSecondFragment;
import com.free.newhdmovies.models.ApplicationSettings;
import com.free.newhdmovies.models.BaseResponse;
import com.free.newhdmovies.models.Songs_list;
import com.free.newhdmovies.models.singlePost;
import com.free.newhdmovies.networks.Network;
import com.free.newhdmovies.networks.NetworkCall;
import com.free.newhdmovies.networks.OnNetworkResponse;
import com.free.newhdmovies.utils.AdsTypes;
import com.free.newhdmovies.utils.RequestCodes;
import com.free.newhdmovies.utils.Utils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class GridViewActivity extends AppCompatActivity implements OnNetworkResponse {

    public static int clickCount = 0;
    ArrayList<Songs_list> songsList;
    ArrayList<Songs_list> categoryList, categoryTwoList = new ArrayList<>();
    public static ApplicationSettings applicationSettings;
    int itemId = 0;
    int itemPosition;
    public boolean isSingleVideoFrag = false, isCategory = false;
    //    static GridViewActivity instance;
    String inFragment = "";
    TestingGridViewBinding binding;
    MaxAdView maxAdView;
    String categoryName;
    MaxInterstitialAd maxinterstitialAd;
    private com.google.android.gms.ads.AdView admobAdView;
    private InterstitialAd admobInterstitialAd;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TestingGridViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AudienceNetworkAds.initialize(this);
        applicationSettings = (ApplicationSettings) getIntent().getSerializableExtra("applicationSettings");
        fragmentTrx(new HomeFragment(), null, "HomeFragment");
//        applicationSettings.setAdds(1);
//        applicationSettings.setAdMobLimit("2");
    }

    public void getSinglePost(int id) {
        showAd();
        NetworkCall.make()
                .setCallback(this)
                .autoLoadingCancel(Utils.getLoading(this, "Loading"))
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

    public void admobBannerAds() {
        admobAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        admobAdView.loadAd(adRequest);
        admobAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
//                binding.adSpace.setVisibility(View.GONE);
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

    public void admobInterstitialAds() {
        admobInterstitialAd = new InterstitialAd(this);
        admobInterstitialAd.setAdUnitId(getResources().getString(R.string.ADMOB_INTER_ID));
        admobInterstitialAd.loadAd(new AdRequest.Builder().build());
        AdRequest adRequest = new AdRequest.Builder().build();
        admobInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });
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
                    openVideoFragment("", singlePost1.getBaseApi().getCode(),
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
            binding.facebookBannerLayout.setVisibility(View.VISIBLE);
            binding.DailyMotionBannerContainer.setVisibility(View.VISIBLE);
            binding.maxBanner.setVisibility(View.GONE);
            admobBannerAds();
        } else if (applicationSettings.getAdds() == AdsTypes.facebooksAds) {
            binding.facebookBannerLayout.setVisibility(View.GONE);
            binding.maxBanner.setVisibility(View.VISIBLE);
            loadMaxBannerAd();
        } else if (applicationSettings.getAdds() == AdsTypes.startAppAds) {
            binding.facebookBannerLayout.setVisibility(View.GONE);
            binding.startAppBannerLayout.setVisibility(View.VISIBLE);
            binding.maxBanner.setVisibility(View.GONE);
        }
    }

    public void loadAds() {
        if (applicationSettings.getAdds() == AdsTypes.admobAds) {
            admobInterstitialAds();
        } else if (applicationSettings.getAdds() == AdsTypes.facebooksAds) {
            maxInterstitialAd();
        }
    }
    public void showAd() {
        if (clickCount >= applicationSettings.getAdMobLimit()) {
            if (applicationSettings.getAdds() == AdsTypes.admobAds) {
                if (admobInterstitialAd.isLoaded()) {
                    showingAdDialog();
                    new Handler().postDelayed(() -> {
                        cancelShowingAdDialog();
                        admobInterstitialAd.show();
                    }, 2000);

                }
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
                    StartAppAd.showAd(this);
                }, 2000);

                clickCount = 0;
            }
            loadAds();
        }
    }

    private void loadMaxBannerAd() {
        maxAdView = new MaxAdView(getResources().getString(R.string.APPLOVIN_BANNER_ID), this);
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
        maxinterstitialAd = new MaxInterstitialAd(getResources().getString(R.string.APPLOVIN_INTER_ID), this);
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
