package com.dsantano.worldquiz_app.ui.countrys;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CountryMenuViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CountryMenuViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is country fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}