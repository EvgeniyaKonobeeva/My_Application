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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.appwithfragment.FullScreenPicture.ViewPagerFragment;
import com.example.appwithfragment.RecyclerViewFragment.LoadFromFlickrTask;
import com.example.appwithfragment.RecyclerViewFragment.OnRecyclerViewClickListener;
import com.example.appwithfragment.RecyclerViewFragment.RecyclerViewFragment;
import com.example.appwithfragment.my_package.LoadTask;

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

        Log.i("FrgFullScreenPicture", "create new FragmentFullScreenPicture");
        viewPagerFragment = new ViewPagerFragment();
        bundle = new Bundle();
        bundle.putInt(keyPosition, pos);
        bundle.putSerializable(keyContext, this);

        viewPagerFragment.setArguments(bundle);
        replaceFragment(viewPagerFragment, R.id.LL, "single_img");

    }

    RecyclerViewFragment recyclerViewFragment;
    ViewPagerFragment viewPagerFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        /*Fragment frg =  getSupportFragmentManager().findFragmentByTag("list_img");
        if(frg != null)
            recyclerViewFragment = (RecyclerViewFragment) frg;
        else{
            recyclerViewFragment = new RecyclerViewFragment();
            recyclerViewFragment.setProtocol("https://api.flickr.com/services/rest/?method=flickr.interestingness.getList&" +
                    "api_key=b14e644ffd373999f625f4d2ba244522&format=json&nojsoncallback=1");
        }*/

        /*Fragment frg2 =  getSupportFragmentManager().findFragmentByTag("single_img");
        if(frg2 != null)
            viewPagerFragment = (ViewPagerFragment) frg2;
        else viewPagerFragment = new ViewPagerFragment();*/




        Fragment isOpen = getSupportFragmentManager().findFragmentById(R.id.LL);

        if(isOpen== null) {
            recyclerViewFragment = new RecyclerViewFragment();
            recyclerViewFragment.setProtocol("https://api.flickr.com/services/rest/?method=flickr.interestingness.getList&" +
                    "api_key=b14e644ffd373999f625f4d2ba244522&format=json&nojsoncallback=1");
            recyclerViewFragment.setTask(new LoadFromFlickrTask());
            recyclerViewFragment.setRetainInstance(true);
            addFragment(recyclerViewFragment, R.id.LL, "list_img");
        }


        drawerLayout = setDrawerLayout();
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




    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        actionBarDrawerToggle.syncState();
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
                //setTitle(itemsTitles[i]);
                drawerLayout.closeDrawer(listView);
            }
        });



        return drawerLayout;
    }

    public void itemSelected(int position){
        RecyclerViewFragment rf1;
        Fragment frg = null;
        switch (position){
            case 0:
                frg = getSupportFragmentManager().findFragmentByTag("list_img");
                if(frg != null)
                    recyclerViewFragment = (RecyclerViewFragment) frg;
                else{
                    recyclerViewFragment = new RecyclerViewFragment();
                    recyclerViewFragment.setProtocol("https://api.flickr.com/services/rest/?method=flickr.interestingness.getList&" +
                            "api_key=b14e644ffd373999f625f4d2ba244522&format=json&nojsoncallback=1");
                    recyclerViewFragment.setTask(new LoadFromFlickrTask());
                }
                replaceFragment(recyclerViewFragment, R.id.LL, "list_img");

                break;
            case 1:
                frg = getSupportFragmentManager().findFragmentByTag("tag_"+position);
                Log.d("POSITION", " " + position);
                if(frg == null){
                    rf1 = new RecyclerViewFragment();
                    rf1.setProtocol("https://api.flickr.com/services/rest/?method=flickr.tags.getClusters" +
                            "&api_key=b14e644ffd373999f625f4d2ba244522&format=json" +
                            "&nojsoncallback=1");
                    rf1.setTag("flowers");
                    rf1.setTask(new LoadTask());
                    rf1.setRetainInstance(true);
                    replaceFragment(rf1, R.id.LL, "tag_"+position);
                }else{
                    rf1 = (RecyclerViewFragment) frg;
                    replaceFragment(rf1, R.id.LL, "tag_"+position);
                }
                break;
            case 2:
                 frg = getSupportFragmentManager().findFragmentByTag("tag_"+position);
                Log.d("POSITION", " " + position);
                if(frg == null){
                    rf1 = new RecyclerViewFragment();
                    rf1.setProtocol("https://api.flickr.com/services/rest/?method=flickr.tags.getClusters" +
                            "&api_key=b14e644ffd373999f625f4d2ba244522&format=json" +
                            "&nojsoncallback=1");
                    rf1.setTag("animals");
                    rf1.setTask(new LoadTask());
                    rf1.setRetainInstance(true);
                }else{
                    rf1 = (RecyclerViewFragment) frg;
                }

                replaceFragment(rf1, R.id.LL, "tag_"+position);
                break;
            case 3:
                 frg = getSupportFragmentManager().findFragmentByTag("tag_"+position);
                Log.d("POSITION", " " + position);
                if(frg == null){
                    rf1 = new RecyclerViewFragment();
                    rf1.setProtocol("https://api.flickr.com/services/rest/?method=flickr.tags.getClusters" +
                            "&api_key=b14e644ffd373999f625f4d2ba244522&format=json" +
                            "&nojsoncallback=1");
                    rf1.setTag("city");
                    rf1.setTask(new LoadTask());
                    rf1.setRetainInstance(true);
                }else{
                    rf1 = (RecyclerViewFragment) frg;
                }

                replaceFragment(rf1, R.id.LL, "tag_"+position);
        }

    }
    @Override
    public void setTitle(CharSequence title) {
        //mTitle = title;
        getActionBar().setTitle(title);
    }


}
