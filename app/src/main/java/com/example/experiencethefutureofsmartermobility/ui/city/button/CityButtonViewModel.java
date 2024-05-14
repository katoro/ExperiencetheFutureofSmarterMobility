package com.example.experiencethefutureofsmartermobility.ui.city.button;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CityButtonViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CityButtonViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is CityButton fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}