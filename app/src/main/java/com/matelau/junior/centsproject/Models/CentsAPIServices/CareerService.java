package com.matelau.junior.centsproject.Models.CentsAPIServices;

import com.matelau.junior.centsproject.Models.VizModels.CareerQuery;
import com.matelau.junior.centsproject.Models.VizModels.CareerResponse;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by matelau on 3/31/15.
 */
public interface CareerService {
    @POST("/api/v2/careers")
    void getCareerInfo(@Body CareerQuery careers, Callback<CareerResponse> response);

    @PUT("/api/v2/careers/{name}/{rating}")
    void rateCareer(@Path("name")String careerName, @Path("rating") int rating, @Body HashMap<String, Integer> id, Callback<Response> cb);
}
