package com.example.android.phonerepairerfind.POJO;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;
@IgnoreExtraProperties
public class UserProfile {

    private String mLatLng;
    private String mImageUrl;
    private String mBusinnessPhonenum;
    private String mName;
    private Map<String, Boolean> account_type;
    private long mLikesCount, mCommentCount;
    private float mRating;
    private String mAddress;
    private String mPhoneNum;
    private String mEmail;
    private String mUsername;
    private String mPushId;
    private String mWebsiteUrl;
    private Map<String, Boolean> likes;
    //private Map<String, String> mMonday, mTuesday, mWednessday, mThursday, mFriday, mSaturday, mSunday;
    private Map<String, Object> mAvailability;

//    public UserProfile(String name, String username, String email, String phoneNum, String address, String pushId,
//                       Map<String, Boolean> account_type, Map<String, Object> availability){
//        mName = name;
//        mUsername = username;
//        mEmail = email;
//        mPhoneNum = phoneNum;
//        mAddress = address;
//        mPushId = pushId;
//        this.account_type = account_type;
//        mAvailability = availability;
//    }

    /**
     * Used for phoneShops or PhoneRepairers registration
     * @param username username of person
     * @param email email of person
     * @param phoneNum phonenum of person
     * @param address
     * @param pushId
     * @param availability
     * @param rating
     * @param likesCount
     * @param likes
     */
    public UserProfile(String imageUrl, String businessName, String username, String email,
                       String phoneNum, String businessPhonenum, String address, String pushId,
                       Map<String, Object> availability, float rating, long likesCount,
                       long commentCount, Map<String, Boolean> account_type, Map<String, Boolean> likes,
                       String latLng){
        mImageUrl = imageUrl;
        mName = businessName;
        mUsername = username;
        mEmail = email;
        mPhoneNum = phoneNum;
        mBusinnessPhonenum = businessPhonenum;
        mAddress = address;
        mLatLng = latLng;
        mPushId = pushId;
        mAvailability = availability;
        mRating = rating;
        mLikesCount = likesCount;
        mCommentCount = commentCount;
        this.account_type = account_type;
        this.likes = likes;
        if(likes == null){
            this.likes = new HashMap<String, Boolean>();
        }
    }

    public UserProfile(){

    }

//    @Exclude
//    public Map<String, Object> toMap(){
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("Name", mName);
//        result.put("Username", mUsername);
//        result.put("Email", mEmail);
//        result.put("PhoneNum", mPhoneNum);
//        result.put("BusinessPhoneNum", mBusinnessPhonenum);
//        result.put("Location", mAddress);
//        result.put("PushId", mPushId);
//        result.put("Availability", mAvailability);
//        result.put("Rating", mRating);
//        result.put("LikesCount", mLikesCount);
//        result.put("CommentsCount", mCommentCount);
//        result.put("AccountType", account_type);
//        result.put("Likes", likes);
//        return result;
//    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmPhoneNum() {
        return mPhoneNum;
    }

    public void setmPhoneNum(String mPhoneNum) {
        this.mPhoneNum = mPhoneNum;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmPushId() {
        return mPushId;
    }

    public void setmPushId(String mPushId) {
        this.mPushId = mPushId;
    }

    public long getmLikesCount() {
        return mLikesCount;
    }

    public void setmLikesCount(long mLikesCount) {
        this.mLikesCount = mLikesCount;
    }

    public float getmRating() {
        return mRating;
    }

    public void setmRating(float mRating) {
        this.mRating = mRating;
    }

    public  Map<String, Object> getmAvailability() {
        return mAvailability;
    }

    public void setmAvailability(Map<String, Object> mAvailability) {
        this.mAvailability = mAvailability;
    }

    public Map<String, Boolean> getAccountType() {
        return account_type;
    }

    public void setAccountType(Map<String, Boolean> account_type) {
        this.account_type = account_type;
    }

    public String getmBusinnessPhonenum() {
        return mBusinnessPhonenum;
    }

    public void setmBusinnessPhonenum(String mBusinnessPhonenum) {
        this.mBusinnessPhonenum = mBusinnessPhonenum;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public long getmCommentCount() {
        return mCommentCount;
    }

    public void setmCommentCount(long mCommmentCount) {
        this.mCommentCount = mCommmentCount;
    }

    public Map<String, Boolean> getLikes() {
//        if(likes == null){
//            this.likes = new HashMap<String, Boolean>();
//        }
        return likes;
    }

    public void setLikes(Map<String, Boolean> likes) {
        this.likes = likes;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getmWebsiteUrl() {
        return mWebsiteUrl;
    }

    public void setmWebsiteUrl(String mWebsiteUrl) {
        this.mWebsiteUrl = mWebsiteUrl;
    }
}
