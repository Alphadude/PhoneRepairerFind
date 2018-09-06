package com.example.android.phonerepairerfind.model;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.android.phonerepairerfind.Constants.FirebaseConstants;
import com.example.android.phonerepairerfind.utilities.DatabaseHelper;
import com.example.android.phonerepairerfind.POJO.Chat;
import com.example.android.phonerepairerfind.livedata.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {
    //DatabaseReference chatDatabaseRef = FirebaseInitUser.getChatDataRef();

    public LiveData<List<Chat>> getChatListLiveData(DatabaseReference chatDatabaseRef){
        FirebaseQueryLiveData firebaseQueryLiveData = new FirebaseQueryLiveData(
                chatDatabaseRef.orderByChild("mTimeStamp").getRef(),
                FirebaseConstants.USE_VALUE_LISTENER);

        LiveData<List<Chat>> chatListLiveData = Transformations.map(firebaseQueryLiveData, new Deserializer());
        return chatListLiveData;
    }

    private class Deserializer implements Function<DataSnapshot, List<Chat>>{
        @Override
        public List<Chat> apply(DataSnapshot input) {
            ArrayList<Chat> mChatList = new ArrayList<Chat>();
            for(DataSnapshot dataSnapshot : input.getChildren()){
                Chat chat = dataSnapshot.getValue(Chat.class);
                if(chat != null) {
                    if (chat.getmTimeStamp() != null) {
                        String date = DatabaseHelper.getDateFromTimeStamp((Long) chat.getmTimeStamp());
                        chat.setmDate(date);
                        String time = DatabaseHelper.getTimeFromTimeStamp((Long) chat.getmTimeStamp());
                        chat.setmTime(time);
                    }
                    //mContactList.add(contact);
                }
                mChatList.add(chat);
            }
            return mChatList;
        }
    }
}
