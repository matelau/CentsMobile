package com.matelau.junior.centsproject.Models.CentsAPIServices;

import com.matelau.junior.centsproject.Models.VizModels.MajorQuery;
import com.matelau.junior.centsproject.Models.VizModels.MajorResponse;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by matelau on 3/5/15.
 */
public interface MajorService {
    @POST("/api/v1/majors")
    void getMajorInfo(@Body MajorQuery majs, Callback<MajorResponse> response);

    @PUT("/api/v2/degrees/{level}/{name}/{rating}")
    void rateMajor(@Path("level") String lvl, @Path("name") String majorName, @Path("rating") int rating, @Body HashMap<String, Integer> id, Callback<Response> cb);
}
