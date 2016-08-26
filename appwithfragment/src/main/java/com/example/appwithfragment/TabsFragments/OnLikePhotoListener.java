package com.example.appwithfragment.TabsFragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.example.appwithfragment.DataBasePack.DBHelper;
import com.example.appwithfragment.ListContent;
import com.example.appwithfragment.RecyclerViewFragment.Tasks.GetPhotosList;
import com.example.appwithfragment.RecyclerViewFragment.Tasks.LikePhotoTasksResult;
import com.example.appwithfragment.RecyclerViewFragment.Tasks.RemovePhotoTask;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Евгения on 24.08.2016.
 */
public class OnLikePhotoListener implements IOnLikePhotoListener, Serializable,LikePhotoTasksResult {
    private ArrayList<ListContent> likedList;
    DBHelper dbHelper;
    String category;
    AsyncTask getPhotosList;
    AsyncTask removePhotoTask;


    public OnLikePhotoListener(DBHelper dbHelper, String category){
        Log.d("OnLikePhotoListener", "onLikePhotoListener create");
        likedList = new ArrayList<>();
        setCategory(category);
        if(dbHelper != null){
            this.dbHelper = dbHelper;
            getLikedPhotosFromDB(dbHelper, this.category);
        }

    }
    //метод вызывается фрагментом FragmentFullScreenPicture по нажатию кнопки


    public void onLikePhotoListener(final ListContent lc){

        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.likesUrl, lc.getImgUrl());
                contentValues.put(DBHelper.title, lc.getFullTitle());
                contentValues.put(DBHelper.isliked, true);
                contentValues.put(DBHelper.categoryId, catIdQuery(sqLiteDatabase, category));

                sqLiteDatabase.insert(DBHelper.likesTableN, null, contentValues);

                sqLiteDatabase.close();
            }
        }).start();

        likedList.add(lc);
    }


    //метод вызывается для получения списка фотографий фрагментом LikedPhotosFragment
    public ArrayList<ListContent> getLikedPhotos(){

        return likedList;
    }

    //TODO thread
    public void getLikedPhotosFromDB(DBHelper dbHelper, String category){

        getPhotosList = new GetPhotosList(dbHelper, category, this);
        getPhotosList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        /*list.clear();
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int category_id = catIdQuery(sqLiteDatabase, category);
        Cursor cursor = sqLiteDatabase.query(DBHelper.likesTableN, null, DBHelper.isliked + " = ? and " + DBHelper.categoryId + " = ? ", new String[]{"1", Integer.toString(category_id)}, null, null, null );

        while (cursor.moveToNext()){
            ListContent lc = new ListContent(cursor.getString(cursor.getColumnIndex(DBHelper.likesUrl)), cursor.getString(cursor.getColumnIndex(DBHelper.title)));
            list.add(lc);
        }

        sqLiteDatabase.close();*/
        //Log.d("OnLikePhotoListener", "like getLikedPhotos " + Arrays.toString(list.toArray()));
    }

    @Override
    public void getPhotoResult(ArrayList<ListContent> arr) {

        likedList = arr;
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




    public void removePhoto(ListContent lc){

        removePhotoTask = new RemovePhotoTask(likedList, lc, dbHelper, category, this);
        removePhotoTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public void getRemoveResult(int i, ListContent lc) {
        if(likedList.contains(lc)){
            likedList.remove(lc);
        }else Log.e("OnLikePhotoListener", "removePhoto");


        final int _photoId = i;

        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.isliked, 0);

                sqLiteDatabase.update(DBHelper.likesTableN, contentValues, DBHelper.likesId + " = ? ", new String[]{_photoId+""});

                sqLiteDatabase.close();
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

    @Override
    public void onDestroyApp() {
        if(getPhotosList != null){
            ((GetPhotosList)getPhotosList).setResult(null);
        }
        if(removePhotoTask != null){
            ((RemovePhotoTask)getPhotosList).setRes(null);
        }
    }
}
