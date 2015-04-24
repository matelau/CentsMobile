package com.matelau.junior.centsproject.Models.CentsAPIServices;

import com.matelau.junior.centsproject.Models.VizModels.RecordQuery;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by matelau on 3/3/15.
 */
public interface RecordsService {
    @POST("/api/v1/record_names")
    void getRecords(@Body RecordQuery query, Callback<String[]> s);

    @GET("/api/v2/careers")
    void getRecordsV2(Callback<String[]> s);

    @GET("/api/v2/schools")
    void getSchoolsV2(Callback<String[]> s);

    @GET("/api/v2/cost_of_living?only_state_names=false")
    void getCitiesV2(Callback<String[]> s);
}
