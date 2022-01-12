package com.umer.application.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.umer.application.R;
import com.umer.application.adapters.GridViewAdapter;
import com.umer.application.adapters.MoviesAdapter;
import com.umer.application.adapters.VideoListAdapter;
import com.umer.application.adapters.viewHolders.MoviesViewHolder;
import com.umer.application.callbacks.MoviesCallback;
import com.umer.application.databinding.MovieListFragmentBinding;
import com.umer.application.models.ApplicationSettings;
import com.umer.application.models.Songs_list;
import com.umer.application.models.dailymotionSearchHelper;
import com.umer.application.networks.ApiServices;
import com.umer.application.utils.Constants;
import com.umer.application.utils.DailyMotionSearchHelper;
import com.umer.application.utils.YoutubeSearchHelper;
import com.umer.application.utils.functions;
import com.umer.application.viewModels.MovieListViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieListFragment extends Fragment implements MoviesCallback {

    private MovieListViewModel mViewModel;
    MovieListFragmentBinding binding;
    private VideoListAdapter mAdapter;
    private String keyword, imageURL, colorString, admob_Inter_Id, facebook_Inter_Id;
    private int limit, adds;
    private boolean isYoutube;
    int itemPosition = 0;
    int clickCount = 0;
    private boolean isPlayList;
    List<Songs_list> songsList = new ArrayList<>();
    ApplicationSettings applicationSettings;

    public static MovieListFragment newInstance() {
        return new MovieListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);
        binding = MovieListFragmentBinding.inflate(inflater, container, false);

        binding.headerBar.backbtnHeader.setOnClickListener(v1 -> getParentFragmentManager().beginTransaction().remove(MovieListFragment.this).commit());

        if (getArguments() != null) {
            applicationSettings = (ApplicationSettings) getArguments().getSerializable("applicationSettings");
//            binding.gridView1.setNumColumns(applicationSettings.getRowDisplay());
            binding.headerBar.headerBar.setBackgroundColor(Color.parseColor(applicationSettings.getActionBarColor()));

            songsList = (List<Songs_list>) getArguments().getSerializable("VideosList");
            GridViewAdapter myAdapter;
            if (applicationSettings.getRowDisplay() == 1) {
                myAdapter = new GridViewAdapter(getContext(), R.layout.grid_view_style_single_post, (ArrayList) songsList);

            } else {
                myAdapter = new GridViewAdapter(getContext(), R.layout.gridview_style, (ArrayList) songsList);
            }
//            binding.gridView1.setAdapter(myAdapter);
            setMoviesAdapter(songsList);
//            binding.gridView1.setOnItemClickListener((parent, view, position, id) -> {
////                        Toast.makeText(GridViewActivity.this, "Item clicked"+songsList.get(position).getId(), Toast.LENGTH_SHORT).show();
//
//
//            });

            imageURL = getArguments().getString("appIcon");
            functions.GlideImageLoaderWithPlaceholder(getContext(), binding.headerBar.imageView, Constants.BASE_URL_IMAGES + imageURL);
//            colorString = getArguments().getString("Color");
//            binding.headerBar.headerBar.setBackgroundColor(Color.parseColor(colorString));
//            admob_Inter_Id = getArguments().getString("ADMOB_INTER_ID");
//            facebook_Inter_Id = getArguments().getString("FACEBOOK_INTER_ID");
//
//
//            if (getArguments().getBoolean("isPLAYLIST")) {
//                keyword = getArguments().getString("PLAYLIST_ID");
//            } else {
//                keyword = getArguments().getString("KEYWORD");
//            }
//
//            limit = getArguments().getInt("LIMIT");
//            isYoutube = getArguments().getBoolean("isYoutube");
//            isPlayList = getArguments().getBoolean("isPLAYLIST");
//            adds = getArguments().getInt("ADDS", -1);

        }
//        RecyclerView videoListRecyclerView = binding.getRoot().findViewById(R.id.movies_rv);
//        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//
//        videoListRecyclerView.setLayoutManager(manager);
//
//        mAdapter = new VideoListAdapter(getActivity(), isYoutube, adds, null, admob_Inter_Id, facebook_Inter_Id);
//
//        videoListRecyclerView.setAdapter(mAdapter);
//
//        if (isYoutube) {
//            startLoadingYoutubeVideos();
//        } else if (!isPlayList && !isYoutube) {
//            getDailyMotionVideos(keyword);
//        } else {
//            startLoadingDailyMotionPlayList(getArguments().getString("PLAYLIST_ID"));
//        }
        binding.gridView1.smoothScrollToPosition(0);
        return binding.getRoot();
    }

    private void startLoadingYoutubeVideos() {

        YoutubeSearchHelper searchHelper = new YoutubeSearchHelper(getContext());
        searchHelper.searchYoutube(keyword, isPlayList, videos -> {
            mAdapter.setItems(videos);
            mAdapter.notifyDataSetChanged();
        });

    }

    private void startLoadingDailyMotionPlayList(String playListId) {
        DailyMotionSearchHelper dailyMotionSearchHelper = new DailyMotionSearchHelper(getContext());
        dailyMotionSearchHelper.searchDailyMotion(playListId, videos -> {
                    mAdapter.setItems(videos);
                    mAdapter.notifyDataSetChanged();
                }
        );

    }

    private void getDailyMotionVideos(String keyWord) {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.DAILYMOTION_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiServices getDailyMotionVideos = retrofit.create(ApiServices.class);
        Call<dailymotionSearchHelper> dailymotionSearchHelperCall = getDailyMotionVideos.getDailyMotionVideos(
                "id,thumbnail_url%2Ctitle", "pk", keyWord, limit);
        dailymotionSearchHelperCall.enqueue(new Callback<dailymotionSearchHelper>() {
            @Override
            public void onResponse(Call<dailymotionSearchHelper> call, Response<dailymotionSearchHelper> response) {
                dailymotionSearchHelper daily = response.body();
                mAdapter.setItems(daily.getSongs_List());

            }

            @Override
            public void onFailure(Call<dailymotionSearchHelper> call, Throwable t) {

            }
        });

    }

    public void setMoviesAdapter(List<Songs_list> lists) {
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), applicationSettings.getRowDisplay());
        MoviesAdapter adapter = new MoviesAdapter(getContext(), lists, this);
        binding.gridView1.setLayoutManager(linearLayoutManager);
        binding.gridView1.setAdapter(adapter);
    }

    public void openWatchNowFirstFragment(Songs_list songs_list) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("VideosList", (Serializable) songsList);
        bundle.putSerializable("selectedVideo", songs_list);
        bundle.putSerializable("applicationSettings", applicationSettings);
        WatchNowFirstFragment fragment = new WatchNowFirstFragment();
        fragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction().replace(R.id.container_video_fragment, fragment).addToBackStack(fragment.getTag()).commit();
    }

    @Override
    public void onMovieClick(Songs_list songs_list, MoviesViewHolder viewHolder, int position) {
        itemPosition = songsList.get(position).getId();
        openWatchNowFirstFragment(songsList.get(position));
    }
}