package com.example.experiencethefutureofsmartermobility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "https://d3ic6ryvcaoqdy.cloudfront.net/";
    private static final String NAVER_BASE_URL = "https://openapi.naver.com/v1/";
    private static Retrofit retrofit;

    public static Retrofit getApiClient()
    {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }

    public static Retrofit getNaverApiClient()
    {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request newRequest = originalRequest.newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept","*/*")
                        .addHeader("X-Naver-Client-Id", "ljDmfTJ4LyoZJdFBZp4U")
                        .addHeader("X-Naver-Client-Secret", "y4Xi7oFNNA")
                        .build();
                return chain.proceed(newRequest);
            }
        };

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .build();



        if (retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(NAVER_BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }


}
