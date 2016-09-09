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

public class TabsFragment extends Fragment {

    private ViewPager viewPager;
    private TabViewPagerAdapter adapter;
    private String category;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TabsFragment", "onCreateView " );
        View view = inflater.inflate(R.layout.tabs_layout, null);

        viewPager = (ViewPager) view.findViewById(R.id.tabViewPager);
        viewPager.setOffscreenPageLimit(1);
        adapter = new TabViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        adapter.setTag(category);
        adapter.notifyDataSetChanged();

        viewPager.setCurrentItem(0);



        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    public void setCategory(String category){
        Log.d("TabsFragment", "setCategory " + category);
        this.category = category;
        if(viewPager !=null && viewPager.getAdapter() != null){
            viewPager.setCurrentItem(0);
            Log.d("TabsFragment", "setCategory if " + category);
            adapter.setTag(category);
            adapter.notifyDataSetChanged();

        }
        //viewPager.getAdapter().saveState();



    }
    public ViewPager getViewPager(){
        return viewPager;
    }

    public String getCategory(){
        return category;
    }
}
