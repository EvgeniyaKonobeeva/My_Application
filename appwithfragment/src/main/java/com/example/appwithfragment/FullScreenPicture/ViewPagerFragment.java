package com.example.appwithfragment.FullScreenPicture;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appwithfragment.R;
import com.example.appwithfragment.RecyclerViewFragment.RecyclerViewFragment;

/**
 * Created by e.konobeeva on 16.08.2016.
 */
public class ViewPagerFragment extends Fragment {
    FragmentManager fm;
    ViewPager vp;
    public void setFragmentManager(FragmentManager fm){
        this.fm = fm;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ViewPagerFragment", "create view");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("ViewPagerFragment", "onCreateView");
        View view = inflater.inflate(R.layout.view_pager, null);
         vp = (ViewPager)view.findViewById(R.id.viewPager);

        ViewPagerAdapter vpAdapter = new ViewPagerAdapter(this.getChildFragmentManager(), RecyclerViewFragment.list, this);

        vp.setAdapter(vpAdapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("ViewPagerFragment", "onPageScrolled" + position);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("ViewPagerFragment", "onPageSelected" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                Log.d("ViewPagerFragment", "onPageScrollStateChanged" + state);
            }
        });

        return view;


    }

    @Override
    public void onDestroy() {
        vp.clearOnPageChangeListeners();
        super.onDestroy();
        Log.d("ViewPagerFragment", "destroy view");
    }
}
