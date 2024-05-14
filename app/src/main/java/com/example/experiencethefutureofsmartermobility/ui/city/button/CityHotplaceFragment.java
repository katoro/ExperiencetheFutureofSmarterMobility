package com.example.experiencethefutureofsmartermobility.ui.city.button;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.amplifyframework.api.rest.RestOptions;
import com.amplifyframework.core.Amplify;
import com.example.experiencethefutureofsmartermobility.AppArgConst;
import com.example.experiencethefutureofsmartermobility.R;
import com.google.android.material.button.MaterialButtonToggleGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CityHotplaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CityHotplaceFragment extends Fragment {

    private final String TAG = CityHotplaceFragment.class.getSimpleName();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private int mCheckedCityId = -1;
    private String mCity = "";

    private com.example.experiencethefutureofsmartermobility.databinding.FragmentCityHotplaceBinding binding;
    private CityHotplaceViewModel cityHotplaceViewModel;
    public CityHotplaceFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cityHotplaceViewModel = new ViewModelProvider(this).get(CityHotplaceViewModel.class);

        if (getArguments() != null) {
            mCity = getArguments().getString(AppArgConst.ARG_CITY);
            postCity(mCity);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = com.example.experiencethefutureofsmartermobility.databinding.FragmentCityHotplaceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        cityHotplaceViewModel.getmCityHotplace1().observe(getViewLifecycleOwner(), s -> {
            binding.cityHotplaceToggleBtn1.setText(s);
        });
        cityHotplaceViewModel.getmCityHotplace2().observe(getViewLifecycleOwner(), s -> {
            binding.cityHotplaceToggleBtn2.setText(s);
        });
        cityHotplaceViewModel.getmCityHotplace3().observe(getViewLifecycleOwner(), s -> {
            binding.cityHotplaceToggleBtn3.setText(s);
        });
        cityHotplaceViewModel.getmCityHotplace4().observe(getViewLifecycleOwner(), s -> {
            binding.cityHotplaceToggleBtn4.setText(s);
        });

        cityHotplaceViewModel.getmCityHotplaceLoading().observe(getViewLifecycleOwner(), b->{
            if(!b){
                binding.animationView.cancelAnimation();
                binding.animationView.setVisibility(View.GONE);
            }
        });
        binding.cityHotplaceToggleBtn1.setMaxLines(2);
        binding.cityHotplaceToggleBtn2.setMaxLines(2);
        binding.cityHotplaceToggleBtn3.setMaxLines(2);
        binding.cityHotplaceToggleBtn4.setMaxLines(2);

        binding.cityHotplaceToggle.addOnButtonCheckedListener((materialButtonToggleGroup, i, b) -> {

            Bundle bundle = new Bundle();;
            bundle.putString(AppArgConst.ARG_CITY,mCity);

            String hotplace = "";
            if(binding.cityHotplaceToggle.getCheckedButtonId()==R.id.cityHotplaceToggleBtn1){
                hotplace = cityHotplaceViewModel.getmCityHotplace1().getValue();
            }else if(binding.cityHotplaceToggle.getCheckedButtonId()==R.id.cityHotplaceToggleBtn2){
                hotplace = cityHotplaceViewModel.getmCityHotplace2().getValue();
            }else if(binding.cityHotplaceToggle.getCheckedButtonId()==R.id.cityHotplaceToggleBtn3){
                hotplace = cityHotplaceViewModel.getmCityHotplace3().getValue();
            }else if(binding.cityHotplaceToggle.getCheckedButtonId()==R.id.cityHotplaceToggleBtn4){
                hotplace = cityHotplaceViewModel.getmCityHotplace4().getValue();
            }else{
                Toast.makeText(getContext(),"핫플레이스를 선택하세요.",Toast.LENGTH_SHORT).show();
                Log.d(TAG,binding.cityHotplaceToggle.getCheckedButtonId()+"");
                return;
            }
            bundle.putString(AppArgConst.ARG_CITY_LOCAL_HOTPLACE,hotplace);

            NavController navController = Navigation.findNavController(root);
            navController.navigate(R.id.action_nav_cityhotplace_to_nav_cityactivity,bundle);
        });


        return root;
    }


    public void postCity(String city){
        RestOptions options = RestOptions.builder()
                .addPath("/city/"+city)
                .build();

        Log.i("myapp",city);

        Amplify.API.post(options,
                response -> {
                    Log.i("MyAmplifyApp", "POST succeeded: " + response.getData().asString());
                    String[] localList = response.getData().asString().split(",");
                    if(localList.length>3){
                        cityHotplaceViewModel.postmCityLocal1(localList[0].replace(" ",""));
                        cityHotplaceViewModel.postmCityLocal2(localList[1].replace(" ",""));
                        cityHotplaceViewModel.postmCityLocal3(localList[2].replace(" ",""));
                        cityHotplaceViewModel.postmCityLocal4(localList[3].replace(" ",""));
                    }
                    cityHotplaceViewModel.postmLoadingAnimationFlag(false);
                },
                error -> {
                    Log.e("MyAmplifyApp", "POST failed.", error);
                    cityHotplaceViewModel.postmLoadingAnimationFlag(false);
                }
        );
    }



}