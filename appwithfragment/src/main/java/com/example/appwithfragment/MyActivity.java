package com.example.appwithfragment;

import android.content.res.Configuration;
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

import com.example.appwithfragment.DataBasePack.DBHelper;
import com.example.appwithfragment.FullScreenPicture.ViewPagerFragment;
import com.example.appwithfragment.RecyclerViewFragment.Categories;
import com.example.appwithfragment.RecyclerViewFragment.OnRecyclerViewClickListener;
import com.example.appwithfragment.RecyclerViewFragment.RecyclerViewFragment;
import com.example.appwithfragment.RecyclerViewFragment.Tasks.LoadTask;
import com.example.appwithfragment.TabsFragments.IOnLikePhotoListener;
import com.example.appwithfragment.TabsFragments.TabsFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Евгения on 15.08.2016.
 */
public class MyActivity extends MainActivity implements OnRecyclerViewClickListener, Serializable {
    public static final String keyContext = "Context";
    public static final String keyPosition = "position";
    public static final String keyList = "recyclerViewFragmentList";
    public static final String keyLikeListener = "likeListener";


    public static final String protocolInterestingness = "https://api.flickr.com/services/rest/?method=flickr.interestingness.getList&" +
            "api_key=b14e644ffd373999f625f4d2ba244522&format=json&nojsoncallback=1";
    public static final String protocol = "https://api.flickr.com/services/rest/?method=flickr.tags.getClusters&" +
            "api_key=b14e644ffd373999f625f4d2ba244522&format=json&nojsoncallback=1";


    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private ViewPagerFragment viewPagerFragment;
    private TabsFragment tabsFragment;
    private DBHelper dBHelper;


    @Override
    public void doAction(int pos, List<ListContent> list, IOnLikePhotoListener onLPListener) {

        Log.i("FrgFullScreenPicture", "create new FragmentFullScreenPicture");
        viewPagerFragment = new ViewPagerFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(keyPosition, pos);
        bundle.putSerializable(keyContext, this);
        bundle.putSerializable(keyList, new ArrayList<ListContent>(list));
        bundle.putSerializable(keyLikeListener, onLPListener);

        viewPagerFragment.setArguments(bundle);
        replaceFragment(viewPagerFragment, R.id.LL, "single_img");

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
// /data/data/com.example.appwithfragment/cache
        dBHelper = new DBHelper(getApplicationContext());

        Fragment isOpen = getSupportFragmentManager().findFragmentById(R.id.LL);

        if(isOpen== null) {
            Log.d("MyActivity", "onCreate isOpen " + isOpen);
            tabsFragment = new TabsFragment();
            tabsFragment.setDBHelper(dBHelper);
            tabsFragment.setTag("");
            tabsFragment.setRetainInstance(true);
            addFragment(tabsFragment, R.id.LL, "list_img");
        }
        Log.d("MyActivity", "onCreate isOpen " + isOpen);


        drawerLayout = setDrawerLayout();
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,null, R.string.isOpen, R.string.isClosed){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
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
        Fragment frg = getSupportFragmentManager().findFragmentByTag("list_img");
        if(frg == null){
            tabsFragment = new TabsFragment();
            tabsFragment.setDBHelper(dBHelper);
        }else {
            tabsFragment = (TabsFragment) frg;
        }
        switch (position){
            case 0:
                Log.d("MyActivity" , "position " + position);
                tabsFragment.setTag("");
                break;
            case 1:
                Log.d("MyActivity" , "position " + position);
                tabsFragment.setTag(Categories.flowers.toString());
                break;
            case 2:
                Log.d("MyActivity" , "position " + position);
                tabsFragment.setTag(Categories.animals.toString());
                break;
            case 3:
                Log.d("MyActivity" , "position " + position);
                tabsFragment.setTag(Categories.people.toString());
                break;
            case 4:
                Log.d("MyActivity" , "position " + position);
                tabsFragment.setTag(Categories.cities.toString());
                break;
            case 5:
                Log.d("MyActivity" , "position " + position);
                tabsFragment.setTag(Categories.nature.toString());
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
