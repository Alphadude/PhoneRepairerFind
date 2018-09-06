package com.example.android.phonerepairerfind.DIalog;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.phonerepairerfind.Constants.AdaptersConstant;
import com.example.android.phonerepairerfind.Fragment.ChatDetailFragment;
import com.example.android.phonerepairerfind.R;
import com.example.android.phonerepairerfind.databinding.FragmentShowImageDialogBinding;
import com.example.android.phonerepairerfind.utilities.DatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

public class ShowImageDialogFragment  extends DialogFragment implements View.OnClickListener{
    private Uri imageUriPath;
    FragmentShowImageDialogBinding showImageDialogBinding;
    private String childPushId;
    private String imageUrl;

    public ShowImageDialogFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setDetails(Uri imageUrl){
        this.imageUriPath = imageUrl;
        //this.childPushId = childPushId;

    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        showImageDialogBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_show_image_dialog, container, false);
        showImageDialogBinding.imagePrieviewSend.setOnClickListener(this);
        return showImageDialogBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(imageUriPath != null){
            Picasso
                    .get()
                    .load(imageUriPath)
                    .error(R.color.colorLightGray)
                    .into(showImageDialogBinding.imagePreview);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.image_prieview_send){
            Task<Uri> task =
            DatabaseHelper
                    .uploadChatImage(imageUriPath, childPushId);
            //task.co
            task
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            ChatDetailFragment chatDetailFragment = (ChatDetailFragment)
                                    ((AppCompatActivity) getActivity()).getSupportFragmentManager().findFragmentByTag
                                            (AdaptersConstant.CHAT_FRAGMENT_TAG);
                            if(task.isSuccessful()){
                                imageUrl = task.getResult().toString();
                                if(chatDetailFragment != null) {
                                    chatDetailFragment.sendImage(imageUrl);
                                    dismiss();
                                }
                            }else {
//                                imageUrl = task.getResult().toString();
//                                if(chatDetailFragment != null) {
//                                    chatDetailFragment.sendImage(imageUrl);
//                                }
                                dismiss();
                                Toast.makeText(getActivity(), "Image not sent, try again",
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
            //dismiss();
        }
            //DatabaseHelper.putChatImage
    }

}
