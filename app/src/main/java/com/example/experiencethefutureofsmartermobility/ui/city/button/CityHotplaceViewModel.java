package com.example.experiencethefutureofsmartermobility.ui.city.button;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CityHotplaceViewModel extends ViewModel {

    private final MutableLiveData<Boolean> mCityHotplaceLoading;

    private final MutableLiveData<String> mCityHotplace1;
    private final MutableLiveData<String> mCityHotplace2;
    private final MutableLiveData<String> mCityHotplace3;
    private final MutableLiveData<String> mCityHotplace4;


    public CityHotplaceViewModel() {
        mCityHotplace1 = new MutableLiveData<>();
        mCityHotplace2 = new MutableLiveData<>();
        mCityHotplace3 = new MutableLiveData<>();
        mCityHotplace4 = new MutableLiveData<>();

        mCityHotplaceLoading = new MutableLiveData<>();
        mCityHotplaceLoading.setValue(true);
    }
    public MutableLiveData<String> getmCityHotplace1() {
        return mCityHotplace1;
    }

    public MutableLiveData<String> getmCityHotplace2() {
        return mCityHotplace2;
    }

    public MutableLiveData<String> getmCityHotplace3() {
        return mCityHotplace3;
    }

    public MutableLiveData<String> getmCityHotplace4() {
        return mCityHotplace4;
    }

    public void postmCityLocal1(String s){
        mCityHotplace1.postValue(s);
    }
    public void postmCityLocal2(String s){
        mCityHotplace2.postValue(s);
    }
    public void postmCityLocal3(String s){
        mCityHotplace3.postValue(s);
    }public void postmCityLocal4(String s){
        mCityHotplace4.postValue(s);
    }


    public MutableLiveData<Boolean> getmCityHotplaceLoading() {
        return mCityHotplaceLoading;
    }

    public void postmLoadingAnimationFlag(Boolean b){
        mCityHotplaceLoading.postValue(b);
    }
}