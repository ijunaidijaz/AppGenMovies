package com.imtyaz.quranurdutarjuma.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.imtyaz.quranurdutarjuma.R;
import com.imtyaz.quranurdutarjuma.models.AppSlider;
import com.imtyaz.quranurdutarjuma.utils.Constants;
import com.imtyaz.quranurdutarjuma.utils.functions;

import java.util.ArrayList;

public class imagesAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<AppSlider> sliderArrayList;
    private LayoutInflater layoutInflater;

    public imagesAdapter(Context mContext, ArrayList<AppSlider> sliderArrayList) {
        this.mContext = mContext;
        this.sliderArrayList = sliderArrayList;
    }

    @Override
    public int getCount() {
        return sliderArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View item_view = layoutInflater.inflate(R.layout.slider_image_layout, container, false);

        assert item_view != null;
        ImageView imageView = item_view.findViewById(R.id.sliding_images);

        functions.GlideImageLoaderWithPlaceholder(mContext, imageView, Constants.BASE_URL_IMAGES + sliderArrayList.get(position).getUrl());

        container.addView(item_view);
//        item_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, "Slider Clicked", Toast.LENGTH_SHORT).show();
//                if (sliderArrayList.get(position).getRedirectApp().isEmpty() && !sliderArrayList.get(position).getRedirectApp().equals("")){
//                    if (sliderArrayList.get(position).getWebUrl().isEmpty() && sliderArrayList.get(position).getWebUrl() != null){
//                        //do nothing
//                    }
//                }else if (!sliderArrayList.get(position).getWebUrl().isEmpty()){
//                    openWebUrl(sliderArrayList.get(position).getWebUrl());
//                }else if (!sliderArrayList.get(position).getRedirectApp().isEmpty()){
//                    openAppOnPlayStore(sliderArrayList.get(position).getRedirectApp());
//                }
//            }
//        });

        return item_view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
