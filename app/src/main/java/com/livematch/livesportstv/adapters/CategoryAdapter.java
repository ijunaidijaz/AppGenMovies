package com.livematch.livesportstv.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.livematch.livesportstv.R;
import com.livematch.livesportstv.adapters.viewHolders.CategoryViewHolder;
import com.livematch.livesportstv.callbacks.CategoryCallback;
import com.livematch.livesportstv.models.Songs_list;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private final Context context;
    CategoryCallback callback;
    private List<Songs_list> itemList;

    public CategoryAdapter(Context context, List<Songs_list> itemList, CategoryCallback callback) {
        this.context = context;
        this.itemList = itemList;
        this.callback = callback;

    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.gridview_horizontal_style, parent, false);
        return new CategoryViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
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