package com.example.android.phonerepairerfind.anim;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.android.phonerepairerfind.R;

public class CustomProgressBar {
    private Animation loadingProgressBarAnim;
    private ImageView loadingProgressBar;

    public CustomProgressBar(ImageView view, Activity context){
        loadingProgressBar = view;
        view.setVisibility(View.GONE);
        loadingProgressBarAnim = AnimationUtils.loadAnimation(context, R.anim.anim_custom_loading_progress);
        loadingProgressBarAnim.setRepeatMode(Animation.RESTART);
        loadingProgressBarAnim.setRepeatCount(Animation.INFINITE);
    }

    public void startLoading(){
        loadingProgressBar.setVisibility(View.VISIBLE);
        loadingProgressBar.startAnimation(loadingProgressBarAnim);
    }

    public void stopLoading(){
        loadingProgressBarAnim.cancel();
        loadingProgressBar.clearAnimation();
        loadingProgressBar.setVisibility(View.GONE);
    }
}
