package com.free.newhdmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.InterstitialAd;
import com.free.newhdmovies.R;
import com.free.newhdmovies.activities.DailyMotionVideoPlayer;
import com.free.newhdmovies.activities.MainActivity;
import com.free.newhdmovies.activities.YoutubeVideoPlayer;
import com.free.newhdmovies.models.YoutubeVideoItem;

import java.util.ArrayList;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {

    boolean isYoutube;
    int Adds;
    String id, admob_InterID, facebook_InterID;
    public Context mContext;
    private ArrayList<YoutubeVideoItem> mItems = new ArrayList<>();
    private InterstitialAd admobInterstitialAd;
    private com.facebook.ads.InterstitialAd facebookInterstitialAd;


    public VideoListAdapter(Context c, boolean isYoutube, int adds, @Nullable ArrayList<YoutubeVideoItem> items,
                            String admob_InterID, String facebook_InterID) {

        mContext = c;
        this.isYoutube = isYoutube;
        setItems(items);
        this.Adds = adds;
        this.admob_InterID = admob_InterID;
        this.facebook_InterID = facebook_InterID;
        AudienceNetworkAds.initialize(mContext);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_single_video, parent, false);
//        admobInterstitialAds();
//        facebookInterstitialAds();


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
//            admobInterstitialAds();
//            admobInterstitialAd.isLoaded();

            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    YoutubeVideoItem i = mItems.get(getAdapterPosition());
                    MainActivity.clickCount++;
//                    ((GridViewActivity)mContext).showAd();
                    id = i.id;
                    if (isYoutube) {

                        Intent intent = new Intent(mContext, YoutubeVideoPlayer.class);
                        intent.putExtra("VIDEO_URL", i.id);
                        mContext.startActivity(intent);

                    } else {
                        Intent intent = new Intent(mContext, DailyMotionVideoPlayer.class);
                        intent.putExtra("VIDEO_URL", i.id);
                        mContext.startActivity(intent);

                    }
//                        if (Adds == AdsTypes.admobAds && isYoutube) {
//
//                        Intent intent = new Intent(mContext, YoutubeVideoPlayer.class);
//                        intent.putExtra("VIDEO_URL", i.id);
//                        mContext.startActivity(intent);
//
//                    } else if (Adds == AdsTypes.admobAds && !isYoutube) {
//                        Intent intent = new Intent(mContext, DailyMotionVideoPlayer.class);
//                        intent.putExtra("VIDEO_URL", i.id);
//                        mContext.startActivity(intent);
//
//
//                    } else if (Adds == AdsTypes.facebooksAds && isYoutube || Adds == AdsTypes.facebooksAds && !isYoutube) {
////                        showFacebookInterstitialAds();
//                    } else if (Adds == AdsTypes.startAppAds && isYoutube ) {
////                        showFacebookInterstitialAds();
//                    }else if (Adds == AdsTypes.notActiveAds) {
//                        if (isYoutube) {
//                            Intent intent = new Intent(mContext, YoutubeVideoPlayer.class);
//                            intent.putExtra("VIDEO_URL", id);
//                            mContext.startActivity(intent);
//                        } else {
//                            Intent intent = new Intent(mContext, DailyMotionVideoPlayer.class);
//                            intent.putExtra("VIDEO_URL", id);
//                            mContext.startActivity(intent);
//                        }
//                    }
                }
            });
        }
    }
}
