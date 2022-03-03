package com.livematch.livesportstv.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.ads.InterstitialAd;
import com.livematch.livesportstv.R;
import com.livematch.livesportstv.activities.MainActivity;
import com.livematch.livesportstv.activities.WebViewActivity;
import com.livematch.livesportstv.models.AppSlider;
import com.livematch.livesportstv.models.ApplicationSettings;
import com.livematch.livesportstv.utils.AdsTypes;
import com.livematch.livesportstv.utils.Constants;
import com.livematch.livesportstv.utils.functions;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;


public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    List<AppSlider> images;
    int clickCount = 0;
    ApplicationSettings applicationSettings;
    String webURL;
    private Context context;
    private InterstitialAd admobInterstitialAd;
//    private com.facebook.ads.InterstitialAd facebookInterstitialAd;


    public SliderAdapter(Context context, List<AppSlider> images, ApplicationSettings applicationSettings) {
        this.context = context;
        this.images = images;
        this.applicationSettings = applicationSettings;
//        AudienceNetworkAds.initialize(context);


    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_image_layout, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        if (applicationSettings.getAdds() == AdsTypes.admobAds) {
//            admobInterstitialAds();
        } else if (applicationSettings.getAdds() == AdsTypes.facebooksAds) {
//            facebookInterstitialAds();
        }

        Log.e("Urls Images", "" + Constants.BASE_URL_IMAGES + images.get(position).getUrl());
        functions.GlideImageLoader(context, viewHolder.imageViewBackground, Constants.BASE_URL_IMAGES + images.get(position).getUrl());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webURL = images.get(position).getWebUrl();
                clickCount++;
                if (clickCount > applicationSettings.getAdMobLimit())
                    clickCount = applicationSettings.getAdMobLimit();

                //Ads active and open any normal post
                if (images.get(position).getRedirectApp().isEmpty() && images.get(position).getWebUrl().isEmpty()) {
                    ((MainActivity) context).openSinglePost(images.get(position).getId(), clickCount);
                }
                // ads are active or not open Google play Store without adds
                else if (images.get(position).getWebUrl().isEmpty()) {
                    openAppOnPlayStore(images.get(position).getRedirectApp());
                }
                //Admob ads are Active open Web URL
                else if (images.get(position).getRedirectApp().isEmpty() && applicationSettings.getAdds() == AdsTypes.admobAds) {
                    if (clickCount == applicationSettings.getAdMobLimit()) {
//                        admobInterstitialAds();

                        openWebUrl(images.get(position).getWebUrl());

                    } else {
                        openWebUrl(images.get(position).getWebUrl());
                    }
                }
                //facebook ads are Active open Web URL
                else if (images.get(position).getRedirectApp().isEmpty() && applicationSettings.getAdds() == AdsTypes.facebooksAds) {
                    if (clickCount == applicationSettings.getAdMobLimit()) {

                    } else {
                        openWebUrl(images.get(position).getWebUrl());
                    }

                }
// --------------------------------When there is no Ads Active this flow will work -----------------------------
                else if (applicationSettings.getAdds() == AdsTypes.notActiveAds) {
                    if (images.get(position).getRedirectApp().equals("") && images.get(position).getWebUrl().isEmpty()) {
                        ((MainActivity) context).openSinglePostWithoutAdd(images.get(position).getId());
                    } else if (images.get(position).getRedirectApp().equals("")) {
                        openWebUrl(images.get(position).getWebUrl());
                    } else {
                        openAppOnPlayStore(images.get(position).getRedirectApp());
                    }
                }


            }
        });

    }

    @Override
    public int getCount() {
//slider view count could be dynamic size
        return images.size();
    }

    public void openAppOnPlayStore(String appPackageName) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void openWebUrl(String url) {
        Intent i = new Intent(context, WebViewActivity.class);
        i.putExtra("WEBURL", url);
        context.startActivity(i);
    }


    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.sliding_images);
            this.itemView = itemView;
        }
    }


}