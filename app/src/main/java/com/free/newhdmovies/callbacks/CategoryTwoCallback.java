package com.free.newhdmovies.callbacks;


import com.free.newhdmovies.adapters.viewHolders.CategoryTwoViewHolder;
import com.free.newhdmovies.models.Songs_list;

import java.util.List;

public interface CategoryTwoCallback {
    void onCategoryTowClick(Songs_list songs_list, CategoryTwoViewHolder viewHolder, int position, List<Songs_list> allItems);

}
