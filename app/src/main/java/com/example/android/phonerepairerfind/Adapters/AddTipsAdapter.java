package com.example.android.phonerepairerfind.Adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.phonerepairerfind.Constants.AdaptersConstant;
import com.example.android.phonerepairerfind.POJO.AddTips;
import com.example.android.phonerepairerfind.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AddTipsAdapter extends RecyclerView.Adapter<AddTipsAdapter.AddTipsViewHolder> {

    private int VIEW_TYPE;
    private List<AddTips.AddPhotoText> mTipsComponents;
    private final String TAG = AddTipsAdapter.class.getSimpleName();
    public AddTipsAdapter(ArrayList<AddTips.AddPhotoText> tipsComponent, int type) {
        VIEW_TYPE = type;
        mTipsComponents = tipsComponent;
        notifyDataSetChanged();
    }

    public AddTipsAdapter(){

    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE;
    }

    @NonNull
    @Override
    public AddTipsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.
                add_tips_add_image_layout, parent, false);
        return new AddTipsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddTipsViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case AdaptersConstant.ADD_TIPS_VIEW_TYPE:
                showEditTexts(holder);
                break;
            case AdaptersConstant.VIEW_ADD_TIPS_VIEW_TYPE:
                hideEditTexts(holder);
                break;
        }
        AddTips.AddPhotoText tipsComponent = mTipsComponents.get(holder.getAdapterPosition());
        Picasso
                .get()
                .load(Uri.parse(tipsComponent.getmImageUrl()))
                .into(holder.mImageView);
        holder.mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    mEditText.setText(mTipsComponents.get(getAdapterPosition()).getmText());
//                    mEditText.setText("Hello world");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mTipsComponents.get(holder.getAdapterPosition()).setmText(holder.mEditText.getText().toString());
                //int parcelI = mTipsComponents.get(getAdapterPosition()).getmI();

                //mTipsComponents.get(getAdapterPosition()).writeToParcel(parcel, parcelI).writeString(mEditText.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //editable.append(text);
                mTipsComponents.get(holder.getAdapterPosition()).setmText(editable.toString());
                //Log.e(TAG, "........... afterTextChanged (" + mTipsComponents.get(holder.getAdapterPosition()).getmText() + ")..................");
            }
        });
        Log.e(TAG, "........... onBindViewHolder (" + mTipsComponents.get(holder.getAdapterPosition()).getmText() + ")..................");
    }


    @Override
    public int getItemCount() {
        return mTipsComponents.size();
    }

    private void hideEditTexts(AddTipsViewHolder holder){
        holder.mEditText.setVisibility(View.GONE);
        holder.mTextView.setVisibility(View.VISIBLE);
    }

    private void showEditTexts(AddTipsViewHolder holder){
        holder.mEditText.setVisibility(View.VISIBLE);
        holder.mTextView.setVisibility(View.GONE);
    }

    public class AddTipsViewHolder extends RecyclerView.ViewHolder{
        protected ImageView mImageView;
        protected EditText mEditText;
        protected TextView mTextView;

        public AddTipsViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
//            mEditText.setText(mTipsComponents.get(getLayoutPosition()).getmText());
        }

        public void initViews(View itemView){
            mImageView = (ImageView) itemView.findViewById(R.id.add_tips_add_image_view);
            mEditText = (EditText) itemView.findViewById(R.id.add_tips_add_edittext_view);
            mTextView = (TextView) itemView.findViewById(R.id.add_tips_add_text_view);
        }
    }

    public void addToList(AddTips.AddPhotoText tipComponent){
        mTipsComponents.add(tipComponent);
        notifyDataSetChanged();
    }

    public List<AddTips.AddPhotoText> getList(){
        return mTipsComponents;
    }
}
