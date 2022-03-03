package com.livematch.livesportstv.utils;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.livematch.livesportstv.models.ApplicationSettings;
import com.livematch.livesportstv.models.YoutubeVideoItem;

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

public class DailyMotionSearchHelper {
    private static String DAILY_MOTION_URL_FRONT_PART;
    private static String DAILY_MOTION_URL_BACK_PART;
    Context mContext;
    ApplicationSettings applicationSettings;
    private OnDailyMotionSearchCompleteListener dailyListener;

    public DailyMotionSearchHelper(Context mContext) {
        this.mContext = mContext;
        applicationSettings = new ApplicationSettings().retrieveApplicationSettings(mContext);
        DAILY_MOTION_URL_FRONT_PART = Constants.DAILY_MOTION_PLAYLIST;
        DAILY_MOTION_URL_BACK_PART = "/videos?fields=id,title,thumbnail_240_url";

    }

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

    public interface OnDailyMotionSearchCompleteListener {
        void onDailyMotionSearchComplete(ArrayList<YoutubeVideoItem> videos);
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
                    String thumbnail_URL = o2.getString("thumbnail_240_url");
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

}
