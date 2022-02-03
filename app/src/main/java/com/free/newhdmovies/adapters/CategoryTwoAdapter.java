package com.free.newhdmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.free.newhdmovies.R;
import com.free.newhdmovies.adapters.viewHolders.CategoryTwoViewHolder;
import com.free.newhdmovies.callbacks.CategoryTwoCallback;
import com.free.newhdmovies.models.Songs_list;

import java.util.ArrayList;
import java.util.List;

public class CategoryTwoAdapter extends RecyclerView.Adapter<CategoryTwoViewHolder> {

    private final Context context;
    CategoryTwoCallback callback;
    private List<Songs_list> itemList;

    public CategoryTwoAdapter(Context context, List<Songs_list> itemList, CategoryTwoCallback callback) {
        this.context = context;
        this.itemList = itemList;
        this.callback = callback;

    }

    @NonNull
    @Override
    public CategoryTwoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.gridview_horizontal_style, parent, false);
        return new CategoryTwoViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryTwoViewHolder holder, int position) {
        holder.setData(context, itemList.get(position), holder, position, callback, itemList);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void filteredList(ArrayList<Songs_list> filteredList) {
        this.itemList = filteredList;
        notifyDataSetChanged();
    }

    public void addItem(Songs_list item) {
        itemList.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        if (itemList != null && !itemList.isEmpty()) {
            int size = itemList.size();
            itemList.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

    public List<Songs_list> getAllUsers() {
        return itemList;
    }
}