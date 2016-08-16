package com.example.appwithfragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import com.example.appwithfragment.FullScreenPicture.ViewPagerFragment;
import com.example.appwithfragment.RecyclerViewFragment.OnRecyclerViewClickListener;
import com.example.appwithfragment.RecyclerViewFragment.RecyclerViewFragment;

import java.io.Serializable;

/**
 * Created by Евгения on 15.08.2016.
 */
public class MyActivity extends MainActivity implements OnRecyclerViewClickListener, Serializable {
    private static final String keyContext = "Context";
    private static final String keyPosition = "position";
    private Bundle bundle;


    @Override
    public void doAction(int pos) {

        if(viewPagerFragment == null){
            Log.i("FrgFullScreenPicture", "create new FragmentFullScreenPicture");
            viewPagerFragment = new ViewPagerFragment();
        }

        bundle = new Bundle();
        /*bundle.putCharSequence(keyUrl,listContent.getImgUrl());
        bundle.putSerializable(keyContext,this);
        bundle.putCharSequence(keyTitle, listContent.getFullTitle());*/

        bundle.putInt(keyPosition, pos);
        bundle.putSerializable(keyContext, this);

        viewPagerFragment.setArguments(bundle);
        //viewPagerFragment.setFragmentManager();
        replaceFragment(viewPagerFragment, R.id.LL, "single_img");
    }

    RecyclerViewFragment recyclerViewFragment;
    ViewPagerFragment viewPagerFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Fragment frg =  getSupportFragmentManager().findFragmentByTag("list_img");
        if(frg != null)
            recyclerViewFragment = (RecyclerViewFragment) frg;
        else{
            recyclerViewFragment = new RecyclerViewFragment();
        }

        Fragment frg2 =  getSupportFragmentManager().findFragmentByTag("single_img");
        if(frg2 != null)
            viewPagerFragment = (ViewPagerFragment) frg2;
        else viewPagerFragment = new ViewPagerFragment();

        //viewPagerFragment.setRetainInstance(true);
        recyclerViewFragment.setRetainInstance(true);


        Fragment isOpen = getSupportFragmentManager().findFragmentById(R.id.LL);

        if(isOpen== null) {
            recyclerViewFragment = new RecyclerViewFragment();
            recyclerViewFragment.setRetainInstance(true);
            addFragment(recyclerViewFragment, R.id.LL, "list_img");
        }

    }



}
