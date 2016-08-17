package com.example.appwithfragment;

import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
    private ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;


    @Override
    public void doAction(int pos) {

        //if(viewPagerFragment == null){
            Log.i("FrgFullScreenPicture", "create new FragmentFullScreenPicture");
            viewPagerFragment = new ViewPagerFragment();
        //}

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

        ListView listView = (ListView) findViewById(R.id.navigationList);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.list_view_items, getResources().getStringArray(R.array.list_items)));

        drawerLayout = (DrawerLayout) findViewById(R.id.dddd);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.isOpen, R.string.isClosed){
            @Override
            public void onConfigurationChanged(Configuration newConfig) {
                super.onConfigurationChanged(newConfig);
                Log.d("ActionBarDrawerToggle", "onConfigurationChanged");

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d("ActionBarDrawerToggle", "onDrawerClosed");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d("ActionBarDrawerToggle", "onDrawerOpened");
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                Log.d("ActionBarDrawerToggle", "onOptionsItemSelected");
                return super.onOptionsItemSelected(item);

            }
        };



        drawerLayout.addDrawerListener(actionBarDrawerToggle);

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

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        actionBarDrawerToggle.syncState();
    }
}
