package com.imtyaz.quranurdutarjuma.activities;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.imtyaz.quranurdutarjuma.R;
import com.imtyaz.quranurdutarjuma.app.MainApp;
import com.imtyaz.quranurdutarjuma.models.AppSlider;
import com.imtyaz.quranurdutarjuma.models.ApplicationSettings;
import com.imtyaz.quranurdutarjuma.models.BaseResponse;
import com.imtyaz.quranurdutarjuma.networks.Network;
import com.imtyaz.quranurdutarjuma.networks.NetworkCall;
import com.imtyaz.quranurdutarjuma.networks.OnNetworkResponse;
import com.imtyaz.quranurdutarjuma.utils.RequestCodes;

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
        tryAgain = findViewById(R.id.exit);
        if (!isNetworkAvailable()) {
            onButtonShowPopupWindowClick();
        }
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        isInternetConnected();
        if (!isConnected) {
            internetError.setVisibility(View.VISIBLE);
        }
        getApplicationSlider();
        getApplicationSettings();


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
//                    applicationSettings.setAdds(0);
//                    applicationSettings.setAdMobLimit("2");
                    applicationSettings.saveApplicationSettings(getApplicationContext(), applicationSettings);
                    if (!applicationSettings.getIsActive()) {
                        notActiveDialog();
                    } else {
                        new Handler().postDelayed(() -> {
                            Intent splashIntent = new Intent(SplashActivity.this, MainActivity.class);
                            if (appSlider != null) {
                                if (!appSlider.get(0).getUrl().isEmpty() && applicationSettings != null) {
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    splashIntent.putExtra("applicationSettings", applicationSettings);
                                    splashIntent.putExtra("ApplicationSlider", appSlider);
                                    applicationSettings.saveSlider(appSlider);
                                    editor.putString("App_Header_color", applicationSettings.getActionBarColor());
                                    editor.putString("Logo", applicationSettings.getLog());
                                    editor.apply();
                                    setIDs();
                                    startActivity(splashIntent);
                                    finish();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "No Slider found", Toast.LENGTH_SHORT).show();
                            }
                        }, 4000);
                    }
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

    private void setIDs() {

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
        dialog.setContentView(R.layout.no_internet_layout);
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
        Button exit = dialog.findViewById(R.id.exit);
        exit.setOnClickListener(v1 -> {
            finish();
        });
    }

    public void notActiveDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.not_active_dialog);
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
        Button exit = dialog.findViewById(R.id.exit);
        exit.setOnClickListener(v1 -> {
            finish();
        });
    }


}
