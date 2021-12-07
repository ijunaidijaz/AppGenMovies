package com.umer.application.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.umer.application.models.ApplicationSettings;
import com.umer.application.models.YoutubeVideoItem;
import com.umer.application.models.dailymotionSearchHelper;
import com.umer.application.networks.ApiServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DailyMotionSearchHelper {
    Context mContext;
    ApplicationSettings applicationSettings;
    private static String DAILY_MOTION_URL_FRONT_PART;
    private static String DAILY_MOTION_URL_BACK_PART;

    public DailyMotionSearchHelper(Context mContext) {
        this.mContext = mContext;
        applicationSettings = new ApplicationSettings().retrieveApplicationSettings(mContext);
        DAILY_MOTION_URL_FRONT_PART = Constants.DAILY_MOTION_PLAYLIST;
        DAILY_MOTION_URL_BACK_PART = "/videos?fields=id,title,thumbnail_240_url";

    }

    private OnDailyMotionSearchCompleteListener dailyListener;

    public final URL getRequestUrl(String searchFor) {

        URL url = null;

        try {

            url = new URL(getUrlString(searchFor));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;

    }

    private final String getUrlString(String searchFor) {

        return DAILY_MOTION_URL_FRONT_PART + handleSearchString(searchFor) + DAILY_MOTION_URL_BACK_PART;
    }

    private String handleSearchString(String s) {

        if (s != null && s.length() != 0) {
            return s;

        } else {

            return "comedy";
        }
    }

    public final void searchDailyMotion(String searchFor, @NonNull OnDailyMotionSearchCompleteListener listener) {

        dailyListener = listener;

        URL url = getRequestUrl(searchFor);

        SearchTask st = new SearchTask();
        st.execute(url);
    }

    private class SearchTask extends AsyncTask<URL, Void, ArrayList<YoutubeVideoItem>> {

        @Override
        protected ArrayList<YoutubeVideoItem> doInBackground(URL... urls) {

            URL url = urls[0];

            StringBuilder response = new StringBuilder();

            HttpURLConnection httpconn = null;

            try {
                httpconn = (HttpURLConnection) url.openConnection();

                if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()), 8192);
                    String strLine = null;
                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }
                    input.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayList<YoutubeVideoItem> videos = new ArrayList<>();

            try {

                JSONObject o1 = new JSONObject(response.toString());

                JSONArray a1 = o1.getJSONArray("list");
                String videoId;
                String title;

                videos.clear();

                for (int i = 0; i < a1.length(); i++) {

                    JSONObject o2 = a1.getJSONObject(i);
                    videoId = o2.getString("id");
                    title = o2.getString("title");
                    String thumbnail_URL = o2.getString("thumbnail_240_url") ;
                    YoutubeVideoItem video = new YoutubeVideoItem(videoId, thumbnail_URL, title);

                    videos.add(video);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return videos;
        }

        @Override
        protected void onPostExecute(ArrayList<YoutubeVideoItem> youtubeVideoItems) {
            super.onPostExecute(youtubeVideoItems);

            dailyListener.onDailyMotionSearchComplete(youtubeVideoItems);

        }

    }

    public interface OnDailyMotionSearchCompleteListener {
        void onDailyMotionSearchComplete(ArrayList<YoutubeVideoItem> videos);
    }

}
