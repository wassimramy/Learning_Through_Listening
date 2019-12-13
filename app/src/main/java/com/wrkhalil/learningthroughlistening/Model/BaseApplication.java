package com.wrkhalil.learningthroughlistening.Model;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        BaseApplication.context = getApplicationContext(); //Return the context
    }

    public static Context getAppContext() {
        return BaseApplication.context;
    }

}
