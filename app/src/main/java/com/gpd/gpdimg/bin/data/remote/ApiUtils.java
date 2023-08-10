package com.gpd.gpdimg.bin.data.remote;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.internal.Util;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtils {

    /* public static final String BASE_URL = "http://www.gpduae.com/Addbin/";
     public static String ICON_URL       = "http://gpduae.com/Addbin/data_folder/company/logo/";*/
//    public static final String BASE_URL = "http://192.168.10.111:8081/Anjitha/Gpd_technology/";
//    public static String ICON_URL       = "http://192.168.10.111:8081/Anjitha/Gpd_technology/data_folder/company/logo/";
    public static String BASE_URL = "";
    public static String ICON_URL = "";

    //    public static String BASE_URL = "http://ogesinfotech.com/Add_bin/";
//    public static String ICON_URL       = "http://ogesinfotech.com/Add_bin/data_folder/company/logo/";
    public static SOService getSOService() {
        return RetrofitClient.getClient(BASE_URL).create(SOService.class);
    }

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS).addInterceptor(interceptor)
                .protocols(Util.immutableList(Protocol.HTTP_1_1))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }

}
