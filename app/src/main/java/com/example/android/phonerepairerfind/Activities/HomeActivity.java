package com.example.android.phonerepairerfind.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.phonerepairerfind.Adapters.SlidingImageAdapter;
import com.example.android.phonerepairerfind.App;
import com.example.android.phonerepairerfind.Constants.AdaptersConstant;
import com.example.android.phonerepairerfind.Constants.IntentConstants;
import com.example.android.phonerepairerfind.POJO.UserProfile;
import com.example.android.phonerepairerfind.R;
import com.example.android.phonerepairerfind.anim.SlidingImage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES= {R.drawable.image_test1,R.drawable.image_test2,R.drawable.image_test3,R.drawable.image_test4};
    private static final String[] TEXTS = {"This is image test 1", "This is image test 2", "This is image test 3", "This is image test 4"};
    private ArrayList<Integer> mImagesArray = new ArrayList<Integer>();
    private ArrayList<String> mTextArray = new ArrayList<String>();
    private SlidingImage sliderImage;
    private FragmentManager fragmentManager;
    private CircleImageView mNavProfilePics;
    private TextView mNavUsername, mNavEmail;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;

    //TODO: HANDLE ALL ROTATION PROBLEMS LIKE.. DURING SIGNUP SELECTED IMAGE CLEARS ON ROTATION


    //TODO: In the settings activity or preference do a setting to regulate the amount of time the
    //TODO: sliding image will take to slide or whether the user want to slide manually not automatically
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            if(App.getsUserDetailDataRef() == null) startActivity(new Intent(this,
                    SignInActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK));
            else
            updateUi();
        }else{
            startActivity(new Intent(this,
                    SignInActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }

    private void updateUi(){
        setContentView(R.layout.activity_home);
        //FrameLayout rootView = (FrameLayout) findViewById(R.id.contenr_home_root_view);
        //transitionContainer = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.transition_container, rootView);
        //loadingProgressBar = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anim_custom_loading_progress);

        init();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CardView searchShop = (CardView) findViewById(R.id.home_find_phone_shop);
        CardView searchRepair = (CardView) findViewById(R.id.home_find_repair_shop);
        CardView message = (CardView) findViewById(R.id.home_message_card_view);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        searchShop.setOnClickListener(this);
        searchRepair.setOnClickListener(this);
        message.setOnClickListener(this);

        //TODO: This commented line is for updating the NavHeader
//        UserProfile userProfile = FirebaseInitUser.getUserProfile();
        //UserProfile userProfile = App.getsUserProfile();
        App.getsUserDetailDataRef()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                        if(userProfile != null) {
                            if(userProfile.getmImageUrl() != null)
                                if(!userProfile.getmImageUrl().isEmpty()) {
                                    Picasso
                                            .get()
                                            .load(userProfile.getmImageUrl())
                                            .into(mNavProfilePics);
                                    mNavUsername.setText(userProfile.getmUsername());
                                    mNavEmail.setText(userProfile.getmEmail());
                                }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //onRestoreFragments();
    }

    @Override
    public void onClick(View view) {
        //view.startAnimation(loadingProgressBar);
        //transitionContainer.findViewById(R.id.custom_progress_bar_icon);
        switch (view.getId()){
            case R.id.home_find_phone_shop:
                startActivity(new Intent(HomeActivity.this, SearchResultActivity.class)
                        .putExtra(AdaptersConstant.SEARCH_USERS, AdaptersConstant.SEARCH_SHOPS_USERS));
                break;
            case R.id.home_find_repair_shop:
                startActivity(new Intent(HomeActivity.this, SearchResultActivity.class)
                    .putExtra(AdaptersConstant.SEARCH_USERS, AdaptersConstant.SEARCH_REPAIRS_USERS));
                break;
            case R.id.home_message_card_view:
                openContactFragment();
                break;
        }
    }

    private void openContactFragment(){
        Intent contactListIntent = new Intent(HomeActivity.this, ContactListActivity.class);
        startActivity(contactListIntent);
    }

    private void getTips(){
        App.getsTipsDataRef()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void init() {
        for(int i = 0; i < IMAGES.length; i++) {
            mImagesArray.add(IMAGES[i]);
            mTextArray.add(TEXTS[i]);
        }
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavProfilePics = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.user_profile_image);
        mNavEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_profile_email);
        mNavUsername = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_profile_name);
        Toast.makeText(this, mNavEmail.getText().toString(), Toast.LENGTH_SHORT).show();


        mPager = (ViewPager) findViewById(R.id.sliding_image_view_pager);

        mPager.setAdapter(new SlidingImageAdapter(this, mImagesArray, mTextArray));
        CircleIndicator circlePageIndicator = (CircleIndicator) findViewById(R.id.sliding_indicator);

        circlePageIndicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;
        sliderImage = new SlidingImage(mPager, mImagesArray, mTextArray);
        sliderImage.startSlidingImage();
        //circlePageIndicator.setRadius(5 * density);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.action_logout){
            App.signOut();
            startActivity(new Intent(this, SignInActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_customers) {

        } else if (id == R.id.nav_add_tip) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(IntentConstants.DETAIL_ACTIVITY_EXTRAS, IntentConstants.ADD_TIPS_ACTIVITY);
            intent.putExtra(IntentConstants.DETAIL_ACTIVITY_TIPS, AdaptersConstant.ADD_TIPS_VIEW_TYPE);
            startActivity(intent);
        } else if (id == R.id.nav_my_profile) {
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
