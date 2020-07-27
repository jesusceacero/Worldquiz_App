package com.dsantano.worldquiz_app.retrofit.services;

import com.dsantano.worldquiz_app.models.UnsplashPhotosResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UnspashService {
    @GET("search/photos")
    Call<UnsplashPhotosResult> getEspecificCountryPhotos(@Query("query") String query);
}
