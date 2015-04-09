package com.matelau.junior.centsproject.Models.CentsAPIServices;

import com.matelau.junior.centsproject.Models.VizModels.SchoolRequest;
import com.matelau.junior.centsproject.Models.VizModels.SchoolResponse;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by matelau on 3/4/15.
 */
public interface SchoolService {
    @POST("/api/v1/schools")
    void getSchools(@Body SchoolRequest s, Callback<SchoolResponse> response);

    @PUT("/api/v2/schools/{name}/{rating}")
    void rateSchool(@Path("name") String uni, @Path("rating") int rating, @Body HashMap<String, Integer> user, Callback<Response> cb);
}
