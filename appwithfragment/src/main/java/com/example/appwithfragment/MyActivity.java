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

import com.example.appwithfragment.BackgroundService.NotificationService;
import com.example.appwithfragment.FullScreenPicture.ViewPagerFragment;
import com.example.appwithfragment.RecyclerViewFragment.OnRecyclerViewClickListener;
import com.example.appwithfragment.TabsFragments.IOnLikePhotoListener;
import com.example.appwithfragment.TabsFragments.TabsFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Евгения on 15.08.2016.
 */
public class MyActivity extends MainActivity implements OnRecyclerViewClickListener, Serializable {
    private static final String keyContext = "Context";
    private static final String keyPosition = "position";
    private static final String keyList = "recyclerViewFragmentList";
    private static final String keyLikeListener = "likeListener";

    private NavigationView navigationView;

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private ViewPagerFragment viewPagerFragment;
    private TabsFragment tabsFragment;
    public static Context context;



    @Override
    public void doAction(int pos, List<PhotoObjectInfo> list, IOnLikePhotoListener onLPListener) {

        Log.i("FrgFullScreenPicture", "create new FragmentFullScreenPicture");
        viewPagerFragment = new ViewPagerFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(keyPosition, pos);
        bundle.putSerializable(keyList, new ArrayList<PhotoObjectInfo>(list));
        bundle.putSerializable(keyLikeListener, onLPListener);

        viewPagerFragment.setArguments(bundle);
        replaceFragment(viewPagerFragment, R.id.LL, "single_img");

        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyActivity", "onCreate");
        setContentView(R.layout.activity_main);


// /data/data/com.example.appwithfragment/cache

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        MyApp.getDBHelper().open();
        context = getApplicationContext();

        drawerLayout = setDrawerLayout();


        Fragment isOpen = getSupportFragmentManager().findFragmentById(R.id.LL);

        if(isOpen == null) {
            Log.d("MyActivity", "onCreate isOpen " + isOpen);
            tabsFragment = new TabsFragment();
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
              //  actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
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

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,null, R.string.isOpen, R.string.isClosed);
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
        MyApp.getDBHelper().close();
        super.onDestroy();
    }


    private void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void startAlarm(){
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent serviceIntent = new Intent(getApplicationContext(), NotificationService.class);

        PendingIntent servicePendingIntent = PendingIntent.getService(getApplicationContext(), 0, serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.cancel(servicePendingIntent);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),20000, servicePendingIntent);
    }

    @Override
    protected void onResume() {
        Log.d("MyActivity", "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("MyActivity", "onPause");
        super.onPause();
    }
}
