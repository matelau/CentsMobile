package com.matelau.junior.centsproject.Controllers;

import android.app.Application;
import android.content.Context;

/**
 * Created by matelau on 11/20/14.
 */
public class CentsApplication extends Application{
    static Context _centsContext;

    public CentsApplication(){
        _centsContext = this;
    }

    public static Context getAppContext() {return _centsContext;}


}
