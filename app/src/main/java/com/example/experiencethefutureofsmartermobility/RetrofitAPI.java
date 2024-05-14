package com.example.experiencethefutureofsmartermobility;


import com.example.experiencethefutureofsmartermobility.model.BlogContent;
import com.example.experiencethefutureofsmartermobility.model.Hotplace;
import com.example.experiencethefutureofsmartermobility.model.HotplaceImage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitAPI {

//    @Multipart
//    @POST("emotion")
//    Call <List<UploadResult>>uploadReceipt(
//            @Header("Cookie") String sessionIdAndRz,
//            @Part MultipartBody.Part file
//            @Part("items") RequestBody items,
//            @Part("isAny") RequestBody isAny
//    );
//    @Multipart
//    @POST("emotion")
//    Call<ResponseBody> uploadFile(@Part MultipartBody.Part image);

//    @Multipart
//    @POST("emotion")
//    Call<EmotionResult> uploadFile(@Body RequestBody body);


    @GET("search/local.json")
    Call<Hotplace> getLocalActivity(
//            @Header("X-Naver-Client-Id")String clientId,
//            @Header("X-Naver-Client-Secret")String clientSecret,
            @Query("query") String query,
            @Query("display") Integer display,
            @Query("start") Integer start,
            @Query("sort") String sort
    );

    @GET("search/blog.json")
    Call<BlogContent> getBlogContent(
            @Query("query") String query,
            @Query("display") Integer display,
            @Query("start") Integer start,
            @Query("sort") String sort
    );

    @GET("search/image")
    Call<HotplaceImage> getImage(
            @Query("query") String query,
            @Query("display") Integer display,
            @Query("start") Integer start,
            @Query("sort") String sort
    );


}
