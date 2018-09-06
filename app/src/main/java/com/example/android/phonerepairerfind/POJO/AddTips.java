package com.example.android.phonerepairerfind.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AddTips {
    private String mText;
    private String mTitle, mHeaderImageUrl, mAddTipsPushId, mUploaderPushId;
    private Object mTimeStamp;
    private ArrayList<AddPhotoText> mImageTextList;

    //TODO: Use StringBuilder when appending texts to the view's text arrayList or whatever you create.. in fact i'ts ur business
    public AddTips(){

    }

    public AddTips(String title, String text, String headerImageUrl, ArrayList<AddPhotoText> imageTextList,
                   String addTipsPushId, String uploaderPushId, Object timeStamp){
        mTitle = title;
        mText = text;
        mHeaderImageUrl = headerImageUrl;
        mAddTipsPushId = addTipsPushId;
        mTimeStamp = timeStamp;
        mImageTextList = imageTextList;
        mUploaderPushId = uploaderPushId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmHeaderImageUrl() {
        return mHeaderImageUrl;
    }

    public void setmHeaderImageUrl(String mHeaderImageUrl) {
        this.mHeaderImageUrl = mHeaderImageUrl;
    }

    public String getmAddTipsPushId() {
        return mAddTipsPushId;
    }

    public void setmAddTipsPushId(String mAddTipsPushId) {
        this.mAddTipsPushId = mAddTipsPushId;
    }

    public Object getmTimeStamp() {
        return mTimeStamp;
    }

    public void setmTimeStamp(Object mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }

    public ArrayList<AddPhotoText> getmImageTextList() {
        return mImageTextList;
    }

    public void setmImageTextList(ArrayList<AddPhotoText> mImageTextList) {
        this.mImageTextList = mImageTextList;
    }

    public String getmUploaderPushId() {
        return mUploaderPushId;
    }

    public void setmUploaderPushId(String mUploaderPushId) {
        this.mUploaderPushId = mUploaderPushId;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public static class AddPhotoText implements Parcelable{
        private String mImageUrl, mText;
        //private Uri mImageUriPath;

        private static Parcel mParcel;
        private int mI;
        public AddPhotoText(String imageUrl, String text){
            mImageUrl = imageUrl;
            mText = text;
        }

//        public AddPhotoText(Uri imageUriPath, String text){
//            mImageUriPath = imageUriPath;
//            mText = text;
//        }

        public static final Creator<AddPhotoText> CREATOR = new Creator<AddPhotoText>() {
            @Override
            public AddPhotoText createFromParcel(Parcel in) {
                mParcel = in;
                return new AddPhotoText(in);
            }

            @Override
            public AddPhotoText[] newArray(int size) {
                return new AddPhotoText[size];
            }
        };

        public String getmImageUrl() {
            return mImageUrl;
        }

        public void setmImageUrl(String mImageUrl) {
            this.mImageUrl = mImageUrl;
        }

        public String getmText() {
            return mText;
        }

        public void setmText(String mText) {
            this.mText = mText;
        }

//        public Uri getmImageUriPath() {
//            return mImageUriPath;
//        }
//
//        public void setmImageUriPath(Uri mImageUriPath) {
//            this.mImageUriPath = mImageUriPath;
//        }

        private AddPhotoText(Parcel in) {
            mText = in.readString();
            mImageUrl = in.readString();
            //mImageUriPath = in.
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            mI = i;
            parcel.writeString(mText);
            parcel.writeString(mImageUrl);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static Parcel getmParcel() {
            return mParcel;
        }

        public int getmI() {
            return mI;
        }
    }
}
