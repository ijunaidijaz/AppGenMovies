package com.umer.application.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.umer.application.R;

public class functions {


 public static void GlideImageLoaderWithPlaceholder(Context context, ImageView imageView, String url) {
        url = urlSlashChange(url);
        Glide.with(context)
                .load(url)
                .thumbnail(Glide.with(context.getApplicationContext()).load(R.drawable.loading))
                .into(imageView);
    }

    public static String urlSlashChange(String url)
    {
        if (url!=null&&!url.isEmpty())
        {
                if(url.contains("~")){
                    url = url.replace("~" , "");
                }
        }

        return url;
    }

    public static void GlideImageLoader(Context context, ImageView imageView, String url) {
        url = urlSlashChange(url);
        Glide.with(context)
                .load(url)
                .into(imageView);
    }




}
