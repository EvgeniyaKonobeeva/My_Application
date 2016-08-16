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

import com.example.appwithfragment.ListContent;
import com.example.appwithfragment.MyActivity;

import java.util.ArrayList;

/**
 * Created by e.konobeeva on 16.08.2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private static final String keyContext = "Context";
    private static final String keyPosition = "position";
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
        int pos = position + (Integer)fragment.getArguments().get(keyPosition);
        Log.d("ViewPagerAdapter", "get item 2 : " + pos);
        MyActivity ctx = (MyActivity)fragment.getArguments().get(keyContext);

        return FragmentFullScreenPicture.newInstance(list.get(pos), ctx);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
