package com.example.appwithfragment.TabsFragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by e.konobeeva on 23.08.2016.
 */
public class TabViewPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] fragments;

    public TabViewPagerAdapter(FragmentManager fm, Fragment[] fragments){
        super(fm);
        this.fragments = fragments;
    }
    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position%2 == 0){
            return "Photos";
        }else return "Liked";
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
