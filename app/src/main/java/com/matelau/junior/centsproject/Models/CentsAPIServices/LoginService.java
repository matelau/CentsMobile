package com.matelau.junior.centsproject.Models.CentsAPIServices;

import com.matelau.junior.centsproject.Models.UserModels.Id;
import com.matelau.junior.centsproject.Models.UserModels.Login;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by matelau on 2/3/15.
 */
//POST https://trycents.com/api/v2/users/79
//        Content-Type: application/json
//        {
//        "email": "janedoe@notreal.com",
//        "password": "password123"
//        }
public interface LoginService {
    @POST("/api/v2/users/validate")
    void login(@Body Login l, Callback<Id> cb);
}
