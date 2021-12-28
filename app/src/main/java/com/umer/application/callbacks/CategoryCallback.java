package com.umer.application.callbacks;


import com.umer.application.adapters.viewHolders.CategoryViewHolder;
import com.umer.application.adapters.viewHolders.MoviesViewHolder;
import com.umer.application.models.Songs_list;

public interface CategoryCallback {
    void onCategoryClick(Songs_list songs_list, CategoryViewHolder viewHolder, int position);

}
