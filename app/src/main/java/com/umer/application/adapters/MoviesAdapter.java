package com.umer.application.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.umer.application.R;
import com.umer.application.adapters.viewHolders.MoviesViewHolder;
import com.umer.application.callbacks.MoviesCallback;
import com.umer.application.models.Songs_list;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesViewHolder> {

    private final Context context;
    private List<Songs_list> itemList;
    MoviesCallback callback;

    public MoviesAdapter(Context context, List<Songs_list> itemList, MoviesCallback callback) {
        this.context = context;
        this.itemList = itemList;
        this.callback = callback;

    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.gridview_style, parent, false);
        return new MoviesViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        holder.setData(context, itemList.get(position), holder, position,callback);
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