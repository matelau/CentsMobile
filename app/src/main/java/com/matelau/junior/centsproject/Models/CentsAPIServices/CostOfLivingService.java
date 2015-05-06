package com.matelau.junior.centsproject.Models.CentsAPIServices;

import com.matelau.junior.centsproject.Models.VizModels.ColiResponse;
import com.matelau.junior.centsproject.Models.VizModels.CostOfLiving;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by matelau on 3/3/15.
 */
public interface CostOfLivingService {
    @POST("/api/v2/cost_of_living/compare")
    void getColi(@Body CostOfLiving locs, Callback<ColiResponse> response);
}
