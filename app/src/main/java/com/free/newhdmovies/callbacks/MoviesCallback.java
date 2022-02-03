package com.free.newhdmovies.callbacks;


import com.free.newhdmovies.adapters.viewHolders.MoviesViewHolder;
import com.free.newhdmovies.models.Songs_list;

public interface MoviesCallback {
    void onMovieClick(Songs_list songs_list, MoviesViewHolder viewHolder, int position);

}
