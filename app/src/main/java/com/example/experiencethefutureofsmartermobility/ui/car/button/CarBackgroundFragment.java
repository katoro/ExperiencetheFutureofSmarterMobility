package com.example.experiencethefutureofsmartermobility.ui.car.button;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.experiencethefutureofsmartermobility.AppArgConst;
import com.example.experiencethefutureofsmartermobility.R;
import com.example.experiencethefutureofsmartermobility.databinding.FragmentCarBackgroundBinding;
import com.example.experiencethefutureofsmartermobility.databinding.FragmentCarSelectBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarBackgroundFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarBackgroundFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mCar;
    private String mParam2;

    private FragmentCarBackgroundBinding binding;

    public CarBackgroundFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCar = getArguments().getString(AppArgConst.ARG_CAR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCarBackgroundBinding.inflate(inflater,container,false);
        binding.radioGroupTemp.setOnCheckedChangeListener((radioGroup, i) -> {
            if(radioGroup.getCheckedRadioButtonId()==R.id.radioButtonTemp1){
                binding.carBackgroundToggleBtn1.setChecked(true);
            }else if(radioGroup.getCheckedRadioButtonId()==R.id.radioButtonTemp2){
                binding.carBackgroundToggleBtn2.setChecked(true);
            }else if(radioGroup.getCheckedRadioButtonId()==R.id.radioButtonTemp3){
                binding.carBackgroundToggleBtn3.setChecked(true);
            }else if(radioGroup.getCheckedRadioButtonId()==R.id.radioButtonTemp4){
                binding.carBackgroundToggleBtn4.setChecked(true);
            }
        });
        binding.carBackgroundToggle.setOnCheckedChangeListener((radioGroup, i) -> {
            String car_background = "";

            if(binding.carBackgroundToggle.getCheckedRadioButtonId()==R.id.carBackgroundToggleBtn1){
                car_background = getString(R.string.car_background_key1);
            }else if(binding.carBackgroundToggle.getCheckedRadioButtonId()==R.id.carBackgroundToggleBtn2){
                car_background = getString(R.string.car_background_key2);
            }else if(binding.carBackgroundToggle.getCheckedRadioButtonId()==R.id.carBackgroundToggleBtn3){
                car_background = getString(R.string.car_background_key3);
            }else if(binding.carBackgroundToggle.getCheckedRadioButtonId()==R.id.carBackgroundToggleBtn4){
                car_background = getString(R.string.car_background_key4);
            }

            Bundle bundle = new Bundle();
            bundle.putString(AppArgConst.ARG_CAR,mCar);
            bundle.putString(AppArgConst.ARG_CAR_BACKGROUND,car_background);

            NavController navController = Navigation.findNavController(binding.getRoot());
            navController.navigate(R.id.action_nav_car_background_to_nav_car_result,bundle);
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}