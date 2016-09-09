package com.example.appwithfragment.TabsFragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.CursorLoader;

import com.example.appwithfragment.DataBasePack.DBHelper;
import com.example.appwithfragment.MyApp;

/**
 * Created by Евгения on 28.08.2016.
 */
public class LikedPhotoLoader extends CursorLoader {
    private String category;
    public LikedPhotoLoader(Context context, String category){
        super(context);
        this.category = category;
    }

    @Override
    public Cursor loadInBackground() {
        Cursor cursor = MyApp.getDBHelper().getSSQLiteDatabase().query(DBHelper.likesTableN, null,
                DBHelper.isliked + " = ? and " + DBHelper.categoryId + " = ? ",
                new String[]{"1", Integer.toString(catIdQuery(MyApp.getDBHelper().getSSQLiteDatabase(), category))}, null, null, null);

        return cursor;
    }

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
