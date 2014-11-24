package com.matelau.junior.centsproject.Controllers;

import android.app.Application;
import android.content.Context;

import retrofit.RestAdapter;

/**
 * Created by matelau on 11/20/14.
 */
public class CentsApplication extends Application{
    private static Context _centsContext;
    private static RestAdapter _gdRestAdapter = new RestAdapter.Builder().setEndpoint("http://api.glassdoor.com/api/api.htm?").build();
    private static RestAdapter _indeedRestAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint("http://api.indeed.com").build();

    public CentsApplication(){
        _centsContext = this;
    }

    public static Context getAppContext() {return _centsContext;}
    public static RestAdapter get_gdRestAdapter() {return _gdRestAdapter;}

    public static RestAdapter get_indeedRestAdapter() {
        return _indeedRestAdapter;
    }

    public static void set_indeedRestAdapter(RestAdapter _indeedRestAdapter) {
        CentsApplication._indeedRestAdapter = _indeedRestAdapter;
    }
}
