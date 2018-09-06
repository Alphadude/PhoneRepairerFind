package com.example.android.phonerepairerfind.interfaces;

import com.example.android.phonerepairerfind.POJO.Contact;

import java.util.List;

public interface OnContactItemSelected {

    void toggleSelection(int position);

    void clearSelections();

    int getSelectedCount();

    List<Contact> getSelectedItems();

}
