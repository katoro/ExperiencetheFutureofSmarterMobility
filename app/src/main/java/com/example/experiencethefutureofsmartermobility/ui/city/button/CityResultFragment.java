package com.example.experiencethefutureofsmartermobility.ui.city.button;

import static com.example.experiencethefutureofsmartermobility.CommonUtil.convertJsonObjectToBytes;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.amplifyframework.api.rest.RestOptions;
import com.amplifyframework.core.Amplify;
import com.example.experiencethefutureofsmartermobility.ApiClient;
import com.example.experiencethefutureofsmartermobility.AppArgConst;
import com.example.experiencethefutureofsmartermobility.R;
import com.example.experiencethefutureofsmartermobility.RetrofitAPI;
import com.example.experiencethefutureofsmartermobility.databinding.FragmentCityResultBinding;
import com.example.experiencethefutureofsmartermobility.model.BlogContent;
import com.example.experiencethefutureofsmartermobility.model.Hotplace;
import com.example.experiencethefutureofsmartermobility.model.HotplaceImage;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.skt.tmap.TMapView;
import com.skt.tmap.overlay.TMapMarkerItem;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
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

public class CityResultFragment extends Fragment {

    private CityResultViewModel mViewModel;

    public static CityResultFragment newInstance() {
        return new CityResultFragment();
    }
    private String mCity = "";
    private String mLocalHotPlace = "";
    private String mActivity = "";

    private Hotplace hotplace = new Hotplace();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(CityResultViewModel.class);



        if (getArguments() != null) {
            mCity = getArguments().getString(AppArgConst.ARG_CITY);
            mLocalHotPlace = getArguments().getString(AppArgConst.ARG_CITY_LOCAL_HOTPLACE);
            mActivity = getArguments().getString(AppArgConst.ARG_CITY_ACTIVITY);
            hotplace = (Hotplace) getArguments().getSerializable(AppArgConst.ARG_CITY_LOCAL_HOTPLACE_NAVER_RESULT);
        }
    }

    FragmentCityResultBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentCityResultBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.tvCityResultSummary.setMovementMethod(new ScrollingMovementMethod());
        mViewModel.getmCityResultSummary().observe(getViewLifecycleOwner(), s -> {
            binding.tvCityResultSummary.setText(s);
        });
        mViewModel.getmLoadingAnimationFlag().observe(getViewLifecycleOwner(), flag ->{
            if(!flag){
                binding.animationView.cancelAnimation();
                binding.animationView.setVisibility(View.GONE);
            }
        });

        mViewModel.getmCityResultImgUrl().observe(getViewLifecycleOwner(), s -> {

            Log.d("piccasso", s);
            if(getString(R.string.activity_text1).equals(s)){
                Picasso.get().load(R.drawable.img_food).into(binding.ivCityResultVariation);
            }else if(getString(R.string.activity_text2).equals(s)){
                Picasso.get().load(R.drawable.img_gas).into(binding.ivCityResultVariation);
            }else if(getString(R.string.activity_text3).equals(s)){
                Picasso.get().load(R.drawable.img_carwash).into(binding.ivCityResultVariation);
            }else if(getString(R.string.activity_text4).equals(s)){
                Picasso.get().load(R.drawable.img_electric).into(binding.ivCityResultVariation);
            }

            downloadImagefromS3();
        });


//        postBedrockSummary(hotplace);
        getBlogSearch(mActivity);
        getHotplaceImageUrl();

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

        createTmap(hotplace);

        return root;
    }

    public void getHotplaceImageUrl(){
        RetrofitAPI retrofitInterface = ApiClient.getNaverApiClient().create(RetrofitAPI.class);

        Call<HotplaceImage> call = retrofitInterface.getImage(mCity + " " +
                ""+ mLocalHotPlace +" "+mActivity,10,1,"sim");
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<HotplaceImage> call, Response<HotplaceImage> response) {
                Log.d("TAG", response.toString());
                HotplaceImage hotplaceImage = response.body();
                Log.d("TAG", hotplaceImage.getItems().length+"개 검색됨,");

//                Random random = new Random();
//                int randomIndex = random.nextInt(hotplaceImage.getItems().length);
                uploadImageUrlList(hotplaceImage);
            }

            @Override
            public void onFailure(Call<HotplaceImage> call, Throwable t) {

                t.printStackTrace();
            }

        });

    }


    public void uploadImageUrlList(HotplaceImage hotplaceImage){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("city", mCity);
        jsonObject.addProperty("activity", mActivity);
        jsonObject.addProperty("hotplace", mLocalHotPlace);
        List<String> stringList = new ArrayList<>();
        for(HotplaceImage.Item i : hotplaceImage.getItems()){
            stringList.add(i.getLink());
        }
        JsonArray jsonArray = new Gson().toJsonTree(stringList).getAsJsonArray();
        jsonObject.add("imgurllist", jsonArray);

        RestOptions options = RestOptions.builder()
                .addPath("/uploadimgurl")
                .addBody(convertJsonObjectToBytes(jsonObject))
                .build();

        Amplify.API.post(options,
                response -> {
                    Log.d("TAG",response.getData().asString());
                },
                error -> {

                }
        );
    }

    public void createTmap(Hotplace hotplace){
        TMapView tMapView = new TMapView(getContext());
        binding.mapContainer.addView(tMapView);
        tMapView.setSKTMapApiKey("m1Cr4ruMrw8F9Sb80xjHya1b36yntkco2VxlbmFb");
        tMapView.setOnMapReadyListener(() -> {
            //todo 맵 로딩 완료 후 구현
            double lat_center = 0;
            double lon_center = 0;
            for(Hotplace.Item i : hotplace.getItems()){
                double latitude = Integer.parseInt(i.getMapx()) / 1e7; // 위도
                double longitude = Integer.parseInt(i.getMapy()) / 1e7;; // 경도
                TMapMarkerItem marker = new TMapMarkerItem();
                marker.setId("marker_"+i.getTitle());
                marker.setTMapPoint(longitude,latitude);
                marker.setIcon(BitmapFactory.decodeResource(getResources(), com.skt.tmap.R.drawable.poi));
                tMapView.addTMapMarkerItem(marker);
                lat_center+=latitude;
                lon_center+=longitude;
            }

            tMapView.setCenterPoint(lon_center/5,lat_center/5);
            tMapView.setZoomLevel(13);
        });
    }

    public void getBlogSearch(String activityCategory){
        RetrofitAPI retrofitInterface = ApiClient.getNaverApiClient().create(RetrofitAPI.class);

        List<CompletableFuture<BlogContent>> futures = new ArrayList<>();
        for(Hotplace.Item i : hotplace.getItems()){
            CompletableFuture<BlogContent> future = CompletableFuture.supplyAsync(() -> {
                try {
                    Call<BlogContent> secondCall = retrofitInterface.getBlogContent(mLocalHotPlace+" "+activityCategory+" "+i.getTitle(),5,1,"sim");
                    Response<BlogContent> secondResponse = secondCall.execute();
                    return secondResponse.body();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, Executors.newCachedThreadPool());
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(ignored -> {
                    List<BlogContent> results = new ArrayList<>();
                    for (CompletableFuture<BlogContent> future : futures) {
                        results.add(future.join());
                    }
                    return results;
                })
                .thenAccept(results -> {
                    // 결과 처리 로직
//                    for(int i=0; i<results.size(); i++){
//                        String blogDesc = "";
//                        for(BlogContent.Item item : results.get(i).getItems()){
//                            blogDesc+=item.getDescription();
//                        }
//                        if(i<hotplace.getItems().length){
//                            hotplace.getItems()[i].setDescription(blogDesc);
//                        }
//                    }
                    int minLength = Math.min(results.size(), hotplace.getItems().length);
                    for (int i = 0; i < minLength; i++) {
                        BlogContent blogContent = results.get(i);
                        Hotplace.Item item = hotplace.getItems()[i];
                        String blogDescription = blogContent.getItems().stream()
                                .map(BlogContent.Item::getDescription)
                                .collect(Collectors.joining());
                        item.setDescription(blogDescription);
                        Log.d("getBlogSearch",blogDescription);
                    }
                    postBedrockSummary(hotplace);

                });
    }
    public void postBedrockSummary(Hotplace hotplace){
        Gson gson = new Gson();
        String jsonText = gson.toJson(hotplace.getItems());

        Log.d("postBedrockSummary",jsonText);
        jsonText = "[]";

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("jsontext", jsonText);
        RestOptions options = RestOptions.builder()
                .addPath("/bedrocksummary")
                .addBody(convertJsonObjectToBytes(jsonObject))
                .build();

        Amplify.API.post(options,
                response -> {
                    Log.d("postBedrockSummary TAG",response.getData().asString());
                    mViewModel.posttmCityResultSummary(response.getData().asString());
                    mViewModel.postmLoadingAnimationFlag(false);
                },
                error -> {
                    error.printStackTrace();
                    mViewModel.postmLoadingAnimationFlag(false);
                }
        );
    }
    public void getS3ImageList(){
        //"naverimage/"+city +"/"+mLocalHotPlace.replace(" ","")+ "/" + mDateFormat.format(new Date()) + ".jpg", // S3 키 (예: "images/image.png")
        Log.d("aasd","variation/"+mCity+"/"+mLocalHotPlace.replace(" ","")+"/"+mActivity);

        Amplify.Storage.list(
                "variation/"+mCity+"/"+mLocalHotPlace.replace(" ","")+"/"+mActivity, // S3 버킷 내 폴더 경로
                result -> {
                    if(result.getItems().size()==0){
                        Log.d("result", "City Result Img는 "+ result.getItems().size()+"개");
                        requireActivity().runOnUiThread(() -> whenImgLoadFail(mActivity));
                        return;
                    }
                    Random random = new Random();
                    int randomIndex = random.nextInt(result.getItems().size());
                    mViewModel.postmCityResultImgUrl(result.getItems().get(randomIndex).getKey());
                },
                err -> {
                    Log.e("result", err.toString());
                }
        );
    }

    public void downloadImagefromS3(){

        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);

        Amplify.Storage.downloadFile(
                mViewModel.getmCityResultImgUrl().getValue(),
                new File(getContext().getCacheDir(), mDateFormat.format(new Date()) + ".jpg"),
                result1 -> {
                    Log.i("MyAmplifyApp", "Successfully downloaded: " + result1.getFile().getName());
                    // 다운로드한 이미지 파일로 작업 수행
                    Picasso.get().load(result1.getFile()).into(binding.ivCityResultVariation);
                },
                failure -> {
                    Log.e("MyAmplifyApp",  "Download Failure", failure);
//                    mViewModel.postmCityResultImgUrl(mActivity);
                    whenImgLoadFail(mActivity);
                }

        );
    }


    public void whenImgLoadFail(String s){
        if(getString(R.string.activity_text1).equals(s)){
            Picasso.get().load(R.drawable.img_food).into(binding.ivCityResultVariation);
        }else if(getString(R.string.activity_text2).equals(s)){
            Picasso.get().load(R.drawable.img_gas).into(binding.ivCityResultVariation);
        }else if(getString(R.string.activity_text3).equals(s)){
            Picasso.get().load(R.drawable.img_carwash).into(binding.ivCityResultVariation);
        }else if(getString(R.string.activity_text4).equals(s)){
            Picasso.get().load(R.drawable.img_electric).into(binding.ivCityResultVariation);
        }
    }
}