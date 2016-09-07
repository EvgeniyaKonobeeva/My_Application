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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by e.konobeeva on 05.08.2016.
 */
public class InterestingnessTask extends AsyncTask<Object, Integer, ArrayList> {

    private static final String errorTag = "LoadFFTask";
    private boolean photoEnds;
    private GettingResults fragment;
    private int taskStartsWithPageNum;
    private Map protocol;
    private String baseURL;




    public InterestingnessTask(GettingResults fragment, Map protocol, String baseURL){
        this.fragment = fragment;
        photoEnds = false;
        this.protocol = protocol;
        this.baseURL = baseURL;
    }

    public InterestingnessTask(){}

    @Override
    protected ArrayList<PhotoObjectInfo> doInBackground(Object... voids) {

        ArrayList<PhotoObjectInfo> wholePhotosList = new ArrayList<>();
        ArrayList<PhotoObjectInfo> onePagePhotosList = new ArrayList<>();

        int curPage = fragment.getCurCluster_id();
        taskStartsWithPageNum = curPage;
        int countLoadingPhotos = 0;
        int loadingPhotosPerOnce = 50;
        int pages;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build();
        FlickrAPI flickrAPI = retrofit.create(FlickrAPI.class);

        if (!isCancelled()) {
            try {
                Call<PhotosApi> call = flickrAPI.getInterestingPhotos(protocol, Integer.toString(20), Integer.toString(++curPage));
                Log.d("InterestingnessTask", call.request().url() + "");
                pages = Integer.valueOf(call.execute().body().getPhotos().getPages());

                while (countLoadingPhotos < loadingPhotosPerOnce) {
                    if (curPage <= pages) {
                        call = flickrAPI.getInterestingPhotos(protocol, Integer.toString(20), Integer.toString(curPage++));
                        PhotosApi photosApi;
                        if((photosApi = call.execute().body()) != null) {
                            Photo[] photo = photosApi.getPhotos().getPhoto();
                            onePagePhotosList = ParserJSONTo.PhotoArrayList(photo);
                            addWHoleArrayToAnotherArray(onePagePhotosList, wholePhotosList);
                            countLoadingPhotos += onePagePhotosList.size();
                        }
                    } else {
                        photoEnds = true;
                        break;
                    }
                }
                fragment.setCurCluster_id(curPage);
            } catch (IOException ioException) {
                Log.e(errorTag, ioException.getMessage());
                Log.d("InterestingnessTask", "Exception interrupted");
                this.cancel(true);
            }
        } else {
            Log.d("InterestingnessTask", "interrupted");
            this.cancel(true);
        }
        if(taskStartsWithPageNum == 0){
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
        Log.d("InterestingnessTask", "onCancelled");
        if(fragment != null) {
            fragment.setCurCluster_id(taskStartsWithPageNum);
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