package com.umer.application.activities;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.umer.application.R;
import com.umer.application.adapters.CategoryAdapter;
import com.umer.application.adapters.GridViewAdapter;
import com.umer.application.adapters.MoviesAdapter;
import com.umer.application.adapters.SliderAdapter;
import com.umer.application.adapters.viewHolders.CategoryViewHolder;
import com.umer.application.adapters.viewHolders.MoviesViewHolder;
import com.umer.application.callbacks.CategoryCallback;
import com.umer.application.callbacks.MoviesCallback;
import com.umer.application.databinding.TestingGridViewBinding;
import com.umer.application.fragments.MovieListFragment;
import com.umer.application.fragments.ServerLinksFragment;
import com.umer.application.fragments.VideoListFragment;
import com.umer.application.fragments.WatchNowFirstFragment;
import com.umer.application.fragments.WatchNowSecondFragment;
import com.umer.application.models.AppSlider;
import com.umer.application.models.ApplicationSettings;
import com.umer.application.models.BaseResponse;
import com.umer.application.models.Songs_list;
import com.umer.application.models.singlePost;
import com.umer.application.networks.Network;
import com.umer.application.networks.NetworkCall;
import com.umer.application.networks.OnNetworkResponse;
import com.umer.application.utils.AdsTypes;
import com.umer.application.utils.Constants;
import com.umer.application.utils.RequestCodes;
import com.umer.application.utils.functions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class GridViewActivity extends AppCompatActivity implements View.OnClickListener, OnNetworkResponse, com.facebook.ads.AdListener, MoviesCallback, CategoryCallback {

    ImageView imageView_searchBar, search_button, search_backBtn;
    SliderView sliderView;
    EditText search_EditText;
    TextView description;
    GridView gridView1;
    ArrayList<Songs_list> songsList;
    ArrayList<Songs_list> categoryList;
    ArrayList<AppSlider> appSliders;
    LinearLayout container_gridView, DailyMotion_banner_container, facebook_banner_container;
    RelativeLayout actionBar, bannerLayout;
    FrameLayout container_video_fragment;
    private AdView facebookAdView;
    private com.google.android.gms.ads.AdView admobAdView, myAdView;
    ApplicationSettings applicationSettings;
    public int clickCount = 0;
    int mClickCount = 0;
    private InterstitialAd admobInterstitialAd;
    private com.facebook.ads.InterstitialAd facebookInterstitialAd;
    int itemId = 0;
    int itemPosition;
    boolean isSingleVideoFrag = false, isCategory = false;
    static GridViewActivity instance;
    String inFragment = "";
    TestingGridViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TestingGridViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AudienceNetworkAds.initialize(this);
        applicationSettings = (ApplicationSettings) getIntent().getSerializableExtra("applicationSettings");
        appSliders = (ArrayList<AppSlider>) getIntent().getSerializableExtra("ApplicationSlider");
        bannerLayout = findViewById(R.id.facebookBannerLayout);
        facebook_banner_container = findViewById(R.id.facebook_banner_container);
        DailyMotion_banner_container = findViewById(R.id.DailyMotion_banner_container);
        // Initialize the Audience Network SDK
        instance = this;
        initViews();
        applicationSettings.setAdds(2);
        applicationSettings.setAdMobLimit("2");
//        loadAds();
       bannerLayout.setVisibility(View.VISIBLE);
            facebook_banner_container.setVisibility(View.VISIBLE);
            facebookBannerAds();
         facebookInterstitialAds();

//        if (applicationSettings.getAdds() == AdsTypes.admobAds){
//            bannerLayout.setVisibility(View.VISIBLE);
//            DailyMotion_banner_container.setVisibility(View.VISIBLE);
//            admobBannerAds();
//            admobInterstitialAds();
//        }
//        else if (applicationSettings.getAdds() == AdsTypes.facebooksAds) {
//            bannerLayout.setVisibility(View.VISIBLE);
//            facebook_banner_container.setVisibility(View.VISIBLE);
//            facebookBannerAds();
//            facebookInterstitialAds();
//
//        } else if (applicationSettings.getAdds() == AdsTypes.fyberAds) {
//            bannerLayout.setVisibility(View.VISIBLE);
//            facebook_banner_container.setVisibility(View.VISIBLE);
//            facebookBannerAds();
//            facebookInterstitialAds();
//
//        }


        functions.GlideImageLoaderWithPlaceholder(this, imageView_searchBar, Constants.BASE_URL_IMAGES + applicationSettings.getLog());
        setListeners();
        if (applicationSettings.getAppSubCategories() != null && !applicationSettings.getAppSubCategories().isEmpty()) {
            binding.firstSubCatTitle.setText(applicationSettings.getAppSubCategories().get(0).getName());
            getPostByCategory(applicationSettings.getPostCategory().getId(), applicationSettings.getAppSubCategories().get(0).getId(), RequestCodes.API.GET_POST_BY_CATEGORY);
            if (applicationSettings.getAppSubCategories().size() > 1) {
                binding.secondSubCatTitle.setText(applicationSettings.getAppSubCategories().get(1).getName());
                getPostByCategory(applicationSettings.getPostCategory().getId(), applicationSettings.getAppSubCategories().get(1).getId(), RequestCodes.API.GET_POST_BY_CATEGORY_2);

            }

        }
        getAllPosts();


    }

    public static GridViewActivity getInstance() {
        return instance;
    }

    public void initViews() {

        sliderView = findViewById(R.id.slider);
        description = findViewById(R.id.description);
        description.setText(applicationSettings.getDiscraption());
        imageView_searchBar = findViewById(R.id.image_view);
        actionBar = findViewById(R.id.actionBar);
        actionBar.setBackgroundColor(Color.parseColor(applicationSettings.getActionBarColor()));
        search_button = findViewById(R.id.search_btn);
        search_EditText = findViewById(R.id.search_et);
        search_backBtn = findViewById(R.id.search_backBtn);
//        gridView1 = findViewById(R.id.gridView1);
        myAdView = findViewById(R.id.myAddJu);
//        gridView1.setNumColumns(applicationSettings.getRowDisplay());
        container_gridView = findViewById(R.id.container_gridView);
        container_video_fragment = findViewById(R.id.container_video_fragment);
        setSlider();
//        loadBannerAd();

    }

    public void setSlider() {
        SliderAdapter adapter = new SliderAdapter(this, appSliders, applicationSettings);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_btn:

                if (search_EditText.getText().toString().isEmpty()) {
                    imageView_searchBar.setVisibility(View.GONE);
                    search_backBtn.setVisibility(View.VISIBLE);
                    search_EditText.setVisibility(View.VISIBLE);
                } else {
                    openVideoFragment(search_EditText.getText().toString(), "", false, 20,
                            applicationSettings.getIsYoutubePost(), applicationSettings.getAdds(), applicationSettings.getActionBarColor(),
                            applicationSettings.getLog(), getResources().getString(R.string.ADMOB_INTER_ID), getResources().getString(R.string.FACEBOOK_INTER_ID));
                }
                break;

            case R.id.search_backBtn:
                imageView_searchBar.setVisibility(View.VISIBLE);
                search_backBtn.setVisibility(View.GONE);
                search_EditText.setVisibility(View.GONE);
                break;
        }
    }

    private void setListeners() {
        search_button.setOnClickListener(this);
        search_backBtn.setOnClickListener(this);
    }

    private void getAllPosts() {
        NetworkCall.make()
                .setCallback(this)
                .autoLoading(getSupportFragmentManager())
                .setTag(RequestCodes.API.GET_ALL_POSTS)
                .enque(new Network().apis().getAllPosts(getResources().getString(R.string.TEST_PACKAGE_NAME)))
                .execute();

    }

    private void getPostByCategory(Integer categoryId, Integer subcategoryId, Integer RequestCode) {
        NetworkCall.make()
                .setCallback(this)
                .autoLoading(getSupportFragmentManager())
                .setTag(RequestCode)
                .enque(new Network().apis().getPostsByCategory(categoryId, subcategoryId, 100, 1))
                .execute();

    }

    public void getSinglePost(int id) {
        clickCount++;
        loadAds();
        NetworkCall.make()
                .setCallback(this)
                .autoLoading(getSupportFragmentManager())
                .setTag(RequestCodes.API.GET_SINGLE_POST)
                .enque(new Network().apis().getSinglePost(id))
                .execute();

    }


    @Override
    public void onSuccess(Call call, Response response, Object tag) {
        switch ((int) tag) {
            case RequestCodes.API.GET_ALL_POSTS:
//                Toast.makeText(this, "Successful get All posts", Toast.LENGTH_SHORT).show();
                if (response.body() != null) {
                    songsList = new ArrayList<>();
                    songsList = (ArrayList<Songs_list>) response.body();
                    GridViewAdapter myAdapter;
                    if (applicationSettings.getRowDisplay() == 1) {
                        myAdapter = new GridViewAdapter(this, R.layout.grid_view_style_single_post, songsList);

                    } else {
                        myAdapter = new GridViewAdapter(this, R.layout.gridview_style, songsList);
                    }
                    setMoviesAdapter(songsList);
//                    setCategoryOneAdapter(songsList);
//                    setCategoryTwoAdapter(songsList);
//                    gridView1.setAdapter(myAdapter);
//                    gridView1.setOnItemClickListener((parent, view, position, id) -> {
////                        Toast.makeText(GridViewActivity.this, "Item clicked"+songsList.get(position).getId(), Toast.LENGTH_SHORT).show();
//                        itemPosition = songsList.get(position).getId();
//                        clickCount++;
//                        if (songsList.get(position).getRedirectApp().isEmpty()) {
//                            openSinglePost(itemPosition, clickCount);
//                        } else {
//                            openAppOnPlayStore(songsList.get(position).getRedirectApp());
//                        }


//                    });

                }
                break;
            case RequestCodes.API.GET_SINGLE_POST:
//                Toast.makeText(this, "Successful get Single posts", Toast.LENGTH_SHORT).show();
                if (response.body() != null) {
                    singlePost singlePost1 = (singlePost) response.body();
                    singlePostResponseHandling(singlePost1);
                }
                break;
            case RequestCodes.API.GET_POST_BY_CATEGORY:
                if (response.body() != null) {
                    List<Songs_list> songs_list = new ArrayList<>();
                    songs_list.addAll((ArrayList<Songs_list>) response.body());
                    setCategoryOneAdapter(songs_list);
                }
                break;
            case RequestCodes.API.GET_POST_BY_CATEGORY_2:
                if (response.body() != null) {
                    List<Songs_list> songs_list = new ArrayList<>();
                    songs_list.addAll((ArrayList<Songs_list>) response.body());
                    setCategoryTwoAdapter(songs_list);
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
//                Toast.makeText(this, "UnSuccessful" + response.message, Toast.LENGTH_SHORT).show();
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
//        com.google.android.gms.ads.AdView adView = new com.google.android.gms.ads.AdView(this);
//        adView.setAdSize(com.google.android.gms.ads.AdSize.BANNER);
//        adView.setAdUnitId(getResources().getString(R.string.ADMOB_BANNER_ID));
//        MobileAds.initialize(this, getResources().getString(R.string.ADMOB_APP_ID));

        admobAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        admobAdView.loadAd(adRequest);
        admobAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if (clickCount >= applicationSettings.getAdMobLimit()) {
                    clickCount = 0;
                }
                admobInterstitialAds();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                admobInterstitialAds();
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
                if (clickCount >= applicationSettings.getAdMobLimit()) {
                    clickCount = 0;
                }
                admobInterstitialAds();
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

    public void admobInterstitialAds() {
        admobInterstitialAd = new InterstitialAd(this);
        admobInterstitialAd.setAdUnitId(getResources().getString(R.string.ADMOB_INTER_ID));
        admobInterstitialAd.loadAd(new AdRequest.Builder().build());
        AdRequest adRequest = new AdRequest.Builder().build();
        admobInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if (clickCount >= applicationSettings.getAdMobLimit()) {
                    clickCount = 0;
                }
//                admobInterstitialAds();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                admobInterstitialAds();
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
                if (clickCount >= applicationSettings.getAdMobLimit()) {
                    clickCount = 0;
                }
                admobInterstitialAd.show();
//                admobInterstitialAds();
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

    public void facebookBannerAds() {
        if (facebookAdView == null) {
            facebookAdView = new AdView(this, getResources().getString(R.string.FACEBOOK_BANNER_ID), AdSize.BANNER_HEIGHT_50);
//        "IMG_16_9_APP_INSTALL#1197001037304304_1197772060560535"
            // Find the Ad Container
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.facebook_banner_container);

            // Add the ad view to your activity layout
            adContainer.addView(facebookAdView);

            // Request an ad
            facebookAdView.loadAd();
        }
    }


    @Override
    public void onError(Ad ad, AdError adError) {

    }

    @Override
    public void onAdLoaded(Ad ad) {
        if (clickCount >= applicationSettings.getAdMobLimit()) {
            clickCount = 0;
        }
    }

    @Override
    public void onAdClicked(Ad ad) {

    }

    @Override
    public void onLoggingImpression(Ad ad) {

    }

    public void facebookInterstitialAds() {
//        facebookInterstitialAd = new com.facebook.ads.InterstitialAd(this, getResources().getString(R.string.FACEBOOK_INTER_ID));
////        "IMG_16_9_APP_INSTALL#1197001037304304_1197857703885304"
//        facebookInterstitialAd.setAdListener(new InterstitialAdListener() {
//            @Override
//            public void onInterstitialDisplayed(Ad ad) {
//
//            }
//
//            @Override
//            public void onInterstitialDismissed(Ad ad) {
//                if (itemPosition!= 0){
//                    getSinglePost(itemPosition);
//                }if (clickCount == applicationSettings.getAdMobLimit()){
//                    clickCount = 0;
//                }
//
//            }
//
//            @Override
//            public void onError(Ad ad, AdError adError) {
////                Toast.makeText(GridViewActivity.this, "Error in ad loading ", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//
//            }
//
//        });
//
//        facebookInterstitialAd.loadAd();
        AdSettings.setTestAdType(AdSettings.TestAdType.IMG_16_9_APP_INSTALL);
        AdSettings.getTestAdType();
        AdSettings.setDebugBuild(true);
        facebookInterstitialAd = new com.facebook.ads.InterstitialAd(this, getResources().getString(R.string.FACEBOOK_INTER_ID));
        facebookInterstitialAd.setAdListener(new AbstractAdListener() {
            public void onAdLoaded(Ad ad) {
//                adfacebook = ad;
                facebookInterstitialAd.show();
            }
        });
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                facebookInterstitialAd.loadAd();
            }
        }, 3000);

        facebookInterstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e("TAG", "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e("TAG", "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e("TAG", "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d("TAG", "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                //  interstitial.loadAd();
                facebookInterstitialAd.show();
                if (clickCount >= applicationSettings.getAdMobLimit()) {
                    clickCount = 0;
                }
//                adfacebook = ad;
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d("TAG", "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d("TAG", "Interstitial ad impression logged!");
            }
        });

    }

    public void showFacebookInterstitialAds() {
        if (facebookInterstitialAd == null || !facebookInterstitialAd.isAdLoaded()) {
//            Toast.makeText(this, "facebookInterstitialAd is null or not loaded", Toast.LENGTH_SHORT).show();
            facebookInterstitialAds();
        }
        // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
        if (facebookInterstitialAd.isAdInvalidated()) {
//            Toast.makeText(this, "facebookInterstitialAd ad invalidate", Toast.LENGTH_SHORT).show();
            facebookInterstitialAds();
        }
        if (facebookInterstitialAd != null && facebookInterstitialAd.isAdLoaded() && !facebookInterstitialAd.isAdInvalidated())
            // Show the ad
            facebookInterstitialAd.show();
        facebookInterstitialAds();
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
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_video_fragment, fragment).addToBackStack(null).commit();
    }

    public void openMovieListFragment(String keyword, String playListId, boolean isPlayList,
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
        bundle.putSerializable("VideosList", songsList);
        bundle.putSerializable("applicationSettings", applicationSettings);
        MovieListFragment fragment = new MovieListFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_video_fragment, fragment).addToBackStack(fragment.getTag()).commit();
    }

    public void openWebUrl(singlePost singlePost) {
        Intent i = new Intent(this, WebViewActivity.class);
        i.putExtra("WEBURL", singlePost.getBaseApi().getUrl());
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.container_video_fragment);
            if (f instanceof MovieListFragment || f instanceof WatchNowFirstFragment || f instanceof WatchNowSecondFragment
                    || f instanceof VideoListFragment || f instanceof ServerLinksFragment) {
                popBackStack();
            }
        } else if (inFragment.equals("inFragment")) {
            super.onBackPressed();
            admobInterstitialAds();
            facebookInterstitialAds();
            clickCount = 0;
            inFragment = "";
        } else if (inFragment.equals("")) {
            onButtonShowPopupWindowClick();
            clickCount = 0;
        }
    }

    public void popBackStack() {
        FragmentManager fm = getLastFragmentManagerWithBack(getSupportFragmentManager());
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();

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
//        else if (clickCount > applicationSettings.getAdMobLimit()){
//            clickCount = 0;
//             if (applicationSettings.getAdds() == AdsTypes.admobAds){
//                admobInterstitialAds();
//            }else if (applicationSettings.getAdds() == AdsTypes.facebooksAds){
//                facebookInterstitialAds();
//            }else{
//                Toast.makeText(instance, "want to quit app", Toast.LENGTH_SHORT).show();
//
//            }
//        }


    public void singlePostResponseHandling(singlePost singlePost1) {

        if (singlePost1.getRedirectApp().isEmpty() && !singlePost1.isRedirectLink()) {
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
            if (applicationSettings.getAdds() == AdsTypes.admobAds) {
                if (admobInterstitialAd.isLoaded()) {
                    admobInterstitialAd.show();
                    admobInterstitialAds();
                    admobInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            openWebUrl(singlePost1);
                        }
                    });
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
            } else if (applicationSettings.getAdds() == AdsTypes.facebooksAds) {
//                showFacebookInterstitialAds();
//                facebookInterstitialAds();
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
    }


    public void openSinglePost(int position, int clickCount) {
        isSingleVideoFrag = false;
        itemId = position;
        if (clickCount == applicationSettings.getAdMobLimit() && applicationSettings.getAdds() == AdsTypes.admobAds) {
            if (admobInterstitialAd.isLoaded()) {
                admobInterstitialAd.show();
                admobInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        getSinglePost(position);

                    }
                });
            } else {
//              Toast.makeText(GridViewActivity.this, "interstitial ads not loaded", Toast.LENGTH_SHORT).show();
                getSinglePost(position);
            }

        } else if (clickCount == applicationSettings.getAdMobLimit() && applicationSettings.getAdds() == AdsTypes.facebooksAds) {
            showFacebookInterstitialAds();
        } else {
            getSinglePost(position);

        }
    }

    public void openSinglePost(int position, int clickCount, boolean isVideoFrag) {
        isSingleVideoFrag = true;
        itemId = position;
        if (clickCount == applicationSettings.getAdMobLimit() && applicationSettings.getAdds() == AdsTypes.admobAds) {
            if (admobInterstitialAd.isLoaded()) {
                admobInterstitialAd.show();
                admobInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        getSinglePost(position);

                    }
                });
            } else {
//              Toast.makeText(GridViewActivity.this, "interstitial ads not loaded", Toast.LENGTH_SHORT).show();
                getSinglePost(position);
            }

        } else if (clickCount == applicationSettings.getAdMobLimit() && applicationSettings.getAdds() == AdsTypes.facebooksAds) {
            showFacebookInterstitialAds();
        } else {
            getSinglePost(position);

        }
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

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        if (applicationSettings.getAdds() == AdsTypes.admobAds) {
            admobInterstitialAds();
        } else if (applicationSettings.getAdds() == AdsTypes.facebooksAds) {
            facebookInterstitialAds();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        if (applicationSettings.getAdds() == AdsTypes.admobAds) {
            admobInterstitialAds();
        } else if (applicationSettings.getAdds() == AdsTypes.facebooksAds) {
            facebookInterstitialAds();
        }
    }

    private void loadBannerAd() {
        // Creating  a Ad Request
        AdRequest adRequest = new AdRequest.Builder().build();

        // load Ad with the Request
        myAdView.loadAd(adRequest);

        // Showing a simple Toast message to user when an ad is Loading
        Toast.makeText(this, "Banner Ad is loading ", Toast.LENGTH_LONG).show();

    }

    //    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//       int myClick=AppSession.getInt("myClick");
//       if (myClick==2){
//           AppSession.put("myClick",0);
//           myAdView.setVisibility(View.VISIBLE);
//           loadBannerAd();
//       }else if (myClick<2){
//           AppSession.put("myClick",myClick+1);
//       }
//        return super.dispatchTouchEvent(ev);
//    }
    public void setMoviesAdapter(List<Songs_list> lists) {
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, applicationSettings.getRowDisplay());
        MoviesAdapter adapter = new MoviesAdapter(this, lists, this);
        binding.gridView1.setLayoutManager(linearLayoutManager);
        binding.gridView1.setAdapter(adapter);
    }

    public void setCategoryOneAdapter(List<Songs_list> lists) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        CategoryAdapter adapter = new CategoryAdapter(this, lists, this);
        binding.categoryOneRv.setLayoutManager(linearLayoutManager);
        binding.categoryOneRv.setAdapter(adapter);
    }

    public void setCategoryTwoAdapter(List<Songs_list> lists) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        CategoryAdapter adapter = new CategoryAdapter(this, lists, this);
        binding.categoryTwoRv.setLayoutManager(linearLayoutManager);
        binding.categoryTwoRv.setAdapter(adapter);
    }

    @Override
    public void onMovieClick(Songs_list songs_list, MoviesViewHolder viewHolder, int position) {
//        Toast.makeText(GridViewActivity.this, "Item clicked"+songsList.get(position).getId(), Toast.LENGTH_SHORT).show();
        this.itemPosition = position;
        isCategory = false;
        itemId = songsList.get(position).getId();
        clickCount++;
        loadAds();
        openSinglePost(itemId, clickCount);
//        if (songsList.get(position).getRedirectApp().isEmpty()) {
//            openSinglePost(itemId, clickCount);
//        } else {
//            openAppOnPlayStore(songsList.get(position).getRedirectApp());
//        }
    }

    @Override
    public void onCategoryClick(Songs_list songs_list, CategoryViewHolder viewHolder, int position, List<Songs_list> allItems) {
        isCategory = true;
        this.itemPosition = position;
        itemId = songsList.get(position).getId();
        clickCount++;
        loadAds();
        categoryList = new ArrayList<>();
        categoryList.addAll(allItems);
        openSinglePost(itemId, clickCount);
//        if (songsList.get(position).getRedirectApp().isEmpty()) {
//            openSinglePost(itemId, clickCount);
//        } else {
//            openAppOnPlayStore(songsList.get(position).getRedirectApp());
//        }
    }

    public void openWatchNowFirstFragment(List<Songs_list> songs_list) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("VideosList", (Serializable) songsList);
        bundle.putSerializable("selectedVideo", songs_list.get(itemPosition));
        bundle.putSerializable("applicationSettings", applicationSettings);
        WatchNowFirstFragment fragment = new WatchNowFirstFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_video_fragment, fragment).addToBackStack(fragment.getTag()).commit();
    }

    public void loadAds() {
        if (clickCount >= applicationSettings.getAdMobLimit()) {
            if (applicationSettings.getAdds() == AdsTypes.admobAds) {
                bannerLayout.setVisibility(View.VISIBLE);
                DailyMotion_banner_container.setVisibility(View.VISIBLE);
                admobBannerAds();
                admobInterstitialAds();
            } else if (applicationSettings.getAdds() == AdsTypes.facebooksAds) {
                bannerLayout.setVisibility(View.VISIBLE);
                facebook_banner_container.setVisibility(View.VISIBLE);
                facebookBannerAds();
                facebookInterstitialAds();

            } else if (applicationSettings.getAdds() == AdsTypes.fyberAds) {
                bannerLayout.setVisibility(View.VISIBLE);
                facebook_banner_container.setVisibility(View.VISIBLE);
                facebookBannerAds();
                facebookInterstitialAds();

            }
        }
    }
}
