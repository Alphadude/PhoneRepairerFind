package com.example.android.phonerepairerfind.Fragment;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.android.phonerepairerfind.Adapters.ContactAdapters;
import com.example.android.phonerepairerfind.POJO.Contact;
import com.example.android.phonerepairerfind.R;
import com.example.android.phonerepairerfind.model.ContactViewModel;
import com.example.android.phonerepairerfind.utilities.DatabaseHelper;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment implements
        ContactAdapters.ListContactActionModeViewCallbacks{

    private final String TAG = ChatDetailFragment.class.getSimpleName();
    private ContactAdapters.DataPassListener dataPassListener;
    private ContactViewModel mModel;
    private RecyclerView recyclerView;
    private List<Contact> mContacts = new ArrayList<Contact>();
    private ContactAdapters mContactAdapter;
    private FrameLayout mEmptyStateView;
    private ActionMode.Callback mActionModeCallback;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            dataPassListener = (ContactAdapters.DataPassListener) getActivity();
        }catch (ClassCastException e){
            throw new ClassCastException("Error in retrieving data pls try again");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        setUpContactListFragment(view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactAdapter = new ContactAdapters(getActivity().getSupportFragmentManager());
        mContactAdapter.setActivity(getActivity());
        mModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        FirebaseApp.initializeApp(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setAdapter(mContactAdapter);
        mContactAdapter.setRecyclerView(recyclerView);
        mContactAdapter.setActionListSelected(this);

        if(mModel != null){
            LiveData<List<Contact>> msgLiveData = mModel.getContactListLiveData();
            msgLiveData.observe(getActivity(), (List<Contact> contacts) ->{
                mContacts = contacts;
                mContactAdapter.setList(contacts);
                //Log.e(TAG, "ContactList: ++++++++++++++ " + mContacts.toString() + " ++++++++++++++ ");
            });
        }
//        if(mContacts.isEmpty()){
//            mEmptyStateView.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.INVISIBLE);
//        }else{
//            mEmptyStateView.setVisibility(View.INVISIBLE);
//            recyclerView.setVisibility(View.VISIBLE);
//        }
    }

    public ContactAdapters getAdapter(){
        return mContactAdapter;
    }

    private void setUpContactListFragment(View view){
//        ArrayList<Contact> contacts = new ArrayList<Contact>();
//
//        String pushId = "";
//        int count = 0; //Test
//        for(int i = 0; i < 2; i++){
//            DatabaseReference databaseReference = FirebaseInitUser.getContactDataRef().push();
//            pushId = databaseReference.getKey();
////            databaseReference.setValue(contact);
//            if(count == 0){
//                databaseReference.setValue(new Contact("Sanderson", "you are mehn", "Sanderson","09:00 pm",
//                        R.drawable.image_test1, "12/09/18", pushId, 3, false, false, false));
////                contacts.add(new Contact("Sanderson", "you are mehn", "Sanderson","09:00 pm",
////                        R.drawable.image_test1, "12/09/18", pushId, 3, false, false, false));
//            }else if(count == 1){
//                databaseReference.setValue(new Contact("Madness", "yeah!!", "Jasper Essien","02:00 pm",
//                        R.drawable.image_test1, "11/09/18", pushId, 0, false, false, false));
////                contacts.add(new Contact("Madness", "yeah!!", "Jasper Essien","02:00 pm",
////                        R.drawable.image_test1, "11/09/18", pushId, 0, false, false, false));
//            }
//            count++;
//        }
        recyclerView = view.findViewById(R.id.contacts_recycler_view);

        Toolbar toolbar = (Toolbar) ((View)recyclerView.getParent()).findViewById(R.id.toolbar);
        toolbar.setTitle("Messages");

        toolbar.setTitleTextColor(ContextCompat.getColor(recyclerView.getContext(), R.color.white));
        //toolbar.setNavigationIcon(R.drawable.ic_navigate_before);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        if(appCompatActivity != null && appCompatActivity.getSupportActionBar() != null) {
            appCompatActivity.setSupportActionBar(toolbar);
            appCompatActivity.getSupportActionBar().setHomeButtonEnabled(true);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mContactAdapter);
        mEmptyStateView = (FrameLayout) view.findViewById(R.id.contact_empty_state_view);
    }

    public ActionMode actionMode;
    @Override
    public void onListItemSelect(int position) {
        mContactAdapter.toggleSelection(position);

        final boolean hasCheckedItems = mContactAdapter.getSelectedItems().size() > 0;

        if(hasCheckedItems && actionMode == null){
            actionMode = ((AppCompatActivity)getActivity())
                    .startSupportActionMode(new ListContactToolbarActionModeCallbacks(
                            this, mContactAdapter));

        }else if(!hasCheckedItems && actionMode != null){
            actionMode.finish();
        }

        if(actionMode != null){
            actionMode.setTitle(String.valueOf(mContactAdapter.getSelectedCount()));
        }
    }

    @Override
    public void onDestroyActionMode() {
        actionMode = null;
    }

    @Override
    public void onDeleteActionButton() {
        DatabaseHelper.deleteContact(mContactAdapter.getSelectedItems());
    }

    @Override
    public void onCancelActionButton() {

    }

    @Override
    public void onAddToCustomerList() {

    }

    private class ListContactToolbarActionModeCallbacks implements ActionMode.Callback{

        private final ContactAdapters.ListContactActionModeViewCallbacks actionModeViewCallBacks;
        private  final ContactAdapters contactAdapters;

        public ListContactToolbarActionModeCallbacks(
                final ContactAdapters.ListContactActionModeViewCallbacks actionModeViewCallBacks,
                ContactAdapters contactAdapters){
            this.actionModeViewCallBacks = actionModeViewCallBacks;
            this.contactAdapters = contactAdapters;
        }
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.item_selecteed_edit_actions, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.findItem(R.id.action_cancel).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_delete:
                    actionModeViewCallBacks.onDeleteActionButton();
                    mode.finish();
                    return true;
                case R.id.action_cancel:
                    actionModeViewCallBacks.onCancelActionButton();
                    mode.finish();
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionModeViewCallBacks.onDestroyActionMode();
            contactAdapters.clearSelections();
        }
    }
}
