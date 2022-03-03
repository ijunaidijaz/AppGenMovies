package com.livematch.livesportstv.callbacks;


import com.livematch.livesportstv.adapters.viewHolders.CategoryTwoViewHolder;
import com.livematch.livesportstv.models.Songs_list;

import java.util.List;

public interface CategoryTwoCallback {
    void onCategoryTowClick(Songs_list songs_list, CategoryTwoViewHolder viewHolder, int position, List<Songs_list> allItems);

}
