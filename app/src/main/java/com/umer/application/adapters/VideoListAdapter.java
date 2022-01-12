package com.umer.application.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.umer.application.R;
import com.umer.application.activities.DailyMotionVideoPlayer;
import com.umer.application.activities.GridViewActivity;
import com.umer.application.activities.YoutubeVideoPlayer;
import com.umer.application.models.ApplicationSettings;
import com.umer.application.models.YoutubeVideoItem;
import com.umer.application.utils.AdsTypes;

import java.util.ArrayList;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<YoutubeVideoItem> mItems = new ArrayList<>();
    boolean isYoutube ;
    int Adds ;
    String id , admob_InterID , facebook_InterID ;
    private InterstitialAd admobInterstitialAd;
    private com.facebook.ads.InterstitialAd facebookInterstitialAd;



    public VideoListAdapter(Context c,boolean isYoutube,int adds ,  @Nullable ArrayList<YoutubeVideoItem> items ,
                            String admob_InterID , String facebook_InterID) {

        mContext = c;
        this.isYoutube = isYoutube ;
        setItems(items);
        this.Adds = adds ;
        this.admob_InterID = admob_InterID;
        this.facebook_InterID = facebook_InterID;
        AudienceNetworkAds.initialize(mContext);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_single_video, parent, false);
        admobInterstitialAds();
        facebookInterstitialAds();


        ViewHolder v = new ViewHolder(view);

        return v;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        YoutubeVideoItem item = mItems.get(position);

        holder.mTitleTV.setText(item.title);
        Glide.with(mContext)
                .load(item.thumbnail_url)
                .thumbnail(Glide.with(mContext.getApplicationContext()).load(R.drawable.loader))
                .centerCrop()
                .into(holder.mImageView);

    }

    @Override
    public int getItemCount() {

        return (mItems == null) ? 0 : mItems.size();

    }


    public void setItems(ArrayList<YoutubeVideoItem> items) {

        mItems.clear();

        if (items != null) {
            mItems.addAll(items);
        }
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitleTV;
        private ImageView mImageView;



        public ViewHolder(View itemView) {
            super(itemView);

            mTitleTV = itemView.findViewById(R.id.songTitle);
            mImageView = itemView.findViewById(R.id.single_image_view);
            admobInterstitialAds();
            admobInterstitialAd.isLoaded();

            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    YoutubeVideoItem i = mItems.get(getAdapterPosition());
                            id = i.id ;
                    if (Adds == AdsTypes.admobAds && isYoutube) {
                        if (admobInterstitialAd.isLoaded()) {
                            admobInterstitialAd.show();
                            admobInterstitialAd.setAdListener(new AdListener() {
                                @Override
                                public void onAdClosed() {

                                    Intent intent = new Intent(mContext, YoutubeVideoPlayer.class);
                                    intent.putExtra("VIDEO_URL", i.id);
                                    mContext.startActivity(intent);
                                }
                            });
                        }else {
                            admobInterstitialAds();
                            admobInterstitialAd.isLoaded();

                            Intent intent = new Intent(mContext, YoutubeVideoPlayer.class);
                            intent.putExtra("VIDEO_URL", i.id);
                            mContext.startActivity(intent);
                        }
                    }
                        else if (Adds == AdsTypes.admobAds && !isYoutube){
                            if (admobInterstitialAd.isLoaded()) {
                                admobInterstitialAd.show();
                                admobInterstitialAd.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdClosed() {

                                        Intent intent = new Intent(mContext, DailyMotionVideoPlayer.class);
                                        intent.putExtra("VIDEO_URL", i.id);
                                        mContext.startActivity(intent);
                                    }
                                });
                        }else {
                                admobInterstitialAds();
                                Intent intent = new Intent(mContext, DailyMotionVideoPlayer.class);
                                intent.putExtra("VIDEO_URL", i.id);
                                mContext.startActivity(intent);
                            }
                    }
                        else if (Adds == AdsTypes.facebooksAds && isYoutube || Adds == AdsTypes.facebooksAds && !isYoutube ){
                            showFacebookInterstitialAds();
                        }
                        else if (Adds == AdsTypes.notActiveAds){
                        if (isYoutube) {
                            Intent intent = new Intent(mContext, YoutubeVideoPlayer.class);
                            intent.putExtra("VIDEO_URL", id);
                            mContext.startActivity(intent);
                        }else {
                            Intent intent = new Intent(mContext, DailyMotionVideoPlayer.class);
                            intent.putExtra("VIDEO_URL", id);
                            mContext.startActivity(intent);
                        }
                    }
                }
            });
        }
    }
    public void admobInterstitialAds()
    {
        admobInterstitialAd = new InterstitialAd(mContext);
        admobInterstitialAd.setAdUnitId(admob_InterID);
//        "ca-app-pub-3940256099942544/1033173712"
        admobInterstitialAd.loadAd(new AdRequest.Builder().build());
    }
    public void facebookInterstitialAds() {
        facebookInterstitialAd = new com.facebook.ads.InterstitialAd(mContext, facebook_InterID );
//        "IMG_16_9_APP_INSTALL#1197001037304304_1197857703885304"
        facebookInterstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

                    if (isYoutube) {
                        Intent intent = new Intent(mContext, YoutubeVideoPlayer.class);
                        intent.putExtra("VIDEO_URL", id);
                        mContext.startActivity(intent);
                }else {
                        Intent intent = new Intent(mContext, DailyMotionVideoPlayer.class);
                        intent.putExtra("VIDEO_URL", id);
                        mContext.startActivity(intent);
                    }

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
            if (isYoutube) {
                Intent intent = new Intent(mContext, YoutubeVideoPlayer.class);
                intent.putExtra("VIDEO_URL", id);
                mContext.startActivity(intent);
            }else {
                Intent intent = new Intent(mContext, DailyMotionVideoPlayer.class);
                intent.putExtra("VIDEO_URL", id);
                mContext.startActivity(intent);
            }
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
}
