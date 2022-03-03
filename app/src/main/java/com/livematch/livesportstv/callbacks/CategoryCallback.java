package com.livematch.livesportstv.callbacks;


import com.livematch.livesportstv.adapters.viewHolders.CategoryViewHolder;
import com.livematch.livesportstv.models.Songs_list;

import java.util.List;

public interface CategoryCallback {
    void onCategoryClick(Songs_list songs_list, CategoryViewHolder viewHolder, int position, List<Songs_list> allItems);

}
