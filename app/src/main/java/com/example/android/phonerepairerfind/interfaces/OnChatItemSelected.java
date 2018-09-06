package com.example.android.phonerepairerfind.interfaces;

import com.example.android.phonerepairerfind.POJO.Chat;

import java.util.List;

public interface OnChatItemSelected {
    void toggleSelection(int position);

    void clearSelections();

    int getSelectedCount();

    List<Chat> getSelectedItems();

}
