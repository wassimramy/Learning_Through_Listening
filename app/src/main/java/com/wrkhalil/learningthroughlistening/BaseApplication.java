package com.wrkhalil.learningthroughlistening;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;

public class BaseApplication extends Application {
    private static Context context;
    public static ProgressDialog progressDialog;

    public void onCreate() {
        super.onCreate();
        BaseApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return BaseApplication.context;
    }

}
