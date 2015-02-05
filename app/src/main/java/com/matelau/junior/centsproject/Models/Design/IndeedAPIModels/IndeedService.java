package com.matelau.junior.centsproject.Models.Design.IndeedAPIModels;

/**
 * Created by matelau on 11/23/14.
 */

import java.util.Map;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.QueryMap;

//http://api.indeed.com/ads/apisearch?publisher=4507844392785871&q=java&l=austin%2C+tx&radius=50&&jt=fulltime&limit=50&filter=0&co=us&chnl=&userip=1.2.3.4&useragent=Mozilla/%2F4.0%28Firefox%29&v=2&format=json
public interface IndeedService {
      @GET("/ads/apisearch")
      void listResults(@QueryMap Map<String, String> filters, Callback<IndeedQueryResults> cb );
}
