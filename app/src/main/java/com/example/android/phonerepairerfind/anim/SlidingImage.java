package com.example.android.phonerepairerfind.anim;

import android.os.Handler;
import android.support.v4.view.ViewPager;

import com.example.android.phonerepairerfind.Adapters.SlidingImageAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SlidingImage implements ViewPager.OnPageChangeListener{
    private ViewPager mViewPager;
    private ArrayList<Integer> mImages;
    private ArrayList<String> mTexts;
    private int currentPage = 0;
    private int numberOfPages;
    private Timer swipeTimer;

    //TODO add an argument to the SlidingImage constructor a tipPushId and also username of uploader
    //TODO: and time of upload
    public SlidingImage(ViewPager viewPager, ArrayList<Integer> images, ArrayList<String> texts){
        mViewPager = viewPager;
        mImages = images;
        mTexts = texts;
        mViewPager.setAdapter(new SlidingImageAdapter(viewPager.getContext(), images, texts));
        mViewPager.setOnPageChangeListener(this);
    }

    public SlidingImage(ViewPager viewPager, ArrayList<Integer> images){
        mViewPager = viewPager;
        mImages = images;
    }

    public void startSlidingImage(){
        numberOfPages = mImages.size();

        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if(currentPage == numberOfPages){
                    currentPage = 0;
                }
                mViewPager.setCurrentItem(currentPage++, true);
            }
        };

        swipeTimer = new Timer();

        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        },4000, 5000  );

    }

    public void stopSlidingImage(){
        swipeTimer.cancel();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        currentPage = position;
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
