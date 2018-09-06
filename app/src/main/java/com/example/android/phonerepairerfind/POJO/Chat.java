package com.example.android.phonerepairerfind.POJO;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Chat {
    private boolean mIsComment;
    private String mChatPushId;
    private String mChatImageUrl;
    private String mMessage, mMessageName, mDate, mTime;
    private Object mTimeStamp;
    private boolean mIsSent, mIsRead, mIsDelivered;
//    Map<String, Boolean> isSent = new HashMap<>();
//    Map<String, Boolean> isDelivered = new HashMap<>();
//    Map<String, Boolean> isRead = new HashMap<>();
//    //Map<String, Integer> i = new HashMap<>();

    public Chat(){

    }

    //TODO: Delete this Chat constructor below
//    public Chat(String messageName, String message, String timeStamp, String date){
//        mMessageName = messageName;
//        mMessage = message;
//        //mTimeStamp = timeStamp;
//        mDate = date;
////        mIsSent = isSent;
////        mIsRead = isRead;
////        mIsDelivered = isDelivered;
//    }


    public Chat(String messageName, String message, String chatImageUrl, Object timeStamp,
                String chatPushId){
        mChatImageUrl = chatImageUrl;
        mMessageName = messageName;
        mMessage = message;
        mTimeStamp = timeStamp;
        mChatPushId = chatPushId;
//        mTimeStamp = timeStamp;
//        mDate = date;
//        mIsSent = isSent;
//        mIsRead = isRead;
//        mIsDelivered = isDelivered;
    }


    public Chat(String commenterName, String comment, String comentImageUrl, Object timeStamp,
                String comentPushId, boolean isComment){
        mChatImageUrl = comentImageUrl;
        mMessageName = commenterName;
        mMessage = comment;
        mTimeStamp = timeStamp;
        mChatPushId = comentPushId;
        mIsComment = isComment;
    }

    public Chat(String messageName, String message, String timeStamp, String date, boolean isSent,
                boolean isRead, boolean isDelivered){
        mMessageName = messageName;
        mMessage = message;
        //mTimeStamp = timeStamp;
        mDate = date;
        mIsSent = isSent;
        mIsRead = isRead;
        mIsDelivered = isDelivered;
    }
//
//    @Exclude
//    public Map<String, Object> toMap(){
//        HashMap<String, Object> chatHasMap = new HashMap<>();
//        chatHasMap.put("mMessageName", mMessageName);
//        chatHasMap.put("mMessage", mMessage);
//        chatHasMap.put("mTimeStamp", mTimeStamp);
//        chatHasMap.put("mDate", mDate);
//        chatHasMap.put("mIsSent", mIsSent);
//        chatHasMap.put("mIsRead", mIsRead);
//        chatHasMap.put("mIsDelivered", mIsDelivered);
//        return chatHasMap;
//    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getmMessageName() {
        return mMessageName;
    }

    public void setmMessageName(String mMessageName) {
        this.mMessageName = mMessageName;
    }

    public Object getmTimeStamp() {
        return mTimeStamp;
    }

    public void setmTimeStamp(Object mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public boolean ismIsSent() {
        return mIsSent;
    }

    public void setmIsSent(boolean mIsSent) {
        this.mIsSent = mIsSent;
    }

    public boolean ismIsRead() {
        return mIsRead;
    }

    public void setmIsRead(boolean mIsRead) {
        this.mIsRead = mIsRead;
    }

    public boolean ismIsDelivered() {
        return mIsDelivered;
    }

    public void setmIsDelivered(boolean mIsDelivered) {
        this.mIsDelivered = mIsDelivered;
    }

    public String getmChatImageUrl() {
        return mChatImageUrl;
    }

    public void setmChatImageUrl(String mChatImageUrl) {
        this.mChatImageUrl = mChatImageUrl;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmChatPushId() {
        return mChatPushId;
    }

    public void setmChatPushId(String mChatPushId) {
        this.mChatPushId = mChatPushId;
    }

    public boolean ismIsComment() {
        return mIsComment;
    }

    public void setmIsComment(boolean mIsComment) {
        this.mIsComment = mIsComment;
    }
}
