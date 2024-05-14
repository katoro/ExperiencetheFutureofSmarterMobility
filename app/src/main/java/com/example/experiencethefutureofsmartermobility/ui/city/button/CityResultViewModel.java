package com.example.experiencethefutureofsmartermobility.ui.city.button;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CityResultViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<String> mCityResultSummary;

    private final MutableLiveData<String> mCityResultImgUrl;

    private final MutableLiveData<Boolean> mLoadingAnimationFlag;

    public CityResultViewModel() {
        mCityResultSummary = new MutableLiveData<>();
        mLoadingAnimationFlag = new MutableLiveData<>();
        mCityResultImgUrl = new MutableLiveData<>();
        mLoadingAnimationFlag.setValue(true);
    }
    public MutableLiveData<String> getmCityResultSummary() {
        return mCityResultSummary;
    }

    public MutableLiveData<String> getmCityResultImgUrl() {
        return mCityResultImgUrl;
    }

    public MutableLiveData<Boolean> getmLoadingAnimationFlag() {return mLoadingAnimationFlag;}

    public void posttmCityResultSummary(String s){
        mCityResultSummary.postValue(s);
    }
    public void postmLoadingAnimationFlag(Boolean b){
        mLoadingAnimationFlag.postValue(b);
    }

    public void postmCityResultImgUrl(String s){mCityResultImgUrl.postValue(s);}
}