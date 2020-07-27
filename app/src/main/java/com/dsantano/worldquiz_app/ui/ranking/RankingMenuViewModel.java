package com.dsantano.worldquiz_app.ui.ranking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RankingMenuViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RankingMenuViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is ranking fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}