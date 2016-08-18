package com.example.appwithfragment.FullScreenPicture;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.appwithfragment.ListContent;
import com.example.appwithfragment.MyActivity;

import java.util.ArrayList;

/**
 * Created by e.konobeeva on 16.08.2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private static final String keyContext = "Context";
    private ArrayList<ListContent> list;
    private Fragment fragment;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<ListContent> list, Fragment fragment){
        super(fm);
        Log.d("ViewPagerAdapter", "Create adapter");
        this.list = list;
        this.fragment = fragment;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("ViewPagerAdapter", "get item 1 : " + position);
        MyActivity ctx = (MyActivity)fragment.getArguments().get(keyContext);
        return FragmentFullScreenPicture.newInstance(list.get(position), ctx);
    }

    @Override
    public int getCount() {
        if(list == null){
            Log.d("ViewPagerAdapter", "getCount null");
            return -1;
        }else
            return list.size();
    }


}
