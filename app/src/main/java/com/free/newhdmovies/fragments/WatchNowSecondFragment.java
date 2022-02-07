package com.free.newhdmovies.fragments;

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

import com.free.newhdmovies.R;
import com.free.newhdmovies.activities.MainActivity;
import com.free.newhdmovies.adapters.GridViewAdapter;
import com.free.newhdmovies.adapters.MoviesAdapter;
import com.free.newhdmovies.adapters.VideoListAdapter;
import com.free.newhdmovies.adapters.viewHolders.MoviesViewHolder;
import com.free.newhdmovies.callbacks.MoviesCallback;
import com.free.newhdmovies.databinding.WatchNowSecondFragmentBinding;
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
import com.free.newhdmovies.viewModels.WatchNowSecondViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class WatchNowSecondFragment extends Fragment implements MoviesCallback, OnNetworkResponse {

    WatchNowSecondFragmentBinding binding;
    int itemPosition = 0;
    int clickCount = 0;
    int itemID = 0, position = 0;
    List<Songs_list> songsList = new ArrayList<>();
    ApplicationSettings applicationSettings;
    private WatchNowSecondViewModel mViewModel;
    private VideoListAdapter mAdapter;
    private String keyword, imageURL, colorString, admob_Inter_Id, facebook_Inter_Id;
    private int limit, adds;
    private boolean isYoutube;
    private boolean isPlayList;

    public static WatchNowSecondFragment newInstance() {
        return new WatchNowSecondFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(WatchNowSecondViewModel.class);
        binding = WatchNowSecondFragmentBinding.inflate(inflater, container, false);
        ((MainActivity) getActivity()).scrollToTop();
        binding.header.backbtnHeader.setOnClickListener(v1 -> getActivity().onBackPressed());
        if (getArguments() != null) {
            applicationSettings = (ApplicationSettings) getArguments().getSerializable("applicationSettings");
            Songs_list song = (Songs_list) getArguments().getSerializable("selectedVideo");
            functions.GlideImageLoaderWithPlaceholder(getContext(), binding.imageView, Constants.BASE_URL_IMAGES + song.getUrl());
            binding.movieTitle.setText(song.getTitle());

//            binding.gridView1.setNumColumns(applicationSettings.getRowDisplay());
            binding.header.headerBar.setBackgroundColor(Color.parseColor(applicationSettings.getActionBarColor()));

            songsList = (List<Songs_list>) getArguments().getSerializable("VideosList");
            GridViewAdapter myAdapter;
            if (applicationSettings.getRowDisplay() == 1) {
                myAdapter = new GridViewAdapter(getContext(), R.layout.grid_view_style_single_post, (ArrayList) songsList);

            } else {
                myAdapter = new GridViewAdapter(getContext(), R.layout.gridview_style, (ArrayList) songsList);
            }
            setMoviesAdapter(songsList);
            binding.gridView1.smoothScrollToPosition(0);
            binding.watchNow.setOnClickListener(v -> {
                ((MainActivity) getActivity()).clickCount++;
                ((MainActivity) getActivity()).showAd();
                openServerLinkFragment(song);
            });

            imageURL = applicationSettings.getLog();
            functions.GlideImageLoaderWithPlaceholder(getContext(), binding.header.imageView, Constants.BASE_URL_IMAGES + imageURL);


        }
        binding.gridView1.smoothScrollToPosition(0);
        return binding.getRoot();
    }

    public void openServerLinkFragment(Songs_list songs_list) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("VideosList", (Serializable) songsList);
        bundle.putSerializable("selectedVideo", songs_list);
        bundle.putSerializable("applicationSettings", applicationSettings);
        ServerLinksFragment fragment = new ServerLinksFragment();
        fragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction().replace(R.id.container_video_fragment, fragment).addToBackStack(fragment.getTag()).commit();
    }

    public void setMoviesAdapter(List<Songs_list> lists) {
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), applicationSettings.getRowDisplay());
        MoviesAdapter adapter = new MoviesAdapter(getContext(), lists, this);
        binding.gridView1.setLayoutManager(linearLayoutManager);
        binding.gridView1.setAdapter(adapter);
        binding.gridView1.smoothScrollToPosition(0);
    }

    @Override
    public void onMovieClick(Songs_list songs_list, MoviesViewHolder viewHolder, int position) {
        this.position = position;
        itemID = songsList.get(position).getId();
        getSinglePost(itemID);
    }

    public void getSinglePost(int id) {
        ((MainActivity) getActivity()).clickCount++;
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
            case RequestCodes.API.GET_SINGLE_POST:
//                Toast.makeText(this, "Successful get Single posts", Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(this, "UnSuccessful" + response.message, Toast.LENGTH_SHORT).show();
        }
    }

    public void singlePostResponseHandling(singlePost singlePost1) {
        openWatchNowSecondFragment(songsList.get(position));

    }

    public void openWatchNowSecondFragment(Songs_list songs_list) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("VideosList", (Serializable) songsList);
        bundle.putSerializable("selectedVideo", songs_list);
        bundle.putSerializable("applicationSettings", applicationSettings);
        WatchNowSecondFragment fragment = new WatchNowSecondFragment();
        fragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction().replace(R.id.container_video_fragment, fragment).addToBackStack(fragment.getTag()).commit();
    }
}