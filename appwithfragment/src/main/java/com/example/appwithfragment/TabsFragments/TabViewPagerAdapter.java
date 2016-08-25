package com.example.appwithfragment.TabsFragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.example.appwithfragment.DataBasePack.DBHelper;
import com.example.appwithfragment.RecyclerViewFragment.RecyclerViewFragment;

/**
 * Created by e.konobeeva on 23.08.2016.
 */

//TODO make singlton
// get fragment manager
// when add to fm - set Tag, when get from fm - find by tag previous version

public class TabViewPagerAdapter extends FragmentPagerAdapter {
    private static TabViewPagerAdapter tabViewPagerAdapter;
    private static String tag = "";
    private final static int countItems = 2;
    private IOnLikePhotoListener likePhotoListener;
    private DBHelper dbHelper;

    //private FragmentManager fragmentManager;

    public TabViewPagerAdapter(FragmentManager fm){

        super(fm);

        Log.d("TabViewPagerAdapter", "constructor");
    }
    @Override
    public int getCount() {
        return countItems;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("TabViewPagerAdapter", "getItem");

        if(position % 2 == 0){
            Log.d("TabViewPagerAdapter", "getItem tag " + tag);
            Fragment f= RecyclerViewFragment.getNewInstance(tag, dbHelper);
            likePhotoListener = ((RecyclerViewFragment)f).getLikePhotoListener();
            return f;
        }else{
            Log.d("TabViewPagerAdapter", "getItem tag " + tag);
            //RecyclerViewFragment recyclerViewFragment = (RecyclerViewFragment) getItem(0);
            Fragment f= LikedPhotosFragment.getNewInstance(likePhotoListener);
            return f;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position%2 == 0){
            return "Photos";
        }else return "Liked";
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d("TabViewPagerAdapter", "instantiateItem " + position);
        Log.d("TabViewPagerAdapter", "instantiateItem tag " + tag);
        //Log.d("TabViewPagerAdapter", "instantiateItem " + super.instantiateItem(container, position));
        //fragmentMap.put(getItemId(position), )
        return super.instantiateItem(container, position);
    }

    @Override
    public long getItemId(int position) {
        if(position == 0){
            //if(tag != null)
                return position + tag.hashCode();
            //else return position;
        }else return position + (tag + "FS").hashCode();

    }

    public void setTag(String tag){
        Log.d("TabViewPagerAdapter", "setTag 1 " + this.tag);
        this.tag = tag;
        Log.d("TabViewPagerAdapter", "setTag 2 " + this.tag);
    }

    public void setDbHelper(DBHelper dbHelper){
        this.dbHelper = dbHelper;
    }
}
