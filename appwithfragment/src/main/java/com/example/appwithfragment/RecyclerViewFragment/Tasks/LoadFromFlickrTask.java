package com.example.appwithfragment.RecyclerViewFragment.Tasks;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.example.appwithfragment.DataBasePack.DBHelper;
import com.example.appwithfragment.MyActivity;
import com.example.appwithfragment.PhotoObjectInfo;
import com.example.appwithfragment.RecyclerViewFragment.GettingResults;
import com.example.appwithfragment.RecyclerViewFragment.ParserJSONTo;
import com.example.appwithfragment.RetrofitPack.FlickrAPI;
import com.example.appwithfragment.RetrofitPack.Photo;
import com.example.appwithfragment.RetrofitPack.PhotosApi;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by e.konobeeva on 05.08.2016.
 */
public class LoadFromFlickrTask extends AsyncTask<Object, Integer, ArrayList> {

    private static final String errorTag = "LoadFFTask";
    private boolean photoEnds;
    private String protocol;
    private GettingResults fragment;
    private int startPageNum;


    public LoadFromFlickrTask(GettingResults fragment, String protocol){
        StringBuilder sb = new StringBuilder(protocol);
        sb.append("&per_page=20&page=");
        protocol = sb.toString();
        this.fragment = fragment;
        this.protocol = protocol;
        photoEnds = false;
    }

    public LoadFromFlickrTask(){}

    @Override
    protected ArrayList<PhotoObjectInfo> doInBackground(Object... voids) {

        ArrayList<PhotoObjectInfo> wholePhotosList = new ArrayList<>();
        ArrayList<PhotoObjectInfo> onePagePhotosList = new ArrayList<>();

        int curPage = fragment.getCurCluster_id();
        startPageNum = curPage;
        int countLoadingPhotos = 0;
        int loadingPhotosPerOnce = 50;
        int pages;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(MyActivity.baseURL).addConverterFactory(GsonConverterFactory.create()).build();
        FlickrAPI flickrAPI = retrofit.create(FlickrAPI.class);

        if (!isCancelled()) {
            try {


                while (countLoadingPhotos < loadingPhotosPerOnce) {
                    if (curPage <= pages) {
                        Call<PhotosApi> call = flickrAPI.getPhotos(MyActivity.clusters, Integer.toString(20), Integer.toString(curPage++));
                        call.enqueue(new Callback<PhotosApi>() {
                            @Override
                            public void onResponse(Call<PhotosApi> call, Response<PhotosApi> response) {
                                response.body();
                                Photo[] photo = response.body().getPhotos().getPhoto();
                                for(int i = 0; i < photo.length; i++){
                                    //textView.append(photo[i].getTitle() + "\n");
                                }
                            }

                            @Override
                            public void onFailure(Call<PhotosApi> call, Throwable t) {
                                Log.d("Main", t.getMessage());
                            }
                        });
                        onePagePhotosList = ParserJSONTo.PhotoArrayList(photo);
                        addWHoleArrayToAnotherArray(onePagePhotosList, wholePhotosList);
                        countLoadingPhotos += onePagePhotosList.size();
                    } else {
                        photoEnds = true;
                        break;
                    }
                }
                fragment.setCurCluster_id(curPage);

            } catch (JSONException jsonException) {
                Log.e(errorTag, jsonException.getMessage());
                Log.d("LoadFromFlickrTask", "Exception interrupted");
                this.cancel(true);
            } catch (IOException ioException) {
                Log.e(errorTag, ioException.getMessage());
                Log.d("LoadFromFlickrTask", "Exception interrupted");
                this.cancel(true);
            }
        } else {
            Log.d("LoadFromFlickrTask", "interrupted");
            this.cancel(true);
        }
        if(startPageNum == 0){
            addNewPhotosToDB(wholePhotosList);
        }
        return wholePhotosList;
    }


    @Override
    protected void onPostExecute(ArrayList aVoid) {
        if(fragment != null && aVoid.size() != 0) {
            Log.d(errorTag, "onPostExecute");
            fragment.onGettingResult(aVoid, photoEnds);
        }
    }

    @Override
    protected void onCancelled() {
        Log.d("LoadFromFlickrTask", "onCancelled");
        if(fragment != null) {
            fragment.setCurCluster_id(startPageNum);
        }
        super.onCancelled();

    }

    public void setFragment(GettingResults fragment){
        this.fragment = fragment;
    }

    public void addWHoleArrayToAnotherArray(ArrayList putArr, ArrayList addToArr){
        for(int i = 0; i < putArr.size(); i++){
            addToArr.add(putArr.get(i));
        }
    }

    public void addNewPhotosToDB(ArrayList<PhotoObjectInfo> list){
        DBHelper dbHelper = DBHelper.getInstance(MyActivity.context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getSSQLiteDatabase();
        ContentValues contentValues = new ContentValues();
        sqLiteDatabase.delete(DBHelper.interestingTableName, null, null);
        if(list.size() > 10) {
            for (int i = 0; i < 10; i++) {
                contentValues.put(DBHelper.photoUrl, list.get(i).getImgUrl());
                contentValues.put(DBHelper.photoTitle, list.get(i).getFullTitle());
                sqLiteDatabase.insert(DBHelper.interestingTableName, null, contentValues);
            }
        }
    }

}