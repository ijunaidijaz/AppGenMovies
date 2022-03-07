package com.imtyaz.quranurdutarjuma.callbacks;


import com.imtyaz.quranurdutarjuma.adapters.viewHolders.CategoryTwoViewHolder;
import com.imtyaz.quranurdutarjuma.models.Songs_list;

import java.util.List;

public interface CategoryTwoCallback {
    void onCategoryTowClick(Songs_list songs_list, CategoryTwoViewHolder viewHolder, int position, List<Songs_list> allItems);

}
