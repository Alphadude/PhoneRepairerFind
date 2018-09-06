package com.example.android.phonerepairerfind.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.phonerepairerfind.Constants.AdaptersConstant;
import com.example.android.phonerepairerfind.Constants.FirebaseConstants;
import com.example.android.phonerepairerfind.Constants.IntentConstants;
import com.example.android.phonerepairerfind.DIalog.TimePickerFragment;
import com.example.android.phonerepairerfind.Fragment.SignupChoiceDialogFragment;
import com.example.android.phonerepairerfind.POJO.UserProfile;
import com.example.android.phonerepairerfind.R;
import com.example.android.phonerepairerfind.anim.CustomProgressBar;
import com.example.android.phonerepairerfind.databinding.FragmentSignUpShopUserBinding;
import com.example.android.phonerepairerfind.utilities.DatabaseHelper;
import com.example.android.phonerepairerfind.utilities.GetUserLocation;
import com.example.android.phonerepairerfind.utilities.ImagePicker;
import com.example.android.phonerepairerfind.utilities.Validation;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = SignupActivity.class.getSimpleName();
    private final String EMPTY_FIELD_TEXT = "Field must not be empty";
    private final String EMAIL_NOT_VALID = "Enter a valid email";
    private final String URL_NOT_VALID = "Enter a valid web url";
    private final String PASSWORD_LESS = "Password must be greater than six characters";
    private final String CONFIRM_PASSWORD = "Password does not match";
    private final String OPEN_TIME = "Opening Time!";
    private final String CLOSING_TIME = "Closing Time";
    ImageView customProgressBar;
    private TextInputLayout mNameL, mUsernameL, mEmailL, mPasswordL, mPhoneNumL, mConfirmPasswordL, mBusinessPhoneNumL, mWebUrlL;
    private EditText mNameE, mUsernameE, mEmailE, mPasswordE, mPhoneNumE, mConfirmPasswordE, mBusinessPhoneNumE, mWebUrlE;
    private FloatingActionButton signUpWithEmailPasswordFab, signUpWIthFacebookFab,
            signUpWithGoogleFab, signUpFab;
    private ImageButton mImagePickerButton;

    private String mMondayAvailTime, mTuesdayAvailTime, mWednessdayAvailTime, mThurdayAvailTime,
            mFridayAvailTime, mSaturdayAvailTime, mSundayAvailTime;
    private String mMondayAvailStartTime, mTuesdayAvailStartTime, mWednessdayAvailStartTime, mThurdayAvailStartTime,
            mFridayAvailStartTime, mSaturdayAvailStartTime, mSundayAvailStartTime;
    private String mMondayAvailEndTime, mTuesdayAvailEndTime, mWednessdayAvailEndTime, mThurdayAvailEndTime,
            mFridayAvailEndTime, mSaturdayAvailEndTime, mSundayAvailEndTime;
    private Button mPickLocationButton;
    private CircleImageView mProfilePic;
    private String mName, mUsername, mEmail, mPassword, mPhoneNum, mConfirmPassword, mBusinessPhoneNum, mWebUrl;
    private DatabaseReference mNormalUserRef, mShopsUserRef, mRepairsUserRef;
    private int userType;
    private FirebaseAuth mAuth;
    private boolean isFabOpen = true, atLeastACheckboxSelected = false, checkBoxTuesdaySelected = false,
            checkBoxWednesdaySelected = false, checkBoxThursdaySelected = false, checkBoxFridaySelected = false,
            checkBoxSaturdaySelected = false, checkBoxSundaySelected = false;
    //private boolean isMondayChecked, isTuesdayChecked, isWednesdayChecked, isThursdayChecked, isFridayChecked, isSaturdayChecked, isSundayChecked;
    private Animation fab_open;
    private Animation fab_close;
    private Animation fab_icon_rotate_forward;
    private Animation fab_icon_rotate_backward;
    private Uri imageUriPath;
    private ImagePicker imagePicker;
    private FragmentSignUpShopUserBinding fragmentSignUpShopUserBinding;
    private GoogleApiClient mGoogleApiClient;
    private String mLatLng;
    private String mLocationAddress;
    private Snackbar mLocationNeededSnack, mAvailabilitySelectSnack;


    //TODO: If User inputs website, update the UserProfile
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: Handle on rotation image removes
        super.onCreate(savedInstanceState);
        setContentView(new View(this));
        Log.e(TAG, "SIGNUP ACTIVITY OOOOH");
        mAuth = FirebaseAuth.getInstance();
        fab_open = AnimationUtils.loadAnimation(this, R.anim.anime_fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.anime_fab_close);
        fab_icon_rotate_forward = AnimationUtils.loadAnimation(this, R.anim.anime_fab_rotate_forward);
        fab_icon_rotate_backward = AnimationUtils.loadAnimation(this, R.anim.anime_fab_rotate_backward);
//        mGoogleApiClient = new GoogleApiClient.Builder(this, this, this)
//                .addApi(LOCATION_SERVICE)
//                .build();
        if (getIntent() != null) {
            if (getIntent().hasExtra(IntentConstants.SIGNUP_NOT_COMPLETED_KEY)) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment prevFrag = getSupportFragmentManager().findFragmentByTag("SignupChoiceDialog");
                if (prevFrag != null) {
                    fragmentTransaction.remove(prevFrag);
                }
                //fragmentTransaction.addToBackStack("SignupChoiceDialog");
                SignupChoiceDialogFragment dialogFragment = new SignupChoiceDialogFragment();
                dialogFragment.onDismiss(new DialogInterface() {
                    @Override
                    public void cancel() {
                        finish();
                        //TODO close calling activity if its called grom signupactivity
                    }

                    @Override
                    public void dismiss() {
                        startActivity(new Intent(SignupActivity.this, SignInActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }
                });
                dialogFragment.show(fragmentTransaction, "SignupChoiceDialog");
                //finish();
            } else setUpUsers();
        } else setUpUsers();
        //setUpUsers();
        if (savedInstanceState != null) {
            if (savedInstanceState.get("ImageUriPath") != null)
                Picasso
                        .get()
                        .load(Uri.parse(savedInstanceState.getString("ImageUriPath")))
                        .error(R.color.colorLightGray)
                        .into(mProfilePic);
        }
    }

    @Override
    public boolean navigateUpTo(Intent upIntent) {
        return super.navigateUpTo(upIntent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (imageUriPath != null)
            outState.putString("ImageUriPath", imageUriPath.toString());
        outState.putBoolean(IntentConstants.MONDAY_CLICKED, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //fragmentSignUpShopUserBinding.signupMonday.m
    }

    private void setUpUsers() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(AdaptersConstant.SIGNUP_CHOICE_EXTRA)) {
                switch (intent.getIntExtra(AdaptersConstant.SIGNUP_CHOICE_EXTRA, -1)) {
                    case AdaptersConstant.SIGNUP_NORMAL_USERS_VALUES:
                        userType = AdaptersConstant.SIGNUP_NORMAL_USERS_VALUES;
                        setContentView(R.layout.fragment_sign_up_normal_user);
                        break;
                    case AdaptersConstant.SIGNUP_REPAIRERS_USERS_VALUES:
                        userType = AdaptersConstant.SIGNUP_REPAIRERS_USERS_VALUES;

                        fragmentSignUpShopUserBinding =
                                DataBindingUtil.setContentView(this, R.layout.fragment_sign_up_shop_user);
                        //setContentView(R.layout.fragment_sign_up_shop_user);
                        break;
                    case AdaptersConstant.SIGNUP_SHOPS_USERS_VALUES:
                        userType = AdaptersConstant.SIGNUP_SHOPS_USERS_VALUES;
                        fragmentSignUpShopUserBinding =
                                DataBindingUtil.setContentView(this, R.layout.fragment_sign_up_shop_user);
                        //setContentView(R.layout.fragment_sign_up_shop_user);
                        break;
                }
            }
        }
        initViews();
    }

    private int authUser() {
        setNormalUsersError();
        int error = 0;
        mName = getTextFromEditText(mNameE);
        mUsername = getTextFromEditText(mUsernameE);
        mPassword = getTextFromEditText(mPasswordE);
        mConfirmPassword = getTextFromEditText(mConfirmPasswordE);
        mPhoneNum = getTextFromEditText(mPhoneNumE);
        mEmail = getTextFromEditText(mEmailE);
        mWebUrl = getTextFromEditText(mWebUrlE);
        if (!Validation.validateFields(mName)) {
            mNameL.setError(EMPTY_FIELD_TEXT);
            error++;
        }
        if (!Validation.validateFields(mUsername)) {
            mUsernameL.setError(EMPTY_FIELD_TEXT);
            error++;
        }
        if (mPassword.length() < 6) {
            mPasswordL.setError(PASSWORD_LESS);
            error++;
        }
        if (!mConfirmPassword.equals(mPassword)) {
            mConfirmPasswordL.setError(CONFIRM_PASSWORD);
            error++;
        }
        if (!Validation.validateFields(mPhoneNum)) {
            mPhoneNumL.setError(EMPTY_FIELD_TEXT);
            error++;
        }
        if (!Validation.validateEmail(mEmail)) {
            mEmailL.setError(EMAIL_NOT_VALID);
            error++;
        }
        if (userType == AdaptersConstant.SIGNUP_REPAIRERS_USERS_VALUES ||
                userType == AdaptersConstant.SIGNUP_SHOPS_USERS_VALUES) {

            mBusinessPhoneNum = getTextFromEditText(mBusinessPhoneNumE);
            if (!mWebUrl.isEmpty())
                if (!Validation.ValidateUrl(mWebUrl)) {
                    mWebUrlL.setError(URL_NOT_VALID);
                    error++;
                }

            if (fragmentSignUpShopUserBinding.signupMonday.isChecked()) {
                //showTimePickerFragment();
                atLeastACheckboxSelected = true;
                mMondayAvailTime = checkAvailTime(true, mMondayAvailStartTime, mMondayAvailEndTime);
            } else mMondayAvailTime = checkAvailTime(false, "", "");

            if (fragmentSignUpShopUserBinding.signupTuesday.isChecked()) {
                //showTimePickerFragment();
                atLeastACheckboxSelected = true;
                mTuesdayAvailTime = checkAvailTime(true, mTuesdayAvailStartTime, mTuesdayAvailEndTime);
            } else mTuesdayAvailTime = checkAvailTime(false, "", "");

            if (fragmentSignUpShopUserBinding.signupWednesday.isChecked()) {
                //showTimePickerFragment();
                atLeastACheckboxSelected = true;
                mWednessdayAvailTime = checkAvailTime(true, mWednessdayAvailStartTime, mWednessdayAvailEndTime);
            } else mWednessdayAvailTime = checkAvailTime(false, "", "");

            if (fragmentSignUpShopUserBinding.signupThursday.isChecked()) {
                //showTimePickerFragment();
                atLeastACheckboxSelected = true;
                mThurdayAvailTime = checkAvailTime(true, mThurdayAvailStartTime, mThurdayAvailEndTime);
            } else mThurdayAvailTime = checkAvailTime(false, "", "");

            if (fragmentSignUpShopUserBinding.signupFriday.isChecked()) {
                //showTimePickerFragment();
                atLeastACheckboxSelected = true;
                mFridayAvailTime = checkAvailTime(true, mFridayAvailStartTime, mFridayAvailEndTime);
            } else mFridayAvailTime = checkAvailTime(false, "", "");

            if (fragmentSignUpShopUserBinding.signupSaturday.isChecked()) {
                //showTimePickerFragment();
                atLeastACheckboxSelected = true;
                mSaturdayAvailTime = checkAvailTime(true, mSaturdayAvailStartTime, mSaturdayAvailEndTime);
            } else mSaturdayAvailTime = checkAvailTime(false, "", "");

            if (fragmentSignUpShopUserBinding.signupSunday.isChecked()) {
                //showTimePickerFragment();
                atLeastACheckboxSelected = true;
                mSundayAvailTime = checkAvailTime(true, mSundayAvailStartTime, mSundayAvailEndTime);
            } else mSundayAvailTime = checkAvailTime(false, "", "");

//            if(!atLeastACheckboxSelected || !checkBoxTuesdaySelected || !checkBoxWednesdaySelected ||
//                    !checkBoxThursdaySelected || !checkBoxFridaySelected || !checkBoxSaturdaySelected|
//                    !checkBoxSundaySelected){
//                mAvailabilitySelectSnack =
//                        Snackbar.make(((CoordinatorLayout)findViewById(R.id.signUp_coordinatorLayout)),
//                        "At least one available day must be selected", Snackbar.LENGTH_SHORT);
//                        mAvailabilitySelectSnack.show();
//                error++;
//            }

            if (!atLeastACheckboxSelected) {
                mAvailabilitySelectSnack =
                        Snackbar.make(((CoordinatorLayout) findViewById(R.id.signUp_coordinatorLayout)),
                                "At least one available day must be selected", Snackbar.LENGTH_SHORT);
                mAvailabilitySelectSnack.show();
                error++;
            }

            if (mLatLng == null || mLocationAddress == null) {
                mLocationNeededSnack =
                        Snackbar.make(((CoordinatorLayout) findViewById(R.id.signUp_coordinatorLayout)),
                                "Please Location is needed for best result", Snackbar.LENGTH_SHORT);
                mLocationNeededSnack.show();
                error++;
            } else {
                if (mLatLng.isEmpty() && mLocationAddress.isEmpty()) {
                    mLocationNeededSnack =
                            Snackbar.make(((CoordinatorLayout) findViewById(R.id.signUp_coordinatorLayout)),
                                    "Please Location is needed for best result", Snackbar.LENGTH_SHORT);
                    mLocationNeededSnack.show();
                    error++;
                }
            }

            if (!Validation.validateFields(mBusinessPhoneNum)) {
                mBusinessPhoneNumL.setError(EMAIL_NOT_VALID);
                error++;
            }
        }
        return error;
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

    private void setUpShopUsers() {

    }

    private void setUpRepairUsers() {

    }

    private String getTextFromEditText(EditText editText) {
        return editText.getText().toString().trim();
    }

    private void setNormalUsersError() {
        mUsernameL.setError(null);
        mNameL.setError(null);
        mEmailL.setError(null);
        mPasswordL.setError(null);
        mConfirmPasswordL.setError(null);
        mPhoneNumL.setError(null);
        if (userType == AdaptersConstant.SIGNUP_REPAIRERS_USERS_VALUES ||
                userType == AdaptersConstant.SIGNUP_SHOPS_USERS_VALUES) {
            mBusinessPhoneNumL.setError(null);
            if (mAvailabilitySelectSnack != null)
                mAvailabilitySelectSnack.dismiss();
            if (mLocationNeededSnack != null)
                //((ViewGroup)mLocationNeededSnack.getView()).rem
                mLocationNeededSnack.dismiss();
        }
    }

    private void initViews() {
        mUsernameL = (TextInputLayout) findViewById(R.id.signup_username_textinput_layout);
        mUsernameE = (EditText) findViewById(R.id.signup_username_edittext);
        mNameL = (TextInputLayout) findViewById(R.id.signup_name_textinput_layout);
        mNameE = (EditText) findViewById(R.id.signup_name_edittext);
        mEmailL = (TextInputLayout) findViewById(R.id.signup_email_textinput_layout);
        mEmailE = (EditText) findViewById(R.id.signup_email_edittext);
        mPasswordL = (TextInputLayout) findViewById(R.id.signup_password_textinput_layout);
        mPasswordE = (EditText) findViewById(R.id.signup_password_edittext);
        mConfirmPasswordL = (TextInputLayout) findViewById(R.id.signup_confirm_password_textinput_layout);
        mConfirmPasswordE = (EditText) findViewById(R.id.signup_confirm_password_edittext);
        mPhoneNumL = (TextInputLayout) findViewById(R.id.signup_phoneNum_textinput_layout);
        mPhoneNumE = (EditText) findViewById(R.id.signup_phoneNum_edittext);
        ///mSignupFab = (FloatingActionButton) findViewById(R.id.signup_fab);
        //mSignupFab.setOnClickListener(this);
        mProfilePic = (CircleImageView) findViewById(R.id.signup_profile_pics);
        mImagePickerButton = (ImageButton) findViewById(R.id.signup_image_picker_button);
        mImagePickerButton.setOnClickListener(this);
        customProgressBar = (ImageView) findViewById(R.id.custom_progress_bar_icon);
        signUpFab = (FloatingActionButton) findViewById(R.id.signup_more_fab);
        signUpWithEmailPasswordFab = (FloatingActionButton) findViewById(R.id.signup_with_email_pasword_fab);
        signUpWithGoogleFab = (FloatingActionButton) findViewById(R.id.signup_with_google_fab);
        signUpWIthFacebookFab = (FloatingActionButton) findViewById(R.id.signup_with_facebook_fab);
        signUpFab.setOnClickListener(this);
        signUpWithEmailPasswordFab.setOnClickListener(this);
        signUpWithGoogleFab.setOnClickListener(this);
        signUpWIthFacebookFab.setOnClickListener(this);
        mPickLocationButton = (Button) findViewById(R.id.signup_location_button);
        signUpFab.startAnimation(fab_icon_rotate_forward);
        signUpWithEmailPasswordFab.startAnimation(fab_open);
        signUpWIthFacebookFab.startAnimation(fab_open);
        signUpWithGoogleFab.startAnimation(fab_open);
        signUpWithEmailPasswordFab.setClickable(true);
        signUpWIthFacebookFab.setClickable(true);
        signUpWithGoogleFab.setClickable(true);
        signUpWithEmailPasswordFab.setVisibility(View.VISIBLE);
        signUpWIthFacebookFab.setVisibility(View.VISIBLE);
        signUpWithGoogleFab.setVisibility(View.VISIBLE);
        isFabOpen = true;
        if (userType == AdaptersConstant.SIGNUP_REPAIRERS_USERS_VALUES ||
                userType == AdaptersConstant.SIGNUP_SHOPS_USERS_VALUES) {
            mBusinessPhoneNumL = (TextInputLayout) findViewById(R.id.signup_businesss_phoneNum_textinput_layout);
            mBusinessPhoneNumE = (EditText) findViewById(R.id.signup_businesss_phoneNum_edittext);
            fragmentSignUpShopUserBinding.signupLocationButton.setOnClickListener(this);
            mWebUrlL = fragmentSignUpShopUserBinding.signupBusinesssWebsiteTextinputLayout;
            mWebUrlE = fragmentSignUpShopUserBinding.signupBusinesssWebsiteEdittext;
            fragmentSignUpShopUserBinding.signupMonday.setOnClickListener(this);
            fragmentSignUpShopUserBinding.signupTuesday.setOnClickListener(this);
            fragmentSignUpShopUserBinding.signupWednesday.setOnClickListener(this);
            fragmentSignUpShopUserBinding.signupThursday.setOnClickListener(this);
            fragmentSignUpShopUserBinding.signupFriday.setOnClickListener(this);
            fragmentSignUpShopUserBinding.signupSaturday.setOnClickListener(this);
            fragmentSignUpShopUserBinding.signupSunday.setOnClickListener(this);
        }
    }

    private void animateFab() {
        if (isFabOpen) {
            signUpFab.startAnimation(fab_icon_rotate_backward);

            signUpWithEmailPasswordFab.startAnimation(fab_close);
            signUpWIthFacebookFab.startAnimation(fab_close);
            signUpWithGoogleFab.startAnimation(fab_close);
            signUpWithEmailPasswordFab.setClickable(false);
            signUpWIthFacebookFab.setClickable(false);
            signUpWithGoogleFab.setClickable(false);
            signUpWithEmailPasswordFab.setVisibility(View.INVISIBLE);
            signUpWIthFacebookFab.setVisibility(View.INVISIBLE);
            signUpWithGoogleFab.setVisibility(View.INVISIBLE);
            isFabOpen = false;
        } else {
            signUpFab.startAnimation(fab_icon_rotate_forward);
            signUpWithEmailPasswordFab.startAnimation(fab_open);
            signUpWIthFacebookFab.startAnimation(fab_open);
            signUpWithGoogleFab.startAnimation(fab_open);
            signUpWithEmailPasswordFab.setClickable(true);
            signUpWIthFacebookFab.setClickable(true);
            signUpWithGoogleFab.setClickable(true);
            signUpWithEmailPasswordFab.setVisibility(View.VISIBLE);
            signUpWIthFacebookFab.setVisibility(View.VISIBLE);
            signUpWithGoogleFab.setVisibility(View.VISIBLE);
            isFabOpen = true;
        }
    }


    @Override
    public void onClick(View view) {
        CustomProgressBar customProgressBarAnime = new CustomProgressBar(customProgressBar, SignupActivity.this);
        String[] time = null;
        switch (view.getId()) {
            case R.id.signup_more_fab:
                animateFab();
                break;
            case R.id.signup_with_email_pasword_fab:
                customProgressBarAnime.startLoading();
                FirebaseDatabase.getInstance().goOnline();
                Map<String, Boolean> map = new HashMap<String, Boolean>();
                Map<String, Boolean> mapLikes = new HashMap<String, Boolean>();

                if (userType == AdaptersConstant.SIGNUP_NORMAL_USERS_VALUES) {
                    map.put(FirebaseConstants.NORMAL_USERS_NODE, Boolean.TRUE);
                } else if (userType == AdaptersConstant.SIGNUP_SHOPS_USERS_VALUES) {
                    map.put(FirebaseConstants.SHOPS_USERS_NODE, Boolean.TRUE);
                } else if (userType == AdaptersConstant.SIGNUP_REPAIRERS_USERS_VALUES) {
                    map.put(FirebaseConstants.REPAIRS_USERS_NODE, Boolean.TRUE);
                }

                int errorCount = authUser();
                if (errorCount != 0) {
                    customProgressBarAnime.stopLoading();
                }
                if (errorCount == 0) {
                    customProgressBarAnime.startLoading();
                    Map<String, Object> availabilityMap = new HashMap<>();
                    availabilityMap.put("Monday", mMondayAvailTime);
                    availabilityMap.put("Tuesday", mTuesdayAvailTime);
                    availabilityMap.put("Wednesday", mWednessdayAvailTime);
                    availabilityMap.put("Thursday", mThurdayAvailTime);
                    availabilityMap.put("Friday", mFridayAvailTime);
                    availabilityMap.put("Saturday", mSaturdayAvailTime);
                    availabilityMap.put("Sunday", mSundayAvailTime);
                    DatabaseHelper.setActivity(this);
                    UserProfile userProfile = new UserProfile("", mName, mUsername, mEmail,
                            mPhoneNum, mBusinessPhoneNum, mLocationAddress, "",
                            availabilityMap, 0, 0, 0, map, mapLikes, mLatLng);
                    if (!mWebUrl.isEmpty())
                        userProfile.setmWebsiteUrl(mWebUrl);
                    DatabaseHelper.createAccountWithEmailPassword(signUpWithEmailPasswordFab, customProgressBarAnime, mEmail,
                            mPassword, userType, imageUriPath, userProfile, ((CoordinatorLayout) findViewById(R.id.signUp_coordinatorLayout)),
                            this);
                }
                break;
            case R.id.signup_image_picker_button:
                imagePicker = new ImagePicker(this, IntentConstants.PROFILE_PICS_PICK_IMAGE_REQUEST);
                imagePicker.chooseFileIntent();
                break;
            case R.id.signup_location_button:
                new GetUserLocation(this, IntentConstants.PLACE_PICKER_REQUEST);
                break;
            //switch (id) {
            case R.id.signup_monday:
                Log.e(TAG, "signup monday CLICKED!!!");
                time = ifCheckedShowTimePickers((CheckBox) view, 1);
                break;

            case R.id.signup_tuesday:
                time = ifCheckedShowTimePickers((CheckBox) view, 2);
                Log.e(TAG, "signup tuesday CLICKED!!!");

                break;
            case R.id.signup_wednesday:
                time = ifCheckedShowTimePickers((CheckBox) view, 3);

                break;
            case R.id.signup_thursday:
                time = ifCheckedShowTimePickers((CheckBox) view, 4);

                break;
            case R.id.signup_friday:
                time = ifCheckedShowTimePickers((CheckBox) view, 5);
                Log.e(TAG, "signup friday CLICKED!!!");

                break;
            case R.id.signup_saturday:
                time = ifCheckedShowTimePickers((CheckBox) view, 6);
                Log.e(TAG, "signup saturday CLICKED!!!");

                break;
            case R.id.signup_sunday:
                time = ifCheckedShowTimePickers((CheckBox) view, 7);
                Log.e(TAG, "signup sunday CLICKED!!!");

                break;
            //}
        }

    }

    public TimePickerFragment showTimePickerFragment(String title, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment prevFrag = getSupportFragmentManager().findFragmentByTag(tag);
        if (prevFrag != null) {
            fragmentTransaction.remove(prevFrag);
        }
        //fragmentTransaction.addToBackStack(tag);
        //fragmentTransaction
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setActivity(this);
        timePickerFragment.setTitle(title);
        timePickerFragment.show(fragmentTransaction, tag);

//        TimePickerFragment timePickerFragment = new TimePickerFragment();
//        timePickerFragment.show(getSupportFragmentManager(), tag);
        //return mTime.getTime();
        return timePickerFragment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IntentConstants.PROFILE_PICS_PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUriPath = data.getData();
            signUpWithEmailPasswordFab
                    .setEnabled(true);
            Picasso
                    .get()
                    .load(imageUriPath)
                    .error(R.color.colorLightGray)
                    .into(mProfilePic);
        }
        if (requestCode == IntentConstants.PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                String lat = String.valueOf(place.getLatLng().latitude);
                String lng = String.valueOf(place.getLatLng().longitude);

                mLatLng = lat + ", " + lng;
                if (place.getAddress() != null)
                    mLocationAddress = place.getAddress().toString();
                Toast.makeText(this, "LatLng " + mLatLng, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Address  " + mLocationAddress, Toast.LENGTH_SHORT).show();
            }
        }
    }

    //@Override
//    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//        int id = compoundButton.getId();
//        String[] time = null;
//        switch (id) {
//            case R.id.signup_monday:
//                time = ifCheckedShowTimePickers(compoundButton);
//                mMondayAvailStartTime = time[0];
//                mMondayAvailEndTime = time[1];
//                break;
//
//            case R.id.signup_tuesday:
//                time = ifCheckedShowTimePickers(compoundButton);
//                mTuesdayAvailStartTime = time[0];
//                mTuesdayAvailEndTime = time[1];
//                break;
//            case R.id.signup_wednesday:
//                time = ifCheckedShowTimePickers(compoundButton);
//                mWednessdayAvailStartTime = time[0];
//                mWednessdayAvailEndTime = time[1];
//                break;
//            case R.id.signup_thursday:
//                time = ifCheckedShowTimePickers(compoundButton);
//                mThurdayAvailStartTime = time[0];
//                mThurdayAvailEndTime = time[1];
//                break;
//            case R.id.signup_friday:
//                time = ifCheckedShowTimePickers(compoundButton);
//                mFridayAvailStartTime = time[0];
//                mFridayAvailEndTime = time[1];
//                break;
//            case R.id.signup_saturday:
//                time = ifCheckedShowTimePickers(compoundButton);
//                mSaturdayAvailStartTime = time[0];
//                mSaturdayAvailEndTime = time[1];
//                break;
//            case R.id.signup_sunday:
//                time = ifCheckedShowTimePickers(compoundButton);
//                mSundayAvailStartTime = time[0];
//                mSundayAvailEndTime = time[1];
//                break;
//        }
//    }


//    public DialogInterface.OnDismissListener onDismissListener = new DialogInterface.OnDismissListener() {
//        @Override
//        public void onDismiss(DialogInterface dialogInterface) {
//            TimePickerFragment pickerFragment2 = showTimePickerFragment(CLOSING_TIME);
//            if(pickerFragment2.getTime() != null)
//                time[1] = pickerFragment2.getTime();
//            else time[1] = "";
//        }
//
//    };

    private String[] ifCheckedShowTimePickers(CompoundButton compoundButton, int num) {
        String time[] = new String[2];

//        compoundButton.isChecked()
        //if (compoundButton.isChecked()) {
//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            Fragment prevFrag = getSupportFragmentManager().findFragmentByTag("SignupChoiceDialog");
//            if(prevFrag != null){
//                fragmentTransaction.remove(prevFrag);
//            }
//            fragmentTransaction.addToBackStack("SignupChoiceDialog");
//            SignupChoiceDialogFragment dialogFragment = new SignupChoiceDialogFragment();
//            dialogFragment.show(fragmentTransaction, "SignupChoiceDialog");

            TimePickerFragment pickerFragment1 = showTimePickerFragment(CLOSING_TIME, "timePicker1");
            //pickerFragment1.setRetainInstance(true);
            //pickerFragment1.is
//            pickerFragment1.
//            TimePickerFragment pickerFragment2 = showTimePickerFragment(CLOSING_TIME);
//            if(pickerFragment2.getTime() != null)
//                time[1] = pickerFragment2.getTime();
//            else time[1] = "";
            pickerFragment1
                    .onDismiss(new DialogInterface() {
                        @Override
                        public void cancel() {

                        }

                        @Override
                        public void dismiss() {
                            if (pickerFragment1.getTime() != null)
                                time[0] = pickerFragment1.getTime();
                            else time[0] = "";
                            TimePickerFragment pickerFragment2 = showTimePickerFragment(OPEN_TIME,
                                    "timePicker2");
                            pickerFragment2.setRetainInstance(true);
                            pickerFragment2.onDismiss(new DialogInterface() {
                                @Override
                                public void cancel() {

                                }

                                @Override
                                public void dismiss() {
                                    if (pickerFragment2.getTime() != null)
                                        time[1] = pickerFragment2.getTime();
                                    else time[1] = "";

                                    switch(num){
                                        case 1:
                                            Log.e(TAG, "monday startTime: " + time[0] + " ------- " + "endTime: " + time[1]);
                                            mMondayAvailStartTime = time[0];
                                            mMondayAvailEndTime = time[1];
                                            break;
                                        case 2:
                                            Log.e(TAG, "tuesday startTime: " + time[0] + " ------- " + "endTime: " + time[1]);
                                            mTuesdayAvailStartTime = time[0];
                                            mTuesdayAvailEndTime = time[1];
                                            break;
                                        case 3:
                                            Log.e(TAG, "signup wednesday CLICKED!!!");
                                            Log.e(TAG, "wednesday startTime: " + time[0] + " ------- " + "endTime: " + time[1]);
                                            mWednessdayAvailStartTime = time[0];
                                            mWednessdayAvailEndTime = time[1];
                                            break;
                                        case 4:
                                            Log.e(TAG, "signup thursday CLICKED!!!");
                                            Log.e(TAG, "thursday startTime: " + time[0] + " ------- " + "endTime: " + time[1]);
                                            mThurdayAvailStartTime = time[0];
                                            mThurdayAvailEndTime = time[1];
                                            break;
                                        case 5:
                                            Log.e(TAG, "friday startTime: " + time[0] + " ------- " + "endTime: " + time[1]);
                                            mFridayAvailStartTime = time[0];
                                            mFridayAvailEndTime = time[1];
                                            break;
                                        case 6:
                                            Log.e(TAG, "saturday startTime: " + time[0] + " ------- " + "endTime: " + time[1]);
                                            mSaturdayAvailStartTime = time[0];
                                            mSaturdayAvailEndTime = time[1];
                                            break;
                                        case 7:
                                            Log.e(TAG, "sunday startTime: " + time[0] + " ------- " + "endTime: " + time[1]);
                                            mSundayAvailStartTime = time[0];
                                            mSundayAvailEndTime = time[1];
                                            break;
                                    }
                                }
                            });
                        }
                    });

        //}
        return time;
    }

    public interface Time {
        String getTime();
    }

}
