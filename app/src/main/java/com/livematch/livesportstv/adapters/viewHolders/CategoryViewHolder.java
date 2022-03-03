package com.livematch.livesportstv.adapters.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.livematch.livesportstv.R;
import com.livematch.livesportstv.callbacks.CategoryCallback;
import com.livematch.livesportstv.models.Songs_list;
import com.livematch.livesportstv.utils.Constants;
import com.livematch.livesportstv.utils.functions;

import java.util.List;


public class CategoryViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public ImageView imageView;
    public View parent;
    LinearLayout titleBg;
    Context context;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        parent = itemView;
//        title = itemView.findViewById(R.id.title);
        imageView = itemView.findViewById(R.id.imageView);


    }


    public void setData(Context context, Songs_list item, CategoryViewHolder holder, int position, CategoryCallback callback, List<Songs_list> itemList) {
        functions.GlideImageLoaderWithPlaceholder(context, imageView, Constants.BASE_URL_IMAGES + item.getUrl());
        parent.setOnClickListener(v -> {
            callback.onCategoryClick(item, holder, position, itemList);
        });
    }
}
