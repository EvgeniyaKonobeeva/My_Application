package com.example.appwithfragment.TabsFragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.appwithfragment.DataBasePack.DBHelper;
import com.example.appwithfragment.ListContent;
import com.example.appwithfragment.RecyclerViewFragment.Categories;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 * Created by Евгения on 24.08.2016.
 */
public class OnLikePhotoListener implements IOnLikePhotoListener, Serializable{
    private Stack<ListContent> likedList;
    DBHelper dbHelper;
    String category;

    public OnLikePhotoListener(DBHelper dbHelper, String category){
        Log.d("OnLikePhotoListener", "onLikePhotoListener create");
        likedList = new Stack<>();

        setCategory(category);
        if(dbHelper != null){
            this.dbHelper = dbHelper;
            getLikedPhotos();
        }

    }
    //метод вызывается фрагментом FragmentFullScreenPicture по нажатию кнопки
    public void onLikePhotoListener(ListContent lc){
        likedList.push(lc);


        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.likesUrl, lc.getImgUrl());
        contentValues.put(DBHelper.title, lc.getFullTitle());
        contentValues.put(DBHelper.isliked, true);
        contentValues.put(DBHelper.categoryId, catIdQuery(sqLiteDatabase, category));

        sqLiteDatabase.insert(DBHelper.likesTableN, null, contentValues);

        sqLiteDatabase.close();

        //getLikedPhotos();


    }

    //метод вызывается для получения списка фотографий фрагментом LikedPhotosFragment
    public Stack getLikedPhotos(){

        likedList.clear();
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int category_id = catIdQuery(sqLiteDatabase, category);
        Cursor cursor = sqLiteDatabase.query(DBHelper.likesTableN, null, DBHelper.categoryId + " = ?", new String[]{Integer.toString(category_id)}, null, null, null );

        while (cursor.moveToNext()){
            ListContent lc = new ListContent(cursor.getString(cursor.getColumnIndex(DBHelper.likesUrl)), cursor.getString(cursor.getColumnIndex(DBHelper.title)));
            likedList.push(lc);
        }

        sqLiteDatabase.close();
        Log.d("OnLikePhotoListener", "getLikedPhotos " + Arrays.toString(likedList.toArray()));
        return likedList;
    }

    @Override
    public boolean isLikedPhoto(ListContent lc) {
        //Log.d("OnLikePhotoListener", "getLikedPhotos " + Arrays.toString(likedList.toArray()));
        //Log.d("OnLikePhotoListener", "getLikedPhotos " + likedList.peek().getImgUrl());
        //getLikedPhotos();
        Log.d("OnLikePhotoListener", "isLikedPhoto " + lc);
        if(likedList.contains(lc)){
            Log.d("OnLikePhotoListener", "isLikedPhoto true");
            return true;
        }else {
            Log.d("OnLikePhotoListener", "isLikedPhoto false");
            return false;
        }
    }

    public void removePhoto(ListContent lc){
        Log.d("OnLikePhotoListener", "removePhoto " + lc);
        likedList.remove(lc);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        sqLiteDatabase.delete(DBHelper.likesTableN, DBHelper.likesUrl + " = ?", new String[]{lc.getImgUrl()});


        sqLiteDatabase.close();

        getLikedPhotos();

    }

    public void setCategory(String category){
        if(category == null){
            this.category = "interestingness";
        }else
            this.category = category;
    }


    //возвращает id категории
    private int catIdQuery(SQLiteDatabase sqLiteDatabase, String catName) {

        Cursor cursor = sqLiteDatabase.query(DBHelper.tableName,
                new String[]{DBHelper.category_id},
                DBHelper.category_col + " = ?",
                new String[]{catName} ,
                null,null,null,null);

        cursor.moveToFirst();

        return cursor.getInt(cursor.getColumnIndex(DBHelper.category_id));
    }



}
