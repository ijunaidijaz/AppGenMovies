package com.livematch.livesportstv.networks;

import com.livematch.livesportstv.models.AppSlider;
import com.livematch.livesportstv.models.ApplicationSettings;
import com.livematch.livesportstv.models.Songs_list;
import com.livematch.livesportstv.models.dailymotionSearchHelper;
import com.livematch.livesportstv.models.singlePost;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {

    @GET("v3/application/AppSetting")
    Call<ApplicationSettings> getApplicationSettings(@Query("input") String packageName);

    @GET("v2/applicationPost/GetAllPost")
    Call<ArrayList<Songs_list>> getAllPosts(@Query("categoryId") Integer categoryId,
                                            @Query("subcategoryId") Integer subcategoryId);
    @GET("v2/applicationPost/GetAllPost")
    Call<ArrayList<Songs_list>> getAllPosts(@Query("categoryId") Integer categoryId,
                                            @Query("subcategoryId") String subcategories);
    @GET("v2/applicationPost/getPostByCategory")
    Call<ArrayList<Songs_list>> getPostsByCategory(@Query("categoryId") Integer categoryId,
                                                   @Query("subcategoryId") Integer subcategoryId,
                                                   @Query("pageSize") Integer pageSize,
                                                   @Query("page") Integer page);

    @GET("v2/applicationPost/getPostByCategory")
    Call<ArrayList<Songs_list>> getPostsByCategory(@Query("categoryId") Integer categoryId,
                                                   @Query("subcategoryId") List<Integer> subcategories,
                                                   @Query("pageSize") Integer pageSize,
                                                   @Query("page") Integer page);
    @GET("v2/applicationPost/getPostDescription")
    Call<singlePost> getSinglePost(@Query("postId") Integer postID);

        @GET("v3/application/AppSlider")
    Call<ArrayList<AppSlider>> getApplicationSlider(@Query("packageName") String packageName);

    @GET("videos")
    Call<dailymotionSearchHelper> getDailyMotionVideos(@Query("fields") String fields, @Query("country") String country, @Query("search") String keyWord,
                                                       @Query("limit") int limit);

}