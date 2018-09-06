package com.example.android.phonerepairerfind.utilities;

import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;

public class GetUserLocation {
    LocationManager lm;
    private AppCompatActivity mActivity;

    public GetUserLocation(AppCompatActivity activity, final int PLACE_PICKER_REQUEST) {
        mActivity = activity;

        PlacePicker
                .IntentBuilder builder = new PlacePicker.IntentBuilder();
        //builder.setLatLngBounds(new LatLngBounds())
        try {
            mActivity.startActivityForResult(builder.build(mActivity), PLACE_PICKER_REQUEST);
        }catch (GooglePlayServicesNotAvailableException e){
            Toast.makeText(mActivity, "GooglePlayServices not available", Toast.LENGTH_SHORT)
                    .show();
        }catch(GooglePlayServicesRepairableException e){}


    }
}
