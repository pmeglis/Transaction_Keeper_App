package com.example.raide_000.transactionkeeper;

import android.app.Application;
import android.content.Context;

/**
 * myapplication.java
 *
 * This class is used just to get the context of the app.
 *
 * Created by Peter Meglis on 10/27/2016.
 */
public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
