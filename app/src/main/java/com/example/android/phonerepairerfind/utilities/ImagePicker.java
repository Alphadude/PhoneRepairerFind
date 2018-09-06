package com.example.android.phonerepairerfind.utilities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

public class ImagePicker {
    private Activity activity;
    private int imagePickerConstant;
    public ImagePicker(Activity activity, int imagePickerConstant){
        this.activity = activity;
        this.imagePickerConstant = imagePickerConstant;
    }

    public void chooseFileIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Profile Pics"), imagePickerConstant);
    }

    public String getFileExtension(Uri uri) {
        if(uri == null){
            Toast.makeText(activity, "Insert an image", Toast.LENGTH_SHORT).show();
            return null;
        }
        ContentResolver cR = activity.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

//    public void loadImage(Uri imageUriPath, View v){
//        Picasso
//                .get()
//                .load(imageUriPath)
//                .into(v);
//    }
}
