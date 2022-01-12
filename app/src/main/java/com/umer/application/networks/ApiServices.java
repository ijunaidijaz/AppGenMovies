package com.umer.application.networks;

import com.umer.application.models.AppSlider;
import com.umer.application.models.ApplicationSettings;
import com.umer.application.models.Songs_list;
import com.umer.application.models.dailymotionSearchHelper;
import com.umer.application.models.singlePost;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServices {

//    @GET("v2/application/setting")
    @GET("v3/application/AppSetting")
//    Call<ApplicationSettings> getApplicationSettings(@Query("pakgeName") String packageName);
    Call<ApplicationSettings> getApplicationSettings(@Query("input") String packageName);

    @GET("v1/GetVideo/getAllPost")
    Call<ArrayList<Songs_list>> getAllPosts(@Query("pakgeName") String packageName);
    @GET("v2/applicationPost/getPostByCategory")
    Call<ArrayList<Songs_list>> getPostsByCategory(@Query("categoryId") Integer categoryId,
                                            @Query("subcategoryId") Integer subcategoryId,
                                            @Query("pageSize") Integer pageSize,
                                            @Query("page") Integer page);

//    @GET("v2/applicationPost/getPostDiscraption")
    @GET("v2/applicationPost/getPostDiscraption")
    Call<singlePost> getSinglePost(@Query("postId") Integer postID);

//    @GET("v2/application/slider")
    @GET("vi/GetAppSlider/PakegName")
    Call<ArrayList<AppSlider>> getApplicationSlider(@Query("pakgeName") String packageName);

    @GET("videos")
    Call<dailymotionSearchHelper> getDailyMotionVideos(@Query("fields") String fields , @Query("country") String country , @Query("search") String keyWord,
                                                       @Query("limit") int limit);

}