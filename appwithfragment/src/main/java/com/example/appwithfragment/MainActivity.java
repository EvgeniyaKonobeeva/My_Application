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

    private Bundle bundle;


    @Override
    public void doAction(ListContent listContent) {

       if(fragment2 == null){
           Log.i("FrgFullScreenPicture", "create new FragmentFullScreenPicture");
           fragment2 = new FragmentFullScreenPicture();
       }

        bundle = new Bundle();
        bundle.putCharSequence(keyUrl,listContent.getImgUrl());
        bundle.putSerializable(keyContext,this);
        bundle.putCharSequence(keyTitle, listContent.getFullTitle());
        fragment2.setArguments(bundle);


        FragmentTransaction fragTrans = getFragmentManager().beginTransaction();
        fragTrans.replace(R.id.LL, fragment2, "single_img");
        fragTrans.addToBackStack(null);
        fragTrans.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("FrgFullScreenPicture", "activity onDestroy");
       // fragment2 = null;
    }

    RecyclerViewFragment fragment1;
    FragmentFullScreenPicture fragment2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment frg =  getFragmentManager().findFragmentByTag("list_img");
        if(frg != null)
            fragment1 = (RecyclerViewFragment) frg;
        else{
            fragment1 = new RecyclerViewFragment();
        }

        Fragment frg2 =  getFragmentManager().findFragmentByTag("single_img");

        if(frg2 != null)
            fragment2 = (FragmentFullScreenPicture) frg2;
        else fragment2 = new FragmentFullScreenPicture();

        fragment2.setRetainInstance(true);
        fragment1.setRetainInstance(true);


        Fragment isOpen = getFragmentManager().findFragmentById(R.id.LL);

        if(isOpen== null) {
            fragment1 = new RecyclerViewFragment();
            fragment1.setRetainInstance(true);
            FragmentTransaction fragTrans = getFragmentManager().beginTransaction();
            fragTrans.add(R.id.LL, fragment1, "list_img");
            fragTrans.addToBackStack(null);
            fragTrans.commit();
        }



    }


    @Override
    public void onBackPressed() {
        Log.i("FrgFullScreenPicture", "call to onBackPressed");
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
