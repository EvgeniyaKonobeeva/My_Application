package com.example.appwithfragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.appwithfragment.BackgroundService.NotificationService;
import com.example.appwithfragment.DataBasePack.DBHelper;
import com.example.appwithfragment.FullScreenPicture.ViewPagerFragment;
import com.example.appwithfragment.RecyclerViewFragment.OnRecyclerViewClickListener;
import com.example.appwithfragment.TabsFragments.IOnLikePhotoListener;
import com.example.appwithfragment.TabsFragments.TabsFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Евгения on 15.08.2016.
 */
public class MyActivity extends MainActivity implements OnRecyclerViewClickListener, Serializable {
    public static final String keyContext = "Context";
    public static final String keyPosition = "position";
    public static final String keyList = "recyclerViewFragmentList";
    public static final String keyLikeListener = "likeListener";

    public static Context context;

    private NavigationView navigationView;

    public static final String protocolInterestingness = "https://api.flickr.com/services/rest/?method=flickr.interestingness.getList&" +
            "api_key=b14e644ffd373999f625f4d2ba244522&format=json&nojsoncallback=1";

    private Map<String, String> queryData = new HashMap<>();

    private void setQueryData(){
        queryData.put("api_key", "b14e644ffd373999f625f4d2ba244522");
        queryData.put("format", "json");
        queryData.put("nojsoncallback","1");
    }


    public static Map<String, String> interesting = new HashMap<>();
    public static Map<String, String> clusters = new HashMap<>();
    public static Map<String, String> tags = new HashMap<>();

    public static String baseURL = "https://api.flickr.com";



    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private ViewPagerFragment viewPagerFragment;
    private TabsFragment tabsFragment;
    private DBHelper dBHelper;



    @Override
    public void doAction(int pos, List<PhotoObjectInfo> list, IOnLikePhotoListener onLPListener) {

        Log.i("FrgFullScreenPicture", "create new FragmentFullScreenPicture");
        viewPagerFragment = new ViewPagerFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(keyPosition, pos);
        bundle.putSerializable(keyContext, this);
        bundle.putSerializable(keyList, new ArrayList<PhotoObjectInfo>(list));
        bundle.putSerializable(keyLikeListener, onLPListener);

        viewPagerFragment.setArguments(bundle);
        replaceFragment(viewPagerFragment, R.id.LL, "single_img");

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyActivity", "onCreate");
        setContentView(R.layout.activity_main);


        setQueryData();
        setInterestingMap();
        setTagsMap();
        setClustersMap();

// /data/data/com.example.appwithfragment/cache

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        context = getApplicationContext();
        dBHelper = DBHelper.getInstance(context);
        dBHelper.open();

        drawerLayout = setDrawerLayout();


        Fragment isOpen = getSupportFragmentManager().findFragmentById(R.id.LL);

        if(isOpen == null) {
            Log.d("MyActivity", "onCreate isOpen " + isOpen);
            tabsFragment = new TabsFragment();
            tabsFragment.setDBHelper(dBHelper);
            tabsFragment.setCategory("interestingness");
            tabsFragment.setRetainInstance(true);
            navigationView.setCheckedItem(R.id.interestingness);
            setTitle(tabsFragment.getCategory());
            addFragment(tabsFragment, R.id.LL, "list_img");
        }else if(isOpen instanceof TabsFragment){
            tabsFragment = (TabsFragment) isOpen;
            setTitle(tabsFragment.getCategory());
        }

        startAlarm();






    }


    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        //Log.d("MyActivity", "onPostCreate");
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
        getSupportActionBar().setTitle(title.toString().toUpperCase());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("MyActivity", "onOptionsItemSelected " + item.getItemId());
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            Log.d("MyActivity", "actionBarDrawerToggle.onOptionsItemSelected " + item.getItemId() + " : " + item.getTitle());
            return true;
        }
        if(item.getItemId() == android.R.id.home){
            Log.d("MyActivity", "actionBarDrawerToggle.onOptionsItemSelected home " + item.getItemId() + " : " + item.getTitle());
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
            Log.d("MyActivity", "close drower");
        }else{
            Log.d("MyActivity", "another");
            Fragment isOpen = getSupportFragmentManager().findFragmentById(R.id.LL);
            if(isOpen instanceof TabsFragment){
                Log.d("MyActivity", "if 1 tabsFrag");
                tabsFragment = (TabsFragment)isOpen;
                if(getSupportFragmentManager().getBackStackEntryCount() > 1){
                    Log.d("MyActivity", "if 2 " + getSupportFragmentManager().getBackStackEntryCount());
                    tabsFragment.setCategory(navigationView.getMenu().findItem(R.id.interestingness).getTitle().toString());
                    setTitle(navigationView.getMenu().findItem(R.id.interestingness).getTitle());
                    navigationView.setCheckedItem(R.id.interestingness);
                    clearBackStack();
                    replaceFragment(tabsFragment,R.id.LL,"list_img");
                }else{
                    Log.d("MyActivity", "else 2 " + getSupportFragmentManager().getBackStackEntryCount());
                    if(getSupportFragmentManager().getBackStackEntryCount() == 1){
                        this.finish();
                    }
                    //clearBackStack();
                    super.onBackPressed();
                }
            }else {
                Log.d("MyActivity", "else 1 fullScrFrag");
                super.onBackPressed();
            }
        }
    }

    public DrawerLayout setDrawerLayout(){

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                itemSelected(item);
                setTitle(item.getTitle());
                item.setChecked(true);
                drawerLayout.closeDrawer(navigationView);
                return true;
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,null, R.string.isOpen, R.string.isClosed){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_today);
            }
        };
        //actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return drawerLayout;
    }

    public void itemSelected(MenuItem item){
        Log.d("MyActivity" , "itemSelected " + item.getItemId());
        int position = item.getItemId();
        Fragment frg = getSupportFragmentManager().findFragmentByTag("list_img");
        if(frg == null){
            tabsFragment = new TabsFragment();
            tabsFragment.setDBHelper(dBHelper);
            tabsFragment.setCategory("");
            replaceFragment(tabsFragment, R.id.LL, "list_img");
        }else {
            tabsFragment = (TabsFragment) frg;
            replaceFragment(tabsFragment, R.id.LL, "list_img");
        }
        tabsFragment.setCategory(item.getTitle().toString());

    }


    @Override
    protected void onDestroy() {
        if(dBHelper != null && dBHelper.isOpen())
            dBHelper.close();
        super.onDestroy();
    }

    public void setDrawerIndicatorEnabled(boolean set) {
        this.actionBarDrawerToggle.setDrawerIndicatorEnabled(set);
    }

    private void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void startAlarm(){
        Toast.makeText(this, "run alarm", Toast.LENGTH_SHORT).show();
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent serviceIntent = new Intent(this, NotificationService.class);
        PendingIntent servicePendingIntent = PendingIntent.getService(this, 0, serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.cancel(servicePendingIntent);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10000, servicePendingIntent);
    }

    @Override
    protected void onResume() {
        Log.d("MyActivity", "onResume");
        super.onResume();
    }

    public void setInterestingMap(){
        interesting.put("method", "flickr.interestingness.getList");
        interesting.putAll(queryData);
    }

    public void setClustersMap(){
        clusters.put("method", "method=flickr.tags.getClusters");
        clusters.putAll(queryData);
    }

    public void setTagsMap(){
        tags.put("method", "flickr.tags.getClusterPhotos");
        tags.putAll(queryData);
    }
}
