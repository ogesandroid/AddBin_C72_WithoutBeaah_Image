package com.gpd.gpdimg.bin.data.remote;


import com.gpd.gpdimg.bin.data.model.CapacityResponse;
import com.gpd.gpdimg.bin.data.model.GovernorateResponse;
import com.gpd.gpdimg.bin.data.model.ManufacturerResponse;
import com.gpd.gpdimg.bin.data.model.UploadBinDetailsResponse;
import com.gpd.gpdimg.bin.data.model.WillayatResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface SOService {

    @GET("get_oges_apiv2.php?p=1")
    Call<ArrayList<GovernorateResponse>> getGovernorate();

    @GET("get_oges_apiv2.php?p=2")
    Call<ArrayList<WillayatResponse>> getWillayat();

    @GET("get_oges_apiv2.php?p=3")
    Call<ArrayList<CapacityResponse>> getCapacity();


    @GET("get_oges_apiv2.php?p=4")
    Call<ArrayList<ManufacturerResponse>> getManufacturer();

    @POST("get_oges_apiv2.php?")
    @FormUrlEncoded
    Call<ArrayList<GovernorateResponse>> getGovernorate1(@Field("p") String p, @Field("company_id") String company_id);

    @POST("get_oges_apiv2.php?")
    @FormUrlEncoded
    Call<ArrayList<WillayatResponse>> getWillayat1(@Field("p") String p, @Field("company_id") String company_id);

    @POST("get_oges_apiv2.php?")
    @FormUrlEncoded
    Call<ArrayList<CapacityResponse>> getCapacity1(@Field("p") String p, @Field("company_id") String company_id);


    @POST("get_oges_apiv2.php?")
    @FormUrlEncoded
    Call<ArrayList<ManufacturerResponse>> getManufacturer1(@Field("p") String p, @Field("company_id") String company_id);


    @Headers("Content-Type: application/json")
    @POST("get_oges_apiv2.php?p=5")
    Call<UploadBinDetailsResponse> uploadBinDetails(@Body String body);


}