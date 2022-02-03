package com.free.newhdmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.free.newhdmovies.R;
import com.free.newhdmovies.activities.GridViewActivity;
import com.free.newhdmovies.activities.WebViewActivity;
import com.free.newhdmovies.models.AppSlider;
import com.free.newhdmovies.models.ApplicationSettings;
import com.free.newhdmovies.utils.AdsTypes;
import com.free.newhdmovies.utils.Constants;
import com.free.newhdmovies.utils.functions;

import java.util.List;


public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    List<AppSlider> images;
    int clickCount = 0;
    ApplicationSettings applicationSettings;
    String webURL;
    private Context context;
    private InterstitialAd admobInterstitialAd;
    private com.facebook.ads.InterstitialAd facebookInterstitialAd;


    public SliderAdapter(Context context, List<AppSlider> images, ApplicationSettings applicationSettings) {
        this.context = context;
        this.images = images;
        this.applicationSettings = applicationSettings;
        AudienceNetworkAds.initialize(context);


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
                    ((GridViewActivity) context).openSinglePost(images.get(position).getId(), clickCount);
                }
                // ads are active or not open Google play Store without adds
                else if (images.get(position).getWebUrl().isEmpty()) {
                    openAppOnPlayStore(images.get(position).getRedirectApp());
                }
                //Admob ads are Active open Web URL
                else if (images.get(position).getRedirectApp().isEmpty() && applicationSettings.getAdds() == AdsTypes.admobAds) {
                    if (clickCount == applicationSettings.getAdMobLimit()) {
//                        admobInterstitialAds();
                        if (admobInterstitialAd.isLoaded()) {
                            admobInterstitialAd.show();
                            admobInterstitialAd.setAdListener(new AdListener() {
                                @Override
                                public void onAdClosed() {
                                    admobInterstitialAds();
                                    openWebUrl(images.get(position).getWebUrl());

                                }
                            });
                        } else {
                            admobInterstitialAds();
                            openWebUrl(images.get(position).getWebUrl());

                        }

                    } else {
                        admobInterstitialAds();
                        openWebUrl(images.get(position).getWebUrl());
                    }
                }
                //facebook ads are Active open Web URL
                else if (images.get(position).getRedirectApp().isEmpty() && applicationSettings.getAdds() == AdsTypes.facebooksAds) {
                    if (clickCount == applicationSettings.getAdMobLimit()) {
                        showFacebookInterstitialAds();

                    } else {
                        openWebUrl(images.get(position).getWebUrl());
                    }

                }
// --------------------------------When there is no Ads Active this flow will work -----------------------------
                else if (applicationSettings.getAdds() == AdsTypes.notActiveAds) {
                    if (images.get(position).getRedirectApp().equals("") && images.get(position).getWebUrl().isEmpty()) {
                        ((GridViewActivity) context).openSinglePostWithoutAdd(images.get(position).getId());
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

    public void admobInterstitialAds() {
        admobInterstitialAd = new InterstitialAd(context);
        admobInterstitialAd.setAdUnitId(context.getResources().getString(R.string.ADMOB_INTER_ID));
        admobInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    public void facebookInterstitialAds() {
        facebookInterstitialAd = new com.facebook.ads.InterstitialAd(context, context.getResources().getString(R.string.FACEBOOK_INTER_ID));
        facebookInterstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                openWebUrl(webURL);

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

        });

        facebookInterstitialAd.loadAd();
    }

    public void showFacebookInterstitialAds() {
        if (facebookInterstitialAd == null || !facebookInterstitialAd.isAdLoaded()) {
            facebookInterstitialAds();
            openWebUrl(webURL);
        }
        // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
        if (facebookInterstitialAd.isAdInvalidated()) {

            facebookInterstitialAds();
        }
        if (facebookInterstitialAd != null && facebookInterstitialAd.isAdLoaded() && !facebookInterstitialAd.isAdInvalidated())
            // Show the ad
            facebookInterstitialAd.show();
        facebookInterstitialAds();
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