package com.umer.application.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.umer.application.R;
import com.umer.application.adapters.VideoListAdapter;
import com.umer.application.models.YoutubeVideoItem;
import com.umer.application.models.dailymotionSearchHelper;
import com.umer.application.networks.ApiServices;
import com.umer.application.utils.Constants;
import com.umer.application.utils.DailyMotionSearchHelper;
import com.umer.application.utils.YoutubeSearchHelper;
import com.umer.application.utils.functions;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.umer.application.utils.Constants.DAILYMOTION_BASE_URL;

public class VideoListFragment extends Fragment {


    private VideoListAdapter mAdapter;
    private String keyword , imageURL , colorString , admob_Inter_Id , facebook_Inter_Id;
    private int limit , adds;
    private boolean isYoutube;
    private boolean isPlayList ;
    private ImageView imageView , backBtn;
    private RelativeLayout header ;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EventBus.getDefault().post("inFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_video_list, container, false);
        backBtn = v.findViewById(R.id.backbtn_header);
        imageView = v.findViewById(R.id.image_view);
        header = v.findViewById(R.id.headerBar);

        backBtn.setOnClickListener(v1 -> getFragmentManager().beginTransaction().remove(VideoListFragment.this).commit());

        if (getArguments() != null) {
            imageURL = getArguments().getString("appIcon");
            functions.GlideImageLoaderWithPlaceholder(getContext(), imageView, Constants.BASE_URL_IMAGES + imageURL);
            colorString = getArguments().getString("Color");
            header.setBackgroundColor(Color.parseColor(colorString));
            admob_Inter_Id = getArguments().getString("ADMOB_INTER_ID");
            facebook_Inter_Id = getArguments().getString("FACEBOOK_INTER_ID");


            if (getArguments().getBoolean("isPLAYLIST")){
                keyword = getArguments().getString("PLAYLIST_ID");
            }
            else {
                keyword = getArguments().getString("KEYWORD");
            }

            limit = getArguments().getInt("LIMIT");
            isYoutube = getArguments().getBoolean("isYoutube");
            isPlayList = getArguments().getBoolean("isPLAYLIST") ;
            adds = getArguments().getInt("ADDS" , -1);

        }
        RecyclerView videoListRecyclerView = v.findViewById(R.id.recyclerView_videoList);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        videoListRecyclerView.setLayoutManager(manager);

        mAdapter = new VideoListAdapter(getActivity(),isYoutube,adds, null , admob_Inter_Id , facebook_Inter_Id);

        videoListRecyclerView.setAdapter(mAdapter);

        if (isYoutube){
            startLoadingYoutubeVideos();
        } else if(!isPlayList && !isYoutube){
            getDailyMotionVideos(keyword);
        }else {
            startLoadingDailyMotionPlayList(getArguments().getString("PLAYLIST_ID"));
        }

        return v;
    }

    private void startLoadingYoutubeVideos() {

        YoutubeSearchHelper searchHelper = new YoutubeSearchHelper(getContext());
        searchHelper.searchYoutube(keyword, isPlayList, videos -> {
            mAdapter.setItems(videos);
            mAdapter.notifyDataSetChanged();
        });

    }

    private void startLoadingDailyMotionPlayList(String playListId){
        DailyMotionSearchHelper dailyMotionSearchHelper = new DailyMotionSearchHelper(getContext());
        dailyMotionSearchHelper.searchDailyMotion(playListId, videos -> {
            mAdapter.setItems(videos);
            mAdapter.notifyDataSetChanged();
        }
        );

    }

    private void getDailyMotionVideos(String keyWord){

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.DAILYMOTION_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            ApiServices getDailyMotionVideos = retrofit.create(ApiServices.class);
        Call<dailymotionSearchHelper> dailymotionSearchHelperCall = getDailyMotionVideos.getDailyMotionVideos(
                "id,thumbnail_url%2Ctitle" , "pk",keyWord , limit);
        dailymotionSearchHelperCall.enqueue(new Callback<dailymotionSearchHelper>() {
            @Override
            public void onResponse(Call<dailymotionSearchHelper> call, Response<dailymotionSearchHelper> response) {
                dailymotionSearchHelper daily = response.body();
                mAdapter.setItems(daily.getSongs_List());

            }

            @Override
            public void onFailure(Call<dailymotionSearchHelper> call, Throwable t) {

            }
        });

    }


}