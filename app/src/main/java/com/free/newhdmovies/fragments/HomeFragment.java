package com.free.newhdmovies.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.free.newhdmovies.R;
import com.free.newhdmovies.activities.MainActivity;
import com.free.newhdmovies.activities.WebViewActivity;
import com.free.newhdmovies.adapters.CategoryAdapter;
import com.free.newhdmovies.adapters.CategoryTwoAdapter;
import com.free.newhdmovies.adapters.GridViewAdapter;
import com.free.newhdmovies.adapters.MoviesAdapter;
import com.free.newhdmovies.adapters.SliderAdapter;
import com.free.newhdmovies.adapters.viewHolders.CategoryTwoViewHolder;
import com.free.newhdmovies.adapters.viewHolders.CategoryViewHolder;
import com.free.newhdmovies.adapters.viewHolders.MoviesViewHolder;
import com.free.newhdmovies.callbacks.CategoryCallback;
import com.free.newhdmovies.callbacks.CategoryTwoCallback;
import com.free.newhdmovies.callbacks.MoviesCallback;
import com.free.newhdmovies.databinding.HomeFragmentBinding;
import com.free.newhdmovies.models.AppSlider;
import com.free.newhdmovies.models.ApplicationSettings;
import com.free.newhdmovies.models.BaseResponse;
import com.free.newhdmovies.models.Songs_list;
import com.free.newhdmovies.models.singlePost;
import com.free.newhdmovies.networks.Network;
import com.free.newhdmovies.networks.NetworkCall;
import com.free.newhdmovies.networks.OnNetworkResponse;
import com.free.newhdmovies.utils.Constants;
import com.free.newhdmovies.utils.RequestCodes;
import com.free.newhdmovies.utils.Utils;
import com.free.newhdmovies.utils.functions;
import com.free.newhdmovies.viewModels.HomeViewModel;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener, OnNetworkResponse, MoviesCallback, CategoryCallback, CategoryTwoCallback {

    ImageView imageView_searchBar, search_button, search_backBtn;
    SliderView sliderView;
    EditText search_EditText;
    TextView description;
    ArrayList<Songs_list> songsList;
    ArrayList<Songs_list> categoryList, categoryTwoList = new ArrayList<>();
    ArrayList<AppSlider> appSliders;
    RelativeLayout actionBar;
    ApplicationSettings applicationSettings = new ApplicationSettings();
    int itemId = 0;
    int itemPosition;
    boolean isSingleVideoFrag = false, isCategory = false;
    HomeFragmentBinding binding;
    String categoryName;
    private HomeViewModel mViewModel;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = HomeFragmentBinding.inflate(inflater, container, false);

        applicationSettings = applicationSettings.retrieveApplicationSettings(getActivity());
        MainActivity.applicationSettings = applicationSettings;
        appSliders = (ArrayList<AppSlider>) applicationSettings.getSlider(getActivity());
        initViews(binding.getRoot());
        setListeners();
        setCategories();
        loadAds();

        return binding.getRoot();
    }

    public void initViews(View view) {
        sliderView = view.findViewById(R.id.slider);
        description = view.findViewById(R.id.description);
        description.setText(applicationSettings.getDiscraption());
        imageView_searchBar = view.findViewById(R.id.image_view);
        actionBar = view.findViewById(R.id.actionBar);
        actionBar.setBackgroundColor(Color.parseColor(applicationSettings.getActionBarColor()));
        search_button = view.findViewById(R.id.search_btn);
        search_EditText = view.findViewById(R.id.search_et);
        search_backBtn = view.findViewById(R.id.search_backBtn);
        functions.GlideImageLoaderWithPlaceholder(getContext(), imageView_searchBar, Constants.BASE_URL_IMAGES + applicationSettings.getLog());
        setSlider();
    }

    private void setCategories() {
        if (applicationSettings.getAppSubCategories() != null && !applicationSettings.getAppSubCategories().isEmpty()) {
            binding.firstSubCatTitle.setText(applicationSettings.getAppSubCategories().get(0).getName());
            getPostByCategory(applicationSettings.getPostCategory().getId(), applicationSettings.getAppSubCategories().get(0).getId(), RequestCodes.API.GET_POST_BY_CATEGORY);
            if (applicationSettings.getAppSubCategories().size() > 1) {
                binding.secondSubCatTitle.setText(applicationSettings.getAppSubCategories().get(1).getName());
                getPostByCategory(applicationSettings.getPostCategory().getId(), applicationSettings.getAppSubCategories().get(1).getId(), RequestCodes.API.GET_POST_BY_CATEGORY_2);
            }
        } else {
            binding.firstSubCatTitle.setVisibility(View.GONE);
            binding.secondSubCatTitle.setVisibility(View.GONE);
        }
    }

    public void setSlider() {
        SliderAdapter adapter = new SliderAdapter(getActivity(), appSliders, applicationSettings);
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

    private void loadAds() {
        new Handler().postDelayed(() -> {
            getAllPosts();
            ((MainActivity) getActivity()).loadBanners();
        }, 500);
        ((MainActivity) getActivity()).loadAds();
    }

    private void getAllPosts() {
        NetworkCall.make()
                .setCallback(this)
                .autoLoadingCancel(Utils.getLoading(getActivity(), "Loading"))
                .setTag(RequestCodes.API.GET_ALL_POSTS)
                .enque(new Network().apis().getAllPosts(getResources().getString(R.string.TEST_PACKAGE_NAME)))
                .execute();

    }

    private void getPostByCategory(Integer categoryId, Integer subcategoryId, Integer RequestCode) {
        NetworkCall.make()
                .setCallback(this)
                .setTag(RequestCode)
                .enque(new Network().apis().getPostsByCategory(categoryId, subcategoryId, 100, 1))
                .execute();

    }

    public void getSinglePost(int id) {
        ((MainActivity) getActivity()).showAd();
        NetworkCall.make()
                .setCallback(this)
                .autoLoadingCancel(Utils.getLoading(getActivity(), "Loading"))
                .setTag(RequestCodes.API.GET_SINGLE_POST)
                .enque(new Network().apis().getSinglePost(id))
                .execute();

    }


    @Override
    public void onSuccess(Call call, Response response, Object tag) {
        switch ((int) tag) {
            case RequestCodes.API.GET_ALL_POSTS:
                if (response.body() != null) {
                    songsList = new ArrayList<>();
                    songsList = (ArrayList<Songs_list>) response.body();
                    GridViewAdapter myAdapter;
                    setMoviesAdapter(songsList);

                }
                break;
            case RequestCodes.API.GET_SINGLE_POST:
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
        }
    }

    public void openAppOnPlayStore(String appPackageName) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
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
        ((MainActivity) getActivity()).fragmentTrx(fragment, bundle, "VideoListFragment");
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
        ((MainActivity) getActivity()).fragmentTrx(fragment, bundle, "MovieListFragment");
    }

    public void openWebUrl(singlePost singlePost) {
        Intent i = new Intent(getActivity(), WebViewActivity.class);
        i.putExtra("WEBURL", singlePost.getBaseApi().getUrl());
        startActivity(i);
    }


    public void singlePostResponseHandling(singlePost singlePost1) {
        if (isCategory) {
            openMovieListFragment(singlePost1.getKeyword(), "",
                    singlePost1.getPlayList(), singlePost1.getLimit(), applicationSettings.getIsYoutubePost(),
                    applicationSettings.getAdds(), applicationSettings.getActionBarColor(), applicationSettings.getLog(),
                    getResources().getString(R.string.ADMOB_INTER_ID), getResources().getString(R.string.FACEBOOK_INTER_ID));

        } else if (singlePost1.isRedirectLink()) {
            openWebUrl(singlePost1);
        } else {
            openWatchNowFirstFragment(songsList);
        }
    }


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


    public void setMoviesAdapter(List<Songs_list> lists) {
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), applicationSettings.getRowDisplay());
        MoviesAdapter adapter = new MoviesAdapter(getContext(), lists, this);
        binding.gridView1.setLayoutManager(linearLayoutManager);
        binding.gridView1.setAdapter(adapter);
    }

    public void setCategoryOneAdapter(List<Songs_list> lists) {
        categoryList = (ArrayList<Songs_list>) lists;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        CategoryAdapter adapter = new CategoryAdapter(getActivity(), categoryList, this);
        binding.categoryOneRv.setLayoutManager(linearLayoutManager);
        binding.categoryOneRv.setAdapter(adapter);
    }

    public void setCategoryTwoAdapter(List<Songs_list> lists) {
        categoryTwoList = (ArrayList<Songs_list>) lists;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        CategoryTwoAdapter adapter = new CategoryTwoAdapter(getActivity(), categoryTwoList, this);
        binding.categoryTwoRv.setLayoutManager(linearLayoutManager);
        binding.categoryTwoRv.setAdapter(adapter);
    }

    @Override
    public void onMovieClick(Songs_list songs_list, MoviesViewHolder viewHolder, int position) {
        this.itemPosition = position;
        isCategory = false;
        itemId = songsList.get(position).getId();
        addClick();
        scrollToTop();
        openSinglePost(itemId, ((MainActivity) getActivity()).clickCount);
        ((MainActivity) getActivity()).showAd();
    }

    public void scrollToTop() {
        ((MainActivity) getActivity()).scrollToTop();
    }

    @Override
    public void onCategoryClick(Songs_list songs_list, CategoryViewHolder viewHolder, int position, List<Songs_list> allItems) {
        categoryName = applicationSettings.getAppSubCategories().get(0).getName();
        isCategory = true;
        this.itemPosition = position;
        categoryList = new ArrayList<>();
        categoryList.addAll(allItems);
        itemId = categoryList.get(position).getId();
        addClick();
        scrollToTop();
        openSinglePost(itemId, ((MainActivity) getActivity()).clickCount);
    }

    @Override
    public void onCategoryTowClick(Songs_list songs_list, CategoryTwoViewHolder viewHolder, int position, List<Songs_list> allItems) {
        categoryName = applicationSettings.getAppSubCategories().get(1).getName();
        isCategory = true;
        this.itemPosition = position;
        categoryTwoList = new ArrayList<>();
        categoryTwoList.addAll(allItems);
        itemId = categoryTwoList.get(position).getId();
        addClick();
        scrollToTop();
        openSinglePost(itemId, ((MainActivity) getActivity()).clickCount);
        ((MainActivity) getActivity()).showAd();

    }

    private void addClick() {
        ((MainActivity) getActivity()).clickCount++;
    }

    public void openWatchNowFirstFragment(List<Songs_list> songs_list) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("VideosList", (Serializable) songsList);
        bundle.putSerializable("selectedVideo", songs_list.get(itemPosition));
        bundle.putSerializable("applicationSettings", applicationSettings);
        WatchNowFirstFragment fragment = new WatchNowFirstFragment();
        ((MainActivity) getActivity()).fragmentTrx(fragment, bundle, "OpenWatchNow");
    }
}