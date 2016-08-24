package com.example.appwithfragment.TabsFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.appwithfragment.R;

/**
 * Created by e.konobeeva on 23.08.2016.
 */

//TODO make method to give tag to the adapter
public class TabsFragment extends Fragment {

    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabs_layout, null);

        viewPager = (ViewPager) view.findViewById(R.id.tabViewPager);
        viewPager.setAdapter(TabViewPagerAdapter.getInstance(getChildFragmentManager()));
        viewPager.setCurrentItem(0);

        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    public void setTag(String tag){
        TabViewPagerAdapter.getInstance(getChildFragmentManager()).setTag(tag);
        TabViewPagerAdapter.getInstance(getChildFragmentManager()).notifyDataSetChanged();
        viewPager.setCurrentItem(0);

    }
}
