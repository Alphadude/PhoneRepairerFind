package com.example.android.phonerepairerfind.model;

import android.arch.core.util.Function;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.phonerepairerfind.Adapters.RepairersNearYouAdapter;
import com.example.android.phonerepairerfind.App;
import com.example.android.phonerepairerfind.Constants.AdaptersConstant;
import com.example.android.phonerepairerfind.Constants.FirebaseConstants;
import com.example.android.phonerepairerfind.POJO.RepairersNearYou;
import com.example.android.phonerepairerfind.POJO.UserProfile;
import com.example.android.phonerepairerfind.livedata.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchUsersViewModel extends ViewModel {
    private DatabaseReference databaseReference;
    private FirebaseQueryLiveData firebaseQueryLiveData;
    private LifecycleOwner mActivity;
    private RepairersNearYouAdapter nearYouAdapter;
    private MutableLiveData<RepairersNearYou> selectedLiveData = new MutableLiveData<>();
    private  FirebaseQueryLiveData firebaseQuerySingleLiveData;

    //private FirebaseQueryLiveData firebaseQueryUserProfileLiveData;
    //private Context context;
    private List<LiveData<DataSnapshot>> liveDataList = new ArrayList<>();
    private final String TAG = SearchUsersViewModel.class.getSimpleName();

    public void setActivity(LifecycleOwner activity){
        mActivity = activity;
    }

    public void setAdapter(RepairersNearYouAdapter adapter){
        nearYouAdapter = adapter;
    }

    public void setUsersToSearchFor(int user){
        //this.context = context;
        switch (user){
            case AdaptersConstant.SEARCH_REPAIRS_USERS:
                databaseReference = App.getsRepairsDataRef();
                break;
            case AdaptersConstant.SEARCH_SHOPS_USERS:
                databaseReference = App.getsShopsDataRef();
                break;
            case AdaptersConstant.SEARCH_NORMAL_USERS:
                databaseReference = App.getsNormalDataRef();
                break;
            default:
                throw new IllegalArgumentException();
        }

        firebaseQueryLiveData = new FirebaseQueryLiveData(databaseReference,
                FirebaseConstants.USE_VALUE_LISTENER);

    }

    public void select(RepairersNearYou repairersNearYou){
        selectedLiveData.setValue(repairersNearYou);
    }

    public LiveData<RepairersNearYou> getSelected(){
        return selectedLiveData;
    }
//
    public LiveData<RepairersNearYou> getSingleRepairLivedata(DatabaseReference dbRef){
        firebaseQuerySingleLiveData = new FirebaseQueryLiveData(dbRef,
                FirebaseConstants.USE_VALUE_LISTENER);

        LiveData<RepairersNearYou> getRepair = Transformations.map(firebaseQuerySingleLiveData, new Deserializer());
        return getRepair;
    }

    public List<LiveData<DataSnapshot>> getLivedataList(){
        databaseReference
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        nearYouAdapter.clearList();
                        //nearYouAdapter.notifyDataSetChanged();
                        for(DataSnapshot snapshot :dataSnapshot.getChildren()) {
                            String userId = snapshot.getKey();
                            //Log.e("SearchUsersModel", "UserId ------------ " + userId.toString() + "--------------------");
                            if(userId != null) {
                                //getData(userId);
                                liveDataList.add(getLivedata(userId));
                                Log.e(TAG, "************ getLiveData --- " + getLivedata(userId).toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        Log.e(TAG, "**************liveDataList --- " + liveDataList.toString());
        return liveDataList;
    }

    public void getList(){
        databaseReference
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        nearYouAdapter.clearList();
                        //nearYouAdapter.notifyDataSetChanged();
                        for(DataSnapshot snapshot :dataSnapshot.getChildren()) {
                            String userId = snapshot.getKey();
                            //Log.e("SearchUsersModel", "UserId ------------ " + userId.toString() + "--------------------");
                            if(userId != null) {
                                getData(userId);
                                //liveDataList.add(getLivedata(userId));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }



    public void getData(String userId){
        DatabaseReference db =
        App.getsUsersDataRef()
                .child(userId)
                .child(FirebaseConstants.USER_DETAILS_NODE);
        FirebaseQueryLiveData firebaseQueryLiveData = new FirebaseQueryLiveData(db,
                FirebaseConstants.USE_VALUE_LISTENER);
        firebaseQueryLiveData.observe(mActivity, new Observer<DataSnapshot>() {

            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    UserProfile userProfile = (UserProfile) dataSnapshot.getValue(UserProfile.class);
                    if (userProfile != null) {
                        RepairersNearYou repairersNearYou = new RepairersNearYou(
                                userProfile.getmName(), userProfile.getmAddress(),
                                userProfile.getmAvailability(), "5 mins", userProfile.
                                getmImageUrl(), 3f, true, userProfile.getmLikesCount(),
                                userProfile.getmCommentCount(), userProfile.getLikes(), userProfile.getmPushId(),
                                userProfile.getmEmail(), userProfile.getmWebsiteUrl(), userProfile.getmPhoneNum(),
                                userProfile.getmBusinnessPhonenum()
                        );
                        nearYouAdapter.addToList(repairersNearYou);
                    }
                }
            }
        });
    }

    public LiveData<DataSnapshot> getLivedata(String userId) {
        DatabaseReference db =
                App.getsUsersDataRef()
                        .child(userId)
                        .child(FirebaseConstants.USER_DETAILS_NODE);
        FirebaseQueryLiveData firebaseQueryLiveData = new FirebaseQueryLiveData(db,
                FirebaseConstants.USE_VALUE_LISTENER);
//        firebaseQueryLiveData.observe(mActivity, new Observer<DataSnapshot>() {
//
//            @Override
//            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
//                if (dataSnapshot != null) {
//                    UserProfile userProfile = (UserProfile) dataSnapshot.getValue(UserProfile.class);
//                    if (userProfile != null) {
//                        RepairersNearYou repairersNearYou = new RepairersNearYou(
//                                userProfile.getmName(), userProfile.getmAddress(),
//                                userProfile.getmAvailability(), "5 mins", userProfile.
//                                getmImageUrl(), 3f, true, userProfile.getmLikesCount(),
//                                userProfile.getmCommentCount(), userProfile.getLikes(), userProfile.getmPushId(),
//                                userProfile.getmEmail(), "www.changeThisFool.com", userProfile.getmPhoneNum(),
//                                userProfile.getmBusinnessPhonenum()
//                        );
//                        nearYouAdapter.addToList(repairersNearYou);
//                    }
//                }
//            }
//        });
//    }

        return firebaseQueryLiveData;
    }

    private class Deserializer implements Function<DataSnapshot, RepairersNearYou> {
        @Override
        public RepairersNearYou apply(DataSnapshot input) {
            RepairersNearYou repairersNearYou = null;
            if(input != null) {
                repairersNearYou = input.getValue(RepairersNearYou.class);
                return repairersNearYou;
            }
            return repairersNearYou;
        }
    }
}
