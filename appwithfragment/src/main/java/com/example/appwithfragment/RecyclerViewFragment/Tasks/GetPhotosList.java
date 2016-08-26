package com.example.appwithfragment.RecyclerViewFragment.Tasks;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.example.appwithfragment.DataBasePack.DBHelper;
import com.example.appwithfragment.ListContent;

import java.util.ArrayList;

/**
 * Created by e.konobeeva on 26.08.2016.
 */
public class GetPhotosList extends AsyncTask<Object, Void, ArrayList> {
    private DBHelper dbHelper;
    private String category;
    private LikePhotoTasksResult result;
    public GetPhotosList(DBHelper dbHelper, String category, LikePhotoTasksResult res){
        this.dbHelper = dbHelper;
        this.category = category;
        this.result = res;
    }
    @Override
    protected ArrayList doInBackground(Object... objects) {
        ArrayList<ListContent> arr = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int category_id = catIdQuery(sqLiteDatabase, category);
        Cursor cursor = sqLiteDatabase.query(DBHelper.likesTableN, null, DBHelper.isliked + " = ? and " + DBHelper.categoryId + " = ? ", new String[]{"1", Integer.toString(category_id)}, null, null, null );

        while (cursor.moveToNext()){
            ListContent lc = new ListContent(cursor.getString(cursor.getColumnIndex(DBHelper.likesUrl)), cursor.getString(cursor.getColumnIndex(DBHelper.title)));
            arr.add(lc);
        }

        sqLiteDatabase.close();
        return arr;
    }

    @Override
    protected void onPostExecute(ArrayList list) {
        if (result != null)
            result.getPhotoResult(list);
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

    public void setResult(LikePhotoTasksResult result) {
        this.result = result;
    }
}
