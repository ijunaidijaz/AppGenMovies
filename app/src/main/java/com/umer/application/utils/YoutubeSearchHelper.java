package com.umer.application.utils;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;

import com.umer.application.R;
import com.umer.application.models.ApplicationSettings;
import com.umer.application.models.YoutubeVideoItem;

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

public class YoutubeSearchHelper  {

    Context mContext ;
    ApplicationSettings applicationSettings ;
    private static int NUM_OF_VIDEOS_IN_ONE_REQUEST ;
    public static String API_KEY ;
    private static String URL_FRONT_PART ;
    private static  String URL_BACK_PART ;


    public YoutubeSearchHelper(Context context) {
        mContext = context ;
        applicationSettings = new ApplicationSettings().retrieveApplicationSettings(mContext);
        NUM_OF_VIDEOS_IN_ONE_REQUEST = 5 ;
        API_KEY =applicationSettings.getYouTubeApiKey();
        URL_FRONT_PART = Constants.URL_FRONT_PART;
        URL_BACK_PART = "&type=video&maxResults=" + NUM_OF_VIDEOS_IN_ONE_REQUEST + "&order=date&key=" + API_KEY;

    }

    private OnSearchCompleteListener mListener;

    public final URL getRequestUrl(String searchFor , boolean isPlayList) {

        URL url = null;

        try {

            url = new URL(getUrlString(searchFor , isPlayList));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;

    }


    private final String getUrlString(String searchFor , boolean isPlayList ) {
        if (isPlayList){
            return Constants.PLAYLIST_URL_FRONT_PART + "&playlistId=" + searchFor +"&key="+ API_KEY ;
        }else {
            return URL_FRONT_PART + handleSearchString(searchFor) + URL_BACK_PART;

        }

    }


    private String handleSearchString(String s) {

        if (s != null && s.length() != 0) {
            return s;

        } else {

            return "comedy";
        }
    }

    public final void searchYoutube(String searchFor,boolean isPlaylist ,  @NonNull OnSearchCompleteListener listener) {

        mListener = listener;

        URL url = getRequestUrl(searchFor , isPlaylist);

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

                JSONArray a1 = o1.getJSONArray("items");
                String videoId ;

                videos.clear();

                for (int i = 0; i < a1.length(); i++) {

                    JSONObject o2 = a1.getJSONObject(i);

                    if (o2.get("id") instanceof String){

                        videoId = o2.getJSONObject("snippet").getJSONObject("resourceId").getString("videoId");
                    }else{
                        videoId = o2.getJSONObject("id").getString("videoId");

                    }


                    String title = o2.getJSONObject("snippet").getString("title");

                    String thumbnailUrl = o2.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("medium").getString("url");

                    YoutubeVideoItem video = new YoutubeVideoItem(videoId ,thumbnailUrl , title);

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

            mListener.onSearchComplete(youtubeVideoItems);

        }

    }

    public interface OnSearchCompleteListener {
        void onSearchComplete(ArrayList<YoutubeVideoItem> videos);
    }

}
