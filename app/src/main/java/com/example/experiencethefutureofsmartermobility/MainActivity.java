package com.example.experiencethefutureofsmartermobility;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.amplifyframework.api.rest.RestOptions;
import com.amplifyframework.core.Amplify;
import com.example.experiencethefutureofsmartermobility.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



//        binding.question1Toggle.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
//            if(isChecked){
//                listener.onFragment1Interaction(checkedId);
//            }
//        });

//        if(!mParam1.isEmpty()){
//            switch (mParam1){
//                case "spring":
//                    binding.question1Toggle.check(R.id.question1ToggleBtn1);
//                    break;
//                case "summer":
//                    binding.question1Toggle.check(R.id.question1ToggleBtn2);
//                    break;
//                case "autumn":
//                    binding.question1Toggle.check(R.id.question1ToggleBtn3);
//                    break;
//                case "winter":
//                    binding.question1Toggle.check(R.id.question1ToggleBtn4);
//                    break;
//            }
//        }

        postCity("City");
    }

    public void postCity(String city){
        RestOptions options = RestOptions.builder()
                .addPath("/city/"+city)
//                .addBody("{\"name\":\"Mow the lawn\"}".getBytes())
                .build();

        Amplify.API.post(options,
                response -> Log.i("MyAmplifyApp", "POST succeeded: " + response.getData().asString()),
                error -> Log.e("MyAmplifyApp", "POST failed.", error)
        );
    }
}