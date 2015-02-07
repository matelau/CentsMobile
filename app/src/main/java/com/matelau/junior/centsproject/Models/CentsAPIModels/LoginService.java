package com.matelau.junior.centsproject.Models.CentsAPIModels;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by matelau on 2/3/15.
 */
//POST https://localhost:3000/api/v1/login
//        Content-Type: application/json
//        {
//        "email": "janedoe@notreal.com",
//        "password": "password123"
//        }
public interface LoginService {
    @POST("/api/v1/login")
    void login(@Body Login l, Callback<Response> cb);
}
