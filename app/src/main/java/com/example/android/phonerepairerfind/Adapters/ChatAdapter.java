package com.example.android.phonerepairerfind.Adapters;

import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.phonerepairerfind.Constants.AdaptersConstant;
import com.example.android.phonerepairerfind.Fragment.ChatDetailFragment;
import com.example.android.phonerepairerfind.POJO.Chat;
import com.example.android.phonerepairerfind.R;
import com.example.android.phonerepairerfind.interfaces.OnChatItemSelected;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>
            implements OnChatItemSelected{
    private List<Chat> mChats = new ArrayList<>();
    private String mUsername;
    private boolean isLastMsgSentByUser;
    private SparseBooleanArray mSelectedItem = new SparseBooleanArray();
    private AppCompatActivity mActivity;
    private ActionModeViewCallBacks actionModeViewCallBacks;

    public ChatAdapter(String username) {
        //mChats = chats;
        mUsername = username;
    }

    public void setActivity(AppCompatActivity activity){
        mActivity = activity;
    }

    public void setActionListSelected(ActionModeViewCallBacks callBacks){
        actionModeViewCallBacks = callBacks;
    }

    @Override
    public int getItemViewType(int position) {
        String msgName = mChats.get(position).getmMessageName();

        if(mUsername.equals(msgName)){
            isLastMsgSentByUser = true;
            return AdaptersConstant.SENT_MESSAGE_VIEW_TYPE;
        }else{
            isLastMsgSentByUser = false;
            return AdaptersConstant.RECIEVED_MESSAGE_VIEW_TYPE;
        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType){
            case AdaptersConstant.SENT_MESSAGE_VIEW_TYPE:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_chat_card_view,
                        parent, false);
                break;
            case AdaptersConstant.RECIEVED_MESSAGE_VIEW_TYPE:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.received_chat_card_view,
                        parent, false);
                break;
            default:
                throw new IllegalArgumentException();
        }
        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = mChats.get(holder.getAdapterPosition());
        holder.itemView.setActivated(mSelectedItem.get(position));
        holder.itemView.setSelected(mSelectedItem.get(position));
        if(chat.getmMessage() != null) {
            holder.mImageChatView.setVisibility(View.GONE);
            holder.mTextChatView.setVisibility(View.VISIBLE);
            holder.mTextChatView.setText(chat.getmMessage());
        }
        if(chat.getmChatImageUrl() != null) {
            holder.mImageChatView.setVisibility(View.VISIBLE);
            holder.mTextChatView.setVisibility(View.GONE);
            Picasso
                    .get()
                    .load(chat.getmChatImageUrl())
                    .error(R.color.colorLightGray)
                    .into(holder.mImageChatView);
        }
        setDate(holder.getAdapterPosition(), chat, holder);
        holder.mChatTimeView.setText(chat.getmTime());
        if(isLastMsgSentByUser){
            setMessageStatus(chat, holder);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatDetailFragment chatDetailFragment = (ChatDetailFragment)
                        ((AppCompatActivity) mActivity).getSupportFragmentManager().findFragmentByTag
                                (AdaptersConstant.CHAT_FRAGMENT_TAG);
                if(chatDetailFragment != null && chatDetailFragment.actionMode != null){
                    actionModeViewCallBacks.onListItemSelect(holder.getAdapterPosition());
                }else;

            }
        });
    }

    @Override
    public int getItemCount() {
        if(mChats != null)
            return mChats.size();
        else return 0;
    }

    @Override
    public void toggleSelection(int position) {
        if(mSelectedItem.get(position)){
            mSelectedItem.delete(position);
        }else {
            mSelectedItem.put(position, true);
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
    public List<Chat> getSelectedItems() {
        final List<Chat> chats = new LinkedList<>();
        for(int i = 0; i < mSelectedItem.size(); i++){
            chats.add(mChats.get(mSelectedItem.keyAt(i)));
        }
        return chats;
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder implements
            View.OnLongClickListener{
        protected ImageView mMessageStatusView, mImageChatView;
        protected TextView mTextChatView, mChatTimeView, mChatDateView;
        public ChatViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
            final int[][] states = new int[][]{
                    new int[]{android.R.attr.state_activated}, //checked
                    new int[]{-android.R.attr.state_activated}, //unchecked
            };
            final int[] colors = new int[]{
                    ContextCompat.getColor(itemView.getContext(), R.color.colorAppBottomColor),
                    ContextCompat.getColor(itemView.getContext(), R.color.white),
            };
            itemView.setBackgroundTintList(new ColorStateList(states, colors));
            itemView.setOnLongClickListener(this);
        }

        private void initView(View itemView){
            mChatDateView = (TextView) itemView.findViewById(R.id.chat_detail_date);
            mImageChatView = (ImageView) itemView.findViewById(R.id.chat_image_chat);
            mTextChatView = (TextView) itemView.findViewById(R.id.chat_text_chat);
            mChatTimeView = (TextView) itemView.findViewById(R.id.chat_time);
            if(isLastMsgSentByUser) {
                mMessageStatusView = (ImageView) itemView.findViewById(R.id.chat_status_indicator);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            actionModeViewCallBacks.onListItemSelect(getAdapterPosition());
            return true;
        }
    }

    public void setList(List<Chat> list){
        mChats.clear();
        mChats = list;
        notifyDataSetChanged();
    }

    private void setDate(int position, Chat chat, ChatViewHolder holder){
        RelativeLayout dateViewParent = (RelativeLayout) holder.mChatDateView.getParent();
        //TODO: Arrange the dates in the list in descending order before carrying out the if statements
        //if (position == 0 || position == mChats.size()) {
        if(chat.getmDate() != null) {
            if (position == 0) {
                dateViewParent.setVisibility(View.VISIBLE);
                holder.mChatDateView.setText(chat.getmDate());
            } else if (position > 0) {
                if (!chat.getmDate().equals(mChats.get(position - 1).getmDate())) {
                    dateViewParent.setVisibility(View.VISIBLE);
                    holder.mChatDateView.setText(chat.getmDate());
                } else {
                    dateViewParent.setVisibility(View.GONE);
                }
            } else dateViewParent.setVisibility(View.GONE);
        }
    }

    private void setMessageStatus(Chat chat, ChatViewHolder holder){
        if(isLastMsgSentByUser){
            boolean isMsgDelivered = chat.ismIsDelivered();
            boolean isMsgRead = chat.ismIsRead();
            boolean isMsgSent = chat.ismIsSent();
            if(isMsgSent){
                holder.mMessageStatusView.setImageResource(R.drawable.ic_sent);
                if(isMsgDelivered && (!isMsgRead)){
                    holder.mMessageStatusView.setImageResource(R.drawable.ic_delivered);
                }
                if(isMsgDelivered && isMsgRead){
                    holder.mMessageStatusView.setImageResource(R.drawable.ic_read);
                }
            }else{
                holder.mMessageStatusView.setImageResource(R.drawable.ic_not_sent);
            }
        }else {

        }
    }

    //private

    public interface ActionModeViewCallBacks{
        void onListItemSelect(final int position);

        void onDestroyActionMode();
    }

    public interface ListChatActionModeViewCallbacks extends ActionModeViewCallBacks {
        void onDeleteActionButton();

        void onCancelActionButton();

        void onAddToCustomerList();

        void onCopyChatList();
    }
}
