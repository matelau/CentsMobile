package com.matelau.junior.centsproject.Models.CentsAPIModels;

import com.matelau.junior.centsproject.Models.VizModels.MajorQuery;
import com.matelau.junior.centsproject.Models.VizModels.MajorResponse;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by matelau on 3/5/15.
 */
public interface MajorService {
    @POST("/api/v1/majors")
    void getMajorInfo(@Body MajorQuery majs, Callback<MajorResponse> response);
}
