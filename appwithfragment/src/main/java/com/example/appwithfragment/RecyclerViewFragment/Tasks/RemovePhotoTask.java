package com.example.appwithfragment.RecyclerViewFragment.Tasks;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.example.appwithfragment.DataBasePack.DBHelper;
import com.example.appwithfragment.ListContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by e.konobeeva on 26.08.2016.
 */
public class RemovePhotoTask extends AsyncTask<Object, Void, Integer> {
    private ListContent listContent;
    private DBHelper dbHelper;
    private String category;
    private ArrayList<ListContent> list;
    private LikePhotoTasksResult res;
    public RemovePhotoTask(ArrayList list, ListContent lc, DBHelper dbHelper, String category, LikePhotoTasksResult res){
        listContent = lc;
        this.dbHelper = dbHelper;
        this.category = category;
        this.list = list;
        this.res = res;
    }

    @Override
    protected Integer doInBackground(Object... objects) {

        Map<Integer, ListContent> map = getPhotosWithId(dbHelper, category);
        int photoId = 0;
        for (Map.Entry entry : map.entrySet()){
            if(entry.getValue().equals(listContent)) {
                photoId = (Integer) entry.getKey();
                break;
            }
        }
        return photoId;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if(res != null)
            res.getRemoveResult(integer, listContent);

    }

    public Map getPhotosWithId(DBHelper dbHelper, String category){

        Map<Integer, ListContent> map = new HashMap<>();

        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int catId = catIdQuery(sqLiteDatabase, category);
        Cursor cursor = sqLiteDatabase.query(DBHelper.likesTableN,
                new String[]{
                        DBHelper.likesUrl,
                        DBHelper.title,
                        DBHelper.likesId},
                DBHelper.categoryId + " = ? ",
                new String[]{catId+""},
                null, null, null, null);

        while (cursor.moveToNext()){
            String url = cursor.getString(cursor.getColumnIndex(DBHelper.likesUrl));
            String title = cursor.getString(cursor.getColumnIndex(DBHelper.title));
            int id = cursor.getInt(cursor.getColumnIndex(DBHelper.likesId));

            map.put(id, new ListContent(url, title));

        }
        cursor.close();
        sqLiteDatabase.close();

        return map;
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

    public void setRes(LikePhotoTasksResult res) {
        this.res = res;
    }
}
