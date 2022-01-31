package com.umer.application.activities;

import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.umer.application.R;
import com.umer.application.models.ApplicationSettings;

public class YoutubeVideoPlayer extends YouTubeBaseActivity {
    YouTubePlayerView youtubeVideoView;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    String videoId;
    ApplicationSettings applicationSettings = new ApplicationSettings();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_youtube_video_player);
        applicationSettings = applicationSettings.retrieveApplicationSettings(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        videoId = bundle.getString("VIDEO_URL");

        initViews();

        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                youTubePlayer.loadVideo(videoId);
                youTubePlayer.play();
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

//        youtubeVideoView.initialize(getResources().getString(R.string.YOUTUBE_API_KEY), onInitializedListener);
        youtubeVideoView.initialize(applicationSettings.getYouTubeApiKey(), onInitializedListener);


    }

    private void initViews() {

        youtubeVideoView = findViewById(R.id.youtubeVideoView);
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

}