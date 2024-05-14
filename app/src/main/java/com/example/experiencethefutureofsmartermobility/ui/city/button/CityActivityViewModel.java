package com.example.experiencethefutureofsmartermobility.ui.city.button;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.experiencethefutureofsmartermobility.model.Hotplace;

public class CityActivityViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<Hotplace> mHotplace;

    public CityActivityViewModel() {
        mHotplace = new MutableLiveData<>();
    }


}