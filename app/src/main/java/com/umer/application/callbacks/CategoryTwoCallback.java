package com.umer.application.callbacks;


import com.umer.application.adapters.viewHolders.CategoryTwoViewHolder;
import com.umer.application.models.Songs_list;

import java.util.List;

public interface CategoryTwoCallback {
    void onCategoryTowClick(Songs_list songs_list, CategoryTwoViewHolder viewHolder, int position, List<Songs_list> allItems);

}
