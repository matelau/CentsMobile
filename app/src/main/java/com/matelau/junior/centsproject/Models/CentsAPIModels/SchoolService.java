package com.matelau.junior.centsproject.Models.CentsAPIModels;

import com.matelau.junior.centsproject.Models.VizModels.SchoolResponse;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by matelau on 3/4/15.
 */
public interface SchoolService {
    @POST("/api/v1/schools")
    void getSchools(@Body SchoolRequest s, Callback<SchoolResponse> response);
}
