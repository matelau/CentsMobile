package com.matelau.junior.centsproject.Models.VizModels;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Cents API service used to communicate with our WEB API
 */
public interface CentsService {
    @POST("/api/v1/coli")
    void coliQuery(@Body COLQuery q, Callback<ColiResponse> cb);
}
