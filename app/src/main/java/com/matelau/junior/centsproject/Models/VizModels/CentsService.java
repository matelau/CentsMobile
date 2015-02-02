package com.matelau.junior.centsproject.Models.VizModels;

import com.matelau.junior.centsproject.Models.User.User;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Cents API service used to communicate with our WEB API
 */
public interface CentsService {
    @POST("/api/v1/register")
    void register(@Body User user, Callback<String> cb);

    @POST("/api/v1/coli")
    void coliQuery(@Body coliQuery q, Callback<String> cb);

}
