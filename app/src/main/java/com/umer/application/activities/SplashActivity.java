package com.umer.application.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.umer.application.R;
import com.umer.application.app.MainApp;
import com.umer.application.models.AppSlider;
import com.umer.application.models.ApplicationSettings;
import com.umer.application.models.BaseResponse;
import com.umer.application.networks.Network;
import com.umer.application.networks.NetworkCall;
import com.umer.application.networks.OnNetworkResponse;
import com.umer.application.utils.RequestCodes;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity implements OnNetworkResponse {

    public static final String MyPREFERENCES = "MyPrefs";
    ImageView backgroundImage;
    LinearLayout internetError;
    boolean isConnected = false;
    ApplicationSettings applicationSettings;
    ArrayList<AppSlider> appSlider;
    SharedPreferences sharedpreferences;
    Button tryAgain;

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) MainApp.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//        boolean success = false;
//        new Thread(() -> {
//            //Do whatever
//            try {
//                URL url = new URL("https://google.com");
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setConnectTimeout(10000);
//                connection.connect();
//                success = connection.getResponseCode() == 200;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        return success;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
//        backgroundImage = findViewById(R.id.background_image);
        internetError = findViewById(R.id.internet_Error);
        tryAgain = findViewById(R.id.tryAgain);
        if (!isNetworkAvailable()) {
            onButtonShowPopupWindowClick();
        }
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        isInternetConnected();
        if (!isConnected) {
            internetError.setVisibility(View.VISIBLE);
        }
        getApplicationSettings();
        getApplicationSlider();

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(SplashActivity.this, SplashActivity.class);
                startActivity(t);
                finish();
            }
        });
    }

    private void getApplicationSettings() {
        NetworkCall.make()
                .setCallback(this)
                .setTag(RequestCodes.API.GET_APPLICATION_SETTINGS)
                .enque(new Network().apis().getApplicationSettings(getResources().getString(R.string.PACKAGE_NAME)))
                .execute();

    }

    private void getApplicationSlider() {
        NetworkCall.make()
                .setCallback(this)
                .setTag(RequestCodes.API.GET_APPLICATION_SLIDER)
                .enque(new Network().apis().getApplicationSlider(getResources().getString(R.string.PACKAGE_NAME)))
                .execute();

    }

    @Override
    public void onSuccess(Call call, Response response, Object tag) {
        switch ((int) tag) {

            case RequestCodes.API.GET_APPLICATION_SLIDER:
                appSlider = new ArrayList<>();
                appSlider = (ArrayList<AppSlider>) response.body();

                break;

            case RequestCodes.API.GET_APPLICATION_SETTINGS:
                applicationSettings = new ApplicationSettings();
                applicationSettings = (ApplicationSettings) response.body();

                if (applicationSettings != null) {
                    applicationSettings.saveApplicationSettings(getApplicationContext(), applicationSettings);
                    new Handler().postDelayed(() -> {
                        Intent splashIntent = new Intent(SplashActivity.this, GridViewActivity.class);
                        if (!appSlider.get(0).getUrl().isEmpty() && applicationSettings != null) {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            splashIntent.putExtra("applicationSettings", applicationSettings);
                            splashIntent.putExtra("ApplicationSlider", appSlider);
                            applicationSettings.saveSlider(appSlider);
                            editor.putString("App_Header_color", applicationSettings.getActionBarColor());
                            editor.putString("Logo", applicationSettings.getLog());
                            editor.apply();
                            startActivity(splashIntent);
                            finish();
                        }
                    }, 2000);
                }
                break;


            default:
                break;
        }
    }

    @Override
    public void onFailure(Call call, BaseResponse response, Object tag) {
        switch ((int) tag) {
            case RequestCodes.API.GET_APPLICATION_SETTINGS:
                Log.d("Response Failure", "Error=" + response.message);
                break;
            case RequestCodes.API.GET_APPLICATION_SLIDER:
                Log.d("Slider Failure", "Error=" + response.message);
                break;
        }
    }

    public boolean isInternetConnected() {

        ConnectivityManager mgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mgr.getActiveNetworkInfo();

        if (netInfo != null) {
            if (netInfo.isConnected()) {
                // Internet Available
                isConnected = true;
            } else {
                //No internet
                isConnected = false;
            }
        } else {
            //No internet
            isConnected = false;
        }
        return isConnected;
    }

    public void onButtonShowPopupWindowClick() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.exit_popup_window);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        dialog.setCancelable(false);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        TextView appName = dialog.findViewById(R.id.title);
        Button exit = dialog.findViewById(R.id.exit_btn);
        Button cancel = dialog.findViewById(R.id.cancel_btn);
        ImageView ratingImage = dialog.findViewById(R.id.rateAppIcon);
        TextView ratingText = dialog.findViewById(R.id.rateAppText);
        View line = dialog.findViewById(R.id.line);
        appName.setText("No Internet!");
        cancel.setVisibility(View.GONE);
        exit.setText("Ok");
        exit.setOnClickListener(v1 -> {
            finish();
        });
    }


}
