package com.bear.bp.ui.Love;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoveViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LoveViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is loves fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}