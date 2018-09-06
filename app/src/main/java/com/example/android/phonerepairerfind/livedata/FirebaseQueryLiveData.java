package com.example.android.phonerepairerfind.livedata;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.phonerepairerfind.Constants.FirebaseConstants;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirebaseQueryLiveData extends LiveData<DataSnapshot> {
    private static final String LOG_TAG = "FirebaseQueryLiveData";

    private int mUseListener;
    private final Query query;
    private final ValueEventListener eventListener = new FirebaseEventListener();
    private final ChildEventListener childEventListener = new MyChildEventListener();

    public FirebaseQueryLiveData(Query query) {
        this.query = query;
    }

    public FirebaseQueryLiveData(DatabaseReference dataReference, int useListener) {
//        if(dataReference == null)
//            return;
        this.query = dataReference;
        mUseListener = useListener;
        Log.e("FirebaseQueryLiveData", "DatabaseRefernce " + dataReference.toString());
    }

    @Override
    protected void onActive() {
        super.onActive();
        Log.e(LOG_TAG, "onActive");
        if(mUseListener == FirebaseConstants.USE_CHILD_LISTENER)
            query.addChildEventListener(childEventListener);
        if(mUseListener == FirebaseConstants.USE_VALUE_LISTENER)
            query.addValueEventListener(eventListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        Log.e(LOG_TAG, "onActive");
        if(mUseListener == FirebaseConstants.USE_CHILD_LISTENER)
            query.removeEventListener(childEventListener);
        if(mUseListener == FirebaseConstants.USE_VALUE_LISTENER)
            query.removeEventListener(eventListener);
    }

    private class FirebaseEventListener implements ValueEventListener{
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(dataSnapshot);
            Log.e(LOG_TAG, "__________________Value set________________");
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(LOG_TAG, "Can't listen to query " + query, databaseError.toException());
        }

    }


    private class MyChildEventListener implements ChildEventListener{
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            //dataSnapshot.getKey();
            setValue(dataSnapshot);
//            FirebaseInitUser.getChatDataRef().push()
//            Log.e(LOG_TAG, "onChildAdded(): previous child name = " + s);
////            String key = dataSnapshot.getKey();
////            for(DataSnapshot snap : dataSnapshot.getChildren()){
////                Message message = snap.getValue(Message.class);
//                messageList.add(message);
//            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

}

