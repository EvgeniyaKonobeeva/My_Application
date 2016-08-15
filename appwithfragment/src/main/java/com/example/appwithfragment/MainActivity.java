package com.example.appwithfragment;

import android.app.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

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
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    public void addFragment(Fragment fragment, int containerView, String tag){
        FragmentTransaction fragTrans = getFragmentManager().beginTransaction();
        fragTrans.add(containerView, fragment, tag);
        fragTrans.addToBackStack(null);
        fragTrans.commit();
    }

    public void replaceFragment(Fragment fragment, int containerView, String tag){
        FragmentTransaction fragTrans = getFragmentManager().beginTransaction();
        fragTrans.replace(containerView, fragment, tag);
        fragTrans.addToBackStack(null);
        fragTrans.commit();
    }



}
