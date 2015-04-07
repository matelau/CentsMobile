package com.matelau.junior.centsproject.Models.CentsAPIServices;

import com.matelau.junior.centsproject.Models.UserModels.Query;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by matelau on 4/6/15.
 */
public interface UserService {
    @POST("/api/v2/users/{id}/query")
    void storeQuery(@Body Query q, @Path("id") int id, Callback<Response> cb);

    @GET("/api/v2/users/{id}/query")
    void getQueries(@Path("id") int id, Callback<Response> cb);

}
