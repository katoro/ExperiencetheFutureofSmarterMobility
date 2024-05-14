package com.example.experiencethefutureofsmartermobility.ui.car.button;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CarResultViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<String> mCarResultSummary;
    private final MutableLiveData<String> mCarResultImgUrl;

    private final MutableLiveData<Boolean> mLoadingAnimationFlag;
    public CarResultViewModel() {
        mCarResultSummary = new MutableLiveData<>();
        mCarResultImgUrl = new MutableLiveData<>();
        mLoadingAnimationFlag = new MutableLiveData<>();
        mLoadingAnimationFlag.setValue(true);

    }

    public MutableLiveData<String> getmCarResultSummary() {
        return mCarResultSummary;
    }

    public MutableLiveData<String> getmCarResultImgUrl() {
        return mCarResultImgUrl;
    }

    public MutableLiveData<Boolean> getmLoadingAnimationFlag() {return mLoadingAnimationFlag;}

    public void postmCarResultSummary(String s){
        mCarResultSummary.postValue(s);
    }
    public void postmCarResultUrl(String s){
        mCarResultImgUrl.postValue(s);
    }
    public void postmLoadingAnimationFlag(Boolean b){
        mLoadingAnimationFlag.postValue(b);
    }


}