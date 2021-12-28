package com.umer.application.adapters.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.umer.application.R;
import com.umer.application.callbacks.MoviesCallback;
import com.umer.application.models.Songs_list;
import com.umer.application.utils.Constants;
import com.umer.application.utils.functions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class MoviesViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public ImageView imageView;
    public View parent;
    LinearLayout titleBg;
    Context context;

    public MoviesViewHolder(@NonNull View itemView) {
        super(itemView);
        parent = itemView;
//        title = itemView.findViewById(R.id.title);
        imageView = itemView.findViewById(R.id.imageView);


    }


    public void setData(Context context, Songs_list item, MoviesViewHolder holder, int position, MoviesCallback callback) {
        functions.GlideImageLoaderWithPlaceholder(context , imageView , Constants.BASE_URL_IMAGES+item.getUrl());
        parent.setOnClickListener(v -> {
            callback.onMovieClick(item, holder, position);
        });
    }
}
