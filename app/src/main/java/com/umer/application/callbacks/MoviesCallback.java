package com.umer.application.callbacks;


import com.umer.application.adapters.viewHolders.MoviesViewHolder;
import com.umer.application.models.Songs_list;

public interface MoviesCallback {
    void onMovieClick(Songs_list songs_list, MoviesViewHolder viewHolder, int position);

}
