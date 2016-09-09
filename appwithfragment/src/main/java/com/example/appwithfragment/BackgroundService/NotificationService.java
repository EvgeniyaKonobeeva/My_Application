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

import com.example.appwithfragment.DataBasePack.DBHelper;
import com.example.appwithfragment.MyActivity;
import com.example.appwithfragment.PhotoObjectInfo;
import com.example.appwithfragment.R;
import com.example.appwithfragment.RecyclerViewFragment.ParserJSONTo;
import com.example.appwithfragment.RetrofitPack.FlickrAPI;
import com.example.appwithfragment.RetrofitPack.Photo;
import com.example.appwithfragment.RetrofitPack.PhotosApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Евгения on 01.09.2016.
 */
public class NotificationService extends Service {

    public static int NOT_EQUALS = 0;
    public static int NULL_OBJ = -1;
    public static int EQUALS = 1;

    private static int PAGE = 1;
    private static int PER_PAGE = 10;

    private Map protocol = new HashMap();
    private String baseURL = "https://api.flickr.com";
    private static String tag = "NotificationService";



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        final Map protocol = new HashMap();
//        final String baseURL = "https://api.flickr.com";
//        protocol.put("api_key", "b14e644ffd373999f625f4d2ba244522");
//        protocol.put("format", "json");
//        protocol.put("nojsoncallback","1");
//        protocol.put("method", "flickr.interestingness.getList");
        protocol.put("api_key", "b14e644ffd373999f625f4d2ba244522");
        protocol.put("format", "json");
        protocol.put("nojsoncallback","1");
        protocol.put("method", "flickr.interestingness.getList");

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList newPhotos = getNewPhotos(baseURL, protocol);
                ArrayList oldPhotos = getOldPhotos();
                int result = compareLists(newPhotos, oldPhotos);
                if(result == NOT_EQUALS) {
                    sendNotification("has new");
                }
//                }else if(result == EQUALS){
//                    sendNotification("no new");
//                }
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

    public int compareLists(ArrayList list1, ArrayList list2){
        int res = -1;
        if(list1 != null && list2 != null) {
            if (list1.size() == list2.size()) {
               for(int i = 0; i < list1.size(); i++){
                   if(!list2.contains(list1.get(i))){
                       return NOT_EQUALS;
                   }else res = EQUALS;
               }
            }else return NOT_EQUALS;
        }else return NULL_OBJ;
        return res;
    }

    public ArrayList<PhotoObjectInfo> getNewPhotos(String baseURL, Map protocol){
        ArrayList onePagePhotosList = null;
        try {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build();
            FlickrAPI flickrAPI = retrofit.create(FlickrAPI.class);
            Call<PhotosApi> call = flickrAPI.getInterestingPhotos(protocol, Integer.toString(PER_PAGE), Integer.toString(PAGE));
            Photo[] photo = call.execute().body().getPhotos().getPhoto();
            onePagePhotosList = ParserJSONTo.PhotoArrayList(photo);
        }catch (IOException ioEx){
            Log.e(tag, ioEx.getMessage());
        }

        return onePagePhotosList;
    }

}
