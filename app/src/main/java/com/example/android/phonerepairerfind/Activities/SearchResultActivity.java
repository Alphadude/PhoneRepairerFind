package com.example.android.phonerepairerfind.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.example.android.phonerepairerfind.Adapters.RepairersNearYouAdapter;
import com.example.android.phonerepairerfind.Constants.AdaptersConstant;
import com.example.android.phonerepairerfind.POJO.RepairersNearYou;
import com.example.android.phonerepairerfind.POJO.UserProfile;
import com.example.android.phonerepairerfind.R;
import com.example.android.phonerepairerfind.model.SearchUsersViewModel;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    private RecyclerView mResultRecyclerView;
    private ImageView loadingProgressBar;
    private Animation loadingProgressBarAnim;
    private boolean isEnded = false;
    private SearchUsersViewModel model;
    private RepairersNearYouAdapter mAdapter;
    UserProfile userProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
//        mAdapter = new RepairersNearYouAdapter();

        Intent intent = getIntent();
        int searchType = AdaptersConstant.SEARCH_REPAIRS_USERS;
        model = ViewModelProviders.of(this).get(SearchUsersViewModel.class);
        FirebaseApp.initializeApp(this);
        mAdapter = new RepairersNearYouAdapter(model, this);
        //mAdapter.setActivity(this);
        if(intent != null && intent.hasExtra(AdaptersConstant.SEARCH_USERS)){
            searchType = intent.getIntExtra(AdaptersConstant.SEARCH_USERS, AdaptersConstant.SEARCH_REPAIRS_USERS);
        }
        CoordinatorLayout rootView = (CoordinatorLayout) findViewById(R.id.activity_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mResultRecyclerView = findViewById(R.id.repairers_search_result_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mResultRecyclerView.setLayoutManager(layoutManager);
        mResultRecyclerView.setAdapter(mAdapter);
        mResultRecyclerView.setHasFixedSize(true);
        mAdapter.setRecyclerView(mResultRecyclerView);
        //TODO: CLEAR LIST BEFORE ADDING ITEM WHEN DATACHANGES
        mAdapter.clearList();
        Log.e("SearchResultActivity", "*********** I'm in here in searchResultActivity*************");
        List<RepairersNearYou> repairersNearYouList = new ArrayList<RepairersNearYou>();
        if(model != null){
            model.setUsersToSearchFor(searchType);
            model.setActivity(this);
            model.setAdapter(mAdapter);
            model.getList();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mAdapter.clearList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.clearList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.clearList();
    }
}
