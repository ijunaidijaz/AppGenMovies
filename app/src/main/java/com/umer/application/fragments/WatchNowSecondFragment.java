package com.umer.application.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umer.application.R;
import com.umer.application.activities.GridViewActivity;
import com.umer.application.adapters.GridViewAdapter;
import com.umer.application.adapters.VideoListAdapter;
import com.umer.application.databinding.WatchNowFirstFragmentBinding;
import com.umer.application.databinding.WatchNowSecondFragmentBinding;
import com.umer.application.models.ApplicationSettings;
import com.umer.application.models.Songs_list;
import com.umer.application.utils.Constants;
import com.umer.application.utils.functions;
import com.umer.application.viewModels.WatchNowFirstViewModel;
import com.umer.application.viewModels.WatchNowSecondViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WatchNowSecondFragment extends Fragment {

    private WatchNowSecondViewModel mViewModel;
    WatchNowSecondFragmentBinding binding;
    private VideoListAdapter mAdapter;
    private String keyword, imageURL, colorString, admob_Inter_Id, facebook_Inter_Id;
    private int limit, adds;
    private boolean isYoutube;
    int itemPosition=0;
    int clickCount=0;
    private boolean isPlayList;
    List<Songs_list> songsList = new ArrayList<>();
    ApplicationSettings applicationSettings;

    public static WatchNowSecondFragment newInstance() {
        return new WatchNowSecondFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(WatchNowSecondViewModel.class);
        binding = WatchNowSecondFragmentBinding.inflate(inflater, container, false);

        binding.header.backbtnHeader.setOnClickListener(v1 -> getParentFragmentManager().beginTransaction().remove(WatchNowSecondFragment.this).commit());

        if (getArguments() != null) {
            applicationSettings = (ApplicationSettings) getArguments().getSerializable("applicationSettings");
            Songs_list song = (Songs_list) getArguments().getSerializable("selectedVideo");
            functions.GlideImageLoaderWithPlaceholder(getContext(), binding.imageView, Constants.BASE_URL_IMAGES + song.getUrl());
            binding.movieTitle.setText(song.getTitle());

            binding.gridView1.setNumColumns(applicationSettings.getRowDisplay());
            binding.header.headerBar.setBackgroundColor(Color.parseColor(applicationSettings.getActionBarColor()));

            songsList = (List<Songs_list>) getArguments().getSerializable("VideosList");
            GridViewAdapter myAdapter;
            if (applicationSettings.getRowDisplay() == 1) {
                myAdapter = new GridViewAdapter(getContext(), R.layout.grid_view_style_single_post, (ArrayList) songsList);

            } else {
                myAdapter = new GridViewAdapter(getContext(), R.layout.gridview_style, (ArrayList) songsList);
            }
            binding.gridView1.setAdapter(myAdapter);
            binding.gridView1.smoothScrollToPosition(0);
            binding.gridView1.setOnItemClickListener((parent, view, position, id) -> {
//                        Toast.makeText(GridViewActivity.this, "Item clicked"+songsList.get(position).getId(), Toast.LENGTH_SHORT).show();
                itemPosition = songsList.get(position).getId();
                ((GridViewActivity)getActivity()).getSinglePost(itemPosition);

            });
            binding.watchNow.setOnClickListener(v -> {
                openServerLinkFragment(song);
            });

            imageURL =  applicationSettings.getLog();
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
}