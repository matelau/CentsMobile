package com.matelau.junior.centsproject.Models.CentsAPIModels;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by matelau on 3/31/15.
 */
public interface CareerService {
    @POST("/api/v2/careers")
    void getCareerInfo(@Body CareerQuery careers, Callback<CareerResponse> response);
}
