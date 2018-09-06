//package com.example.android.phonerepairerfind.utilities;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.net.Uri;
//import android.support.annotation.NonNull;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.example.android.phonerepairerfind.Activities.HomeActivity;
//import com.example.android.phonerepairerfind.Constants.AdaptersConstant;
//import com.example.android.phonerepairerfind.Constants.FirebaseConstants;
//import com.example.android.phonerepairerfind.POJO.UserProfile;
//import com.google.android.gms.tasks.Continuation;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//public class FirebaseInitUser {
//
//    private static DatabaseReference shopsDataRef;
//    private static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//    private static DatabaseReference normalDataRef;
//    private static DatabaseReference repairsDataRef;
//    private static DatabaseReference contactDataRef;
//    private static DatabaseReference chatDataRef;
//    private static DatabaseReference userDetailDataRef;
//    private static DatabaseReference usersDataRef;
//    private static DatabaseReference accountTypeDataRef;
//    private static DatabaseReference userIdDataRef;
//    private static DatabaseReference accountMemberDataRef;
//    private static String mStaticPushIdRef;
//    private static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//    private static UserProfile userProfile;
//    private static StorageReference storageReference = FirebaseStorage.getInstance().getReference();
//    private static StorageReference profilePicsStorageRef;
//    private Map<String, Boolean> likes;
//    private long likeCount, commentCount;
//    private float rating;
//    //private Map<String, Map<String, Boolean>> accountTypeMap;
//    private Map<String, Boolean> accountTypeMap;
//    private String pushIdRef;
//    private String username;
//    private String email;
//    private String phoneNum;
//    private String location;
//    private String name;
//    private Map<String, Object> availability;
//    private String businessPhoneNum;
//    private String imageUrl;
//    private int userType;
//
////    public FirebaseInitUser(){
////        String currentUserId = "";
////        if(user != null){
////            currentUserId = user.getUid();
////        }
////
////        usersDataRef = firebaseDatabase.getReference().child(FirebaseConstants.USERS_NODE);
////        accountTypeDataRef = firebaseDatabase.getReference().child(FirebaseConstants.ACCOUNT_TYPE_NODE);
////        userDetailDataRef = userIdDataRef.child(currentUserId).child(FirebaseConstants.USER_DETAILS_NODE);
////        normalDataRef = accountTypeDataRef.child(FirebaseConstants.NORMAL_USERS_NODE);
////        repairsDataRef = accountTypeDataRef.child(FirebaseConstants.REPAIRS_USERS_NODE);
////        shopsDataRef = accountTypeDataRef.child(FirebaseConstants.SHOPS_USERS_NODE);
////
////        contactDataRef = usersDataRef.child(currentUserId).child(FirebaseConstants.CONTACTS_NODE);
////        chatDataRef = usersDataRef.child(currentUserId).child(FirebaseConstants.CHATS_NODE);
////        userDetailDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                userProfile = (UserProfile) dataSnapshot.getValue();
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError databaseError) {
////
////            }
////        });
//////        accountMemberDataRef = accountTypeDataRef.child(FirebaseConstants.ACCOUNT_TYPE_MEMBERS_NODE);
////        if(userProfile.getAccountType().get(FirebaseConstants.ACCOUNT_TYPE_NODE)
////                .get(FirebaseConstants.NORMAL_USERS_NODE)){
////            accountMemberDataRef = normalDataRef.child(FirebaseConstants.ACCOUNT_TYPE_MEMBERS_NODE);
////        }else if(userProfile.getAccountType().get(FirebaseConstants.ACCOUNT_TYPE_NODE)
////                .get(FirebaseConstants.REPAIRS_USERS_NODE)){
////            accountMemberDataRef = repairsDataRef.child(FirebaseConstants.ACCOUNT_TYPE_MEMBERS_NODE);
////        }else if(userProfile.getAccountType().get(FirebaseConstants.ACCOUNT_TYPE_NODE)
////                .get(FirebaseConstants.SHOPS_USERS_NODE)){
////            accountMemberDataRef = shopsDataRef.child(FirebaseConstants.ACCOUNT_TYPE_MEMBERS_NODE);
////        }
////    }
////    public FirebaseInitUser(){
////        normalDataRef = firebaseDatabase.getReference().child(FirebaseConstants.NORMAL_USERS_NODE);
////        repairsDataRef = firebaseDatabase.getReference().child(FirebaseConstants.REPAIRS_USERS_NODE);
////        shopsDataRef = firebaseDatabase.getReference().child(FirebaseConstants.SHOPS_USERS_NODE);
////
////        normalDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                if(dataSnapshot.hasChild(user.getUid()));{
////                    userIdDataRef = normalDataRef.child(user.getUid());
////                }
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError databaseError) {
////
////            }
////        });
////
////        repairsDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                if(dataSnapshot.hasChild(user.getUid()));{
////                    userIdDataRef = repairsDataRef.child(user.getUid());
////                }
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError databaseError) {
////
////            }
////        });
////
////        shopsDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                if(dataSnapshot.hasChild(user.getUid()));{
////                    userIdDataRef = shopsDataRef.child(user.getUid());
////                }
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError databaseError) {
////
////            }
////        });
////        //TODO: CHeck if account is a normal user or phone service account
////        //pushIdRef = normalDataRef.child("pushid").toString(); //TODO:use push instead, passing "PushId" is for test purpose only
////        //userIdDataRef = normalDataRef.child(pushIdRef);
////        userDetailDataRef = userIdDataRef.child(FirebaseConstants.USER_DETAILS_NODE);
////        contactDataRef = userIdDataRef.child(FirebaseConstants.CONTACTS_NODE);
////        chatDataRef = userIdDataRef.child(FirebaseConstants.CHATS_NODE);
////    }
//
//
//    /**
//     * For cases where a phone shop have a repair shop too (its a dual purpose shop) then pass in usertype in a list
//     *
//     * @param username
//     * @param email
//     * @param phoneNum
//     * @param location
//     * @param userType
//     * @param pushIdRef
//     */
//    public FirebaseInitUser(String username, String email, String phoneNum, String location,
//                            ArrayList<Integer> userType, String pushIdRef) {
//
//    }
//
//    public FirebaseInitUser(String name, String username, String email, String businessPhoneNum,
//                            String phoneNum, String location, String availability, int userType,
//                            int rating, long likeCount, long commentCount, Map<String, Boolean> likes,
//                            String pushIdRef, Map<String, Boolean> accountTypeMap) {
//        this.username = username;
//        this.email = email;
//        this.phoneNum = phoneNum;
//        this.location = location;
//        this.userType = userType;
//        this.pushIdRef = pushIdRef;
//        this.accountTypeMap = accountTypeMap;
//        this.name = name;
//        this.rating = rating;
//        this.likeCount = likeCount;
//        this.commentCount = commentCount;
//        this.likes = likes;
//        if (businessPhoneNum != null && availability != null) {
//            this.businessPhoneNum = businessPhoneNum;
//            this.availability = availability;
//        }
//
//        usersDataRef = firebaseDatabase.getReference().child(FirebaseConstants.USERS_NODE);
//        accountTypeDataRef = firebaseDatabase.getReference().child(FirebaseConstants.ACCOUNT_TYPE_NODE);
//
//        normalDataRef = accountTypeDataRef.child(FirebaseConstants.NORMAL_USERS_NODE);
//        repairsDataRef = accountTypeDataRef.child(FirebaseConstants.REPAIRS_USERS_NODE);
//        shopsDataRef = accountTypeDataRef.child(FirebaseConstants.SHOPS_USERS_NODE);
//        //TODO: CHeck if account is a normal user or phone service account
////        Map<String, Object> memberMap = new HashMap<>();
////        if(userType == AdaptersConstant.SIGNUP_NORMAL_USERS_VALUES){
////            memberMap.put(pushIdRef, true);
////            normalDataRef.updateChildren(memberMap);
////
////        }else if(userType == AdaptersConstant.SIGNUP_REPAIRERS_USERS_VALUES){
////            memberMap.put(pushIdRef, true);
////            repairsDataRef.updateChildren(memberMap);
////
////        }else if(userType == AdaptersConstant.SIGNUP_SHOPS_USERS_VALUES){
////            memberMap.put(pushIdRef, true);
////            shopsDataRef.updateChildren(memberMap);
////        }
////      else {
////            userIdDataRef = null;
////            userDetailDataRef = null;
////        }
//
//    }
//
//    public static void setUpFirebaseReferences(StorageReference storageReference) {
//        String currentUserId = "";
//        //currentUserId = user.getUid();
//        FirebaseInitUser.storageReference = storageReference;
//        usersDataRef = firebaseDatabase.getReference().child(FirebaseConstants.USERS_NODE);
//        accountTypeDataRef = firebaseDatabase.getReference().child(FirebaseConstants.ACCOUNT_TYPE_NODE);
//        normalDataRef = accountTypeDataRef.child(FirebaseConstants.NORMAL_USERS_NODE);
//        repairsDataRef = accountTypeDataRef.child(FirebaseConstants.REPAIRS_USERS_NODE);
//        shopsDataRef = accountTypeDataRef.child(FirebaseConstants.SHOPS_USERS_NODE);
//
//        if (user != null) {
//            currentUserId = user.getUid();
//            userIdDataRef = usersDataRef.child(currentUserId);
//            userDetailDataRef = userIdDataRef.child(FirebaseConstants.USER_DETAILS_NODE);
//            contactDataRef = usersDataRef.child(currentUserId).child(FirebaseConstants.CONTACTS_NODE);
//            chatDataRef = usersDataRef.child(currentUserId).child(FirebaseConstants.CHATS_NODE);
//            profilePicsStorageRef = storageReference.child(currentUserId);
//
//            userDetailDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    userProfile = (UserProfile) dataSnapshot.getValue(UserProfile.class);
//
//                    if (userProfile != null) {
//                        if (userProfile.getAccountType().get(FirebaseConstants.NORMAL_USERS_NODE)) {
//                            accountMemberDataRef = normalDataRef.child(FirebaseConstants.ACCOUNT_TYPE_MEMBERS_NODE);
//                        } else if (userProfile.getAccountType().get(FirebaseConstants.REPAIRS_USERS_NODE)) {
//                            accountMemberDataRef = repairsDataRef.child(FirebaseConstants.ACCOUNT_TYPE_MEMBERS_NODE);
//                        } else if (userProfile.getAccountType().get(FirebaseConstants.SHOPS_USERS_NODE)) {
//                            accountMemberDataRef = shopsDataRef.child(FirebaseConstants.ACCOUNT_TYPE_MEMBERS_NODE);
//                        }
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
////        accountMemberDataRef = accountTypeDataRef.child(FirebaseConstants.ACCOUNT_TYPE_MEMBERS_NODE);
//        }
//
//    }
//
//    public static DatabaseReference getNormalDataRef() {
////        if (normalDataRef == null) {
////            FirebaseInitUser.setUpFirebaseReferences();
////        }
//        return normalDataRef;
//    }
//
//    public static DatabaseReference getRepairsDataRef() {
////        if (repairsDataRef == null) {
////            FirebaseInitUser.setUpFirebaseReferences();
////        }
//        return repairsDataRef;
//    }
//
//    public static DatabaseReference getShopsDataRef() {
////        if (shopsDataRef == null) {
////            FirebaseInitUser.setUpFirebaseReferences();
////        }
//        return shopsDataRef;
//    }
//
//    public static String currentUserId() {
//        return user.getUid();
//    }
//
//    //TODO: This is obsolete update update it or complete the getUserProfile method
//    public static DatabaseReference getUserDetailDataRef() {
////        if (userDetailDataRef == null) {
////            FirebaseInitUser.setUpFirebaseReferences();
////        }
//        return userDetailDataRef;
//    }
//
//    public static DatabaseReference getContactDataRef() {
////        if (contactDataRef == null) {
////            FirebaseInitUser.setUpFirebaseReferences();
////        }
//        return contactDataRef;
//    }
//
//    public static DatabaseReference getUsersDataRef() {
//        usersDataRef = firebaseDatabase.getReference().child(FirebaseConstants.USERS_NODE);
//        return usersDataRef;
//    }
//
//    public static DatabaseReference getChatDataRef() {
////        if (chatDataRef == null) {
////            FirebaseInitUser.setUpFirebaseReferences();
////        }
//        return chatDataRef;
//    }
//
////    public static StorageReference getProfilePicsStorageRef() {
////        if (profilePicsStorageRef == null) {
////            FirebaseInitUser.setUpFirebaseReferences();
////        }
////        return profilePicsStorageRef;
////    }
//
//    //private static UserProfile userProfile;
//    public static UserProfile getUserProfile() {
//        if (userProfile == null) {
//           //FirebaseInitUser.setUpFirebaseReferences(FirebaseStorage.getInstance().getReference());
//        }
//        return userProfile;
//    }
//
//    public static String getDateFromTimeStamp(Long timeStamp) {
//        Date date = new Date(timeStamp);
//        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yy");
//        return dt.format(date);
//    }
//
//    public static String getTimeFromTimeStamp(Long timeStamp) {
//        Date time = new Date(timeStamp);
//        SimpleDateFormat tm = new SimpleDateFormat("HH:mm");
//        return tm.format(time);
//    }
//
//    public static String getDateDayFromTimeStamp(Long timeStamp) {
//        Date date = new Date(timeStamp);
//        return null;
//    }
//
//    //private boolean success;
//    public void setNewUser(Activity activity, Uri photoPath, String pushIdRef, FirebaseUser firebaseUser) {
//        profilePicsStorageRef = storageReference.child(pushIdRef);
//        userIdDataRef = usersDataRef.child(pushIdRef);
//        userDetailDataRef = userIdDataRef.child(FirebaseConstants.USER_DETAILS_NODE);
//        UserProfile userProfile = new UserProfile(imageUrl, name, username, email, phoneNum, businessPhoneNum,
//                location, pushIdRef, availability, rating, likeCount, commentCount, accountTypeMap, likes);
//
//        int success = uploadProfilePic(activity, photoPath, userProfile);
//        contactDataRef = userIdDataRef.child(FirebaseConstants.CONTACTS_NODE);
//        chatDataRef = userIdDataRef.child(FirebaseConstants.CHATS_NODE);
//
//        if(success != -1) {
//            userDetailDataRef.setValue(userProfile)
//                    .addOnSuccessListener(activity, new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Map<String, Object> memberMap = new HashMap<>();
//                            if (userType == AdaptersConstant.SIGNUP_NORMAL_USERS_VALUES) {
//                                memberMap.put(pushIdRef, true);
//                                normalDataRef.updateChildren(memberMap);
//
//                            } else if (userType == AdaptersConstant.SIGNUP_REPAIRERS_USERS_VALUES) {
//                                memberMap.put(pushIdRef, true);
//                                repairsDataRef.updateChildren(memberMap);
//
//                            } else if (userType == AdaptersConstant.SIGNUP_SHOPS_USERS_VALUES) {
//                                memberMap.put(pushIdRef, true);
//                                shopsDataRef.updateChildren(memberMap);
//                            }
//
//                            activity.finish();
//                            activity.startActivity(new Intent(activity, HomeActivity.class)
//                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
//                            Toast.makeText(activity, "Done", Toast.LENGTH_SHORT).show();
//
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(activity, String.valueOf(e), Toast.LENGTH_SHORT).show();
//                            //success = false;
//                            firebaseUser.delete();
//                            firebaseUser.reload();
//                        }
//                    });
//        }else{
//
//        }
//    }
//
//
//    private int success = 0;
//    private int uploadProfilePic(Activity activity, Uri filePath, UserProfile userProfile) {
//        if(filePath != null){
//            StorageReference ref = profilePicsStorageRef.child(FirebaseConstants
//                    .STORAGE_DATABASE_PROFILE_PICS_NODE + System.currentTimeMillis() + filePath.getLastPathSegment());
//
//                    ref.putFile(filePath)
//                        .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                            @Override
//                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                                if(!task.isSuccessful()){
//                                    throw task.getException();
//                                }
//                                return ref.getDownloadUrl();
//                            }
//                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Uri> task) {
//                            if(task.isSuccessful()){
//                                imageUrl = task.getResult().toString();
//                                userProfile.setmImageUrl(imageUrl);
//                                success = 1;
//                                Log.e("SignUpActivity-imageUrl", "******** -- " + imageUrl + " ---- *******");
//                                Toast.makeText(activity.getApplicationContext(), "Image Saved..", Toast.LENGTH_SHORT).show();
//                            }else{
//                                success = -1;
//                            }
//                        }
//                    });
//////                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
//////                        @Override
//////                        public void onSuccess(Uri uri) {
//////                            imageUrl = uri.toString();
//////                            userProfile.setmImageUrl(imageUrl);
//////                            success = 1;
//////                            Log.e("SignUpActivity-imageUrl", "******** -- " + imageUrl + " ---- *******");
//////                            Toast.makeText(activity.getApplicationContext(), "Image Saved..", Toast.LENGTH_SHORT).show();
//////                        }
//////                    }).addOnFailureListener(new OnFailureListener() {
//////                @Override
//////                public void onFailure(@NonNull Exception e) {
//////                    success = -1;
//////                }
////            });
//        }
//        return success;
//    }
//
//
//    public String getPushIdRef() {
//        return pushIdRef;
//    }
//
//    public void setPushIdRef(String pushIdRef) {
//        this.pushIdRef = pushIdRef;
//    }
//
////    public interface UserAccountType{
////        String mAccountType = "";
////        public void setmAccountType(String accountType);
////    }
//}
