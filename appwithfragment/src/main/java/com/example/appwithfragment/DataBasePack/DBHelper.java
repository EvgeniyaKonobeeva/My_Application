package com.example.appwithfragment.DataBasePack;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.appwithfragment.RecyclerViewFragment.Categories;

/**
 * Created by e.konobeeva on 25.08.2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "likedPhotosDb.db";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase sqLiteDatabase;
    private static DBHelper dbHelper;

    /*col category table*/
    public final static String tableName = "categories";
    public final static String category_col = "category";
    public final static String category_id = "categories_id";


    /*col likes table*/
    public final static String likesTableN = "likes";
    public final static String likesUrl = "url";
    public final static String likesId = "likes_id";
    public final static String isliked = "isliked";
    public final static String title = "title";
    public final static String categoryId = "category_id";


    private String createCategoryTableQuery = "create table " + tableName + " ( " + category_id +
            " integer primary key autoincrement, " + category_col +
            " text not null); ";

    private String createLikesTableQuery = "create table likes ( " +
            "likes_id integer primary key autoincrement, " +
            "url text not null unique," +
            " title text not null," +
            " isliked boolean not null," +
            " category_id integer);";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    public DBHelper(Context ctx){
        super(ctx, DB_NAME, null, DB_VERSION);
        //SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS 'likes' ");
        //sqLiteDatabase.execSQL(createLikesTableQuery);
        //SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        //sqLiteDatabase.delete(likesTableN, null, null);
    }

    public void open(){
        sqLiteDatabase = this.getWritableDatabase();
    }
    public SQLiteDatabase getSSQLiteDatabase(){
        return sqLiteDatabase;
    }
    public void close(){
        sqLiteDatabase.close();
    }
    public static DBHelper getInstance(Context context){
        if(dbHelper == null){
            dbHelper = new DBHelper(context);
            dbHelper.open();
            return dbHelper;
        }else {
            dbHelper.open();
            return dbHelper;
        }

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(createCategoryTableQuery);
        sqLiteDatabase.execSQL(createLikesTableQuery);
        putCategories(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void putCategories(SQLiteDatabase sqLiteDatabase){
        ContentValues conValues = new ContentValues();
        for(Categories cats : Categories.values()){
            conValues.put(category_col, cats.toString());
            sqLiteDatabase.insert(tableName, null, conValues);
        }
    }


}
