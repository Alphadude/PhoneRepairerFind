package com.example.android.phonerepairerfind.DIalog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.example.android.phonerepairerfind.Activities.ProfileActivity;

import java.util.Calendar;

/**
 * Created by JAHSWILL on 4/22/2018.
 */

public class TimePickerFragment extends android.support.v4.app.DialogFragment
    implements TimePickerDialog.OnTimeSetListener{
    private String time;
    private AppCompatActivity mActivity;
    private String mTitle;
    private String mKey;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, min, DateFormat.is24HourFormat
                (getActivity()));

    }

    public void setKey(String key){
        mKey = key;
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        time = hourOfDay + ":" + minute;
        if(getActivity() != null){
            if(getActivity() instanceof ProfileActivity){
                ((ProfileActivity)getActivity()).startEndTime.put(mKey, time);
            }
        }
    }

    public String getTime() {
        return time;
    }

    public void setActivity(AppCompatActivity activity){
        mActivity = activity;
    }

    public void setTitle(@NonNull String title){
        mTitle = title;
    }

//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        //super.onDismiss(dialog);
////        Toast.makeText(mActivity, "TimePickerFrag: Time -------- " + time, Toast.LENGTH_SHORT).show();
//        Log.e("TimePickerFrag", "Time -------- " + time);
//        if(dialog != null)
//        dialog.dismiss();
//        //super.onDismiss(dialog);
//    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        //dismiss();
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        Log.e("TimePicker", "onDetach()");
//        dismiss();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        Log.e("TimePicker", "onDestroyView()!!!");
//        Fragment frag = getActivity().getSupportFragmentManager().findFragmentByTag("timePicker");
//        if(frag != null){
//
//            Log.e("TimePicker", "Fragment NOT NULL!!!");
////            frag.deta
//            //((TimePickerFragment)frag).dismiss();
//        }
//    }



    @Override
    public void setupDialog(Dialog dialog, int style) {
        //super.setupDialog(dialog, style);
        dialog.setTitle(mTitle);
    }
}
