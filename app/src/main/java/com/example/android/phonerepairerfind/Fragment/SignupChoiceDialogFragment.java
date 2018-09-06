package com.example.android.phonerepairerfind.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.android.phonerepairerfind.Activities.SignupActivity;
import com.example.android.phonerepairerfind.Constants.AdaptersConstant;
import com.example.android.phonerepairerfind.R;

public class SignupChoiceDialogFragment extends DialogFragment implements View.OnClickListener {

    private Button normalUsersButton, repairersUsersButton, shopsUsersButton;
    private ImageButton closeButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_choice, container, false);
        normalUsersButton = (Button) view.findViewById(R.id.signup_choice_normal_user_button);
        repairersUsersButton = (Button) view.findViewById(R.id.signup_choice_phone_repaiers);
        shopsUsersButton = (Button) view.findViewById(R.id.signup_choice_phone_shop);
        closeButton = (ImageButton) view.findViewById(R.id.signup_choice_close_button);
        normalUsersButton.setOnClickListener(this);
        repairersUsersButton.setOnClickListener(this);
        shopsUsersButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        Intent signup = new Intent(view.getContext(), SignupActivity.class);
        switch (view.getId()){
            case R.id.signup_choice_normal_user_button:
                signup.putExtra(AdaptersConstant.SIGNUP_CHOICE_EXTRA, AdaptersConstant.SIGNUP_NORMAL_USERS_VALUES);
                break;
            case R.id.signup_choice_phone_repaiers:
                signup.putExtra(AdaptersConstant.SIGNUP_CHOICE_EXTRA, AdaptersConstant.SIGNUP_REPAIRERS_USERS_VALUES);
                break;
            case R.id.signup_choice_phone_shop:
                signup.putExtra(AdaptersConstant.SIGNUP_CHOICE_EXTRA, AdaptersConstant.SIGNUP_SHOPS_USERS_VALUES);
                break;
            case R.id.signup_choice_close_button:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.getPrimaryNavigationFragment();
                SignupChoiceDialogFragment frag = (SignupChoiceDialogFragment) fragmentManager.findFragmentByTag("SignupChoiceDialog");
                frag.dismiss();
                fragmentManager.beginTransaction().remove(frag).commit();
//                if(frag != null){
//                    frag.dismiss();
//                }
                break;
        }
        startActivity(signup);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(getActivity() instanceof SignupActivity)
            dialog.dismiss();
    }
}
