package com.example.wallpaper.client;

import com.example.wallpaper.model.HitsList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitAPI {

    @GET("api/")
    Observable<HitsList> getHits(@Query("key") String key);

    @GET("api/")
    Observable<HitsList> getCategoryHits(@Query("key") String key, @Query("category") String category);

    @GET("api/")
    Observable<HitsList> getSearchHits(@Query("key") String key, @Query("q") String search);

}
