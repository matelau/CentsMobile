package com.matelau.junior.centsproject.Models.GoogleAPIModels;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by matelau on 11/25/14.
 */
public interface GoogleImageService {
    @GET("/ajax/services/search/images")
    void getImage(@QueryMap Map<String,String> qmap, Callback<ResponseData> cb);
}
