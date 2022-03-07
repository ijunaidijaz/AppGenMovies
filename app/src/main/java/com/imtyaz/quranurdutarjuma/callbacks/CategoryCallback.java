package com.imtyaz.quranurdutarjuma.callbacks;


import com.imtyaz.quranurdutarjuma.adapters.viewHolders.CategoryViewHolder;
import com.imtyaz.quranurdutarjuma.models.Songs_list;

import java.util.List;

public interface CategoryCallback {
    void onCategoryClick(Songs_list songs_list, CategoryViewHolder viewHolder, int position, List<Songs_list> allItems);

}
