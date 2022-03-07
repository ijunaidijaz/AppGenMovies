package com.imtyaz.quranurdutarjuma.callbacks;


import com.imtyaz.quranurdutarjuma.adapters.viewHolders.MoviesViewHolder;
import com.imtyaz.quranurdutarjuma.models.Songs_list;

public interface MoviesCallback {
    void onMovieClick(Songs_list songs_list, MoviesViewHolder viewHolder, int position);

}
