package com.free.newhdmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.free.newhdmovies.R;
import com.free.newhdmovies.models.Songs_list;
import com.free.newhdmovies.utils.Constants;
import com.free.newhdmovies.utils.functions;

import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter {
    ArrayList<Songs_list> songsList = new ArrayList<>();


    public GridViewAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context, textViewResourceId, objects);
        songsList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.gridview_style, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        functions.GlideImageLoaderWithPlaceholder(getContext(), imageView, Constants.BASE_URL_IMAGES + songsList.get(position).getUrl());
        return view;
    }
}
