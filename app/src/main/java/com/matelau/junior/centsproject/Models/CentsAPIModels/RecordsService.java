package com.matelau.junior.centsproject.Models.CentsAPIModels;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by matelau on 3/3/15.
 */
public interface RecordsService {
    @POST("/api/v1/record_names")
    void getRecords(@Body RecordQuery query, Callback<Response> s);
}
