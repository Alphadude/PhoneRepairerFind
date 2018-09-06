package com.example.android.phonerepairerfind.Adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.phonerepairerfind.R;

import java.util.ArrayList;

public class SlidingImageAdapter extends PagerAdapter {

    private ArrayList<Integer> mImages;
    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<String> mTexts;

    public SlidingImageAdapter(Context context, ArrayList<Integer> images, ArrayList<String> texts) {
        mImages = images;
        mContext = context;
        mTexts = texts;
        mInflater = LayoutInflater.from(context);
    }

    public SlidingImageAdapter(Context context, ArrayList<Integer> images) {
        mImages = images;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View slidingLayout = mInflater.inflate(R.layout.sliding_images_layout, container, false);
        if(slidingLayout != null){
            final ImageView image = (ImageView) slidingLayout.findViewById(R.id.tips_slider_image);
            final TextView text = (TextView) slidingLayout.findViewById(R.id.tips_slider_text);

            image.setImageResource(mImages.get(position));
            if(!(null == mTexts)) {
                text.setText(mTexts.get(position));
            }
            container.addView(slidingLayout);
            return slidingLayout;
        }
        return new Object();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public void restoreState(@Nullable Parcelable state, @Nullable ClassLoader loader) {
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Nullable
    @Override
    public Parcelable saveState() {
        return null;
    }
}
