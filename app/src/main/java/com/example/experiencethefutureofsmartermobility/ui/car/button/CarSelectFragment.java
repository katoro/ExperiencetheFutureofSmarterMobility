package com.example.experiencethefutureofsmartermobility.ui.car.button;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.experiencethefutureofsmartermobility.AppArgConst;
import com.example.experiencethefutureofsmartermobility.R;
import com.example.experiencethefutureofsmartermobility.databinding.FragmentCarSelectBinding;

public class CarSelectFragment extends Fragment {

    private CarSelectViewModel mViewModel;

    public static CarSelectFragment newInstance() {
        return new CarSelectFragment();
    }

    FragmentCarSelectBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentCarSelectBinding.inflate(inflater, container, false);
        binding.radioGroupTemp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(radioGroup.getCheckedRadioButtonId()==R.id.radioButtonTemp1){
                    binding.radioButton1.setChecked(true);
                }else if(radioGroup.getCheckedRadioButtonId()==R.id.radioButtonTemp2){
                    binding.radioButton2.setChecked(true);
                }else if(radioGroup.getCheckedRadioButtonId()==R.id.radioButtonTemp3){
                    binding.radioButton3.setChecked(true);
                }else if(radioGroup.getCheckedRadioButtonId()==R.id.radioButtonTemp4){
                    binding.radioButton4.setChecked(true);
                }
            }
        });

        binding.radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {

            String car = "";

            if(binding.radioGroup.getCheckedRadioButtonId()==R.id.radioButton1){
                car = getString(R.string.car_key1);
            }else if(binding.radioGroup.getCheckedRadioButtonId()==R.id.radioButton2){
                car = getString(R.string.car_key2);
            }else if(binding.radioGroup.getCheckedRadioButtonId()==R.id.radioButton3){
                car = getString(R.string.car_key3);
            }else if(binding.radioGroup.getCheckedRadioButtonId()==R.id.radioButton4){
                car = getString(R.string.car_key4);
            }else if(binding.radioGroup.getCheckedRadioButtonId()==R.id.radioButton5){
                car = getString(R.string.car_key5);
            }

            Bundle bundle = new Bundle();
            bundle.putString(AppArgConst.ARG_CAR,car);

            NavController navController = Navigation.findNavController(binding.getRoot());
            navController.navigate(R.id.action_nav_car_select_to_nav_car_background,bundle);
        });
        return binding.getRoot();
    }

}