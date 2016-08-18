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
    private static final String keyPosition = "position";
    ViewPager vp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ViewPagerFragment", "create view");
    }
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("ViewPagerFragment", "onCreateView");
        View view = inflater.inflate(R.layout.view_pager, null);
        vp = (ViewPager)view.findViewById(R.id.viewPager);

        final ViewPagerAdapter vpAdapter = new ViewPagerAdapter(this.getChildFragmentManager(), new RecyclerViewFragment().getList(), this);

        vp.setAdapter(vpAdapter);
        vp.setCurrentItem((int)getArguments().get(keyPosition));

        return view;


    }

    @Override
    public void onDestroy() {
        vp.clearOnPageChangeListeners();
        super.onDestroy();
        Log.d("ViewPagerFragment", "destroy view");
    }



}
