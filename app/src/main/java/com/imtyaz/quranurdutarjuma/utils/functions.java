package com.imtyaz.quranurdutarjuma.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.imtyaz.quranurdutarjuma.R;

public class functions {


    public static void GlideImageLoaderWithPlaceholder(Context context, ImageView imageView, String url) {
        url = urlSlashChange(url);
        Glide.with(context)
                .load(url)
                .thumbnail(Glide.with(context.getApplicationContext()).load(R.drawable.loader))
                .into(imageView);
    }

    public static String urlSlashChange(String url) {
        if (url != null && !url.isEmpty()) {
            if (url.contains("~")) {
                url = url.replace("~", "");
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
