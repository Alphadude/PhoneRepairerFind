package com.example.android.phonerepairerfind.Adapters;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.phonerepairerfind.Activities.DetailActivity;
import com.example.android.phonerepairerfind.App;
import com.example.android.phonerepairerfind.Constants.AdaptersConstant;
import com.example.android.phonerepairerfind.Constants.FirebaseConstants;
import com.example.android.phonerepairerfind.Constants.IntentConstants;
import com.example.android.phonerepairerfind.Fragment.ChatDetailFragment;
import com.example.android.phonerepairerfind.POJO.RepairersNearYou;
import com.example.android.phonerepairerfind.POJO.UserProfile;
import com.example.android.phonerepairerfind.R;
import com.example.android.phonerepairerfind.model.SearchUsersViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RepairersNearYouAdapter extends RecyclerView.Adapter<RepairersNearYouAdapter.RepairersNearYouHolder> {

    private RecyclerView mRecyclerView;
    private List<RepairersNearYou> mRepairersNearYouList = new ArrayList<RepairersNearYou>();
    private boolean mIsLiked;
    private SearchUsersViewModel model;
    private LinkedHashMap<String, RepairersNearYou> mapList = new LinkedHashMap<>();
    private AppCompatActivity mActivity;

    public RepairersNearYouAdapter() {
    }

    public RepairersNearYouAdapter(List<RepairersNearYou> repairersNearYouList) {
        mRepairersNearYouList = repairersNearYouList;
    }

    public RepairersNearYouAdapter(SearchUsersViewModel model, AppCompatActivity activity) {
        this.model = model;
        mActivity = activity;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public RepairersNearYouHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.repairers_near_you_result_card_view, parent, false);
//        initViews(itemView);
        RepairersNearYouHolder nearYouHolder = new RepairersNearYouHolder(itemView);
        return nearYouHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RepairersNearYouHolder holder, int position) {
        final RepairersNearYou repairersNearYou = mRepairersNearYouList.get(holder.getAdapterPosition());
        holder.bindViews(repairersNearYou);
    }

    @Override
    public int getItemCount() {
        if (mRepairersNearYouList == null)
            return 0;
        return mRepairersNearYouList.size();
    }

    public void setList(List<RepairersNearYou> list) {
        mRepairersNearYouList.clear();
        mRepairersNearYouList = list;
        notifyDataSetChanged();
    }

    public void clearList() {
        mRepairersNearYouList.clear();
        mapList.clear();
        notifyDataSetChanged();
    }

    public void addToList(RepairersNearYou repairersNearYou) {
        List<RepairersNearYou> list = new ArrayList<>();
        mapList.put(repairersNearYou.getmPushId(), repairersNearYou);
        mRepairersNearYouList.clear();
        list.addAll(mapList.values());
        setList(list);
    }

    private void onLikeButtonClicked(View view, DatabaseReference itemUserDetailRef) {
        mIsLiked = !mIsLiked;
        view.setActivated(mIsLiked);

        itemUserDetailRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                UserProfile userProfile = mutableData.getValue(UserProfile.class);
                if (userProfile == null) {
                    return Transaction.success(mutableData);
                }
                Map<String, Boolean> mapLike;
                if (userProfile.getLikes() == null) {
                    mapLike = new HashMap<String, Boolean>();
                } else {
                    mapLike = userProfile.getLikes();
                }
                if (mapLike.containsKey(App.getsCurrentUser().getUid())) {
                    userProfile.setmLikesCount(userProfile.getmLikesCount() - 1);
                    mapLike.remove(App.getsCurrentUser().getUid());

                    userProfile.setLikes(mapLike);
                } else {
                    mapLike.put(App.getsCurrentUser().getUid(), true);
                    userProfile.setLikes(mapLike);
                    userProfile.setmLikesCount(userProfile.getmLikesCount() + 1);
                }
                //TODO:check if the counter works well now else uncomment the line below
                //mLikeCounts.setText(userProfile.getmLikesCount() + "like(s)");
                mutableData.setValue(userProfile);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                Toast.makeText(view.getContext(), "Completed", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void openChatFragment(int pos){
        RepairersNearYou nearYou = mRepairersNearYouList.get(pos);
//        if(mActivity != null)
//            if(mActivity.getSupportActionBar() != null)
//        mActivity.getSupportActionBar().hide();
        ChatDetailFragment fragment = ChatDetailFragment.newInstance(true);
        fragment.setRetainInstance(true);
        FragmentTransaction fragmentTransaction =  mActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activity_search_result, fragment, AdaptersConstant.CHAT_FRAGMENT_TAG);
        fragmentTransaction.addToBackStack(AdaptersConstant.CHAT_FRAGMENT_TAG);
        fragment.getReceivedData(nearYou.getmName(), nearYou.getmName(), nearYou.getmImageUrl(),
                nearYou.getmPushId());
        fragmentTransaction.commit();
    }

    private boolean checkIfItsLiked(RepairersNearYou repairersNearYou) {

        boolean like = false;
        try {
            like = repairersNearYou.getLikes().containsKey(App.getsCurrentUser().getUid());
        } catch (NullPointerException e) {
        }

        if (like) return true;
        else return false;
    }

    public class RepairersNearYouHolder extends RecyclerView.ViewHolder {
        protected TextView mRepairerNameView, mRepairerLocationView, mRepairerAvailabilityView, mRepairerMinsView,
                mLikeCounts, mCommentsCount;
        protected AppCompatRatingBar mRepairRatingBar;
        protected AppCompatImageButton mLikeButton, mCommentButton;
        protected ImageView mImage;
        public RepairersNearYouHolder(final View itemView) {
            super(itemView);
            initViews(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //model.select(mRepairersNearYouList.get(getAdapterPosition()));
                    Intent intent = new Intent(itemView.getContext(), DetailActivity.class)
                            .putExtra(IntentConstants.DETAIL_ACTIVITY_EXTRAS, IntentConstants.SEARCH_VIEW_RESULT_DETAIL_ACTIVITY);
                    intent.putExtra(IntentConstants.SEARCH_VIEW_ITEM_OBJECT,
                            mRepairersNearYouList.get(getAdapterPosition()));
                    intent.putExtra(IntentConstants.REPAIRS_NEAR_YOU_ID,
                            mRepairersNearYouList.get(getAdapterPosition()).getmPushId());
                    itemView.getContext().startActivity(intent);
                }
            });

            //final RepairersNearYou repairersNearYou = ;
        }

        private void initViews(View itemView) {
            mRepairerNameView = (TextView) itemView.findViewById(R.id.repairer_near_you_result_name);
            mRepairerLocationView = itemView.findViewById(R.id.repairer_near_you_location);
            mRepairerAvailabilityView = itemView.findViewById(R.id.repairer_near_you_availability);
            mRepairerMinsView = itemView.findViewById(R.id.repairers_near_you_result_mins);
            mRepairRatingBar = (AppCompatRatingBar) itemView.findViewById(R.id.repairers_near_you_ratingBar);
            mLikeButton = (AppCompatImageButton) itemView.findViewById(R.id.repairer_near_you_like_button);
            mCommentButton = (AppCompatImageButton) itemView.findViewById(R.id.repair_near_you_result_comment_button);
            mImage = itemView.findViewById(R.id.repairer_near_you_result_image);
            mLikeCounts = (TextView) itemView.findViewById(R.id.repairers_near_you_like_count);
            mCommentsCount = (TextView) itemView.findViewById(R.id.repairers_near_you_comment_count);
        }

        private void bindViews(final RepairersNearYou repairersNearYou) {
            StringBuilder availability = new StringBuilder();
            if (repairersNearYou.getmAvailability() != null) {
                String monday = "Monday";
                String tuesday = "Tuesday";
                String wednesday = "Wednesday";
                String thursday = "Thursday";
                String friday = "Friday";
                String saturday = "Saturday";
                String sunday = "Sunday";
                if (!((String) repairersNearYou.getmAvailability().get(monday)).equals("Closed")) {
                    availability.append(monday + ", ");
                }

                if (!((String) repairersNearYou.getmAvailability().get(tuesday)).equals("Closed")) {
                    availability.append(tuesday + ", ");
                }

                if (!((String) repairersNearYou.getmAvailability().get(wednesday)).equals("Closed")) {
                    availability.append(wednesday + ", ");
                }

                if (!((String) repairersNearYou.getmAvailability().get(thursday)).equals("Closed")) {
                    availability.append(thursday + ", ");
                }

                if (!((String) repairersNearYou.getmAvailability().get(friday)).equals("Closed")) {
                    availability.append(friday + ", ");
                }

                if (!((String) repairersNearYou.getmAvailability().get(saturday)).equals("Closed")) {
                    availability.append(saturday + ", ");
                }

                if (!((String) repairersNearYou.getmAvailability().get(sunday)).equals("Closed")) {
                    availability.append(sunday + ", ");
                }
                availability.deleteCharAt(availability.length() - 1).append(".");
            }
            final int[][] states = new int[][]{
                    new int[]{android.R.attr.state_activated}, //checked
                    new int[]{-android.R.attr.state_activated} //unchecked
            };
            final int[] colors = new int[]{
                    ContextCompat.getColor(itemView.getContext(), R.color.colorAccent),
                    ContextCompat.getColor(itemView.getContext(), R.color.colorLightGray),
            };
            mLikeButton.setSupportImageTintList(new ColorStateList(states, colors));
            mRepairerNameView.setText(repairersNearYou.getmName());
            mRepairerLocationView.setText(repairersNearYou.getmLocation());
            mRepairerAvailabilityView.setText(availability.toString());
            mRepairerMinsView.setText(repairersNearYou.getmMins());
            mRepairRatingBar.setRating(repairersNearYou.getmRating());
            mLikeCounts.setText(repairersNearYou.getmLikesCount() + " like(s)");
            mCommentsCount.setText(repairersNearYou.getmCommentsCount() + " comment(s)");
            //if(repairersNearYou.getLikes() != null) {
            mIsLiked = checkIfItsLiked(repairersNearYou);//repairersNearYou.ismIsLiked();
            mLikeButton.setActivated(mIsLiked);

            if (repairersNearYou.getmImageUrl() != null) {
                if (!repairersNearYou.getmImageUrl().isEmpty()) {
                    Picasso
                            .get()
                            .load(repairersNearYou.getmImageUrl())
                            .into(mImage);
                }
            }


            mCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openChatFragment(getAdapterPosition());
                }
            });

            mLikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onLikeButtonClicked(view,
                            App.getsUsersDataRef().child(mRepairersNearYouList.get(getAdapterPosition())
                                    .getmPushId()).child(FirebaseConstants.USER_DETAILS_NODE));
                }
            });
            //mImage.setImageResource(repairersNearYou.getmImageUrl());
        }

    }
}
