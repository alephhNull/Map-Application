package com.example.mapapp.ui.map;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API_Interface {
    @GET("mapbox.places/{search_text}.json?limit=20")
    Call<API_Response> getResults(@Path("search_text") String location, @Query("access_token") String token);
}
