package com.example.appwithfragment;

import android.app.Application;

import com.example.appwithfragment.DataBasePack.DBHelper;
import com.facebook.stetho.Stetho;

/**
 * Created by e.konobeeva on 26.08.2016.
 */
public class MyApp extends Application {
    private static DBHelper dBHelper;
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        dBHelper = DBHelper.getInstance(getApplicationContext());
    }

    public static DBHelper getDBHelper()
    {
        return  dBHelper;
    }
}
