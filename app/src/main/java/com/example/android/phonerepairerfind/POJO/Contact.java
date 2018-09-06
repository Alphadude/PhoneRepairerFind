package com.example.android.phonerepairerfind.POJO;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Contact {

    private String mUsername, mLastMsg, mLastMsgName, mLastMsgDate, mPushId, mLastMsgPushId;
    private Object  mTimeStamp;
    private String mProfilePicsUrl;
    private int mNotReadMessageCount;
    private boolean mIsLastMsgRead, mIsLastMsgDelivered, mIsLastMsgSent;

    public Contact(){

    }

    public Contact(String contactUsername, String lastMsg, String lastMsgName, Object timeStamp, String profilePicsUrl,
                   String pushId, int notReadMessageCount, boolean isLastMsgRead, String lastMsgPushId,
                   boolean isLastMsgDelivered, boolean isLastMsgSent){

        mUsername = contactUsername;
        mLastMsg = lastMsg;
        mLastMsgName = lastMsgName;
        mTimeStamp = timeStamp;
        mProfilePicsUrl = profilePicsUrl;
        mPushId = pushId;
        mNotReadMessageCount = notReadMessageCount;
        mIsLastMsgRead = isLastMsgRead;
        mIsLastMsgDelivered = isLastMsgDelivered;
        mIsLastMsgSent = isLastMsgSent;
        mLastMsgPushId = lastMsgPushId;
    }
    //TODO: This Contact constructor below should be deleted

//    public Contact(String username, String lastMsg, String lastMsgName, String timeStamp, String profilePics,
//                   String lastMsgDate, String pushId, int notReadMessageCount, boolean isLastMsgRead,
//                   boolean isLastMsgDelivered, boolean isLastMsgSent){
//
//        mUsername = username;
//        mLastMsg = lastMsg;
//        mLastMsgName = lastMsgName;
//        mTimeStamp = timeStamp;
//        mProfilePicsUrl = profilePics;
//        mLastMsgDate = lastMsgDate;
//        mPushId = pushId;
//        mNotReadMessageCount = notReadMessageCount;
//        mIsLastMsgRead = isLastMsgRead;
//        mIsLastMsgDelivered = isLastMsgDelivered;
//        mIsLastMsgSent = isLastMsgSent;
//    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmLastMsg() {
        return mLastMsg;
    }

    public void setmLastMsg(String mLastMsg) {
        this.mLastMsg = mLastMsg;
    }

    public String getmLastMsgName() {
        return mLastMsgName;
    }

    public void setmLastMsgName(String mLastMsgName) {
        this.mLastMsgName = mLastMsgName;
    }

    public Object getmTimeStamp() {
        return mTimeStamp;
    }

    public void setmTimeStamp(Object mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }

    @Exclude
    public String getmLastMsgDate() {
        return mLastMsgDate;
    }

    @Exclude
    public void setmLastMsgDate(String mLastMsgDate) {
        this.mLastMsgDate = mLastMsgDate;
    }

    public String getmPushId() {
        return mPushId;
    }

    public void setmPushId(String mPushId) {
        this.mPushId = mPushId;
    }

    public String getmProfilePicsUrl() {
        return mProfilePicsUrl;
    }

    public void setmProfilePicsUrl(String mProfilePicsUrl) {
        this.mProfilePicsUrl = mProfilePicsUrl;
    }

    public int getmNotReadMessageCount() {
        return mNotReadMessageCount;
    }

    public void setmNotReadMessageCount(int mNotReadMessageCount) {
        this.mNotReadMessageCount = mNotReadMessageCount;
    }

    public boolean ismIsLastMsgRead() {
        return mIsLastMsgRead;
    }

    public void setmIsLastMsgRead(boolean mIsLastMsgRead) {
        this.mIsLastMsgRead = mIsLastMsgRead;
    }

    public boolean ismIsLastMsgDelivered() {
        return mIsLastMsgDelivered;
    }

    public void setmIsLastMsgDelivered(boolean mIsLastMsgDelivered) {
        this.mIsLastMsgDelivered = mIsLastMsgDelivered;
    }

    public boolean ismIsLastMsgSent() {
        return mIsLastMsgSent;
    }

    public void setmIsLastMsgSent(boolean mIsLastMsgSent) {
        this.mIsLastMsgSent = mIsLastMsgSent;
    }

    public String getmLastMsgPushId() {
        return mLastMsgPushId;
    }

    public void setmLastMsgPushId(String mLastMsgPushId) {
        this.mLastMsgPushId = mLastMsgPushId;
    }
}
