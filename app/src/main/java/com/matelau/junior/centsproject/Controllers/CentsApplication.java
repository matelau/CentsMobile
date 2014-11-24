package com.matelau.junior.centsproject.Controllers;

import android.app.Application;
import android.content.Context;

import retrofit.RestAdapter;

/**
 * Created by matelau on 11/20/14.
 */
public class CentsApplication extends Application{
    private static Context _centsContext;
    private static RestAdapter _restAdapter = new RestAdapter.Builder().setEndpoint("http://api.glassdoor.com/api/api.htm?").build();

    public CentsApplication(){
        _centsContext = this;
    }

    public static Context getAppContext() {return _centsContext;}
    public static RestAdapter get_restAdapter() {return _restAdapter;}


}
