package com.example.android.phonerepairerfind.Fragment;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.phonerepairerfind.Adapters.ChatAdapter;
import com.example.android.phonerepairerfind.App;
import com.example.android.phonerepairerfind.Constants.FirebaseConstants;
import com.example.android.phonerepairerfind.Constants.IntentConstants;
import com.example.android.phonerepairerfind.DIalog.ShowImageDialogFragment;
import com.example.android.phonerepairerfind.POJO.Chat;
import com.example.android.phonerepairerfind.POJO.RepairersNearYou;
import com.example.android.phonerepairerfind.POJO.UserProfile;
import com.example.android.phonerepairerfind.R;
import com.example.android.phonerepairerfind.model.ChatViewModel;
import com.example.android.phonerepairerfind.utilities.DatabaseHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatDetailFragment extends Fragment implements View.OnClickListener,
        ChatAdapter.ListChatActionModeViewCallbacks{


    private static final int CHAT_IMAGE_CONSTANT = 887;
    boolean isSuccessful = false;
    private ChatViewModel mModel;
    private List<Chat> mChats = new ArrayList<Chat>();
    private ChatAdapter mChatAdapter;
    private RecyclerView recyclerView;
    private ImageButton mImagePickerButton, mSmileyButton, mToolbarNavButton;
    private AppCompatImageButton mSendButton;
    private EditText mMessageEdittext;
    private Toolbar toolbar;
    private TextView mToolbarTitle;
    private CircleImageView mToolbarProfilePics;
    private String contactUsername, contactPushId = "", username = "";
    private String profilePicsUrl;
    private int[][] states;
    private int[] colors;
    private boolean isComment = false;
    private Bundle bundle;
    private Uri imageUriPath;

    public ChatDetailFragment() {
        // Required empty public constructor
    }

    public static ChatDetailFragment newInstance(boolean isComment) {

        Bundle args = new Bundle();
        args.putBoolean(IntentConstants.CHAT_DETAIL_IS_COMMENT, isComment);
        ChatDetailFragment fragment = new ChatDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void getReceivedData(String username, String contactUsername, String profilePicsUrl, String pushId) {
        this.contactUsername = contactUsername;
        this.username = username;
        this.profilePicsUrl = profilePicsUrl;
        this.contactPushId = pushId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null && getArguments().containsKey(IntentConstants.CHAT_DETAIL_IS_COMMENT)){
            isComment = getArguments().getBoolean(IntentConstants.CHAT_DETAIL_IS_COMMENT, false);
        }
        mChatAdapter = new ChatAdapter(username);
        mModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        FirebaseApp.initializeApp(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mChatAdapter);
        mChatAdapter.setActionListSelected(this);
        mChatAdapter.setActivity(((AppCompatActivity)getActivity()));

        if(!isComment) {
            DatabaseReference chatDatabaseRef = App.getsChatDataRef().child(contactPushId);
            setUpViewModel(chatDatabaseRef);
        }else{
            DatabaseReference commentDatabaseRef = App.getsCommentDataRef().child(contactPushId);
            setUpViewModel(commentDatabaseRef);
        }
    }

    private void setUpViewModel(DatabaseReference dbRef){
        if (mModel != null) {
            LiveData<List<Chat>> msgLiveData = mModel.getChatListLiveData(dbRef);

            msgLiveData.observe(getActivity(), (List<Chat> chats) -> {
                mChats = chats;
                mChatAdapter.setList(chats);
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_chat, container, false);
        setTintEnablePrimaryColor(itemView);
        setUpChatDetailFragment(itemView);
        return itemView;
    }

    private void setUpChatDetailFragment(View itemView) {
        initViews(itemView);
        mToolbarTitle.setText(contactUsername);
        if (profilePicsUrl != null) {
            if (!profilePicsUrl.isEmpty()) {
                Picasso
                        .get()
                        .load(profilePicsUrl)
                        .error(R.color.colorLightGray)
                        .into(mToolbarProfilePics);
            }
        }
        //mToolbarProfilePics.setImageResource(profilePicsUrl);
        mToolbarNavButton.setOnClickListener(this);
        toolbar.setTitleTextColor(ContextCompat.getColor(recyclerView.getContext(), R.color.white));
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if(appCompatActivity != null) {
            appCompatActivity.getSupportActionBar().hide();
            appCompatActivity.setSupportActionBar(toolbar);
            appCompatActivity.getSupportActionBar().show();
            appCompatActivity.getSupportActionBar().setHomeButtonEnabled(true);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setAdapter(mChatAdapter);

        mSendButton.setEnabled(false);
        mSendButton.setSupportImageTintList(new ColorStateList(states, colors));
        mSendButton.setOnClickListener(this);
        mImagePickerButton.setOnClickListener(this);
        // Enable Send button when there's text to send
        mMessageEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        //mMessageEdittext.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});
    }

    private void setTintEnablePrimaryColor(View view) {
        states = new int[][]{
                new int[]{android.R.attr.state_enabled}, //enabled
                new int[]{-android.R.attr.state_enabled} //disabled
        };

        colors = new int[]{
                ContextCompat.getColor(view.getContext(), R.color.colorPrimary),
                ContextCompat.getColor(view.getContext(), R.color.colorLightGray),
        };
    }

    private void uploadComment() {
        //Map<String, Chat> comments = new HashMap<String, Chat>();
        DatabaseReference dbRef =
        App.getsUsersDataRef()
                .child(this.contactPushId)
                .child(FirebaseConstants.COMMENTS_NODE);
        String pushId = dbRef.push().getKey();
        String commentText = mMessageEdittext.getText().toString().trim();
        Object timeStamp = ServerValue.TIMESTAMP;
        App.getsUserDetailDataRef()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserProfile user = dataSnapshot.getValue(UserProfile.class);
                        if(user == null) return;
                        dbRef.child(pushId)
                                .setValue(new Chat(user.getmName(), commentText, null,
                                        timeStamp, pushId, true));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


//                .runTransaction(new Transaction.Handler() {
//                    @NonNull
//                    @Override
//                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
//                        UserProfile userProfile = mutableData.getValue(UserProfile.class);
//                        if (userProfile == null) {
//                            return Transaction.success(mutableData);
//                        }
//                        Map<String, Object> comments;
//                        if(userProfile.getComments() == null){
//                            comments = new HashMap<String, Object>();
//                        }else comments = userProfile.getComments();
//
//                        if (comments.containsKey(App.getsCurrentUser().getUid())) {
//                            userProfile.setmCommentCount(userProfile.getmCommentCount() - 1);
//                            comments.remove(App.getsCurrentUser().getUid());
//
//                            userProfile.setComments(comments);
//                        } else {
//                            comments.put(App.getsCurrentUser().getUid(), new Chat(
//                                    userProfile.getmName(), , , , ,
//                                    ));
//                            userProfile.setComments(comments);
//                            userProfile.setmCommentCount(userProfile.getmCommentCount() + 1);
//                        }
//                    }
//
//                    @Override
//                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
//
//                    }
//                });

    }

    private void initViews(View itemView) {
        recyclerView = (RecyclerView) itemView.findViewById(R.id.chat_recycler_view);
        toolbar = (Toolbar) itemView.findViewById(R.id.chat_detail_tool_bar);
        mToolbarNavButton = (ImageButton) toolbar.findViewById(R.id.chat_detail_tool_bar_nav);
        mToolbarTitle = (TextView) toolbar.findViewById(R.id.chat_detail_tool_bar_username);
        mToolbarProfilePics = (CircleImageView) toolbar.findViewById(R.id.chat_detail_tool_bar_profile_image);
        mImagePickerButton = itemView.findViewById(R.id.chat_detail_image_picker);
        mSmileyButton = itemView.findViewById(R.id.chat_detail_similies_button);
        mSendButton = (AppCompatImageButton) itemView.findViewById(R.id.chat_detail_send_button);
        mMessageEdittext = (EditText) itemView.findViewById(R.id.chat_detail_edit_view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chat_detail_send_button:
                onSendButtonClicked(view);
                break;
            case R.id.chat_detail_image_picker:
                onImagePickerButtonClicked(view);
                break;
            case R.id.chat_detail_similies_button:
                break;
            case R.id.chat_detail_tool_bar_nav:
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void sendChat(){
        String chatText = mMessageEdittext.getText().toString().trim();
        Object timeStamp = ServerValue.TIMESTAMP;
        DatabaseReference databaseReference = App.getsChatDataRef().child(contactPushId).push();
        String chatPushId = databaseReference.getKey();
        Chat chat = new Chat(username, chatText, null, timeStamp, chatPushId);
        Map<String, Object> childUpdates = new HashMap<>();
        Task<Void> chatTask = databaseReference.setValue(chat);
        updateLastMessageInfoInProfile(chatText, null, timeStamp);
        chatTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Map<String, Object> map = new HashMap<>();
                if (chatPushId == null)
                    return;
                Task<Void> chatTask2 =
                        App.getsUsersDataRef()
                                .child(contactPushId)
                                .child(FirebaseConstants.CHATS_NODE)
                                .child(App.getsCurrentUser().getUid())
                                .child(chatPushId)
                                .setValue(new Chat(username, chatText, null, timeStamp, chatPushId));
                updateLastMessageInfoInProfile(chatText, null, timeStamp);
                chatTask2
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                childUpdates.put("mIsSent", true);
                                isSuccessful = true;
                                App.getsChatDataRef().child(contactPushId).child(chatPushId).updateChildren(childUpdates);
                                updateLastMessageInfoInProfile(chatText, null, timeStamp);
                            }
                        });
            }
        });

        updateLastMessageInfoInProfile(chatText, null, timeStamp);
        mMessageEdittext.setText("");
    }

    private void onSendButtonClicked(View view) {

        if(!isComment){
            sendChat();
        }else
            uploadComment();

    }

    private void onImagePickerButtonClicked(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), CHAT_IMAGE_CONSTANT);

    }

    public void sendImage(String imageUrl) {
        DatabaseReference databaseReference = App.getsChatDataRef().child(contactPushId).push();
        String chatPushId = databaseReference.getKey();
        Object timeStamp = ServerValue.TIMESTAMP;
        Chat chat = new Chat(username, null, imageUrl, timeStamp, chatPushId);
        Map<String, Object> childUpdates = new HashMap<>();
        Task<Void> chatTask = databaseReference.setValue(chat);
        updateLastMessageInfoInProfile("Photo", imageUrl, timeStamp);
        chatTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Map<String, Object> map = new HashMap<>();
                if (chatPushId == null)
                    return;
                Task<Void> chatTask2 =
                        App.getsUsersDataRef()
                                .child(contactPushId)
                                .child(FirebaseConstants.CHATS_NODE)
                                .child(App.getsCurrentUser().getUid())
                                .child(chatPushId)
                                .setValue(chat);
                chatTask2
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                childUpdates.put("mIsSent", true);
                                isSuccessful = true;
                                App.getsChatDataRef().child(contactPushId).child(chatPushId).updateChildren(childUpdates);
                            }
                        });
            }
        });
        updateLastMessageInfoInProfile("Photo", imageUrl, timeStamp);
    }

    private void showImageBeforeSend(Uri uriPath) {
        if (uriPath != null) {
//            DatabaseReference databaseReference = App.getsChatDataRef().child(contactPushId).push();
//            String chatPushId = databaseReference.getKey();
            FragmentTransaction fragmentTransaction = ((AppCompatActivity) getActivity())
                    .getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            Fragment prevFrag = ((AppCompatActivity) getActivity()).getSupportFragmentManager()
                    .findFragmentByTag("ShowImageDialogFragment");
            if (prevFrag != null) {
                fragmentTransaction.remove(prevFrag);
            }
            ShowImageDialogFragment dialogFragment = new ShowImageDialogFragment();
            dialogFragment.setDetails(uriPath);
            dialogFragment.show(fragmentTransaction, "ShowImageDialogFragment");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ChatDetailFragment.CHAT_IMAGE_CONSTANT && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            imageUriPath = data.getData();
            showImageBeforeSend(imageUriPath);
        }
    }

    private class ListChatToolbarActionModeCallbacks implements ActionMode.Callback{

        private final ChatAdapter.ListChatActionModeViewCallbacks actionModeCallbacks;
        private final ChatAdapter chatAdapter;

        public ListChatToolbarActionModeCallbacks(ChatAdapter.ListChatActionModeViewCallbacks callbacks,
                                                  ChatAdapter chatAdapter){
            this.actionModeCallbacks = callbacks;
            this.chatAdapter = chatAdapter;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.item_selected_chat_edit_actions, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.findItem(R.id.action_cancel).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.findItem(R.id.action_copy).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_delete:
                    actionModeCallbacks.onDeleteActionButton();
                    mode.finish();
                    return true;
                case R.id.action_cancel:
                    actionModeCallbacks.onCancelActionButton();
                    mode.finish();
                    return true;
                case R.id.action_copy:
                    actionModeCallbacks.onCopyChatList();
                    mode.finish();
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionModeCallbacks.onDestroyActionMode();
            chatAdapter.clearSelections();
        }
    }

    public ActionMode actionMode;

    @Override
    public void onListItemSelect(int position) {
        mChatAdapter.toggleSelection(position);

        final boolean hasCheckedItems = mChatAdapter.getSelectedItems().size() > 0;

        if(hasCheckedItems && actionMode == null){
            actionMode = ((AppCompatActivity)getActivity())
                    .startSupportActionMode(new ChatDetailFragment.ListChatToolbarActionModeCallbacks(
                            this, mChatAdapter));

        }else if(!hasCheckedItems && actionMode != null){
            actionMode.finish();
        }

        if(actionMode != null){
            actionMode.setTitle(String.valueOf(mChatAdapter.getSelectedCount()));
        }
    }

    @Override
    public void onDestroyActionMode() {
        actionMode = null;
    }

    @Override
    public void onDeleteActionButton() {
        DatabaseHelper.deleteChats(mChatAdapter.getSelectedItems(), contactPushId);
    }

    @Override
    public void onCancelActionButton() {

    }

    @Override
    public void onAddToCustomerList() {

    }

    @Override
    public void onCopyChatList() {

    }

    private void updateLastMessageInfoInProfile(String lastMsg, String imageUrl, Object lastMsgTimeStamp) {
        Map<String, Object> lastMessageUpdate = new HashMap<>();
//        Contact contact = new Contact(username, lastMsg, lastMsgName, lastMsgTimeStamp, contactUrl, contactPushId,
//                0, false, false, true);
//        lastMessageUpdate.put(contactPushId, contact);
        App.getsUserDetailDataRef()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                        if (userProfile == null)
                            return;
                        lastMessageUpdate.put("mUsername", username);
                        lastMessageUpdate.put("mLastMsg", lastMsg);
                        lastMessageUpdate.put("mLastMsgName", userProfile.getmName());
                        lastMessageUpdate.put("mTimeStamp", lastMsgTimeStamp);
                        lastMessageUpdate.put("mIsLastMsgSent", true);
                        lastMessageUpdate.put("mPushId", contactPushId);
                        lastMessageUpdate.put("mProfilePicsUrl", profilePicsUrl);
                        lastMessageUpdate.put("mLastMsgPushId", App.getsCurrentUser().getUid());
                        //FirebaseInitUser.getUserDetailDataRef().updateChildren(lastMessageUpdate);
                        App.getsContactDataRef().child(contactPushId).updateChildren(lastMessageUpdate);
                        lastMessageUpdate.put("mUsername", userProfile.getmName());
                        lastMessageUpdate.put("mPushId", userProfile.getmPushId());
                        lastMessageUpdate.put("mProfilePicsUrl", userProfile.getmImageUrl());
                        App.getsUsersDataRef()
                                .child(contactPushId)
                                .child(FirebaseConstants.CONTACTS_NODE)
                                .child(App.getsCurrentUser().getUid())
                                .updateChildren(lastMessageUpdate);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
