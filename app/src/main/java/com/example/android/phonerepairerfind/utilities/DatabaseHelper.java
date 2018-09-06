package com.example.android.phonerepairerfind.utilities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.phonerepairerfind.Activities.HomeActivity;
import com.example.android.phonerepairerfind.App;
import com.example.android.phonerepairerfind.Constants.IntentConstants;
import com.example.android.phonerepairerfind.POJO.Chat;
import com.example.android.phonerepairerfind.POJO.Contact;
import com.example.android.phonerepairerfind.POJO.UserProfile;
import com.example.android.phonerepairerfind.anim.CustomProgressBar;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DatabaseHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static Activity sActivity;
    private static String profilePicsUrl = null;

    public static void setActivity(Activity activity) {
        sActivity = activity;
    }

    public static Task<Uri> uploadTipsImages(String filePath, String tipPushId, String name) {
        Task<Uri> uriTask = null;
        if (filePath != null) {
            Log.e(TAG, "Uri: --- " + Uri.parse(filePath).toString() + "---------------");
            UploadTask uploadTask =
                    App.getsUsersTipsStorageRef()
                            .child(tipPushId)
                            .child(name)
                            .putFile(Uri.parse(filePath));
            uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return App.getsUsersTipsStorageRef()
                            .child(tipPushId)
                            .child(name)
                            .getDownloadUrl();
                }
            });
            return uriTask;
        }
        return uriTask;
    }

    public static void deleteChats(List<Chat> chats, String contactPushId) {
        for (Chat chat : chats) {

            App.getsChatDataRef()
                    .child(contactPushId)
                    .child(chat.getmChatPushId())
                    .setValue(null);
        }
    }

    public static void deleteContact(List<Contact> contacts) {
        //List<String> contactUids = deleteContactsUids(contacts, adapters, recyclerView, selectedItems);
        //deleteContactsUids(contacts, adapters, recyclerView, selectedItems);
        for (Contact contact : contacts) {
            App.getsContactDataRef()
                    .child(contact.getmPushId()).setValue(null);
            App.getsChatDataRef()
                    .child(contact.getmPushId())
                    .setValue(null);
        }
    }

    public static Task<Uri> uploadChatImage(Uri filePath, String chatPushId) {
        Task<Uri> uriTask = null;
        if (filePath != null) {
            UploadTask uploadTask =
                    App.getsChatImageStorageRef()
                            .putFile(filePath);
            uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return App.getsChatImageStorageRef()
                            .getDownloadUrl();
                }
            });
            return uriTask;
        }
        return uriTask;
    }

    public static Task<Uri> uploadProfilePic(Uri filePath, UserProfile userProfile) {
        Task<Uri> uriTask = null;
        if (filePath != null) {
            UploadTask uploadTask =
                    App.getsProfilePicsStorageRef()
                            .putFile(filePath);
            uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return App.getsProfilePicsStorageRef()
                            .getDownloadUrl();
                }
            });
            return uriTask;
        }
        return uriTask;
    }

    public static String getProfilePicsUrl() {
        return profilePicsUrl;
    }

    public static void setProfilePicsUrl(String profilePicsUrl) {
        DatabaseHelper.profilePicsUrl = profilePicsUrl;
    }

    /**
     * This method takes care of adding users to the database
     * @param v Fab to disable button when creating and enable it if failure occured
     * @param customProgressBarAnime progress bar to indicate progress
     * @param email email of user to create
     * @param password password of user
     * @param userType constant to determine the type of user (either normal, phone, or repair)
     * @param imageUriPath the url path that contains the userProfileImage
     * @param userProfile the instance of UserProfile to insert in database
     * @param cordLay instance of the coordinatorLayout to pass to snackbar
     * @param activity the activity class
     */
    public static void createAccountWithEmailPassword(View v, CustomProgressBar customProgressBarAnime,
                                                      String email, String password, int userType,
                                                      Uri imageUriPath, UserProfile userProfile,
                                                      CoordinatorLayout cordLay, Activity activity) {
        if (imageUriPath != null) {
            ((FloatingActionButton)v).setEnabled(false);
            App.getsFirebaseAuth().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                ((FloatingActionButton)v).setEnabled(true);
                                DatabaseReference db = App.getsUsersDataRef();
//
                                if (App.getsCurrentUser() != null) {
                                    //Toast.makeText(sActivity, "Current user Not null", Toast.LENGTH_SHORT).show();
                                    //String uploadUrl =
                                        App.setUserProfile(customProgressBarAnime, sActivity, App.getsCurrentUser().getUid(),
                                                userProfile, userType, imageUriPath, password);
                                    //Log.e("DatabaseHelper", "App.setUserProfile ________________________" + uploadUrl);
                                        //Toast.makeText(sActivity, "An Error Occurred, Try again", Toast.LENGTH_SHORT).show();
                                } else;
                                    //Toast.makeText(sActivity, "Current user Is null", Toast.LENGTH_SHORT).show();

                            } else {
                                ((FloatingActionButton)v).setEnabled(true);
                                //Toast.makeText(sActivity, "Current user Is null", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(sActivity, "An Error Occurred, Try again", Toast.LENGTH_SHORT).show();
                                customProgressBarAnime.stopLoading();
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidCredentialsException eI) {
                                    Snackbar.make(cordLay, "Invalid Credentials",
                                            Snackbar.LENGTH_SHORT).show();

                                } catch (FirebaseAuthUserCollisionException eU) {
                                    Snackbar.make(cordLay, "This account is already " +
                                                    "registered",
                                            Snackbar.LENGTH_SHORT).show();
                                    if(App.getsFirebaseAuth().getCurrentUser() == null) {
                                        App.getsFirebaseAuth().signInWithEmailAndPassword(email, password);
                                        Log.e(TAG, "******** SignedIn ************");
                                    }
                                    if(App.getsAllUsersIdRef() != null){
//                                        if(App.getsFirebaseAuth().getCurrentUser() == null)
//                                            App.getsFirebaseAuth().signInWithEmailAndPassword(email, password);
                                        App
                                                .getsAllUsersIdRef()
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        //if(App.getsCurrentUser().getUid() != null)
                                                        if(!dataSnapshot.hasChild(App.getsCurrentUser().getUid())){
                                                            //String uploadUrl =
                                                            Log.e(TAG, "onDataChange!!.. User key does not exist!!!!!");
                                                            App.setUserProfile(customProgressBarAnime, sActivity, App.getsCurrentUser().getUid(),
                                                                    userProfile, userType, imageUriPath, password);
//                                                        if (uploadUrl == null || uploadUrl.isEmpty()) {
//                                                            customProgressBarAnime.stopLoading();
//                                                        }
                                                        }else {
                                                            Log.e(TAG, "onDataChange!!.. Datasnapshot contains User key !!!!!");
                                                            activity.startActivity(new Intent(activity, HomeActivity.class)
                                                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                                            activity.finish();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                    }else{
                                        Log.e(TAG, "App.getAllUsersRef IS NULL --- !!!!!");
                                        App.setUserProfile(customProgressBarAnime, sActivity, App.getsCurrentUser().getUid(),
                                                userProfile, userType, imageUriPath, password);
                                    }

                                } catch (FirebaseAuthEmailException eE) {
                                    //if()
                                    Snackbar.make(cordLay, "Check your email if its a valid email",
                                            Snackbar.LENGTH_SHORT).show();
                                } catch (FirebaseNetworkException e) {
                                    Snackbar.make(cordLay, "Check your internet connection and " +
                                                    "try again",
                                            Snackbar.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Snackbar.make(cordLay, "Signup failed, Try again",
                                            Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //TODO: Add a snask bar instead of a toast to display
                            Log.e("SignupFailure", "^^^^^^^^^^^^^^ Exception:: " + e.getMessage() + "^^^^^^^^^");
                            customProgressBarAnime.stopLoading();
                        }
                    });
        } else {
            customProgressBarAnime.stopLoading();
            Toast.makeText(sActivity, "Insert an Image", Toast.LENGTH_SHORT).show();
            ImagePicker imagePicker = new ImagePicker(sActivity,
                    IntentConstants.PROFILE_PICS_PICK_IMAGE_REQUEST);
            imagePicker.chooseFileIntent();
        }
    }

    public static String getDateFromTimeStamp(Long timeStamp) {
        Date date = new Date(timeStamp);
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yy");
        return dt.format(date);
    }

    public static String getTimeFromTimeStamp(Long timeStamp) {
        Date time = new Date(timeStamp);
        SimpleDateFormat tm = new SimpleDateFormat("HH:mm");
        return tm.format(time);
    }

    public static String getDateDayFromTimeStamp(Long timeStamp) {
        Date date = new Date(timeStamp);
        return null;
    }
}
