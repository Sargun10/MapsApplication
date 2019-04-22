package com.example.mapapplication.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * single instance of retrofit is created.
 */
public class RetrofitClient {
    private static String BASE_URL = "https://maps.googleapis.com/maps/";
    private static Retrofit mRetrofit = null;

    public RetrofitClient() {
    }

    public static Retrofit getApiService() {
        return initRetrofitService(BASE_URL);
    }

    private static Retrofit initRetrofitService(String url) {
        if (mRetrofit == null) {
            mRetrofit=new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }
}

