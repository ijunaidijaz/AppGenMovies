package com.livematch.livesportstv.callbacks;


import com.livematch.livesportstv.adapters.viewHolders.MoviesViewHolder;
import com.livematch.livesportstv.models.Songs_list;

public interface MoviesCallback {
    void onMovieClick(Songs_list songs_list, MoviesViewHolder viewHolder, int position);

}
