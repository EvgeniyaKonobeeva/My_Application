package com.example.appwithfragment.TabsFragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.appwithfragment.DataBasePack.DBHelper;
import com.example.appwithfragment.ListContent;
import com.example.appwithfragment.MVPPattern.IFragment;
import com.example.appwithfragment.MyActivity;
import com.example.appwithfragment.RecyclerViewFragment.LikedPhotoLoader;
import com.example.appwithfragment.RecyclerViewFragment.Tasks.GetPhotosList;
import com.example.appwithfragment.RecyclerViewFragment.Tasks.RemovePhotoTask;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Евгения on 24.08.2016.
 */
public class OnLikePhotoListener implements IOnLikePhotoListener, Serializable, LoaderManager.LoaderCallbacks<Cursor> {
    private ArrayList<ListContent> likedList;
    DBHelper dbHelper;
    String category;
    private static int LOADER_ID = 1;


    private IFragment fragment;


    public OnLikePhotoListener(DBHelper dbHelper, String category){
        Log.d("OnLikePhotoListener", "onLikePhotoListener create");
        likedList = new ArrayList<>();
        setCategory(category);
        if(dbHelper != null){
            this.dbHelper = dbHelper;
        }

    }

    public void setFragment(IFragment f){
        fragment = f;
        getLikedPhotosFromDB(dbHelper, category);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d("OnLikePhotoListener", "onLoadFinished");
        while (cursor.moveToNext()){
            String url = cursor.getString(cursor.getColumnIndex(DBHelper.likesUrl));
            String title = cursor.getString(cursor.getColumnIndex(DBHelper.title));
            //int id = cursor.getInt(cursor.getColumnIndex(DBHelper.likesId));
            likedList.add(new ListContent(url, title));
        }
        cursor.close();
        fragment.setList(likedList);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("OnLikePhotoListener", "onCreateLoader");
        return new LikedPhotoLoader(MyActivity.context,dbHelper, category);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void onLikePhotoListener(final ListContent lc){

        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.likesUrl, lc.getImgUrl());
                contentValues.put(DBHelper.title, lc.getFullTitle());
                contentValues.put(DBHelper.isliked, true);
                contentValues.put(DBHelper.categoryId, catIdQuery(dbHelper.getSSQLiteDatabase(), category));

                dbHelper.getSSQLiteDatabase().insert(DBHelper.likesTableN, null, contentValues);
            }
        }).start();

        likedList.add(lc);
        fragment.setList(likedList);
    }


    //метод вызывается для получения списка фотографий фрагментом LikedPhotosFragment
    public ArrayList<ListContent> getLikedPhotos(){
        return likedList;
    }

    public void getLikedPhotosFromDB(DBHelper dbHelper, String category){
        Log.d("OnLikePhotoListener", "getLikedPhotosFromDB");
        ((Fragment)fragment).getLoaderManager().restartLoader(LOADER_ID, null, this).forceLoad();
    }


    @Override
    public boolean isLikedPhoto(ListContent lc) {

        if(likedList.contains(lc)){
            Log.d("OnLikePhotoListener", "isLikedPhoto true");
            return true;
        }else {
            Log.d("OnLikePhotoListener", "isLikedPhoto false");
            return false;
        }
    }




    public void removePhoto(final ListContent lc){
        if(likedList.contains(lc)){
            likedList.remove(lc);
        }else Log.e("OnLikePhotoListener", "removePhoto");
        fragment.setList(likedList);
        new Thread(new Runnable() {
            @Override
            public void run() {

                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.isliked, 0);

                dbHelper.getSSQLiteDatabase().update(DBHelper.likesTableN, contentValues, DBHelper.likesUrl + " = ? ", new String[]{lc.getImgUrl()});
            }
        }).start();

    }

    public void setCategory(String category){
        if(category == null || category.isEmpty()){
            this.category = "interestingness";
        }else
            this.category = category;
    }


    //возвращает id категории
    private int catIdQuery(SQLiteDatabase sqLiteDatabase, String catName) {

        Cursor cursor = sqLiteDatabase.query(DBHelper.tableName,
                new String[]{DBHelper.category_id},
                DBHelper.category_col + " = ? ",
                new String[]{catName} ,
                null,null,null,null);

        cursor.moveToFirst();

        return cursor.getInt(cursor.getColumnIndex(DBHelper.category_id));
    }

}
