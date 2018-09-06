package com.example.android.phonerepairerfind.model;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.phonerepairerfind.App;
import com.example.android.phonerepairerfind.Constants.FirebaseConstants;
import com.example.android.phonerepairerfind.utilities.DatabaseHelper;
import com.example.android.phonerepairerfind.POJO.Contact;
import com.example.android.phonerepairerfind.livedata.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class ContactViewModel extends ViewModel {
    private DatabaseReference contactDatabaseRef = App.getsContactDataRef();
    private final String TAG = ContactViewModel.class.getSimpleName();

    private  FirebaseQueryLiveData firebaseQueryLiveData = new FirebaseQueryLiveData(contactDatabaseRef
            .orderByChild("mTimeStamp").getRef(),
            FirebaseConstants.USE_VALUE_LISTENER);

    public LiveData<List<Contact>> getContactListLiveData(){

        LiveData<List<Contact>> contactListLiveData = Transformations.map(firebaseQueryLiveData, new Deserializer());
        return contactListLiveData;
    }

//    public DataSnapshot getSelected(int position){
//
//    }

    private class Deserializer implements Function<DataSnapshot, List<Contact>> {
        @Override
        public List<Contact> apply(DataSnapshot input) {
            ArrayList<Contact> mContactList = new ArrayList<Contact>();
            for(DataSnapshot dataSnapshot : input.getChildren()){
                Contact contact = dataSnapshot.getValue(Contact.class);
                if(contact != null) {
                    if (contact.getmTimeStamp() != null) {
                        String date = DatabaseHelper.getDateFromTimeStamp((Long) contact.getmTimeStamp());
                        contact.setmLastMsgDate(date);
                    }
                    mContactList.add(contact);
                }
            }
            Log.e(TAG, "ContactList: ---------" + mContactList.toString() + " -------- ");
            return mContactList;
        }
    }
}
