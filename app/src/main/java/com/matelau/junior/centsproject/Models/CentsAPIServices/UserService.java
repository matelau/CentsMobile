package com.matelau.junior.centsproject.Models.CentsAPIServices;

import com.matelau.junior.centsproject.Models.UserModels.Id;
import com.matelau.junior.centsproject.Models.UserModels.Login;
import com.matelau.junior.centsproject.Models.UserModels.Query;
import com.matelau.junior.centsproject.Models.UserModels.User;
import com.matelau.junior.centsproject.Models.UserModels.UserResponse;
import com.matelau.junior.centsproject.Models.VizModels.SpendingElementResponse;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by matelau on 4/6/15.
 */
public interface UserService {
    @POST("/api/v2/users/validate")
    void login(@Body Login l, Callback<Id> cb);

    @POST("/api/v2/users")
    void register(@Body User u, Callback<Id> s);

    @GET("/api/v2/users/{id}/query?api_key=re5-fHO6-5CnUSglEAioWg")
    void getQueries(@Path("id") int id, Callback<Response> cb);

    @GET("/api/v2/users/{id}?api_key=re5-fHO6-5CnUSglEAioWg")
    void getProfileData(@Path("id") int id, Callback<UserResponse> cb);

    @GET("/api/v2/users/{id}/ratings?api_key=re5-fHO6-5CnUSglEAioWg")
    void getRatingsData(@Path("id") int id, Callback<UserResponse> cb);

    @GET("/api/v2/users/{id}/completed?api_key=re5-fHO6-5CnUSglEAioWg")
    void getCompletedData(@Path("id") int id, Callback<Response> cb );

    @POST("/api/v2/users/{id}/completed?api_key=re5-fHO6-5CnUSglEAioWg")
    void updateCompletedData(@Path("id") int id, @Body HashMap<String,String> section, Callback<Response> cb);

    @POST("/api/v2/users/{id}/query?api_key=re5-fHO6-5CnUSglEAioWg")
    void storeQuery(@Body Query q, @Path("id") int id, Callback<Response> cb);

    @PATCH("/api/v2/users/{id}?api_key=re5-fHO6-5CnUSglEAioWg")
    void updateFields(@Path("id") int id, @Body HashMap<String, HashMap<String, String>> fields, Callback<Response> cb);

    @GET("/api/v2/users/{id}/spending_breakdown?api_key=re5-fHO6-5CnUSglEAioWg")
    void getAllspendingData(@Path("id") int id, Callback<Response> cb);

    @GET("/api/v2/users/{id}/spending_breakdown/default?api_key=re5-fHO6-5CnUSglEAioWg")
    void getDefaultSpendingData(@Path("id") int id, Callback<SpendingElementResponse[]> cb);

    @GET("/api/v2/users/{id}/spending_breakdown/student?api_key=re5-fHO6-5CnUSglEAioWg")
    void getStudentSpendingData(@Path("id") int id, Callback<SpendingElementResponse[]> cb);

    @GET("/api/v2/users/{id}/spending_breakdown/custom?api_key=re5-fHO6-5CnUSglEAioWg")
    void getCusomSpendingData(@Path("id") int id, Callback<SpendingElementResponse[]> cb);

    @DELETE("/api/v2/users/{id}/spending_breakdown/{category}/{name}?api_key=re5-fHO6-5CnUSglEAioWg")
    void removeSpendingCategory(@Path("id") int id, @Path("category") String category, @Path("name") String name);

    @PATCH("/api/v2/users/{id}/spending_breakdown/{category}?api_key=re5-fHO6-5CnUSglEAioWg")
    void updateSpendingFields(@Path("id") int id, @Path("category") String category, @Body HashMap<String, HashMap<String,String>> fields, Callback<Response> cb);

    @PUT("/api/v2/users/{id}/spending_breakdown/{category}?api_key=re5-fHO6-5CnUSglEAioWg")
    void initSpendingFields(@Path("id") int id, @Path("category") String category, @Body HashMap<String, HashMap<String, String>> fields, Callback<Response> cb);

}
