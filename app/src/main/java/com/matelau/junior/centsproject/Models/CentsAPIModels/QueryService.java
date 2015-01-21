package com.matelau.junior.centsproject.Models.CentsAPIModels;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;


/**
 * Created by matelau on 1/15/15.
 */
//http://current_ip:6001\query\#{query_string} with GET
public interface QueryService {
    @GET("/query/{query}")
    void results(@Path("query") String query, Callback<QueryService> cb );
}


