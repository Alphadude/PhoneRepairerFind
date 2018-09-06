package com.example.android.phonerepairerfind.Adapters;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.phonerepairerfind.App;
import com.example.android.phonerepairerfind.Constants.AdaptersConstant;
import com.example.android.phonerepairerfind.Fragment.ContactFragment;
import com.example.android.phonerepairerfind.POJO.Contact;
import com.example.android.phonerepairerfind.POJO.UserProfile;
import com.example.android.phonerepairerfind.R;
import com.example.android.phonerepairerfind.interfaces.OnContactItemSelected;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapters extends RecyclerView.Adapter<ContactAdapters.ContactViewHolder> implements
            OnContactItemSelected{

    private final String TAG = ContactAdapters.class.getSimpleName();
    private FragmentManager mFragmentManager;
    private List<Contact> mContacts;
    private FrameLayout mEmptyStateView;
    private UserProfile userProfile;
    private String mLastMessageName;
    private boolean isLastMsgSentByUser;
    private String lastMsgCountString;
    private String mUsername;
    private ContactFragment contactFragment;
    private DataPassListener dataPassListener;
    private Activity mActivity;
    private SparseBooleanArray mSelectedItem = new SparseBooleanArray();
    private ContactViewHolder mContactViewHolder;
    private RecyclerView mRecyclerView;
    private ActionModeViewCallBacks actionModeViewCallBacks;
    private Menu mContextMenu;

    public ContactAdapters(FragmentManager fragmentManager) {
        //setUserProfile();
        //mContacts = contacts;
        mFragmentManager = fragmentManager;
        contactFragment = (ContactFragment) fragmentManager.findFragmentByTag(AdaptersConstant.CONTACT_LIST_FRAGMENT_TAG);
        dataPassListener = (DataPassListener) contactFragment.getActivity();

        //mRecyclerView.addOnItemTouchListener(new OnI);
        //notifyDataSetChanged();
    }

    public void setActivity(Activity activity){
        mActivity = activity;
    }

    public void setRecyclerView(RecyclerView recyclerView){
        mRecyclerView = recyclerView;
    }

    public void setActionListSelected(ActionModeViewCallBacks callBacks){
        actionModeViewCallBacks = callBacks;
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_card_view, parent, false);
        //itemView.setOnLongClickListener(this);
        mContactViewHolder = new ContactViewHolder(itemView);
        return mContactViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = mContacts.get(holder.getAdapterPosition());
        if (contact.getmProfilePicsUrl() != null) {
            Picasso
                    .get()
                    .load(contact.getmProfilePicsUrl())
                    .error(R.color.colorLightGray)
                    .into(holder.mProfilePicsView);
        }
        //mProfilePicsView.setImageResource(contact.getmProfilePicsUrl());
        holder.mUsernameView.setText(contact.getmUsername());
        holder.mDateTimeView.setText(contact.getmLastMsgDate());
        holder.mLastMessageView.setText(contact.getmLastMsg());
        //mLastMessageName = contact.getmLastMsgName();

        setUserProfile(holder, contact, holder.mNotReadMessageCountView);
        holder.itemView.setActivated(mSelectedItem.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactFragment contactFragment = (ContactFragment)
                        ((AppCompatActivity) mActivity).getSupportFragmentManager().findFragmentByTag
                                (AdaptersConstant.CONTACT_LIST_FRAGMENT_TAG);
                if(contactFragment != null && contactFragment.actionMode != null){
                    actionModeViewCallBacks.onListItemSelect(holder.getAdapterPosition());
                }else
                dataPassListener.passData(contact.getmUsername(), contact.getmUsername(), contact.getmProfilePicsUrl(), contact.getmPushId());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mContacts != null) {
            return mContacts.size();
        } else return 0;
    }

    public void setList(List<Contact> list) {
        mContacts = list;
        notifyDataSetChanged();
    }

    private void setUserProfile(ContactViewHolder holder, final Contact contact, final View mNotReadMessageCountView) {
        //TODO: Get the User Profile here instead of using FirebaseinitUser.getUserDetailRef()
        App.getsUserDetailDataRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userProfile = dataSnapshot.getValue(UserProfile.class);
                if (userProfile == null)
                    return;
                mUsername = userProfile.getmUsername();
                if (userProfile.getmPushId().equals(contact.getmLastMsgPushId())) {
                    setMessageStatusOrMessageCount(holder, contact, true, mNotReadMessageCountView);
                }
                if (!userProfile.getmPushId().equals(contact.getmLastMsgPushId())) {
                    setMessageStatusOrMessageCount(holder, contact, false, mNotReadMessageCountView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setMessageStatusOrMessageCount(ContactViewHolder holder, Contact contact,
                                                boolean isLastMsgSentByUser, View mNotReadMessageCountView) {
        if (isLastMsgSentByUser) {
            boolean isMsgDelivered = contact.ismIsLastMsgDelivered();
            boolean isMsgRead = contact.ismIsLastMsgRead();
            boolean isMsgSent = contact.ismIsLastMsgSent();
            holder.mMessageStatusView.setVisibility(View.VISIBLE);
            holder.mNotReadMessageCountView.setVisibility(View.INVISIBLE);
            if (isMsgSent) {
                holder.mMessageStatusView.setImageResource(R.drawable.ic_sent);
                if (isMsgDelivered && (!isMsgRead)) {
                    holder.mMessageStatusView.setImageResource(R.drawable.ic_delivered);
                }
                if (isMsgDelivered && isMsgRead) {
                    holder.mMessageStatusView.setImageResource(R.drawable.ic_read);
                }
            } else {
                holder.mMessageStatusView.setImageResource(R.drawable.ic_not_sent);
            }
        } else {
            int lastMsgCount = contact.getmNotReadMessageCount();
            lastMsgCountString = "";
            if (lastMsgCount != 0) {
                lastMsgCountString = String.valueOf(lastMsgCount);
                mNotReadMessageCountView.setVisibility(View.VISIBLE);
                holder.mMessageStatusView.setVisibility(View.INVISIBLE);
                if (lastMsgCount >= 100) {
                    lastMsgCountString = String.valueOf(lastMsgCount).charAt(0) + "h";
                }
                if (lastMsgCount >= 1000) {
                    lastMsgCountString = String.valueOf(lastMsgCount).charAt(0) + "k";
                }
                if (lastMsgCount >= 1_000_000) {
                    lastMsgCountString = String.valueOf(lastMsgCount).charAt(0) + "m";
                }
                ((TextView) mNotReadMessageCountView).setText(lastMsgCountString);
            } else {
                mNotReadMessageCountView.setVisibility(View.INVISIBLE);
                holder.mMessageStatusView.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void toggleSelection(int position) {
        if(mSelectedItem.get(position)){
            //mContactViewHolder.itemView.setActivated(false);
            mSelectedItem.delete(position);
        }else {
            mSelectedItem.put(position, true);
            //mContactViewHolder.itemView.setActivated(true);
        }
        notifyItemChanged(position);
    }

    @Override
    public void clearSelections() {
        mSelectedItem.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getSelectedCount() {
        return mSelectedItem.size();
    }

    @Override
    public List<Contact> getSelectedItems() {
        final List<Contact> contactList = new LinkedList<>();
        for(int i = 0; i < mSelectedItem.size(); i++){
            contactList.add(mContacts.get(mSelectedItem.keyAt(i)));
        }
        return contactList;
    }



    public interface DataPassListener {
        void passData(String username, String contactUsername, String profilePicsUrl, String pushIds);
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        protected CircleImageView mProfilePicsView;
        protected TextView mUsernameView, mLastMessageView, mNotReadMessageCountView, mDateTimeView;
        protected ImageView mMessageStatusView;

        public ContactViewHolder(View itemView) {
            super(itemView);
            //((ContactListActivity)mActivity).setEditOptions(this);
            initContactView(itemView);
            final int[][] states = new int[][]{
                    new int[]{android.R.attr.state_activated}, //checked
                    new int[]{-android.R.attr.state_activated} //unchecked
            };
            final int[] colors = new int[]{
                    ContextCompat.getColor(itemView.getContext(), R.color.colorAppBottomColor),
                    ContextCompat.getColor(itemView.getContext(), R.color.white),
            };
            itemView.setBackgroundTintList(new ColorStateList(states, colors));
            itemView.setOnLongClickListener(this);
        }


        private void initContactView(View itemView) {
            mProfilePicsView = (CircleImageView) itemView.findViewById(R.id.contact_profile_pic);
            mUsernameView = (TextView) itemView.findViewById(R.id.contact_profile_name);
            mLastMessageView = (TextView) itemView.findViewById(R.id.contact_profile_last_message);
            mNotReadMessageCountView = (TextView) itemView.findViewById(R.id.contact_message_not_read_count);
            mDateTimeView = (TextView) itemView.findViewById(R.id.contact_message_date_time);
            mMessageStatusView = (ImageView) itemView.findViewById(R.id.contact_message_status);
        }


        @Override
        public boolean onLongClick(View view) {
            actionModeViewCallBacks.onListItemSelect(getAdapterPosition());
            //((AppCompatActivity)mActivity).startSupportActionMode(mActionModeCallback);
            return true;
        }

    }

    public ContactViewHolder getContactViewHolder(){
        return mContactViewHolder;
    }

    public interface ActionModeViewCallBacks{
        void onListItemSelect(final int position);

        void onDestroyActionMode();
    }

    public interface ListContactActionModeViewCallbacks extends ActionModeViewCallBacks{
        void onDeleteActionButton();

        void onCancelActionButton();

        void onAddToCustomerList();
    }
}
