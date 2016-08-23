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

    private Fragment[] listOfFragments = new Fragment[2];
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabs_layout, null);

        viewPager = (ViewPager) view.findViewById(R.id.tabViewPager);
        viewPager.setAdapter(new TabViewPagerAdapter(this.getChildFragmentManager(), listOfFragments));

        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    public void setListOfFragments(Fragment... fragments){
        for(int i = 0; i < fragments.length; i++){
            listOfFragments[i] = fragments[i];
        }
        if( viewPager != null){
            viewPager.getAdapter().notifyDataSetChanged();
            viewPager.setCurrentItem(0);
            Log.d("TabsFragment", "setListOfFragments if ");
        }

    }
}
