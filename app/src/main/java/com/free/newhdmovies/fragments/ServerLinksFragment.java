package com.free.newhdmovies.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.free.newhdmovies.R;
import com.free.newhdmovies.activities.MainActivity;
import com.free.newhdmovies.activities.WebViewActivity;
import com.free.newhdmovies.adapters.MoviesAdapter;
import com.free.newhdmovies.adapters.VideoListAdapter;
import com.free.newhdmovies.adapters.viewHolders.MoviesViewHolder;
import com.free.newhdmovies.callbacks.MoviesCallback;
import com.free.newhdmovies.databinding.ServerLinksFragmentBinding;
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
import com.free.newhdmovies.viewModels.ServerLinksViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ServerLinksFragment extends Fragment implements MoviesCallback, OnNetworkResponse {

    ServerLinksFragmentBinding binding;
    int itemPosition = 0;
    int clickCount = 0;
    int itemID = 0, position = 0;
    List<Songs_list> songsList = new ArrayList<>();
    ApplicationSettings applicationSettings;
    String linkURL;
    private ServerLinksViewModel mViewModel;
    private VideoListAdapter mAdapter;
    private String keyword, imageURL, colorString, admob_Inter_Id, facebook_Inter_Id;
    private int limit, adds;
    private boolean isWebLink = false;
    private boolean isPlayList;

    public static ServerLinksFragment newInstance() {
        return new ServerLinksFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ServerLinksViewModel.class);

        binding = ServerLinksFragmentBinding.inflate(inflater, container, false);
        ((MainActivity) getActivity()).scrollToTop();
        binding.header.backbtnHeader.setOnClickListener(v1 -> getActivity().onBackPressed());
        if (getArguments() != null) {
            applicationSettings = (ApplicationSettings) getArguments().getSerializable("applicationSettings");
            Songs_list song = (Songs_list) getArguments().getSerializable("selectedVideo");
            functions.GlideImageLoaderWithPlaceholder(getContext(), binding.imageView, Constants.BASE_URL_IMAGES + song.getUrl());
            binding.movieTitle.setText(song.getTitle());

            binding.header.headerBar.setBackgroundColor(Color.parseColor(applicationSettings.getActionBarColor()));

            songsList = (List<Songs_list>) getArguments().getSerializable("VideosList");

            setMoviesAdapter(songsList);

            binding.serverLink1.setOnClickListener(v -> {
                isWebLink = true;
                linkURL = "1";
                getSinglePost(song.getId());
            });
            binding.serverLink2.setOnClickListener(v -> {
                isWebLink = true;
                linkURL = "2";
                getSinglePost(song.getId());
            });
            binding.serverLink3.setOnClickListener(v -> {
                isWebLink = true;
                linkURL = "3";
                getSinglePost(song.getId());
            });
            binding.serverLink4.setOnClickListener(v -> {
                ((MainActivity) getActivity()).clickCount++;
                ((MainActivity) getActivity()).scrollToTop();
                ((MainActivity) getActivity()).isSingleVideoFrag = true;
                ((MainActivity) getActivity()).openSinglePost(song.getId(), clickCount, true);
            });
            imageURL = applicationSettings.getLog();
            functions.GlideImageLoaderWithPlaceholder(getContext(), binding.header.imageView, Constants.BASE_URL_IMAGES + imageURL);


        }
        binding.gridView1.smoothScrollToPosition(0);
        return binding.getRoot();

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
                .autoLoadingCancel(Utils.getLoading(getActivity(), "Please wait..."))
                .setTag(RequestCodes.API.GET_SINGLE_POST)
                .enque(new Network().apis().getSinglePost(id))
                .execute();

    }

    public void openWatchNowFirstFragment(List<Songs_list> songs_list) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("VideosList", (Serializable) songsList);
        bundle.putSerializable("selectedVideo", songs_list.get(position));
        bundle.putSerializable("applicationSettings", applicationSettings);
        WatchNowFirstFragment fragment = new WatchNowFirstFragment();
        fragment.setArguments(bundle);
        setMoviesAdapter(new ArrayList<>());
        getParentFragmentManager().beginTransaction().replace(R.id.container_video_fragment, fragment).addToBackStack(fragment.getTag()).commit();
    }

    public void singlePostResponseHandling(singlePost singlePost) {
        if (isWebLink) {
            openWebUrl(singlePost);
            new Handler().postDelayed(() -> {
                isWebLink = false;
            }, 1500);
        } else openWatchNowFirstFragment(songsList);
    }

    @Override
    public void onSuccess(Call call, Response response, Object tag) {
        switch ((int) tag) {
            case RequestCodes.API.GET_SINGLE_POST:
                if (response.body() != null) {
                    singlePost singlePost1 = (singlePost) response.body();
                    if (isWebLink) singlePostResponseHandling(singlePost1);
                    else openWatchNowFirstFragment(songsList);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(Call call, BaseResponse response, Object tag) {
        Toast.makeText(getContext(), "UnSuccessful" + response.message, Toast.LENGTH_SHORT).show();

    }

    public void openWebUrl(singlePost singlePost) {
        String url = singlePost.getServerLink1();
        if (linkURL.equalsIgnoreCase("1")) {
            url = singlePost.getServerLink1();
        } else if (linkURL.equalsIgnoreCase("2")) {
            url = singlePost.getServerLink2();
        } else if (linkURL.equalsIgnoreCase("3")) {
            url = singlePost.getServerLink3();
        }
        Intent i = new Intent(getActivity(), WebViewActivity.class);
        i.putExtra("WEBURL", url);
        startActivity(i);
    }
}