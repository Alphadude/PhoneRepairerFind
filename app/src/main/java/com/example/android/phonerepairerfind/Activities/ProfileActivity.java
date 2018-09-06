package com.example.android.phonerepairerfind.Activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.phonerepairerfind.App;
import com.example.android.phonerepairerfind.Constants.FirebaseConstants;
import com.example.android.phonerepairerfind.Constants.IntentConstants;
import com.example.android.phonerepairerfind.DIalog.TimePickerFragment;
import com.example.android.phonerepairerfind.POJO.UserProfile;
import com.example.android.phonerepairerfind.R;
import com.example.android.phonerepairerfind.databinding.ProfileLayoutBinding;
import com.example.android.phonerepairerfind.utilities.GetUserLocation;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.LinkedHashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener{
    private final String MONDAY_OPEN = "monday_open";
    private final String MONDAY_END = "monday_end";
    private final String TUESDAY_OPEN = "tuesday_open";
    private final String TUESDAY_END = "tuesday_end";
    private final String WEDNESDAY_OPEN = "wednesday_open";
    private final String WEDNESDAY_END = "wednesday_end";
    private final String THURSDAY_OPEN = "thursday_open";
    private final String THURSDAY_END = "thursday_end";
    private final String FRIDAY_OPEN = "friday_open";
    private final String FRIDAY_END = "friday_end";
    private final String SATURDAY_OPEN = "saturday_open";
    private final String SATURDAY_END = "saturday_end";
    private final String SUNDAY_OPEN = "sunday_open";
    private final String SUNDAY_END = "sunday_end";
    private ProfileLayoutBinding mProfileLayoutBinding;
    private UserProfile mUserProfile;
    private String mLatLng;
    private String mLocationAddress;
    private String mMondayAvailTime, mTuesdayAvailTime, mWednessdayAvailTime, mThurdayAvailTime,
            mFridayAvailTime, mSaturdayAvailTime, mSundayAvailTime;
    private String mMondayAvailStartTime, mTuesdayAvailStartTime, mWednessdayAvailStartTime, mThurdayAvailStartTime,
            mFridayAvailStartTime, mSaturdayAvailStartTime, mSundayAvailStartTime;
    private String mMondayAvailEndTime, mTuesdayAvailEndTime, mWednessdayAvailEndTime, mThurdayAvailEndTime,
            mFridayAvailEndTime, mSaturdayAvailEndTime, mSundayAvailEndTime;
    private final String OPEN_TIME = "Opening Time!";
    private final String CLOSING_TIME = "Closing Time";
    public LinkedHashMap<String, String> startEndTime = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProfileLayoutBinding = DataBindingUtil.setContentView(this, R.layout.profile_layout);
        setUserDetails();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IntentConstants.MONDAY_CLICKED, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mProfileLayoutBinding.profileAvailabilityMonday.setChecked(false);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(IntentConstants.MONDAY_CLICKED)){
                if(savedInstanceState.getBoolean(IntentConstants.MONDAY_CLICKED));
                    //mProfileLayoutBinding.profileAvailabilityMonday.setChecked(true);
            }
        }
        //mProfileLayoutBinding.profileAvailabilityMonday.setChecked(false);
    }

    /**
     * This method takes care of hiding profile views not meant for normal users account
     */
    private void normalUsersProfile() {
        mProfileLayoutBinding.profileWebsiteAddress.setVisibility(View.GONE);
        mProfileLayoutBinding.profileEditWebsiteAddressButton.setVisibility(View.GONE);
        mProfileLayoutBinding.profileAvailabilityGridLayout.setVisibility(View.GONE);
        mProfileLayoutBinding.profileLikeImage.setVisibility(View.GONE);
        mProfileLayoutBinding.profileLikesCount.setVisibility(View.GONE);
        mProfileLayoutBinding.profileRatingBar.setVisibility(View.GONE);
        mProfileLayoutBinding.profileBusinessName.setVisibility(View.GONE);

    }

    /**
     * This method takes care of showing profile views meant for shop or repair users account
     */
    private void shopUsersProfile() {
        mProfileLayoutBinding.profileWebsiteAddress.setVisibility(View.VISIBLE);
        mProfileLayoutBinding.profileEditWebsiteAddressButton.setVisibility(View.VISIBLE);
        mProfileLayoutBinding.profileAvailabilityGridLayout.setVisibility(View.VISIBLE);
        mProfileLayoutBinding.profileLikeImage.setVisibility(View.VISIBLE);
        mProfileLayoutBinding.profileLikesCount.setVisibility(View.VISIBLE);
        mProfileLayoutBinding.profileRatingBar.setVisibility(View.VISIBLE);
        mProfileLayoutBinding.profileBusinessName.setVisibility(View.VISIBLE);
    }

    private void setUserDetails() {
        //TODO: Use addValueListener instead of addSingleListener
        App
                .getsUserDetailDataRef()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mUserProfile = dataSnapshot.getValue(UserProfile.class);
                        if(mUserProfile == null) return;
                        if (mUserProfile.getAccountType() != null) {
                            if (mUserProfile.getAccountType().get(FirebaseConstants.SHOPS_USERS_NODE) ||
                                    mUserProfile.getAccountType().get(FirebaseConstants.REPAIRS_USERS_NODE)) {
                                shopUsersProfile();
                            } else normalUsersProfile();
                        }
                        updateUi();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void updateUi() {
        mProfileLayoutBinding.editUsernameButton.setOnClickListener(this);
        setSupportActionBar(mProfileLayoutBinding.profileToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        mProfileLayoutBinding.profileToolbar.setTitle(mUserProfile.getmEmail());
        if (mUserProfile.getmImageUrl() != null && !mUserProfile.getmImageUrl().isEmpty()) {
            Picasso
                    .get()
                    .load(mUserProfile.getmImageUrl())
                    .error(R.color.colorLightGray)
                    .into(mProfileLayoutBinding.profileProfileImageLayout);
        }
        mProfileLayoutBinding.profileUsernameNameEdit.setText(mUserProfile.getmUsername());
        mProfileLayoutBinding.profileLocationName.setText(mUserProfile.getmAddress());
        mProfileLayoutBinding.profilePhoneNumber.setText(mUserProfile.getmPhoneNum());
        if (mUserProfile.getAccountType().get(FirebaseConstants.SHOPS_USERS_NODE) ||
                mUserProfile.getAccountType().get(FirebaseConstants.REPAIRS_USERS_NODE)) {
            mProfileLayoutBinding.profileBusinessName.setText(mUserProfile.getmName());
            mProfileLayoutBinding.profileWebsiteAddress.setText(mUserProfile.getmWebsiteUrl());
            mProfileLayoutBinding.profileLikesCount.setText(mUserProfile.getmLikesCount() + " like(s)");
            mProfileLayoutBinding.profileEditBusinessName.setOnClickListener(this);
            mProfileLayoutBinding.profileEditPhoneNumButton.setOnClickListener(this);
            mProfileLayoutBinding.profileEditWebsiteAddressButton.setOnClickListener(this);
            mProfileLayoutBinding.profileAvailabilityMonday.setOnCheckedChangeListener(this);
            //mProfileLayoutBinding.butt

        }
    }

    private void editField(EditText editTextToEdit, DatabaseReference editDbRef, String toastMsg){
        hideCancelOkButton();
        editTextToEdit.setFocusable(true);
        //editTextToEdit.setBackground(R.color.white);
        final int[][] states = new int[][]{
                new int[]{android.R.attr.state_focused}, //checked
                new int[]{-android.R.attr.state_focused} //unchecked
        };
        final int[] colors = new int[]{
                ContextCompat.getColor(getBaseContext(), R.color.white),
                ContextCompat.getColor(getBaseContext(), R.color.colorLightGray),
        };
        editTextToEdit.setBackgroundTintList(new ColorStateList(states, colors));
        showCancelOkButton();
        mProfileLayoutBinding.cancelOkButton.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextToEdit.setFocusable(false);
                hideCancelOkButton();
            }
        });

        mProfileLayoutBinding.cancelOkButton.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDbRef.setValue(
                        editTextToEdit.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    hideCancelOkButton();
                                    editTextToEdit.setFocusable(false);
                                } else {
                                    editTextToEdit.setFocusable(false);
                                    Toast.makeText(ProfileActivity.this,
                                            toastMsg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                //hideCancelOkButton();
            }
        });
    }

    private void editName() {
        //((EditText)findViewById(R.id.profile_phone_number))
        mProfileLayoutBinding.profileUsernameNameEdit
                .setFocusable(true);
        editField(mProfileLayoutBinding.profileUsernameNameEdit,
                App.getsUserDetailDataRef().child("mUsername"), "Username Not Changed!");
    }

    private void editNumber() {
        mProfileLayoutBinding.profilePhoneNumber
                .setFocusable(true);
        editField(mProfileLayoutBinding.profilePhoneNumber,
                App.getsUserDetailDataRef().child("mPhoneNum"), "Phone number Not Changed!");
    }

    private void changeLocation() {
        new GetUserLocation(this, IntentConstants.PLACE_PICKER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IntentConstants.PLACE_PICKER_REQUEST){
            if(resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(this, data);
                String lat = String.valueOf(place.getLatLng().latitude);
                String lng = String.valueOf(place.getLatLng().longitude);
                mLatLng = lat + ", " + lng;
                if(place.getAddress() != null)
                    mLocationAddress = place.getAddress().toString();
                App.getsUserDetailDataRef().child("mAddress").setValue(mLocationAddress);
                App.getsUserDetailDataRef().child("mLatLng").setValue(mLatLng);
                Toast.makeText(this, "LatLng " + mLatLng, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Address  " + mLocationAddress, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void changePassword() {

    }

    private void editWebsite() {
        hideCancelOkButton();
        showCancelOkButton();
        mProfileLayoutBinding.profileWebsiteAddress
                .setFocusable(true);
        final int[][] states = new int[][]{
                new int[]{android.R.attr.state_focused}, //checked
                new int[]{-android.R.attr.state_focused} //unchecked
        };
        final int[] colors = new int[]{
                ContextCompat.getColor(getBaseContext(), R.color.white),
                ContextCompat.getColor(getBaseContext(), R.color.colorLightGray),
        };
        mProfileLayoutBinding.profileWebsiteAddress
                .setBackgroundTintList(new ColorStateList(states, colors));
//        editField(mProfileLayoutBinding.profileWebsiteAddress,
//                App.getsUserDetailDataRef().child("mWebsiteUrl"), "Website Url not Changed");
    }

    private void editDay(){}

//    private void changeAvailability() {
//        Map<String, Object> availabilityMap = new HashMap<>();
//        availabilityMap.put("Monday", mMondayAvailTime);
//        availabilityMap.put("Tuesday", mTuesdayAvailTime);
//        availabilityMap.put("Wednesday", mWednessdayAvailTime);
//        availabilityMap.put("Thursday", mThurdayAvailTime);
//        availabilityMap.put("Friday", mFridayAvailTime);
//        availabilityMap.put("Saturday", mSaturdayAvailTime);
//        availabilityMap.put("Sunday", mSundayAvailTime);
//    }

    private void hideCancelOkButton() {

        mProfileLayoutBinding.cancelOkButton.getRoot().setVisibility(View.GONE);
    }

    private void showCancelOkButton() {
        mProfileLayoutBinding.cancelOkButton.getRoot().setVisibility(View.VISIBLE);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id  = compoundButton.getId();
        String[] time = null;
        switch (id){
            case R.id.profile_availability_monday:
                updateAvailability(1);
                ifCheckedShowTimePickers(compoundButton, MONDAY_OPEN, MONDAY_END);
                Log.e("ProfileActivity", "profile_availability_monday.. CHECKED!");
//                mMondayAvailStartTime = getTimeFromHashMap(MONDAY_OPEN);
//                mMondayAvailEndTime = getTimeFromHashMap(MONDAY_END);
                //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                break;

            case R.id.profile_availability_tuesday:
                ifCheckedShowTimePickers(compoundButton, TUESDAY_OPEN, TUESDAY_END);
//                mTuesdayAvailStartTime = getTimeFromHashMap(TUESDAY_OPEN);
//                mTuesdayAvailEndTime = getTimeFromHashMap(TUESDAY_END);
                break;
            case R.id.profile_availability_wednesday:
                ifCheckedShowTimePickers(compoundButton, WEDNESDAY_OPEN, WEDNESDAY_END);
//                mWednessdayAvailStartTime = time[0];
//                mWednessdayAvailEndTime = time[1];
                break;
            case R.id.profile_availability_thursday:
                ifCheckedShowTimePickers(compoundButton, THURSDAY_OPEN, THURSDAY_END);
//                mThurdayAvailStartTime = time[0];
//                mThurdayAvailEndTime = time[1];
                break;
            case R.id.profile_availability_friday:
                ifCheckedShowTimePickers(compoundButton, FRIDAY_OPEN, FRIDAY_END);
//                mFridayAvailStartTime = time[0];
//                mFridayAvailEndTime = time[1];
                break;
            case R.id.profile_availability_saturday:
                ifCheckedShowTimePickers(compoundButton, SATURDAY_OPEN, SATURDAY_END);
//                mSaturdayAvailStartTime = time[0];
//                mSaturdayAvailEndTime = time[1];
                break;
            case R.id.profile_availability_sunday:
                ifCheckedShowTimePickers(compoundButton, SUNDAY_OPEN, SUNDAY_END);
//                mSundayAvailStartTime = time[0];
//                mSundayAvailEndTime = time[1];
                break;
        }
    }

    private void updateAvailability(int day){
        hideCancelOkButton();
        showCancelOkButton();
        App.getsUserDetailDataRef()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, String> oldMap;
                        Map<String, String> map = (Map<String, String>)
                                dataSnapshot.child("mAvailiability").getValue(Map.class);
                        if(map != null){
                            oldMap = map;

                            mProfileLayoutBinding.cancelOkButton.okButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    switch (day){
                                        case 1:
                                            mMondayAvailTime = checkAvailTime(true, getTimeFromHashMap(MONDAY_OPEN),
                                                    getTimeFromHashMap(MONDAY_END));
                                            map.put("Monday", mMondayAvailTime);
                                            Log.e("ProfileActivity", "START_TIME: " + mMondayAvailStartTime);
                                            Log.e("ProfileActivity", "END_TIME: " + mMondayAvailEndTime);
                                            break;
                                        case 2:
                                            mTuesdayAvailTime = checkAvailTime(true, getTimeFromHashMap(TUESDAY_OPEN),
                                                    getTimeFromHashMap(TUESDAY_END));
                                            map.put("Tuesday", mTuesdayAvailTime);
                                            break;
                                        case 3:
                                            mWednessdayAvailTime = checkAvailTime(true, getTimeFromHashMap(WEDNESDAY_OPEN),
                                                    getTimeFromHashMap(WEDNESDAY_END));
                                            map.put("Wednesday", mWednessdayAvailTime);
                                            break;
                                        case 4:
                                            mThurdayAvailTime = checkAvailTime(true, getTimeFromHashMap(THURSDAY_OPEN),
                                                    getTimeFromHashMap(THURSDAY_END));
                                            map.put("Thursday", mThurdayAvailTime);
                                            break;
                                        case 5:
                                            mFridayAvailTime = checkAvailTime(true, getTimeFromHashMap(FRIDAY_OPEN),
                                                    getTimeFromHashMap(FRIDAY_END));
                                            map.put("Friday", mFridayAvailTime);
                                            break;
                                        case 6:
                                            mSaturdayAvailTime = checkAvailTime(true, getTimeFromHashMap(SATURDAY_OPEN),
                                                    getTimeFromHashMap(SATURDAY_END));
                                            map.put("Saturday", mSaturdayAvailTime);
                                            break;
                                        case 7:
                                            mSundayAvailTime = checkAvailTime(true, getTimeFromHashMap(SUNDAY_OPEN),
                                                    getTimeFromHashMap(SUNDAY_END));
                                            map.put("Sunday", mSundayAvailTime);
                                            break;
                                    }
                                    App.getsUserDetailDataRef().child("mAvailability").setValue(map);
                                    hideCancelOkButton();
                                }
                            });
                            mProfileLayoutBinding.cancelOkButton.cancelButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    App.getsUserDetailDataRef().child("mAvailability").setValue(oldMap);
                                    hideCancelOkButton();
                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private String getTimeFromHashMap(String key){
        if(startEndTime.containsKey(key)){
            return startEndTime.get(key);
        }else return "";
    }

    private String[] ifCheckedShowTimePickers(CompoundButton compoundButton, String startKey, String endKey){
        String time[] = new String[2];

//        FragmentTransaction fragmentTransaction = ((AppCompatActivity) getActivity())
//                .getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.addToBackStack(null);
//        Fragment prevFrag = ((AppCompatActivity) getActivity()).getSupportFragmentManager()
//                .findFragmentByTag("ShowImageDialogFragment");
//        if (prevFrag != null) {
//            fragmentTransaction.remove(prevFrag);
//        }

        if(compoundButton.isChecked()){
            showTimePickerFragment(OPEN_TIME, startKey);
//            if(pickerFragment1.getTime() != null)
//                time[0] = pickerFragment1.getTime();
//            else time[0] = "";
            showTimePickerFragment(CLOSING_TIME, endKey);
//            if(pickerFragment2.getTime() != null)
//                time[1] = pickerFragment2.getTime();
//            else time[1] = "";
//            pickerFragment1
//                    .onDismiss(new DialogInterface() {
//                        @Override
//                        public void cancel() {
//
//                        }
//
//                        @Override
//                        public void dismiss() {
//                            Toast.makeText(ProfileActivity.this, pickerFragment1.getTime(), Toast.LENGTH_SHORT).show();
//                            //dismiss();
//                            //pickerFragment1.dismiss();
//                            TimePickerFragment pickerFragment2 = showTimePickerFragment(CLOSING_TIME);
//                            pickerFragment2.onDismiss(new DialogInterface() {
//                                @Override
//                                public void cancel() {
//
//                                }
//
//                                @Override
//                                public void dismiss() {
//                                    if (pickerFragment2.getTime() != null)
//                                        time[1] = pickerFragment2.getTime();
//                                    else time[1] = "";
//                                    Toast.makeText(ProfileActivity.this, pickerFragment2.getTime(), Toast.LENGTH_SHORT).show();
//                                    //dismiss();
////                                    pickerFragment1.dismiss();
////                                    pickerFragment2.dismiss();
//
//                                }
//                            });
////                            if(pickerFragment2.getTime() != null)
////                                time[1] = pickerFragment2.getTime();
////                            else time[1] = "";
////                            pickerFragment1.onDetach();
//                        }
//                    });
        }
        return time;
    }

    public TimePickerFragment showTimePickerFragment(String title, String key){
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        Fragment prevFrag = getSupportFragmentManager().findFragmentByTag("timePicker");
//        if(prevFrag != null){
//            fragmentTransaction.remove(prevFrag);
//        }
//        fragmentTransaction.commit();
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setActivity(this);
        timePickerFragment.setTitle(title);
        timePickerFragment.setKey(key);
        timePickerFragment.show(getSupportFragmentManager(), title);
        //return mTime.getTime();
        return timePickerFragment;
    }

    private String checkAvailTime(boolean isChecked, String startTime, String endTime) {
        if (isChecked) {
            String availability = "";
            if (startTime != null && endTime != null) {
                if (checkIfStartEndTimeNotEmpty(startTime, endTime))
                    availability = startTime + " - " + endTime;
                if (!checkIfStartEndTimeNotEmpty(startTime, endTime))
                    availability = "Open";
                //else availability = startTime + " - " + endTime;

                if (checkIfStartTimeEmpty(startTime, endTime))
                    availability = "Open till " + endTime;
                if (!checkIfStartTimeEmpty(startTime, endTime))
                    availability = startTime;
                //else availability = startTime;
//                if (startTime.isEmpty() & !endTime.isEmpty()) return "Open till " + endTime;
//
//                if (startTime.isEmpty() & endTime.isEmpty()) {
//                    return "Open";
//                }
//                if (!endTime.isEmpty() && !startTime.isEmpty()) return startTime + " - " + endTime;
//
//                if (!startTime.isEmpty() && endTime.isEmpty()) return startTime;
            }
            return "Open";
        } else {
            return "Closed";
        }
//        return "";
    }

    private boolean checkIfStartEndTimeNotEmpty(String startTime, String endTime) {
        if (!startTime.isEmpty() && !endTime.isEmpty()) return true;
        else return false;
    }

    private boolean checkIfStartTimeEmpty(String startTime, String endTime) {
        if (startTime.isEmpty() && !endTime.isEmpty()) return true;
        else if (!startTime.isEmpty() && endTime.isEmpty()) return false;
        else return false;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.edit_username_button:
                editName();
                break;
            case R.id.profile_edit_phone_num_button:
                editNumber();
                break;
            case R.id.profile_edit_website_address_button:
                editWebsite();
                break;
            case R.id.profile_change_password_button:
                break;
//            case R.id.profile_availability_monday:
//                ifCheckedShowTimePickers(true);
//                break;
        }
    }
}
