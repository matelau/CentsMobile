package com.matelau.junior.centsproject.Models;

import org.json.JSONObject;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by matelau on 11/24/14.
 */
public interface GlassdoorService {
    @GET("/api/api.htm")
    void listResults(@QueryMap Map<String,String> qmap, Callback<JSONObject> cb);
}
