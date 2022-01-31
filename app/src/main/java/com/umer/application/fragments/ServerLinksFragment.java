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

import com.umer.application.R;
import com.umer.application.activities.GridViewActivity;
import com.umer.application.adapters.GridViewAdapter;
import com.umer.application.adapters.MoviesAdapter;
import com.umer.application.adapters.VideoListAdapter;
import com.umer.application.adapters.viewHolders.MoviesViewHolder;
import com.umer.application.callbacks.MoviesCallback;
import com.umer.application.databinding.ServerLinksFragmentBinding;
import com.umer.application.models.ApplicationSettings;
import com.umer.application.models.BaseResponse;
import com.umer.application.models.Songs_list;
import com.umer.application.models.singlePost;
import com.umer.application.networks.Network;
import com.umer.application.networks.NetworkCall;
import com.umer.application.networks.OnNetworkResponse;
import com.umer.application.utils.Constants;
import com.umer.application.utils.RequestCodes;
import com.umer.application.utils.functions;
import com.umer.application.viewModels.ServerLinksViewModel;

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
    private ServerLinksViewModel mViewModel;
    private VideoListAdapter mAdapter;
    private String keyword, imageURL, colorString, admob_Inter_Id, facebook_Inter_Id;
    private int limit, adds;
    private boolean isYoutube;
    private boolean isPlayList;

    public static ServerLinksFragment newInstance() {
        return new ServerLinksFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ServerLinksViewModel.class);

        binding = ServerLinksFragmentBinding.inflate(inflater, container, false);
        ((GridViewActivity) getActivity()).scrollToTop();
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
//            binding.gridView1.setOnItemClickListener((parent, view, position, id) -> {
////                        Toast.makeText(GridViewActivity.this, "Item clicked"+songsList.get(position).getId(), Toast.LENGTH_SHORT).show();
//                itemPosition = songsList.get(position).getId();
//                ((GridViewActivity) getActivity()).getSinglePost(itemPosition);
//
//            });
            binding.serverLink1.setOnClickListener(v -> {
                ((GridViewActivity) getActivity()).clickCount++;
                ((GridViewActivity) getActivity()).scrollToTop();
                ((GridViewActivity) getActivity()).openSinglePost(song.getId(), clickCount, true);
            });
            binding.serverLink2.setOnClickListener(v -> {
                ((GridViewActivity) getActivity()).clickCount++;
                ((GridViewActivity) getActivity()).scrollToTop();
                ((GridViewActivity) getActivity()).openSinglePost(song.getId(), clickCount, true);
            });
            binding.serverLink3.setOnClickListener(v -> {
                ((GridViewActivity) getActivity()).clickCount++;
                ((GridViewActivity) getActivity()).scrollToTop();
                ((GridViewActivity) getActivity()).openSinglePost(song.getId(), clickCount, true);
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
        ((GridViewActivity) getActivity()).clickCount++;
        ((GridViewActivity) getActivity()).showAd();
        NetworkCall.make()
                .setCallback(this)
                .autoLoading(getParentFragmentManager())
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

    public void singlePostResponseHandling(singlePost singlePost1) {
        openWatchNowFirstFragment(songsList);

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
}