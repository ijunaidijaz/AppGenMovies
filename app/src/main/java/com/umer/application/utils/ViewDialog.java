package com.umer.application.utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.umer.application.R;

import java.util.Objects;

public class ViewDialog extends DialogFragment {
    public static ViewDialog instance;

    public static ViewDialog getInstance() {
        if (instance == null) {
            instance = new ViewDialog();
        }
        return instance;
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams;
        if (window != null) {
            windowParams = window.getAttributes();
            windowParams.dimAmount = 0.50f;
            windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(windowParams);
        }
    }

    //    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onCreate(savedInstanceState);
//        int theme=android.R.style.Theme_Translucent;
//        setStyle(DialogFragment.STYLE_NO_TITLE, theme);
//    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.custom_dialog, container);
        instance = this;

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        int time = 0;
//        if () {
//            time = 5000;
//        } else {
//            time = 20000;
//        }
        new CountDownTimer(time, 1000) {

            public void onTick(long millisUntilFinished) {
//                Log.e("Timer Dialog Fragment", "seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Log.e("Finish", "Timer Dialog isResumed=" + isResumed());
                if (ViewDialog.getInstance().isVisible() && ViewDialog.getInstance().isResumed()) {
                    ViewDialog.getInstance().dismiss();
                }
            }
        }.start();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
        getDialog().setCancelable(false);
    }

}
