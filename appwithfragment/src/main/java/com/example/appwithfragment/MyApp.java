package com.example.appwithfragment;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by e.konobeeva on 26.08.2016.
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
