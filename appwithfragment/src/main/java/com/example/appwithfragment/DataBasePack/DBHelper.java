package com.example.appwithfragment.DataBasePack;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.appwithfragment.RecyclerViewFragment.Categories;

import java.io.Serializable;

/**
 * Created by e.konobeeva on 25.08.2016.
 */
public class DBHelper extends SQLiteOpenHelper implements Serializable {
    private static final String DB_NAME = "likedPhotosDb.db";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase sqLiteDatabase;
    private static DBHelper dbHelper;
    private static boolean isOpen = false;

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

    /*col interestingness category table*/
    public final static String interestingTableName = "interestingness";
    public final static String interestingId = "interesting_id";
    public final static String photoUrl = "url";
    public final static String photoTitle = "title";
    public final static String date = "date"; /*date in ms*/



    private String createCategoryTableQuery = "create table " + tableName + " ( " + category_id +
            " integer primary key autoincrement, " + category_col +
            " text not null); ";

    private String createLikesTableQuery = "create table likes ( " +
            "likes_id integer primary key autoincrement, " +
            "url text not null unique," +
            " title text not null," +
            " isliked boolean not null," +
            " category_id integer);";

    private String createInterestingTableQuery = "create table " + interestingTableName +
            " ( " + interestingId + " integer primary key autoincrement, " +
            photoUrl + " text not null, " +
            photoTitle + " text not null, " +
            date + " integer not null );";

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
        //sqLiteDatabase.execSQL(createInterestingTableQuery);
        isOpen = true;
    }
    public SQLiteDatabase getSSQLiteDatabase(){
        return sqLiteDatabase;
    }
    public void close(){
        sqLiteDatabase.close();
        isOpen = false;
    }
    public static DBHelper getInstance(Context context){
        if(dbHelper == null){
            dbHelper = new DBHelper(context);
            return dbHelper;
        }else {
            return dbHelper;
        }

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("DBHelper", "onCreate");
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

    public boolean isOpen(){
        return isOpen;
    }


}
