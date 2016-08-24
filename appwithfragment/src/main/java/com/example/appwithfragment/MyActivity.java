package com.example.appwithfragment;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.appwithfragment.FullScreenPicture.ViewPagerFragment;
import com.example.appwithfragment.RecyclerViewFragment.LoadFromFlickrTask;
import com.example.appwithfragment.RecyclerViewFragment.OnRecyclerViewClickListener;
import com.example.appwithfragment.RecyclerViewFragment.RecyclerViewFragment;
import com.example.appwithfragment.RecyclerViewFragment.LoadTask;
import com.example.appwithfragment.TabsFragments.FavoritesFragment;
import com.example.appwithfragment.TabsFragments.TabsFragment;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Евгения on 15.08.2016.
 */
public class MyActivity extends MainActivity implements OnRecyclerViewClickListener, Serializable {
    private static final String keyContext = "Context";
    private static final String keyPosition = "position";
    public static final String protocolInterestingness = "https://api.flickr.com/services/rest/?method=flickr.interestingness.getList&" +
            "api_key=b14e644ffd373999f625f4d2ba244522&format=json&nojsoncallback=1";
    public static final String protocol = "https://api.flickr.com/services/rest/?method=flickr.tags.getClusters&" +
            "api_key=b14e644ffd373999f625f4d2ba244522&format=json&nojsoncallback=1";

    private ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;


    @Override
    public void doAction(int pos, ArrayList<ListContent> list) {

        Log.i("FrgFullScreenPicture", "create new FragmentFullScreenPicture");
        viewPagerFragment = new ViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(keyPosition, pos);
        bundle.putSerializable(keyContext, this);
        bundle.putSerializable("recyclerViewFragmentList", list);

        viewPagerFragment.setArguments(bundle);
        replaceFragment(viewPagerFragment, R.id.LL, "single_img");

    }

    RecyclerViewFragment recyclerViewFragment;
    ViewPagerFragment viewPagerFragment;

    TabsFragment tabsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



        Fragment isOpen = getSupportFragmentManager().findFragmentById(R.id.LL);

        if(isOpen== null) {
            tabsFragment = new TabsFragment();
            addFragment(tabsFragment, R.id.LL, "list_img");
            /*recyclerViewFragment = new RecyclerViewFragment();
            recyclerViewFragment.setProtocol(protocolInterestingness);
            recyclerViewFragment.setTask(new LoadFromFlickrTask());
            recyclerViewFragment.setRetainInstance(true);
            addFragment(recyclerViewFragment, R.id.LL, "list_img");*/
        }


        drawerLayout = setDrawerLayout();
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,null, R.string.isOpen, R.string.isClosed){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_today);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_today);

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("MyActivity", "onOptionsItemSelected " + item.getItemId());
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
        }else{
            super.onBackPressed();
        }
    }

    public DrawerLayout setDrawerLayout(){
        final String itemsTitles[] = getResources().getStringArray(R.array.list_items);

        final ListView listView = (ListView) findViewById(R.id.navigationList);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.navigation_draw_item, itemsTitles));

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.dddd);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemSelected(i);
                listView.setItemChecked(i, true);
                setTitle(itemsTitles[i]);
                drawerLayout.closeDrawer(listView);
            }
        });

        return drawerLayout;
    }

    public void itemSelected(int position){
        switch (position){
            case 0:
                tabsFragment.setTag("");
                break;
            case 1:
                tabsFragment.setTag("flowers");
                break;
            case 2:
                tabsFragment.setTag("animals");
                break;
            case 3:
                tabsFragment.setTag("people");
                break;
            case 4:
                tabsFragment.setTag("cities");
                break;
            case 5:
                tabsFragment.setTag("nature");
                break;
        }

    }

    public void setNewFragment(String tag){
        Fragment frg = getSupportFragmentManager().findFragmentByTag(tag);
        RecyclerViewFragment rf1;
        if(frg == null){
            rf1 = new RecyclerViewFragment();
            rf1.setProtocol(protocol);
            rf1.setTag(tag);
            rf1.setTask(new LoadTask());
            rf1.setRetainInstance(true);
        }else{
            rf1 = (RecyclerViewFragment) frg;
        }

        replaceFragment(rf1, R.id.LL, tag);
    }


}
