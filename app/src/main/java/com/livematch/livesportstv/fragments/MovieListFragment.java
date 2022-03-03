package com.livematch.livesportstv.fragments;

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

import com.livematch.livesportstv.R;
import com.livematch.livesportstv.activities.MainActivity;
import com.livematch.livesportstv.adapters.MoviesAdapter;
import com.livematch.livesportstv.adapters.VideoListAdapter;
import com.livematch.livesportstv.adapters.viewHolders.MoviesViewHolder;
import com.livematch.livesportstv.callbacks.MoviesCallback;
import com.livematch.livesportstv.databinding.MovieListFragmentBinding;
import com.livematch.livesportstv.models.ApplicationSettings;
import com.livematch.livesportstv.models.Songs_list;
import com.livematch.livesportstv.utils.Constants;
import com.livematch.livesportstv.utils.functions;
import com.livematch.livesportstv.viewModels.MovieListViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MovieListFragment extends Fragment implements MoviesCallback {

    MovieListFragmentBinding binding;
    int itemPosition = 0;
    int clickCount = 0;
    List<Songs_list> songsList = new ArrayList<>();
    ApplicationSettings applicationSettings;
    private MovieListViewModel mViewModel;
    private VideoListAdapter mAdapter;
    private String keyword, imageURL, colorString, admob_Inter_Id, facebook_Inter_Id;
    private int limit, adds;
    private boolean isYoutube;
    private boolean isPlayList;

    public static MovieListFragment newInstance() {
        return new MovieListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);
        binding = MovieListFragmentBinding.inflate(inflater, container, false);
        ((MainActivity) getActivity()).scrollToTop();
        binding.headerBar.backbtnHeader.setOnClickListener(v1 -> getActivity().onBackPressed());
        if (getArguments() != null) {
            applicationSettings = (ApplicationSettings) getArguments().getSerializable("applicationSettings");
            binding.headerBar.headerBar.setBackgroundColor(Color.parseColor(applicationSettings.getActionBarColor()));
            binding.listTitle.setText((String) getArguments().get("TITLE"));
            songsList = (List<Songs_list>) getArguments().getSerializable("VideosList");
            setMoviesAdapter(songsList);

            imageURL = getArguments().getString("appIcon");
            functions.GlideImageLoaderWithPlaceholder(getContext(), binding.headerBar.imageView, Constants.BASE_URL_IMAGES + imageURL);

        }
        binding.gridView1.smoothScrollToPosition(0);
        return binding.getRoot();
    }


    public void setMoviesAdapter(List<Songs_list> lists) {
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), applicationSettings.getRowDisplay());
        MoviesAdapter adapter = new MoviesAdapter(getContext(), lists, this);
        binding.gridView1.setLayoutManager(linearLayoutManager);
        binding.gridView1.setAdapter(adapter);
    }

    public void openWatchNowFirstFragment(Songs_list songs_list) {
        ((MainActivity) getActivity()).clickCount++;
        ((MainActivity) getActivity()).showAd();
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