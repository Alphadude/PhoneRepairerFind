package com.example.android.phonerepairerfind.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.phonerepairerfind.App;
import com.example.android.phonerepairerfind.Constants.IntentConstants;
import com.example.android.phonerepairerfind.Fragment.SignupChoiceDialogFragment;
import com.example.android.phonerepairerfind.R;
import com.example.android.phonerepairerfind.anim.CustomProgressBar;
import com.example.android.phonerepairerfind.utilities.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private final String EMAIL_NOT_VALID = "Enter a valid email";
    private final String PASSWORD_LESS = "Password must be greater than six characters";
    private final String TAG = SignInActivity.class.getSimpleName();
    private TextInputLayout mEmailL, mPasswordL;
    private EditText mEmailE, mPasswordE;
    private String mEmail, mPassword;
    private FloatingActionButton signinWithEmailPasswordFab, signinWIthFacebookFab,
            signinWithGoogleFab, signInFab;
    private TextView signup;
    private FirebaseAuth mAuth;
    private ImageView customProgressBar;
    private boolean isFabOpen = true;
    private Animation fab_open;
    private Animation fab_close;
    private Animation fab_icon_rotate_forward;
    private Animation fab_icon_rotate_backward;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_sign_in);
        fab_open = AnimationUtils.loadAnimation(this, R.anim.anime_fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.anime_fab_close);
        fab_icon_rotate_forward = AnimationUtils.loadAnimation(this, R.anim.anime_fab_rotate_forward);
        fab_icon_rotate_backward = AnimationUtils.loadAnimation(this, R.anim.anime_fab_rotate_backward);
        initViews();
        signup = (TextView) findViewById(R.id.signin_signup_link);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignUpChoiceFragment();
            }
        });

    }

    private void showSignUpChoiceFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment prevFrag = getSupportFragmentManager().findFragmentByTag("SignupChoiceDialog");
        if (prevFrag != null) {
            fragmentTransaction.remove(prevFrag);
        }
        fragmentTransaction.addToBackStack("SignupChoiceDialog");
        SignupChoiceDialogFragment dialogFragment = new SignupChoiceDialogFragment();
        dialogFragment.show(fragmentTransaction, "SignupChoiceDialog");
    }

    private void initViews() {
        mEmailL = (TextInputLayout) findViewById(R.id.signin_name_textinput_layout);
        mPasswordL = (TextInputLayout) findViewById(R.id.signin_password_textinput_layout);
        mEmailE = (EditText) findViewById(R.id.signin_name_edittext);
        mPasswordE = (EditText) findViewById(R.id.signin_password_edittext);
        customProgressBar = (ImageView) findViewById(R.id.custom_progress_bar_icon);
        signInFab = (FloatingActionButton) findViewById(R.id.signin_more_fab);
        signinWithEmailPasswordFab = (FloatingActionButton) findViewById(R.id.signin_with_email_pasword_fab);
        signinWithGoogleFab = (FloatingActionButton) findViewById(R.id.signin_with_google_fab);
        signinWIthFacebookFab = (FloatingActionButton) findViewById(R.id.signin_with_facebook_fab);
        signInFab.setOnClickListener(this);
        signinWithEmailPasswordFab.setOnClickListener(this);
        signinWithGoogleFab.setOnClickListener(this);
        signinWIthFacebookFab.setOnClickListener(this);

        signInFab.startAnimation(fab_icon_rotate_forward);
        signinWithEmailPasswordFab.startAnimation(fab_open);
        signinWIthFacebookFab.startAnimation(fab_open);
        signinWithGoogleFab.startAnimation(fab_open);
        signinWithEmailPasswordFab.setClickable(true);
        signinWIthFacebookFab.setClickable(true);
        signinWithGoogleFab.setClickable(true);
        signinWithEmailPasswordFab.setVisibility(View.VISIBLE);
        signinWIthFacebookFab.setVisibility(View.VISIBLE);
        signinWithGoogleFab.setVisibility(View.VISIBLE);
        isFabOpen = true;
    }

    private String getTextFromEditText(EditText editText) {
        return editText.getText().toString().trim();
    }

    private void setAuthError() {
        mEmailL.setError(null);
        mPasswordL.setError(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signin_more_fab:
                animateFab();
                break;
            case R.id.signin_with_email_pasword_fab:
                setSigninWithEmailPassword(view);
                break;
        }
    }

    private void animateFab() {
        if (isFabOpen) {
            signInFab.startAnimation(fab_icon_rotate_backward);

            signinWithEmailPasswordFab.startAnimation(fab_close);
            signinWIthFacebookFab.startAnimation(fab_close);
            signinWithGoogleFab.startAnimation(fab_close);
            signinWithEmailPasswordFab.setClickable(false);
            signinWIthFacebookFab.setClickable(false);
            signinWithGoogleFab.setClickable(false);
            signinWithEmailPasswordFab.setVisibility(View.INVISIBLE);
            signinWIthFacebookFab.setVisibility(View.INVISIBLE);
            signinWithGoogleFab.setVisibility(View.INVISIBLE);
            isFabOpen = false;
        } else {
            signInFab.startAnimation(fab_icon_rotate_forward);
            signinWithEmailPasswordFab.startAnimation(fab_open);
            signinWIthFacebookFab.startAnimation(fab_open);
            signinWithGoogleFab.startAnimation(fab_open);
            signinWithEmailPasswordFab.setClickable(true);
            signinWIthFacebookFab.setClickable(true);
            signinWithGoogleFab.setClickable(true);
            signinWithEmailPasswordFab.setVisibility(View.VISIBLE);
            signinWIthFacebookFab.setVisibility(View.VISIBLE);
            signinWithGoogleFab.setVisibility(View.VISIBLE);
            isFabOpen = true;
        }
    }

    private void setSigninWithEmailPassword(View view) {
        CustomProgressBar customProgressBarAnime = new CustomProgressBar(customProgressBar, SignInActivity.this);
        ((FloatingActionButton) view).setEnabled(false);
        customProgressBarAnime.startLoading();
        setAuthError();
        mEmail = getTextFromEditText(mEmailE);
        mPassword = getTextFromEditText(mPasswordE);
        int errorCount = 0;
        if (!Validation.validateEmail(mEmail)) {
            mEmailL.setError(EMAIL_NOT_VALID);
            errorCount++;
        }
        if (mPassword.length() < 6) {
            mPasswordL.setError(PASSWORD_LESS);
            errorCount++;
        }
        if (errorCount != 0) {
            ((FloatingActionButton) view).setEnabled(true);
            customProgressBarAnime.stopLoading();
        }
        if (errorCount == 0) {
            customProgressBarAnime.startLoading();
            mAuth.signInWithEmailAndPassword(mEmail, mPassword)
                    //.addOnSuccessListener(SignInActivity.this, new O)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                customProgressBarAnime.stopLoading();
//                                mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
//                                    @Override
//                                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                                        FirebaseUser firebaseUser = App.getsCurrentUser();
//                                        if (firebaseUser != null)
//                                            firebaseAuth.updateCurrentUser(firebaseUser);
//                                        //FirebaseInitUser.setUpFirebaseReferences(FirebaseStorage.getInstance().getReference());
//                                    }
//                                });
                                //FirebaseInitUser.setUpFirebaseReferences(FirebaseStorage.getInstance().getReference());
                                if(App.getsUserDetailDataRef() != null) {
                                    App.getsAllUsersIdRef()
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (!dataSnapshot.hasChild(App.getsCurrentUser().getUid())) {
                                                        Toast.makeText(SignInActivity.this, "SignUp not complete!",
                                                                Toast.LENGTH_SHORT).show();
                                                        //showSignUpChoiceFragment();
                                                        Log.e(TAG, "OnDataChange App.getUserDetailRef IS NULL OOH");
                                                        Intent intent = new Intent(SignInActivity.this, SignupActivity.class)
                                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        intent.putExtra(IntentConstants.SIGNUP_NOT_COMPLETED_KEY,
                                                                IntentConstants.SIGNUP_NOT_COMPLETED_VAL);
                                                        startActivity(intent);
                                                        //finish();
                                                    } else {
                                                        Log.e(TAG, "App.getUserDetailRef IS NOT OOH");
                                                        startActivity(new Intent(SignInActivity.this, HomeActivity.class)
                                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                }else {
                                    Log.e(TAG, "App.getUserDetailRef IS NULL OOH");
                                    Intent intent = new Intent(SignInActivity.this, SignupActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra(IntentConstants.SIGNUP_NOT_COMPLETED_KEY,
                                            IntentConstants.SIGNUP_NOT_COMPLETED_VAL);
                                    startActivity(intent);
                                }
//                                if (App.getsUserDetailDataRef() == null) {
//                                    Toast.makeText(SignInActivity.this, "SignUp not complete!",
//                                            Toast.LENGTH_SHORT).show();
//                                    //showSignUpChoiceFragment();
//                                    Log.e(TAG, "App.getUserDetailRef IS NULL OOH");
//                                    Intent intent = new Intent(view.getContext(), SignupActivity.class)
//                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    intent.putExtra(IntentConstants.SIGNUP_NOT_COMPLETED_KEY,
//                                            IntentConstants.SIGNUP_NOT_COMPLETED_VAL);
//                                    startActivity(intent);
//                                    //finish();
//                                }
//                                else {
//                                    Log.e(TAG, "App.getUserDetailRef IS NOT OOH");
//                                    startActivity(new Intent(view.getContext(), HomeActivity.class)
//                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                //}
                            } else {
                                customProgressBarAnime.stopLoading();
                                ((FloatingActionButton) view).setEnabled(true);
                                Toast.makeText(SignInActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    customProgressBarAnime.stopLoading();
                    ((FloatingActionButton) view).setEnabled(true);
                }
            });
            errorCount = 0;
        }
    }
}
