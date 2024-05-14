package com.example.experiencethefutureofsmartermobility.ui.car.button;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.amplifyframework.api.rest.RestOptions;
import com.amplifyframework.core.Amplify;
import com.example.experiencethefutureofsmartermobility.AppArgConst;
import com.example.experiencethefutureofsmartermobility.R;
import com.example.experiencethefutureofsmartermobility.databinding.FragmentCarResultBinding;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CarResultFragment extends Fragment {

    private CarResultViewModel mViewModel;

    public static CarResultFragment newInstance() {
        return new CarResultFragment();
    }

    private String mCar;
    private String mCarBackground;

    FragmentCarResultBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CarResultViewModel.class);

        if (getArguments() != null) {
            mCar = getArguments().getString(AppArgConst.ARG_CAR);
            mCarBackground = getArguments().getString(AppArgConst.ARG_CAR_BACKGROUND);
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCarResultBinding.inflate(inflater, container, false);


        postCreateImage(mCar,mCarBackground);

        postCarManual(mCar,mCarBackground);

        mViewModel.getmCarResultSummary().observe(getViewLifecycleOwner(), s -> {
            binding.tvCarResultSummary.setText(s);
        });
        mViewModel.getmCarResultImgUrl().observe(getViewLifecycleOwner(), s -> {
            downloadImagefromS3();
        });
        mViewModel.getmLoadingAnimationFlag().observe(getViewLifecycleOwner(), flag ->{
            if(!flag){
                binding.animationCarResultTextLoading.cancelAnimation();
                binding.animationCarResultTextLoading.setVisibility(View.GONE);
            }
        });

        Completable.fromAction(() -> {
                    // 파일 업로드 작업 수행
                    getS3ImageList();
                })
                .subscribeOn(Schedulers.io()) // 백그라운드 스레드에서 실행
                .observeOn(AndroidSchedulers.mainThread()) // 결과는 메인 스레드로 전달
                .subscribe(
                        () -> { /* 파일 업로드 완료 */ },
                        error -> { /* 오류 처리 */ }
                );

        return binding.getRoot();
    }

    public void postCarManual(String car, String carBackground){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("car", car);
        jsonObject.addProperty("carbackground", carBackground);

        RestOptions options = RestOptions.builder()
                .addPath("/carmanual")
                .addBody(convertJsonObjectToBytes(jsonObject))
                .build();

        Amplify.API.post(options,
                response -> {
                    Log.d("TAG _ postCarManual",response.getData().asString());
                    mViewModel.postmCarResultSummary(response.getData().asString());
                    mViewModel.postmLoadingAnimationFlag(false);
                },
                error -> {
                    Log.e("TAG _ postCarManual",error.toString());
                }
        );
    }

    public void postCreateImage(String car, String carBackground){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("car", car);
        jsonObject.addProperty("carbackground", carBackground);

        RestOptions options = RestOptions.builder()
                .addPath("/createimage")
                .addBody(convertJsonObjectToBytes(jsonObject))
                .build();

        Amplify.API.post(options,
                response -> {
                    Log.d("TAG _ postCreateImage",response.getData().asString());
                },
                error -> {
                    Log.e("TAG _ postCreateImage",error.toString());
                }
        );
    }
    public static byte[] convertJsonObjectToBytes(JsonObject jsonObject) {
        String jsonString = jsonObject.toString();
        return jsonString.getBytes(StandardCharsets.UTF_8);
    }


    public void getS3ImageList(){
        Log.d("TAG", "car_background/"+mCar+"/"+mCarBackground);
        //"naverimage/"+city +"/"+mLocalHotPlace.replace(" ","")+ "/" + mDateFormat.format(new Date()) + ".jpg", // S3 키 (예: "images/image.png")
        Amplify.Storage.list(
                "car_background/"+mCar+"/"+mCarBackground, // S3 버킷 내 폴더 경로
                result -> {
                    Log.d("result", result.getItems().size()+"개"); // 100개중 10개 랜덤뽑기

                    Random random = new Random();
                    int randomIndex = random.nextInt(result.getItems().size());
//
                    mViewModel.postmCarResultUrl(result.getItems().get(randomIndex).getKey());
                },
                err -> {
                    Log.e("result", err.toString());
                }
        );
    }


    public void downloadImagefromS3(){

        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);

        Amplify.Storage.downloadFile(
                mViewModel.getmCarResultImgUrl().getValue(),
                new File(getContext().getCacheDir(), mDateFormat.format(new Date()) + ".jpg"),
                result1 -> {
                    Log.i("MyAmplifyApp", "Successfully downloaded: " + result1.getFile().getName());
                    // 다운로드한 이미지 파일로 작업 수행
                    requireActivity().runOnUiThread(() -> Picasso.get().load(result1.getFile()).into(binding.ivCarResultVariation));
//                    Picasso.get().load(result1.getFile()).into(binding.ivCarResultVariation);
                },
                failure -> {
                    Log.e("MyAmplifyApp",  "Download Failure", failure);
                    Picasso.get().load(R.drawable.summitseoul).into(binding.ivCarResultVariation);

                }

        );
    }
}