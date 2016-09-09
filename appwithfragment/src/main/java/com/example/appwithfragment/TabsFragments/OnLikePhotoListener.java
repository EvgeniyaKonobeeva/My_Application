package com.example.appwithfragment.TabsFragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.appwithfragment.DataBasePack.DBHelper;
import com.example.appwithfragment.MyApp;
import com.example.appwithfragment.PhotoObjectInfo;
import com.example.appwithfragment.MVPPattern.IFragment;
import com.example.appwithfragment.MyActivity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Евгения on 24.08.2016.
 */
public class OnLikePhotoListener implements IOnLikePhotoListener, Serializable, LoaderManager.LoaderCallbacks<Cursor>, Parcelable {
    private ArrayList<PhotoObjectInfo> likedList;
    String category;
    private static int LOADER_ID = 1;


    private IFragment fragment;
    public static final Parcelable.Creator<OnLikePhotoListener> CREATOR
            = new Parcelable.Creator<OnLikePhotoListener>() {
        public OnLikePhotoListener createFromParcel(Parcel in) {
            return new OnLikePhotoListener(in.readBundle());
        }

        public OnLikePhotoListener[] newArray(int size) {
            return new OnLikePhotoListener[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public OnLikePhotoListener(Bundle bundle){
        String category = bundle.getString("category");
       // DBHelper dbHelper = (DBHelper) bundle.getSerializable("dbHelper");

        Log.d("OnLikePhotoListener", "onLikePhotoListener create");
        likedList = new ArrayList<>();
        setCategory(category);

    }
    public OnLikePhotoListener(String category){
        Log.d("OnLikePhotoListener", "onLikePhotoListener create");
        likedList = new ArrayList<>();
        setCategory(category);
    }



    public void setFragment(IFragment f){
        fragment = f;
        getLikedPhotosFromDB(MyApp.getDBHelper(),category);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d("OnLikePhotoListener", "onLoadFinished");
        while (cursor.moveToNext()){
            String url = cursor.getString(cursor.getColumnIndex(DBHelper.likesUrl));
            String title = cursor.getString(cursor.getColumnIndex(DBHelper.title));
            //int id = cursor.getInt(cursor.getColumnIndex(DBHelper.likesId));
            likedList.add(new PhotoObjectInfo(url, title));
        }
        cursor.close();
        fragment.setList(likedList);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("OnLikePhotoListener", "onCreateLoader");
        return new LikedPhotoLoader(MyActivity.context, category);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void onLikePhotoListener(final PhotoObjectInfo lc){

        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.likesUrl, lc.getImgUrl());
                contentValues.put(DBHelper.title, lc.getFullTitle());
                contentValues.put(DBHelper.isliked, true);
                contentValues.put(DBHelper.categoryId, catIdQuery(MyApp.getDBHelper().getSSQLiteDatabase(), category));

                MyApp.getDBHelper().getSSQLiteDatabase().insert(DBHelper.likesTableN, null, contentValues);
            }
        }).start();

        likedList.add(lc);
        fragment.setList(likedList);
    }


    //метод вызывается для получения списка фотографий фрагментом LikedPhotosFragment
    public ArrayList<PhotoObjectInfo> getLikedPhotos(){
        return likedList;
    }

    public void getLikedPhotosFromDB(DBHelper dbHelper, String category){
        Log.d("OnLikePhotoListener", "getLikedPhotosFromDB");
        ((Fragment)fragment).getLoaderManager().restartLoader(LOADER_ID, null, this).forceLoad();
    }


    @Override
    public boolean isLikedPhoto(PhotoObjectInfo lc) {

        if(likedList.contains(lc)){
            Log.d("OnLikePhotoListener", "isLikedPhoto true");
            return true;
        }else {
            Log.d("OnLikePhotoListener", "isLikedPhoto false");
            return false;
        }
    }




    public void removePhoto(final PhotoObjectInfo lc){
        if(likedList.contains(lc)){
            likedList.remove(lc);
        }else Log.e("OnLikePhotoListener", "removePhoto");
        fragment.setList(likedList);
        new Thread(new Runnable() {
            @Override
            public void run() {

                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.isliked, 0);

                MyApp.getDBHelper().getSSQLiteDatabase().update(DBHelper.likesTableN, contentValues, DBHelper.likesUrl + " = ? ", new String[]{lc.getImgUrl()});
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
