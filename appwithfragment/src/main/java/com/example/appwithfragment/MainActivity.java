package com.example.appwithfragment;

import android.support.v4.app.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements Serializable {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("FrgFullScreenPicture", "activity onDestroy");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    public void addFragment(Fragment fragment, int containerView, String tag){
        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
        //FragmentTransaction fragTrans = getFragmentManager().beginTransaction();
        fragTrans.add(containerView,fragment, tag);
        fragTrans.addToBackStack(null);
        fragTrans.commit();
    }

    public void replaceFragment(Fragment fragment, int containerView, String tag){
        Log.d("MyActivity", "REPLACE");
        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
        fragTrans.replace(containerView, fragment, tag);
        fragTrans.addToBackStack(null);
        fragTrans.commit();
    }



}
