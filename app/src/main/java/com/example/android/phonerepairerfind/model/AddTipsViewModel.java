package com.example.android.phonerepairerfind.model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class AddTipsViewModel extends ViewModel {

    private MutableLiveData<List<Object>> tips;
    private List<Object> tipsView = new ArrayList<>();

    public MutableLiveData<List<Object>> getTips() {
        if(tips == null){
            tips = new MutableLiveData<List<Object>>();
            tips.setValue(tipsView);
        }
        return tips;
    }

    public void addTips(Object tipsView){
        if(tipsView instanceof View){
            this.tipsView.add(tipsView);
        }
    }
}
