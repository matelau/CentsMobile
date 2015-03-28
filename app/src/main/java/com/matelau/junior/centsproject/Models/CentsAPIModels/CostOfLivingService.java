package com.matelau.junior.centsproject.Models.CentsAPIModels;

import com.matelau.junior.centsproject.Models.VizModels.ColiResponse;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by matelau on 3/3/15.
 */
public interface CostOfLivingService {
    @POST("/api/v1/coli")
    void getColi(@Body CostOfLiving locs, Callback<ColiResponse> response);
}
