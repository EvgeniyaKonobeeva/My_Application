package com.example.appwithfragment;

import android.app.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.appwithfragment.FullScreenPicture.*;
import com.example.appwithfragment.RecyclerViewFragment.RecyclerViewFragment;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements RecyclerViewFragment.OnRecyclerViewClickListener, Serializable{

    private static final String keyUrl = "URL";
    private static final String keyContext = "Context";
    private static final String keyTitle = "Title";
    private FragmentFullScreenPicture fragment2;
    private Bundle bundle;


    @Override
    public void doAction(ListContent listContent) {
       if(fragment2 == null){
           fragment2 = new FragmentFullScreenPicture();
       }
        FragmentTransaction fragTrans = getFragmentManager().beginTransaction();
        fragTrans.replace(R.id.LL, fragment2);
        fragTrans.addToBackStack(null);
        fragTrans.commit();
        bundle = new Bundle();
        bundle.putCharSequence(keyUrl,listContent.getImgUrl());
        bundle.putSerializable(keyContext,this);
        bundle.putCharSequence(keyTitle, listContent.getFullTitle());
        fragment2.setArguments(bundle);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        com.example.appwithfragment.RecyclerViewFragment.RecyclerViewFragment fragment = new RecyclerViewFragment();
        FragmentTransaction fragTrans= getFragmentManager().beginTransaction();
        fragTrans.add(R.id.LL, fragment);
        fragTrans.addToBackStack(null);
        fragTrans.commit();
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


}
