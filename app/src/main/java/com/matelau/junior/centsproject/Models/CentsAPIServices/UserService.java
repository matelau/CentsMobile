package com.matelau.junior.centsproject.Models.CentsAPIServices;

import com.matelau.junior.centsproject.Models.UserModels.Field;
import com.matelau.junior.centsproject.Models.UserModels.Query;
import com.matelau.junior.centsproject.Models.UserModels.UserResponse;

import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by matelau on 4/6/15.
 */
public interface UserService {
    @GET("/api/v2/users/{id}/query")
    void getQueries(@Path("id") int id, Callback<Response> cb);

    @GET("/api/v2/users/{id}")
    void getProfileData(@Path("id") int id, Callback<UserResponse> cb);

    @GET("/api/v2/users/{id}/ratings")
    void getRatingsData(@Path("id") int id, Callback<UserResponse> cb);

    @GET("/api/v2/users/{id}/completed")
    void getCompletedData(@Path("id") int id, Callback<Response> cb );

    @POST("/api/v2/users/{id}/completed")
    void updateCompletedData(@Path("id") int id, @Body HashMap<String,String> section, Callback<Response> cb);

    @POST("/api/v2/users/{id}/query")
    void storeQuery(@Body Query q, @Path("id") int id, Callback<Response> cb);

    @PATCH("/api/v2/users/{id}")
    void updateFields(@Path("id") int id, @Body HashMap<String, List<Field>> fields, Callback<Response> cb);



}
