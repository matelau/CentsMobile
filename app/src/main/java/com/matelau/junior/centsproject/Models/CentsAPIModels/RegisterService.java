package com.matelau.junior.centsproject.Models.CentsAPIModels;


import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by matelau on 2/4/15. */
//POST https://localhost:3000/api/v1/register
//        Content-Type: application/json
//        {
//        "first_name": "Jane",
//        "last_name": "Doe",
//        "email": "jdoe@notreal.com",
//        "password": "password123",
//        "password_confirmation": "password123"
//        }
public interface RegisterService {
    @POST("/api/v1/register")
    void register(@Body User u, Callback<Response> s);
}
