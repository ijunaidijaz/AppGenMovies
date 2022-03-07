package com.imtyaz.quranurdutarjuma.activities;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.dailymotion.android.player.sdk.PlayerWebView;
import com.imtyaz.quranurdutarjuma.R;

public class DailyMotionVideoPlayer extends AppCompatActivity {

    PlayerWebView playerWebView;
    String videoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_motion_video_player);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        playerWebView = findViewById(R.id.dm_player_web_view);
        Bundle bundle = getIntent().getExtras();
        videoId = bundle.getString("VIDEO_URL");
        playerWebView.load(videoId);
    }


}