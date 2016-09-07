package com.example.appwithfragment.FullScreenPicture;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.appwithfragment.PhotoObjectInfo;
import com.example.appwithfragment.MyActivity;
import com.example.appwithfragment.TabsFragments.IOnLikePhotoListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by e.konobeeva on 16.08.2016.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<PhotoObjectInfo> list;
    private Fragment viewPagerFragment;

    public ViewPagerAdapter(FragmentManager fm, Fragment viewPagerFragment){
        super(fm);
        Log.d("ViewPagerAdapter", "Create adapter");
        this.list = (ArrayList<PhotoObjectInfo>) viewPagerFragment.getArguments().get(MyActivity.keyList);
        this.viewPagerFragment = viewPagerFragment;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("ViewPagerAdapter", "get item 1 : " + position);
        IOnLikePhotoListener onLPListener = (IOnLikePhotoListener) viewPagerFragment.getArguments().get(MyActivity.keyLikeListener);
        return FragmentFullScreenPicture.newInstance(list.get(position), onLPListener);
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
