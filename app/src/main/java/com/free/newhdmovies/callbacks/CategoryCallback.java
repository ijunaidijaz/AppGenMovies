package com.free.newhdmovies.callbacks;


import com.free.newhdmovies.adapters.viewHolders.CategoryViewHolder;
import com.free.newhdmovies.models.Songs_list;

import java.util.List;

public interface CategoryCallback {
    void onCategoryClick(Songs_list songs_list, CategoryViewHolder viewHolder, int position, List<Songs_list> allItems);

}
