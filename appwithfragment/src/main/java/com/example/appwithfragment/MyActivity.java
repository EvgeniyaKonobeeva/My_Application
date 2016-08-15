package com.example.appwithfragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import com.example.appwithfragment.FullScreenPicture.FragmentFullScreenPicture;
import com.example.appwithfragment.RecyclerViewFragment.RecyclerViewFragment;

import java.io.Serializable;

/**
 * Created by Евгения on 15.08.2016.
 */
public class MyActivity extends MainActivity implements RecyclerViewFragment.OnRecyclerViewClickListener, Serializable {
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

        replaceFragment(fragment2, R.id.LL, "single_img");
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
            addFragment(fragment1, R.id.LL, "list_img");
        }

    }



}
