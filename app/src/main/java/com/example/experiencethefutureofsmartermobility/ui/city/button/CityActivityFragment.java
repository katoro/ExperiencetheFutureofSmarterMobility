package com.example.experiencethefutureofsmartermobility.ui.city.button;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.example.experiencethefutureofsmartermobility.ApiClient;
import com.example.experiencethefutureofsmartermobility.AppArgConst;
import com.example.experiencethefutureofsmartermobility.R;
import com.example.experiencethefutureofsmartermobility.RetrofitAPI;
import com.example.experiencethefutureofsmartermobility.databinding.FragmentCityActivityBinding;
import com.example.experiencethefutureofsmartermobility.model.BlogContent;
import com.example.experiencethefutureofsmartermobility.model.Hotplace;
import com.example.experiencethefutureofsmartermobility.model.HotplaceImage;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityActivityFragment extends Fragment {
    private final String TAG = CityActivityFragment.class.getSimpleName();

    public static CityActivityFragment newInstance() {
        return new CityActivityFragment();
    }

    private CityActivityViewModel mViewModel;
    private String mCity = "";
    private String mLocalHotPlace = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(CityActivityViewModel.class);

        if (getArguments() != null) {
            mCity = getArguments().getString(AppArgConst.ARG_CITY);
            mLocalHotPlace = getArguments().getString(AppArgConst.ARG_CITY_LOCAL_HOTPLACE);

//            getHotplaceImage();
        }
    }

    private FragmentCityActivityBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCityActivityBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        binding.cityActivityNextBtn.setOnClickListener(v->{
//            getHotplaceActivity();
//        });

        binding.radioGroupTemp.setOnCheckedChangeListener((radioGroup, i) -> {
            if(radioGroup.getCheckedRadioButtonId()==R.id.radioButtonTemp1){
                binding.cityActivityToggleBtn1.setChecked(true);
            }else if(radioGroup.getCheckedRadioButtonId()==R.id.radioButtonTemp2){
                binding.cityActivityToggleBtn2.setChecked(true);
            }else if(radioGroup.getCheckedRadioButtonId()==R.id.radioButtonTemp3){
                binding.cityActivityToggleBtn3.setChecked(true);
            }else if(radioGroup.getCheckedRadioButtonId()==R.id.radioButtonTemp4){
                binding.cityActivityToggleBtn4.setChecked(true);
            }
        });

        binding.cityActivityToggle.setOnCheckedChangeListener((radioGroup, i) -> {
            getHotplaceActivity();
        });


        return root;
    }

    public void getHotplaceImage(){
        RetrofitAPI retrofitInterface = ApiClient.getNaverApiClient().create(RetrofitAPI.class);


        Call<HotplaceImage> call = retrofitInterface.getImage(mCity + " " +
                ""+ mLocalHotPlace +" 사진",100,1,"sim");
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<HotplaceImage> call, Response<HotplaceImage> response) {
                Log.d("TAG", response.toString());
                HotplaceImage hotplaceImage = response.body();
                Log.d("TAG", hotplaceImage.getItems().length+"개 검색됨,");


                Random random = new Random();
                int randomIndex = random.nextInt(hotplaceImage.getItems().length);

                uploadImageToS3(hotplaceImage.getItems()[randomIndex].getLink());
                Picasso
                        .get()
                        .load(hotplaceImage.getItems()[randomIndex].getLink())
                        .into(binding.imageView);
            }

            @Override
            public void onFailure(Call<HotplaceImage> call, Throwable t) {

                t.printStackTrace();
            }

        });

    }

    public void getHotplaceActivity(){
        RetrofitAPI retrofitInterface = ApiClient.getNaverApiClient().create(RetrofitAPI.class);

        Bundle bundle = new Bundle();
        bundle.putString(AppArgConst.ARG_CITY,mCity);
        bundle.putString(AppArgConst.ARG_CITY_LOCAL_HOTPLACE,mLocalHotPlace);


        String activityCategory = "";
        if(binding.cityActivityToggle.getCheckedRadioButtonId()==R.id.cityActivityToggleBtn1){
            activityCategory = "맛집";
        }else if(binding.cityActivityToggle.getCheckedRadioButtonId()==R.id.cityActivityToggleBtn2){
            activityCategory = "주유소";
        }else if(binding.cityActivityToggle.getCheckedRadioButtonId()==R.id.cityActivityToggleBtn3){
            activityCategory = "세차장";
        }else if(binding.cityActivityToggle.getCheckedRadioButtonId()==R.id.cityActivityToggleBtn4){
            activityCategory = "전기차충전소";
        }else{
            Toast.makeText(getContext(),"할것을 선택하세요.",Toast.LENGTH_SHORT).show();
            return;
        }
        bundle.putString(AppArgConst.ARG_CITY_ACTIVITY,activityCategory);

        Log.d("getHotplaceActivity", mLocalHotPlace+" "+activityCategory);

        Call<Hotplace> call = retrofitInterface.getLocalActivity(mLocalHotPlace+" "+activityCategory,10,1,"random");
        call.enqueue(new Callback<Hotplace>() {
            @Override
            public void onResponse(Call<Hotplace> call, Response<Hotplace> response) {
                Hotplace hotplace = response.body();
                bundle.putSerializable(AppArgConst.ARG_CITY_LOCAL_HOTPLACE_NAVER_RESULT, hotplace);

                NavController navController = Navigation.findNavController(binding.getRoot());
                navController.navigate(R.id.action_nav_cityactivity_to_nav_cityresult, bundle);
            }

            @Override
            public void onFailure(Call<Hotplace> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public String getBlogSearch(String activityCategory, String detailContent){
        RetrofitAPI retrofitInterface = ApiClient.getNaverApiClient().create(RetrofitAPI.class);
        Call<BlogContent> call = retrofitInterface.getBlogContent(mLocalHotPlace+" "+activityCategory+" "+detailContent,10,1,"sim");

        String description = "";
        try {
            Response<BlogContent> blogContentResponse = call.execute();

            for(BlogContent.Item i : blogContentResponse.body().getItems()){
                description += i.getDescription();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return description;
    }
    public void uploadImageToS3(String imageUrl) {

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            // Permission is not granted
            Log.d("TAG", "READ_EXTERNAL_STORAGE is not granted");
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }


        new Thread(() -> {
            try {
                // 이미지 URL에서 Bitmap 가져오기
                Bitmap bitmap = getBitmapFromURL(imageUrl);

                SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);

                // Bitmap을 ByteArray로 변환
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                byte[] imageData = outputStream.toByteArray();

                // S3 업로드
                Amplify.Storage.uploadInputStream(
                        "naverimage/"+mCity +"/"+mLocalHotPlace.replace(" ","")+ "/" + mDateFormat.format(new Date()) + ".jpg", // S3 키 (예: "images/image.png")
                        new ByteArrayInputStream(imageData),
                        result -> Log.i(TAG, "Successfully uploaded image: " + result.getKey()),
                        error -> Log.e(TAG, "Upload failed", error)
                );
            } catch (IOException e) {
                Log.e(TAG, "Error fetching image from URL", e);
            }
        }).start();
    }

    private Bitmap getBitmapFromURL(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream input = connection.getInputStream();
        return BitmapFactory.decodeStream(input);
    }

}