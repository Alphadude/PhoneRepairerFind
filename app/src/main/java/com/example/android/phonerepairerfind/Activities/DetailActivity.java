package com.example.android.phonerepairerfind.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.phonerepairerfind.Adapters.AddTipsAdapter;
import com.example.android.phonerepairerfind.App;
import com.example.android.phonerepairerfind.Constants.AdaptersConstant;
import com.example.android.phonerepairerfind.Constants.FirebaseConstants;
import com.example.android.phonerepairerfind.Constants.IntentConstants;
import com.example.android.phonerepairerfind.Fragment.ChatDetailFragment;
import com.example.android.phonerepairerfind.POJO.AddTips;
import com.example.android.phonerepairerfind.POJO.RepairersNearYou;
import com.example.android.phonerepairerfind.R;
import com.example.android.phonerepairerfind.databinding.SearchResultItemDetailBinding;
import com.example.android.phonerepairerfind.model.SearchUsersViewModel;
import com.example.android.phonerepairerfind.utilities.DatabaseHelper;
import com.example.android.phonerepairerfind.utilities.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = DetailActivity.class.getSimpleName();
    private final int TIPS_PICKER_CONSTANT = 11;
    private final int BACKGROUND_TIPS_PICKER_CONSTANT = 13;
    boolean showMapClicked = false;
    private ImageView mImageView;
    private ViewPager mViewPager;
    private View mMapFragment;
    private TextView mNameView, mLocationView, mAvailabilityView;
    private RatingBar mRatingBar;
    private Button mMessageBtn, mShowMapBtn;
    private SearchResultItemDetailBinding resultItemDetailBinding;
    private String ADD_TIPS_OUT_STATE = "add_tips_out_state";
    /**
     * The fields below belongs to the AddTipsActivity
     */
    private EditText mTitleEditText, mFirstTextEditText;
    private TextView mTitleTextView, mFirstTextTextView;
    private ImageView mBackgroundImageView;
    private FloatingActionButton addTipsImageFab, uploadTipsFab, showMoreFab;
    private LinearLayout mParentView;
    private AddTipsHelperClass mAddTipsHelperClass;
    private String mBackgroundImageUrl;
    private CoordinatorLayout coordinatorLayout;
    private CollapsingToolbarLayout collapsingToolbar;
    private RepairersNearYou mRepairersNearYou;
    private AddTipsAdapter mAddTipsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(IntentConstants.DETAIL_ACTIVITY_EXTRAS)) {
            switch (intent.getIntExtra(IntentConstants.DETAIL_ACTIVITY_EXTRAS, -1)) {
                case IntentConstants.ADD_TIPS_ACTIVITY:
                    setUpAddTipsActivity(savedInstanceState);
                    break;
                case IntentConstants.SEARCH_VIEW_RESULT_DETAIL_ACTIVITY:
                    setUpResultItemDetailActivity();
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private void setUpResultItemDetailActivity() {
        resultItemDetailBinding = DataBindingUtil.setContentView(this,
                R.layout.search_result_item_detail);
        initResultItemViews();
        SearchUsersViewModel model =
                ViewModelProviders.of(this).get(SearchUsersViewModel.class);
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentConstants.REPAIRS_NEAR_YOU_ID)) {
                String uid = intent.getStringExtra(IntentConstants.REPAIRS_NEAR_YOU_ID);
                if (model != null) {
                    model.getSingleRepairLivedata(App.getsUsersDataRef()
                            .child(uid)
                            .child(FirebaseConstants.USER_DETAILS_NODE))
                            .observe(this, repairersNearYou -> {
                                mRepairersNearYou = repairersNearYou;
                                if(repairersNearYou == null)
                                    return ;
                                //Log.e(TAG, "RepairersNearYou " + repairersNearYou.toString());
                                resultItemDetailBinding.searchResultToolbar.setTitle(mRepairersNearYou.getmName());
                                setSupportActionBar(resultItemDetailBinding.searchResultToolbar);
                                getSupportActionBar().setHomeButtonEnabled(true);
                                if (mRepairersNearYou != null || !mRepairersNearYou.getmImageUrl().isEmpty()) {
                                    Picasso
                                            .get()
                                            .load(mRepairersNearYou.getmImageUrl())
                                            .error(R.color.colorLightGray)
                                            .into(resultItemDetailBinding.searchResultImageView);
                                }
                                setViews();
                            });
                }
            }
        }

        mMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ChatDetailFragment chatDetailFragment = new ChatDetailFragment();
                chatDetailFragment.setRetainInstance(true);
                Intent intent = getIntent();
                if (intent != null) {
                    if (intent.hasExtra(IntentConstants.SEARCH_VIEW_ITEM_OBJECT)) {
                        RepairersNearYou repairersNearYou = (RepairersNearYou)
                                intent.getSerializableExtra(IntentConstants.SEARCH_VIEW_ITEM_OBJECT);
                        chatDetailFragment.getReceivedData(repairersNearYou.getmName(),
                                repairersNearYou.getmName(), repairersNearYou.getmImageUrl(),
                                repairersNearYou.getmPushId());
                        ft.add(R.id.search_result_item_detail_frame_layout, chatDetailFragment,
                                AdaptersConstant.CHAT_FRAGMENT_TAG);
                        ft.addToBackStack(AdaptersConstant.CONTACT_LIST_FRAGMENT_TAG);
                        ft.commit();
                    }
                }
            }
        });

        mShowMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(showMapClicked){
//                    mImageView.setVisibility(View.GONE);
//                    mViewPager.setVisibility(View.GONE);
//                    mMapFragment.setVisibility(View.VISIBLE);
//                    mShowMapBtn.setText("Show Image(s)");
//                    showMapClicked = false;
//                }else {
//                    mImageView.setVisibility(View.VISIBLE);
//                    mViewPager.setVisibility(View.GONE);
//                    mMapFragment.setVisibility(View.GONE);
//                    mShowMapBtn.setText("Show Map");
//                    showMapClicked = true;
//                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeAsUp:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setViews() {
        resultItemDetailBinding.searchResultName.setText(mRepairersNearYou.getmName());
        resultItemDetailBinding.searchResultRatingBar.setRating(mRepairersNearYou.getmRating());
        resultItemDetailBinding.searchResultLocation.setText(mRepairersNearYou.getmLocation());
        resultItemDetailBinding.searchResultEmailAddress.setText(mRepairersNearYou.getmEmail());
        resultItemDetailBinding.searchResultBusinessPhoneNumber.setText(mRepairersNearYou.getmBusinessPhoneNum());
        resultItemDetailBinding.searchResultPhoneNumber.setText(mRepairersNearYou.getmPhoneNum());
        resultItemDetailBinding.searchResultWebSite.setText(mRepairersNearYou.getmWebsite());
        resultItemDetailBinding.searchResultAvailabilityMondayTime.
                setText((String) mRepairersNearYou.getmAvailability().get("Monday"));
        resultItemDetailBinding.searchResultAvailabilityTeusdayTime.
                setText((String) mRepairersNearYou.getmAvailability().get("Tuesday"));
        resultItemDetailBinding.searchResultAvailabilityWednesdayTime.
                setText((String) mRepairersNearYou.getmAvailability().get("Wednesday"));
        resultItemDetailBinding.searchResultAvailabilityThursdayTime.
                setText((String) mRepairersNearYou.getmAvailability().get("Thursday"));
        resultItemDetailBinding.searchResultAvailabilityFridayTime.
                setText((String) mRepairersNearYou.getmAvailability().get("Friday"));
        resultItemDetailBinding.searchResultAvailabilitySaturdayTime.
                setText((String) mRepairersNearYou.getmAvailability().get("Saturday"));
        resultItemDetailBinding.searchResultAvailabilitySundayTime.
                setText((String) mRepairersNearYou.getmAvailability().get("Sunday"));
    }

    private void setUpAddTipsActivity(Bundle savedInstanceState) {
        ArrayList<AddTips.AddPhotoText> addPhotoTexts = new ArrayList<>();
        mAddTipsAdapter = new AddTipsAdapter(addPhotoTexts, AdaptersConstant.ADD_TIPS_VIEW_TYPE);
        setContentView(R.layout.add_tips_layout);
        int type = 0;
        initAddTipsActivity();
        if (getIntent() != null && getIntent().hasExtra(IntentConstants.DETAIL_ACTIVITY_TIPS)) {
            type = getIntent().getIntExtra(IntentConstants.DETAIL_ACTIVITY_TIPS,
                    AdaptersConstant.VIEW_ADD_TIPS_VIEW_TYPE);
            if (type == AdaptersConstant.ADD_TIPS_VIEW_TYPE) showEditTexts();
            if (type == AdaptersConstant.VIEW_ADD_TIPS_VIEW_TYPE) hideEditTexts();
        }
        if (savedInstanceState != null)
            addPhotoTexts =
                    savedInstanceState.getParcelableArrayList(ADD_TIPS_OUT_STATE);
        mAddTipsHelperClass = new AddTipsHelperClass();

        RecyclerView recyclerView = findViewById(R.id.add_tips_add_image_text_recycler_view);
//        Toolbar toolbar = (Toolbar) ((View)recyclerView.getParent()).findViewById(R.id.toolbar);
//        toolbar.setTitle("Messages");
//        toolbar.setTitleTextColor(ContextCompat.getColor(recyclerView.getContext(), R.color.white));
//        toolbar.setNavigationIcon(R.drawable.ic_navigate_before);
//        setSupportActionBar(toolbar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAddTipsAdapter);
    }

    private void initAddTipsActivity() {
        mBackgroundImageView = (ImageView) findViewById(R.id.add_tips_image_view);
        mTitleEditText = (EditText) findViewById(R.id.add_tips_title);
        mFirstTextEditText = (EditText) findViewById(R.id.add_tips_first_text);
        mTitleTextView = (TextView) findViewById(R.id.add_tips_title_text_view);
        mFirstTextTextView = (TextView) findViewById(R.id.add_tips_first_text_text_view);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.add_tips_layout_coordinate);
        addTipsImageFab = (FloatingActionButton) findViewById(R.id.add_tips_image_fab);
        uploadTipsFab = (FloatingActionButton) findViewById(R.id.add_tips_upload_fab);
        showMoreFab = (FloatingActionButton) findViewById(R.id.add_tips_more_fab);
        mBackgroundImageView.setOnClickListener(this);
        showMoreFab.setOnClickListener(this);
        addTipsImageFab.setOnClickListener(this);
        uploadTipsFab.setOnClickListener(this);
        mParentView = (LinearLayout) findViewById(R.id.add_tips_view_group);
    }

    private void hideEditTexts() {
        mFirstTextEditText.setVisibility(View.GONE);
        mTitleEditText.setVisibility(View.GONE);
        mFirstTextTextView.setVisibility(View.VISIBLE);
        mTitleTextView.setVisibility(View.VISIBLE);
        showMoreFab.setVisibility(View.GONE);
    }

    private void showEditTexts() {
        mFirstTextEditText.setVisibility(View.VISIBLE);
        mTitleEditText.setVisibility(View.VISIBLE);
        showMoreFab.setVisibility(View.VISIBLE);
        mFirstTextTextView.setVisibility(View.GONE);
        mTitleTextView.setVisibility(View.GONE);
    }

    private void initResultItemViews() {
        mNameView = (TextView) findViewById(R.id.search_result_name);
        mLocationView = (TextView) findViewById(R.id.search_result_location);
        //mAvailabilityView = (TextView) findViewById(R.id.search_result_availability);
        mRatingBar = (RatingBar) findViewById(R.id.search_result_rating_bar);
        mMessageBtn = (Button) findViewById(R.id.search_result_message_button);
        mShowMapBtn = (Button) findViewById(R.id.search_result_view_map_button);
        mMapFragment = (View) findViewById(R.id.search_result_map_fragment);
        mViewPager = (ViewPager) findViewById(R.id.search_result_images_viewpager);
        mImageView = (ImageView) findViewById(R.id.search_result_image_view);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(IntentConstants.DETAIL_ACTIVITY_EXTRAS)) {
            switch (intent.getIntExtra(IntentConstants.DETAIL_ACTIVITY_EXTRAS, -1)) {
                case IntentConstants.ADD_TIPS_ACTIVITY:
                    outState.putParcelableArrayList(ADD_TIPS_OUT_STATE, new ArrayList<AddTips.AddPhotoText>
                            (mAddTipsAdapter.getList()));
                    //outState.putSerializable(ADD_TIPS_OUT_STATE, mAddTipsAdapter.getList());
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_tips_more_fab:
                mAddTipsHelperClass.animateFab();
                break;
            case R.id.add_tips_image_view:
                ImagePicker imagePicker = new ImagePicker(this,
                        BACKGROUND_TIPS_PICKER_CONSTANT);
                imagePicker.chooseFileIntent();
                break;
            case R.id.add_tips_upload_fab:
                addTipsUploadFabClicked();
                break;
            case R.id.add_tips_image_fab:
                ImagePicker imagePicker2 = new ImagePicker(this, TIPS_PICKER_CONSTANT);
                imagePicker2.chooseFileIntent();
                break;

        }
    }

    public void addTipsUploadFabClicked() {
        boolean notNull = false;
        boolean notEmpty = false;
        if (mTitleEditText.getText().toString() != null &&
                mFirstTextEditText.getText().toString() != null &&
                mBackgroundImageUrl != null)
            notNull = true;

        if (notNull) {
            if (!mTitleEditText.getText().toString().isEmpty() &&
                    !mFirstTextEditText.getText().toString().isEmpty() &&
                    !mBackgroundImageUrl.isEmpty())
                notEmpty = true;
        }

        if (mBackgroundImageUrl == null || mBackgroundImageUrl.isEmpty()) {
            ImagePicker backgroundImage = new ImagePicker(this,
                    BACKGROUND_TIPS_PICKER_CONSTANT);
            backgroundImage.chooseFileIntent();
            Picasso
                    .get()
                    .load(mBackgroundImageUrl)
                    .into(mBackgroundImageView);
        }
        if (notNull && notEmpty) {
            mAddTipsHelperClass.uploadTip();
        } else {
            displaySnackBar(coordinatorLayout, "Title or Text must not be empty!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TIPS_PICKER_CONSTANT && resultCode == RESULT_OK && data != null && data.getData() != null) {
            AddTips.AddPhotoText addPhotoText = new AddTips.AddPhotoText(
                    data.getData().toString(), "");
            mAddTipsAdapter.addToList(addPhotoText);
        }
        if (requestCode == BACKGROUND_TIPS_PICKER_CONSTANT && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mBackgroundImageUrl = data.getData().toString();
            Picasso
                    .get()
                    .load(mBackgroundImageUrl)
                    .into(mBackgroundImageView);
        }
    }

    private void displaySnackBar(CoordinatorLayout coordinatorLayout, String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    private class ResultItemDetailHelperClass {


    }

    private class AddTipsHelperClass {

        private boolean isFabOpen = false;
        private Animation fab_open;
        private Animation fab_close;
        private Animation fab_icon_rotate_forward;
        private Animation fab_icon_rotate_backward;
        private int count = 0;

        public AddTipsHelperClass() {
            fab_open = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anime_fab_open);
            fab_close = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anime_fab_close);
            fab_icon_rotate_forward = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anime_fab_rotate_forward);
            fab_icon_rotate_backward = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anime_fab_rotate_backward);
        }

        private void animateFab() {
            if (isFabOpen) {
                showMoreFab.startAnimation(fab_icon_rotate_backward);

                addTipsImageFab.startAnimation(fab_close);
                uploadTipsFab.startAnimation(fab_close);
                addTipsImageFab.setClickable(false);
                uploadTipsFab.setClickable(false);
                addTipsImageFab.setVisibility(View.INVISIBLE);
                uploadTipsFab.setVisibility(View.INVISIBLE);
                isFabOpen = false;
            } else {
                showMoreFab.startAnimation(fab_icon_rotate_forward);
                addTipsImageFab.startAnimation(fab_open);
                uploadTipsFab.startAnimation(fab_open);
                addTipsImageFab.setClickable(true);
                uploadTipsFab.setClickable(true);
                addTipsImageFab.setVisibility(View.VISIBLE);
                uploadTipsFab.setVisibility(View.VISIBLE);
                isFabOpen = true;
            }
        }

        private void uploadTip() {
            String tipPushId =
                    App.getsTipsDataRef()
                            .push().getKey();
            List<AddTips.AddPhotoText> addPhotoTexts = mAddTipsAdapter.getList();

//            int count = 0;
            AddTips addTips = new AddTips(mTitleEditText.getText().toString(),
                    mFirstTextEditText.getText().toString(), mBackgroundImageUrl,
                    (ArrayList<AddTips.AddPhotoText>) addPhotoTexts, tipPushId,
                    App.getsCurrentUser().getUid(), ServerValue.TIMESTAMP);

            Task<Uri> uploadHeaderImage = DatabaseHelper.uploadTipsImages
                    (mBackgroundImageUrl, tipPushId, "background_tip_image");
            uploadHeaderImage.addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        mBackgroundImageUrl = task.getResult().toString();
                        addTips.setmHeaderImageUrl(mBackgroundImageUrl);
                        Log.e(TAG, "mBackgroundImageUrl----- " + mBackgroundImageUrl);
                        uploadTipImagesTry(addPhotoTexts, tipPushId, addTips);
                    } else
                        mBackgroundImageUrl = null;
                    Log.e(TAG, "mBackgroundImageUrl----- " + mBackgroundImageUrl);
                }
            });

        }

        private void uploadTipImagesTry(List<AddTips.AddPhotoText> addPhotoTexts, String tipPushId,
                                        AddTips addTips) {
            Map<String, Boolean> mapSuccessful = new HashMap<>();
            for (AddTips.AddPhotoText addPhotoText : addPhotoTexts) {
                String name = "tips_image" + count;
                Task<Uri> uriTask = null;
                UploadTask uploadTask =
                        App.getsUsersTipsStorageRef()
                                .child(tipPushId)
                                .child(name)
                                .putFile(Uri.parse(addPhotoText.getmImageUrl()));
                uriTask = uploadTask
                        .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    return null;
                                }
                                return
                                        App.getsUsersTipsStorageRef()
                                                .child(tipPushId)
                                                .child(name)
                                                .getDownloadUrl();
                            }


                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isComplete()) {
                                    if (task.getResult() != null) {
                                        addPhotoTexts.get(count)
                                                .setmImageUrl(task.getResult().toString());
                                        mapSuccessful.put(name, true);
                                    } else {
                                        //mapSuccessful.put(name, false);
                                    }
                                    if (count == addPhotoTexts.size()) {
                                        count = 0;
                                        int successCount = 0;
                                        for (Boolean success : mapSuccessful.values()) {
                                            if (success) {
                                                successCount++;
                                                Log.e(TAG, "successCount == === ===== " + successCount);
                                            }
                                        }
                                        if (successCount == mapSuccessful.size() && mBackgroundImageUrl != null) {
                                            Log.e(TAG, "successCount == mapSuccessful.size() === =====");

                                            Map<String, Object> map = new HashMap<>();
                                            Map<String, Object> map1 = new HashMap<>();

                                            map.put(tipPushId, addTips);
                                            App.getsTipsDataRef()
                                                    .updateChildren(map)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                map1.put(tipPushId, true);
                                                                App.getsUserTipsDataRef()
                                                                        .updateChildren(map1)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                Log.e(TAG, "onComplete() tips added === =====");
                                                                                finish();
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });

                                        } else {
                                            displaySnackBar(coordinatorLayout, "Upload Failed Try again!!");
                                        }
                                        count++;
                            }
                        }

                    }

//                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    Task<Uri> uriTask =
//                                            App.getsUsersTipsStorageRef()
//                                                    .child(tipPushId)
//                                                    .child(name)
//                                                    .getDownloadUrl();
//                                    //TODO: TaskNotYetComplete error crashing app fix it pooh
//                                    addPhotoTexts.get(count)
//                                            .setmImageUrl(uriTask.getResult().toString());
//                                    mapSuccessful.put(name, true);
//                                }
//                                if (count == addPhotoTexts.size()) {
//                                    count = 0;
//                                    int successCount = 0;
//                                    for (Boolean success : mapSuccessful.values()) {
//                                        if (success) {
//                                            successCount++;
//                                            Log.e(TAG, "successCount == === ===== " + successCount);
//                                        }
//                                    }
//                                    if (successCount == mapSuccessful.size() && mBackgroundImageUrl != null) {
//                                        Log.e(TAG, "successCount == mapSuccessful.size() === =====");
//
//                                        Map<String, Object> map = new HashMap<>();
//                                        Map<String, Object> map1 = new HashMap<>();
//
//                                        map.put(tipPushId, addTips);
//                                        App.getsTipsDataRef()
//                                                .updateChildren(map)
//                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<Void> task) {
//                                                        if (task.isSuccessful()) {
//                                                            map1.put(tipPushId, true);
//                                                            App.getsUserTipsDataRef()
//                                                                    .updateChildren(map1)
//                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                        @Override
//                                                                        public void onComplete(@NonNull Task<Void> task) {
//                                                                            Log.e(TAG, "onComplete() tips added === =====");
//                                                                            finish();
//                                                                        }
//                                                                    });
//                                                        }
//                                                    }
//                                                });
//
//                                    } else {
//                                        displaySnackBar(coordinatorLayout, "Upload Failed Try again!!");
//                                    }
//                                }
//                                count++;
//                            }
//                        });

//                if (uploadTask.isSuccessful()) {
//                    Task<Uri> uriTask =
//                            App.getsUsersTipsStorageRef()
//                                    .child(tipPushId)
//                                    .child(name)
//                                    .getDownloadUrl();
//                    addPhotoTexts.get(count)
//                            .setmImageUrl(uriTask.getResult().toString());
//                    mapSuccessful.put(name, true);
//                    Log.e(TAG, "onComplete() imageUrl === " + uriTask.getResult().toString() + "=====");
//                    Log.e(TAG, "onComplete() mapSuccessful === " + mapSuccessful.toString() + "=====");
//                } else {
//                    mapSuccessful.put(name, false);
//                }
//                if (count == addPhotoTexts.size()) count = 0;
//                Log.e(TAG, "inside forLoop mapSuccessful === " + mapSuccessful.toString() + "=====");
//                count++;
                });
            }
//            int successCount = 0;
//            for (Boolean success : mapSuccessful.values()) {
//                if (success) {
//                    successCount++;
//                    Log.e(TAG, "successCount == === ===== " + successCount);
//                }
//            }
//            if (successCount == mapSuccessful.size() && mBackgroundImageUrl != null) {
//                Log.e(TAG, "successCount == mapSuccessful.size() === =====");
//
//                Map<String, Object> map = new HashMap<>();
//                Map<String, Object> map1 = new HashMap<>();
//
//                map.put(tipPushId, addTips);
//                App.getsTipsDataRef()
//                        .updateChildren(map)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    map1.put(tipPushId, true);
//                                    App.getsUserTipsDataRef()
//                                            .updateChildren(map1)
//                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    Log.e(TAG, "onComplete() tips added === =====");
//                                                    finish();
//                                                }
//                                            });
//                                }
//                            }
//                        });
//
//            } else {
//                displaySnackBar(coordinatorLayout, "Upload Failed Try again!!");
//            }
        }

        private void uploadTipImages(List<AddTips.AddPhotoText> addPhotoTexts, String tipPushId,
                                     AddTips addTips) {
            Map<String, Boolean> mapSuccessful = new HashMap<>();
            //UploadTask uploadTask = App.getsUsersStorageRef().getActiveUploadTasks().get(0);

            for (AddTips.AddPhotoText addPhotoText : addPhotoTexts) {
                String name = "tips_image" + count;
                Task<Uri> task = DatabaseHelper.uploadTipsImages(addPhotoText.getmImageUrl(),
                        tipPushId, name);
                task.addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            addPhotoTexts.get(count)
                                    .setmImageUrl(task.getResult().toString());
                            //addPhotoTexts.get(count).notify();
                            mapSuccessful.put(name, true);
                            //mapSuccessful.notify();
                            Log.e(TAG, "onComplete() imageUrl === " + task.getResult().toString() + "=====");
                            Log.e(TAG, "onComplete() mapSuccessful === " + mapSuccessful.toString() + "=====");
                            count++;
                        } else {
                            mapSuccessful.put(name, false);
                            mapSuccessful.notify();
                        }
                    }
                });
//                count++;
                Log.e(TAG, "inside forLoop mapSuccessful === " + mapSuccessful.toString() + "=====");
            }
            int successCount = 0;
            for (Boolean success : mapSuccessful.values()) {
                if (success) {
                    successCount++;
                    Log.e(TAG, "successCount == === ===== " + successCount);
                }
            }
            if (successCount == mapSuccessful.size() && mBackgroundImageUrl != null) {
                Log.e(TAG, "successCount == mapSuccessful.size() === =====");

                Map<String, Object> map = new HashMap<>();
                Map<String, Object> map1 = new HashMap<>();

                map.put(tipPushId, addTips);
                App.getsTipsDataRef()
                        .updateChildren(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    map1.put(tipPushId, true);
                                    App.getsUserTipsDataRef()
                                            .updateChildren(map1)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Log.e(TAG, "onComplete() tips added === =====");
                                                    finish();
                                                }
                                            });
                                }
                            }
                        });

            } else {
                displaySnackBar(coordinatorLayout, "Upload Failed Try again!!");
            }
        }
    }
}