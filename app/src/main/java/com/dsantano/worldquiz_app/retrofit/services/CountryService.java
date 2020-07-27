package com.dsantano.worldquiz_app.retrofit.services;

import com.dsantano.worldquiz_app.models.Country;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CountryService {

    @GET("rest/v2/all")
    Call<List<Country>> allCountry();

    @GET("rest/v2/alpha/{alpha}")
    Call<Country> getCountry(@Path("alpha") String alpha);
}
