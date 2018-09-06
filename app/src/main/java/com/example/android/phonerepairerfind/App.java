package com.example.android.phonerepairerfind;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.android.phonerepairerfind.Activities.HomeActivity;
import com.example.android.phonerepairerfind.Activities.SignInActivity;
import com.example.android.phonerepairerfind.Activities.SignupActivity;
import com.example.android.phonerepairerfind.Constants.AdaptersConstant;
import com.example.android.phonerepairerfind.Constants.FirebaseConstants;
import com.example.android.phonerepairerfind.Constants.IntentConstants;
import com.example.android.phonerepairerfind.POJO.UserProfile;
import com.example.android.phonerepairerfind.anim.CustomProgressBar;
import com.example.android.phonerepairerfind.utilities.DatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class App extends Application {

    public static boolean sIsUserDetailsNull;
    private static FirebaseAuth sFirebaseAuth;
    private static FirebaseDatabase sFirebaseDatabase;
    private static DatabaseReference sShopsDataRef;
    private static DatabaseReference sNormalDataRef;
    private static DatabaseReference sRepairsDataRef;
    private static DatabaseReference sContactDataRef;
    private static DatabaseReference sChatDataRef;
    private static DatabaseReference sCommentDataRef;
    private static DatabaseReference sUserDetailDataRef;
    private static DatabaseReference sUsersDataRef;
    private static DatabaseReference sAllUsersIdRef;
    private static DatabaseReference sAccountTypeDataRef;
    private static DatabaseReference sUserIdDataRef;
    private static DatabaseReference sAccountMemberDataRef;
    private static DatabaseReference sUserTipsDataRef;
    private static DatabaseReference sTipsDataRef;
    //    private static DatabaseReference sPublicTipsDataRef;
    private static String mStaticPushIdRef;
    private static FirebaseUser sCurrentUser;
    private static UserProfile sUserProfile;
    private static StorageReference sStorageReference;
    private static StorageReference sProfilePicsStorageRef;
    private static StorageReference sUsersStorageRef;
    private static StorageReference sUsersTipsStorageRef;
    private static StorageReference sChatImageStorageRef;
    private static String profilePicsUrl;

    public static  void initializeMajorNodes(){
        sUsersDataRef = sFirebaseDatabase.getReference().child(FirebaseConstants.USERS_NODE);
        sAllUsersIdRef = sFirebaseDatabase.getReference().child(FirebaseConstants.ALL_USERS_ID_NODE);
        sAccountTypeDataRef = sFirebaseDatabase.getReference().child(FirebaseConstants.ACCOUNT_TYPE_NODE);
        sNormalDataRef = sAccountTypeDataRef.child(FirebaseConstants.NORMAL_USERS_NODE);
        sRepairsDataRef = sAccountTypeDataRef.child(FirebaseConstants.REPAIRS_USERS_NODE);
        sShopsDataRef = sAccountTypeDataRef.child(FirebaseConstants.SHOPS_USERS_NODE);
        sTipsDataRef = sFirebaseDatabase.getReference().child(FirebaseConstants.TIPS_NODE);
    }

    public static void setUpUserDetail(String uid) {
        //currentUserId = user.getUid();
        initializeMajorNodes();

        if (uid != null) {
            sProfilePicsStorageRef = sStorageReference
                    .child(uid)
                    .child(FirebaseConstants.STORAGE_DATABASE_PROFILE_PICS_NODE);
            sUsersStorageRef = sStorageReference
                    .child(uid);
            sUsersTipsStorageRef = sUsersStorageRef
                    .child(FirebaseConstants.TIPS_NODE);
            sChatImageStorageRef = sUsersStorageRef
                    .child(FirebaseConstants.CHATS_NODE);
            sUserIdDataRef = sUsersDataRef.child(uid);
            sUserDetailDataRef = sUserIdDataRef.child(FirebaseConstants.USER_DETAILS_NODE);
            sContactDataRef = sUsersDataRef.child(uid).child(FirebaseConstants.CONTACTS_NODE);
            sChatDataRef = sUsersDataRef.child(uid).child(FirebaseConstants.CHATS_NODE);
            sCommentDataRef = sUsersDataRef.child(uid).child(FirebaseConstants.COMMENTS_NODE);
            sUserTipsDataRef = sTipsDataRef.child(FirebaseConstants.TIPS_UPLOAD_BY_NODE).child(uid);
//            sAllUsersIdRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    sIsUserDetailsNull = dataSnapshot.hasChild(uid);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
            //sPublicTipsDataRef =

            sUserDetailDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    sUserProfile = (UserProfile) dataSnapshot.getValue(UserProfile.class);

                    if (sUserProfile != null) {
                        //TODO: uncomment this line of code after test
                        //sUserProfile.getAccountType().
                        boolean normal = false, repairs = false, shop = false;
                        try {
                            normal = sUserProfile.getAccountType().get(FirebaseConstants.NORMAL_USERS_NODE);
                        } catch (NullPointerException e) {
                        }
                        try {
                            repairs = sUserProfile.getAccountType().get(FirebaseConstants.REPAIRS_USERS_NODE);
                        } catch (NullPointerException e) {
                        }
                        try {
                            shop = sUserProfile.getAccountType().get(FirebaseConstants.SHOPS_USERS_NODE);
                        } catch (NullPointerException e) {
                        }

                        if (normal) {
                            sAccountMemberDataRef = sNormalDataRef.child(FirebaseConstants.ACCOUNT_TYPE_MEMBERS_NODE);
                        } else if (repairs) {
                            sAccountMemberDataRef = sRepairsDataRef.child(FirebaseConstants.ACCOUNT_TYPE_MEMBERS_NODE);
                        } else if (shop) {
                            sAccountMemberDataRef = sShopsDataRef.child(FirebaseConstants.ACCOUNT_TYPE_MEMBERS_NODE);
                        } else {
                            return;
                        }
                        //sAccountMemberDataRef = sShopsDataRef.child(FirebaseConstants.ACCOUNT_TYPE_MEMBERS_NODE);
                    }
//                    sFirebaseAuth.signOut();
//                    sCurrentUser = null;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    sFirebaseAuth.signOut();
                    sCurrentUser = null;
                }
            });
//        accountMemberDataRef = accountTypeDataRef.child(FirebaseConstants.ACCOUNT_TYPE_MEMBERS_NODE);
        }
    }

    public static void setUserProfile(CustomProgressBar customProgressBar, Activity activity,
                                      String uid, UserProfile userProfile,
                                      int userType, Uri photoPath, String passWord) {
        sAllUsersIdRef = sFirebaseDatabase.getReference().child(FirebaseConstants.ALL_USERS_ID_NODE);
        sUserIdDataRef = sUsersDataRef.child(uid);
        sUserDetailDataRef = sUserIdDataRef.child(FirebaseConstants.USER_DETAILS_NODE);
        Map<String, Object> map = new HashMap<>();
        map.put(uid, true);
        sAllUsersIdRef.updateChildren(map);
        sProfilePicsStorageRef = sStorageReference
                .child(uid)
                .child(FirebaseConstants.STORAGE_DATABASE_PROFILE_PICS_NODE);
        sUsersStorageRef = sStorageReference
                .child(uid);
        sUsersTipsStorageRef = sUsersStorageRef
                .child(FirebaseConstants.TIPS_NODE);
        sChatImageStorageRef = sUsersStorageRef
                .child(FirebaseConstants.CHATS_NODE);

        sContactDataRef = sUserIdDataRef.child(FirebaseConstants.CONTACTS_NODE);
        sChatDataRef = sUserIdDataRef.child(FirebaseConstants.CHATS_NODE);
        sCommentDataRef = sUsersDataRef.child(uid).child(FirebaseConstants.COMMENTS_NODE);
        sTipsDataRef = sFirebaseDatabase.getReference().child(FirebaseConstants.TIPS_NODE);
        sUserTipsDataRef = sTipsDataRef.child(FirebaseConstants.TIPS_UPLOAD_BY_NODE).child(uid);


        DatabaseHelper.setActivity(activity);
        Task<Uri> uploadTask = DatabaseHelper.uploadProfilePic(photoPath, userProfile);
        uploadTask
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            profilePicsUrl = task.getResult().toString();
                            userProfile.setmImageUrl(task.getResult().toString());

                            //if (profilePicsUrl != null && !profilePicsUrl.isEmpty()) {
                            userProfile.setmImageUrl(profilePicsUrl);
                            userProfile.setmPushId(uid);
                            sUserDetailDataRef.setValue(userProfile)
                                    .addOnSuccessListener(activity, new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Map<String, Object> memberMap = new HashMap<>();
                                            if (userType == AdaptersConstant.SIGNUP_NORMAL_USERS_VALUES) {
                                                memberMap.put(uid, true);
                                                sNormalDataRef.updateChildren(memberMap);

                                            } else if (userType == AdaptersConstant.SIGNUP_REPAIRERS_USERS_VALUES) {
                                                memberMap.put(uid, true);
                                                sRepairsDataRef.updateChildren(memberMap);

                                            } else if (userType == AdaptersConstant.SIGNUP_SHOPS_USERS_VALUES) {
                                                memberMap.put(uid, true);
                                                sShopsDataRef.updateChildren(memberMap);
                                            }

                                            activity.startActivity(new Intent(activity, HomeActivity.class)
                                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                            activity.finish();
                                            //Toast.makeText(activity, "Done", Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(activity, String.valueOf(e), Toast.LENGTH_SHORT).show();
                                            //success = false;
                                            //sCurrentUser.delete();
                                            deleteUser(userProfile.getmEmail(), passWord);
                                            customProgressBar.stopLoading();
                                            //firebaseUser.reload();
                                        }
                                    });
                        } else {
                            customProgressBar.stopLoading();
                            profilePicsUrl = null;
                        }
                    }
                });
    }

    public static void deleteUser(String password, String email) {
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);

        // Prompt the user to re-provide their sign-in credentials
        sCurrentUser.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        sCurrentUser.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("App", "User account deleted.");
                                        }
                                    }
                                });

                    }
                });
    }

    public static void signOut() {
        sFirebaseAuth.signOut();
        sCurrentUser = null;
//        if(sFirebaseAuth.getCurrentUser() != null)
//        sFirebaseAuth.updateCurrentUser(sFirebaseAuth.getCurrentUser());
    }

    public static FirebaseAuth getsFirebaseAuth() {
        return sFirebaseAuth;
    }

    public static FirebaseDatabase getsFirebaseDatabase() {
        return sFirebaseDatabase;
    }

    public static DatabaseReference getsShopsDataRef() {
        return sShopsDataRef;
    }

    public static DatabaseReference getsNormalDataRef() {
        return sNormalDataRef;
    }

    public static DatabaseReference getsRepairsDataRef() {
        return sRepairsDataRef;
    }

    public static DatabaseReference getsContactDataRef() {
        return sContactDataRef;
    }

    public static DatabaseReference getsChatDataRef() {
        return sChatDataRef;
    }

    public static DatabaseReference getsCommentDataRef() {
        return sCommentDataRef;
    }

    public static DatabaseReference getsUserDetailDataRef() {
        return sUserDetailDataRef;
    }

    public static DatabaseReference getsUsersDataRef() {
        return sUsersDataRef;
    }

    public static DatabaseReference getsAccountTypeDataRef() {
        return sAccountTypeDataRef;
    }

    public static DatabaseReference getsUserIdDataRef() {
        return sUserIdDataRef;
    }

    public static DatabaseReference getsAccountMemberDataRef() {
        return sAccountMemberDataRef;
    }

    public static String getmStaticPushIdRef() {
        return mStaticPushIdRef;
    }

    public static FirebaseUser getsCurrentUser() {
        return sCurrentUser;
    }

    public static UserProfile getsUserProfile() {
        return sUserProfile;
    }

    public static StorageReference getsStorageReference() {
        return sStorageReference;
    }

    public static StorageReference getsProfilePicsStorageRef() {
        return sProfilePicsStorageRef;
    }

    public static DatabaseReference getsUserTipsDataRef() {
        return sUserTipsDataRef;
    }

    public static DatabaseReference getsTipsDataRef() {
        return sTipsDataRef;
    }

    public static StorageReference getsUsersStorageRef() {
        return sUsersStorageRef;
    }

    public static StorageReference getsUsersTipsStorageRef() {
        return sUsersTipsStorageRef;
    }

    public static StorageReference getsChatImageStorageRef() {
        return sChatImageStorageRef;
    }

    public static DatabaseReference getsAllUsersIdRef() {
        return sAllUsersIdRef;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (FirebaseApp.getInstance() != null) {
            FirebaseApp.initializeApp(this);
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//            FirebaseStorage.getInstance().getReference();
            sFirebaseDatabase = FirebaseDatabase.getInstance();

            sFirebaseAuth = FirebaseAuth.getInstance();
            sStorageReference = FirebaseStorage.getInstance().getReference();
            sCurrentUser = sFirebaseAuth.getCurrentUser();

            //sProfilePicsStorageRef = sStorageReference.child("Users_pics");
            sUsersDataRef = sFirebaseDatabase.getReference().child(FirebaseConstants.USERS_NODE);
            sAccountTypeDataRef = sFirebaseDatabase.getReference().child(FirebaseConstants.ACCOUNT_TYPE_NODE);

            sNormalDataRef = sAccountTypeDataRef.child(FirebaseConstants.NORMAL_USERS_NODE);
            sRepairsDataRef = sAccountTypeDataRef.child(FirebaseConstants.REPAIRS_USERS_NODE);
            sShopsDataRef = sAccountTypeDataRef.child(FirebaseConstants.SHOPS_USERS_NODE);

            /**
             * The line below checks if user is currently signed in if yes
             * check if user details is stored if its stored open the homeactivity
             * if not direct user to signUpActivity to complete registration passing in an intent bundle
             * to indicate user has not completed registration so that SignUpChoiceDialog an be shown
             * if user is not currently signedin direct user to the signInActivity to signin
             * (**Note due to network error a user can be registered but his details not in the node)
             *
             */
            if (sCurrentUser != null) {
                //sUsersNodeDataRef = sRootDatabase.getReference().child(sFirebaseUser.getUid());
                Log.e("App", "sUserDetailDataRef NOT NULL");
                initializeMajorNodes();
                sAllUsersIdRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // = dataSnapshot.hasChild(uid);
                        if(dataSnapshot.getValue() == null){
                            Log.e("App", "Am here oooh......... sUserDetailDataRef IS NULL");
                            startActivity(new Intent(getBaseContext(), SignInActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        }else{
                            if(dataSnapshot.hasChild(sCurrentUser.getUid())){
                                Log.e("App", "sUserDetailDataRef NOT NULL");
                                setUpUserDetail(sCurrentUser.getUid());
                                startActivity(new Intent(getBaseContext(), HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            }else{
                                Intent signin = new Intent(getBaseContext(), SignInActivity.class);
                                signin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                Intent signUp = new Intent(getBaseContext(), SignupActivity.class);
                                signUp.putExtra(IntentConstants.SIGNUP_NOT_COMPLETED_KEY,
                                        IntentConstants.SIGNUP_NOT_COMPLETED_VAL);
                                Intent[] intents = {signin, signUp};
                                startActivities(intents);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            if (sCurrentUser == null) {
                startActivity(new Intent(this, SignInActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }

            sFirebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    //sFirebaseAuth = sFirebaseAuth;
                    if (firebaseAuth.getCurrentUser() != null) {
                        //Log.e("onAUthStateChange", "^^^^^^^^^^^^ getCUrrentUser IS NOT NUlL ^^^^^^^^^^");
                        firebaseAuth.updateCurrentUser(firebaseAuth.getCurrentUser());
                        sCurrentUser = firebaseAuth.getCurrentUser();
                        if(App.getsUserIdDataRef() != null) {
                            App.getsUserIdDataRef()
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.hasChild(sCurrentUser.getUid())) {
                                                setUpUserDetail(firebaseAuth.getCurrentUser().getUid());
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                        }
                    } else {
                        //Log.e("onAUthStateChange", "^^^^^^^^^^^^ getCUrrentUser IS NUlL ^^^^^^^^^^");
                    }
                }
            });

        } else {
            startActivity(new Intent(this, SignInActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }
    }
}
