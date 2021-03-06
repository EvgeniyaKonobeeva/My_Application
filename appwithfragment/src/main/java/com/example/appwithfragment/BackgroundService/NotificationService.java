package com.example.appwithfragment.BackgroundService;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.appwithfragment.DataBasePack.DBHelper;
import com.example.appwithfragment.MyActivity;
import com.example.appwithfragment.PhotoObjectInfo;
import com.example.appwithfragment.R;
import com.example.appwithfragment.RecyclerViewFragment.ParserJSONTo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Евгения on 01.09.2016.
 */
public class NotificationService extends Service {

    private static String tag = "NotificationService";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "run service", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList newPhotos = getNewPhotos();
                ArrayList oldPhotos = getOldPhotos();
                int result = compareLists(newPhotos, oldPhotos);
                if(result == 1){
                    sendNotification("no new");
                }else if(result == 0){
                    sendNotification("has new");
                }
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void sendNotification(String text){
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext());

        Intent activityIntent = new Intent(getApplicationContext(), MyActivity.class);
        PendingIntent activityPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, activityIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        notificationBuilder.setContentIntent(activityPendingIntent);
        notificationBuilder.setContentTitle("Flickr photos");
        notificationBuilder.setContentText(text);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, notificationBuilder.build());
    }

    public ArrayList getNewPhotos(){
        ArrayList onePagePhotosList = null;
        try {
            JSONObject photos = ParserJSONTo.getJSONRootPoint(MyActivity.protocolInterestingness + "&per_page=10&page=1").getJSONObject("photos");
            JSONArray photo = photos.getJSONArray("photo");
            onePagePhotosList = ParserJSONTo.PhotoArrayList(photo);
        }catch (IOException ioException){
            Log.e(tag, ioException.getMessage());
        }catch (JSONException jsonException){
            Log.e(tag, jsonException.getMessage());
        }
        //onePagePhotosList.remove(0);
        //onePagePhotosList.add(0, new PhotoObjectInfo("hhhhhhhhh", "title"));
        return onePagePhotosList;
    }

    public ArrayList getOldPhotos(){
        DBHelper dbHelper = DBHelper.getInstance(this);
        ArrayList<PhotoObjectInfo> list = new ArrayList<>();
        if(!dbHelper.isOpen()){
            dbHelper.open();
        }
        SQLiteDatabase sqLiteDatabase = dbHelper.getSSQLiteDatabase();
        int countRecords = 0;
        Cursor cursor = sqLiteDatabase.query(DBHelper.interestingTableName, new String[]{DBHelper.photoUrl, DBHelper.photoTitle}, null, null, null, null, null);
        while (cursor.moveToNext() && countRecords < 10){
            String url = cursor.getString(cursor.getColumnIndex(DBHelper.photoUrl));
            String title = cursor.getString(cursor.getColumnIndex(DBHelper.photoTitle));
            list.add(new PhotoObjectInfo(url, title));
            countRecords++;
        }
        dbHelper.close();
        return list;
    }

/*-1 - null objects
* 0 - not equals
* 1 - equals*/
    public int compareLists(ArrayList list1, ArrayList list2){
        int res = -1;
        if(list1 != null && list2 != null) {
            if (list1.size() == list2.size()) {
               for(int i = 0; i < list1.size(); i++){
                   if(!list2.contains(list1.get(i))){
                       return 0;
                   }else res = 1;
               }
            }else return 0;
        }else return -1;
        return res;
    }

}
