package com.example.android.phonerepairerfind.POJO;

import java.io.Serializable;
import java.util.Map;

public class RepairersNearYou implements Serializable{

    private String mPushId;
    private String mName;
    private String mLocation, mEmail, mWebsite, mPhoneNum, mBusinessPhoneNum;
    private Map<String, Object> mAvailability;
    private String mMins;

    private String mImageUrl;
    private float mRating;
    private boolean mIsLiked;
    private long mLikesCount, mCommentsCount;
    private Map<String, Boolean> likes;

    public RepairersNearYou(){

    }

    public RepairersNearYou(String name, String locationName, Map<String, Object> availability, String mins, String imageUrl,
                            float rating, boolean isLiked, long likesCount, long commentCount, Map<String, Boolean> likes, String pushId,
                            String email, String website, String phoneNum, String businessPhoneNum){
        mName = name;
        mLocation = locationName;
        mAvailability = availability;
        mMins = mins;
        mImageUrl = imageUrl;
        mRating = rating;
        mIsLiked = isLiked;
        mLikesCount = likesCount;
        this.likes = likes;
        mCommentsCount = commentCount;
        mPushId = pushId;
        mEmail = email;
        mWebsite = website;
        mPhoneNum = phoneNum;
        mBusinessPhoneNum = businessPhoneNum;
    }


    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }


    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public Map<String, Object> getmAvailability() {
        return mAvailability;
    }

    public void setmAvailability(Map<String, Object> mAvailability) {
        this.mAvailability = mAvailability;
    }

    public String getmMins() {
        return mMins;
    }

    public void setmMins(String mMins) {
        this.mMins = mMins;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public float getmRating() {
        return mRating;
    }

    public void setmRating(float mRating) {
        this.mRating = mRating;
    }

    public boolean ismIsLiked() {
        return mIsLiked;
    }

    public void setmIsLiked(boolean mIsLiked) {
        this.mIsLiked = mIsLiked;
    }

    public long getmLikesCount() {
        return mLikesCount;
    }

    public void setmLikesCount(long mLikesCount) {
        this.mLikesCount = mLikesCount;
    }

    public long getmCommentsCount() {
        return mCommentsCount;
    }

    public void setmCommentsCount(long mCommentsCount) {
        this.mCommentsCount = mCommentsCount;
    }

    public Map<String, Boolean> getLikes() {
        return likes;
    }

    public void setLikes(Map<String, Boolean> likes) {
        this.likes = likes;
    }

    public String getmPushId() {
        return mPushId;
    }

    public void setmPushId(String mPushId) {
        this.mPushId = mPushId;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmWebsite() {
        return mWebsite;
    }

    public void setmWebsite(String mWebsite) {
        this.mWebsite = mWebsite;
    }

    public String getmPhoneNum() {
        return mPhoneNum;
    }

    public void setmPhoneNum(String mPhoneNum) {
        this.mPhoneNum = mPhoneNum;
    }

    public String getmBusinessPhoneNum() {
        return mBusinessPhoneNum;
    }

    public void setmBusinessPhoneNum(String mBusinessPhoneNum) {
        this.mBusinessPhoneNum = mBusinessPhoneNum;
    }
}
