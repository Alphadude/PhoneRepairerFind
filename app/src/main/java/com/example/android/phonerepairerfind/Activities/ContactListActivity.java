package com.example.android.phonerepairerfind.Activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;

import com.example.android.phonerepairerfind.Adapters.ContactAdapters;
import com.example.android.phonerepairerfind.Constants.AdaptersConstant;
import com.example.android.phonerepairerfind.Fragment.ChatDetailFragment;
import com.example.android.phonerepairerfind.Fragment.ContactFragment;
import com.example.android.phonerepairerfind.R;
import com.example.android.phonerepairerfind.databinding.ActivityContactListBinding;

public class ContactListActivity extends AppCompatActivity
        implements View.OnClickListener, ContactAdapters.DataPassListener{

    public Menu menu;
   // private EditOptions mEditOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_contact_list);
        ActivityContactListBinding contactListBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_contact_list);
        setSupportActionBar(contactListBinding.toolbar);
        openContactFragment();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
////        int id = item.getItemId();
////        ContactFragment contactFragment = (ContactFragment)
////                getSupportFragmentManager().findFragmentByTag(AdaptersConstant.CONTACT_LIST_FRAGMENT_TAG);
////        if (id == R.id.action_delete) {
////            if(contactFragment == null) return false;
////            contactFragment.getAdapter().getContactViewHolder().onDeleteOptionMenuClicked();
////            //mEditOptions.onMultipleDeleteButton(true);
////            return true;
////        }
////
////        if(id == R.id.action_cancel){
////            if(contactFragment == null) return false;
////            contactFragment.getAdapter().getContactViewHolder().onCancelSelect();
////            return true;
////        }
////        return super.onOptionsItemSelected(item);
//    }

    private void openContactFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ContactFragment contactFragment = new ContactFragment();
        contactFragment.setRetainInstance(true);
        getSupportActionBar().hide();
        ft.add(R.id.activity_contact_list, contactFragment, AdaptersConstant.CONTACT_LIST_FRAGMENT_TAG);
        //ft.addToBackStack(AdaptersConstant.CONTACT_LIST_FRAGMENT_TAG);
        ft.commit();
    }

    @Override
    public void passData(String contactUsername, String lastMsgUsername, String profilePicsUrl, String pushIds) {
        ContactFragment formerFragment = (ContactFragment) getSupportFragmentManager().findFragmentByTag(
                AdaptersConstant.CONTACT_LIST_FRAGMENT_TAG);
        this.getSupportActionBar().hide();
        ChatDetailFragment fragment = new ChatDetailFragment();
        fragment.setRetainInstance(true);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(formerFragment.getId(), fragment, AdaptersConstant.CHAT_FRAGMENT_TAG);
        fragmentTransaction.addToBackStack(AdaptersConstant.CHAT_FRAGMENT_TAG);
        fragment.getReceivedData(contactUsername, lastMsgUsername, profilePicsUrl, pushIds);
        fragmentTransaction.commit();
    }

//    public void setEditOptions(EditOptions editOptions){
//        mEditOptions = editOptions;
//    }

//    public interface ActionModeViewCallBacks{
//        void onListItemSelect(final int position);
//
//        void onDestroyActionMode();
//    }
//
//    public interface ListContactActionModeViewCallbacks extends ActionModeViewCallBacks{
//        void onDeleteActionButton();
//    }
}
