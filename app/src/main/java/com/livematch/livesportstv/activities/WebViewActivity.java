package com.livematch.livesportstv.activities;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.livematch.livesportstv.R;
import com.livematch.livesportstv.models.ApplicationSettings;
import com.livematch.livesportstv.utils.Constants;
import com.livematch.livesportstv.utils.ViewDialog;
import com.livematch.livesportstv.utils.functions;

public class WebViewActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    public ViewDialog viewDialog;
    WebView browser;
    String URL, Log, barColor;
    ApplicationSettings applicationSettings;
    ImageView search_btn, imageView_searchBar, imageView;
    RelativeLayout actionBar;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        autoLoading(getSupportFragmentManager());
        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        Log = prefs.getString("Logo", "");
        barColor = prefs.getString("App_Header_color", "");
        browser = (WebView) findViewById(R.id.webView);
        imageView_searchBar = findViewById(R.id.image_view);
        search_btn = findViewById(R.id.search_btn);
        search_btn.setVisibility(View.GONE);
        actionBar = findViewById(R.id.actionBar);
        actionBar.setBackgroundColor(Color.parseColor(barColor));
        functions.GlideImageLoaderWithPlaceholder(this, imageView_searchBar, Constants.BASE_URL_IMAGES + Log);
        imageView = findViewById(R.id.imageView);


        browser.getSettings().setJavaScriptEnabled(true);

        WebSettings webSettings = browser.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        browser.setWebChromeClient(new WebChromeClient());
        browser.setWebViewClient(new WebViewController());
        URL = getIntent().getExtras().getString("WEBURL");
        browser.loadUrl(URL);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cancelLoading();
            }
        }, 3000);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.browser.canGoBack()) {
            this.browser.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void cancelLoading() {
        if (viewDialog != null && viewDialog.isVisible() && viewDialog.isResumed()) {
            viewDialog.dismiss();
        }
    }

    public void autoLoading(FragmentManager manager) {
        this.viewDialog = new ViewDialog();
        if (viewDialog.isVisible() && viewDialog.isResumed()) {
            viewDialog.dismiss();
        } else {
            viewDialog.show(manager, Constants.tagViewDialog);
        }
//        return this;
    }

    public class WebViewController extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return true;
        }
    }

}