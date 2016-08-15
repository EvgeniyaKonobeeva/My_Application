package com.example.appwithfragment.RecyclerViewFragment;

import android.os.AsyncTask;
import android.util.Log;

import com.example.appwithfragment.JSONObjects;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by e.konobeeva on 05.08.2016.
 */
public class LoadFromFlickrTask extends AsyncTask<Void, Integer, Void> {
    private static final String errorTag = "ERROR";
    private String protocol = " https://api.flickr.com/services/rest/?method=flickr.interestingness.getList&api_key=b14e644ffd373999f625f4d2ba244522" +
            "&format=json&nojsoncallback=1";
    private GettingResults fragment;

    private static int loadingPhotosPerOnce = 50;
    private static int page = 1;
    private static boolean photoEnds = false;
    private static int pages;

    static ArrayList<String> photoUrls = new ArrayList<>();
    static ArrayList<String> photosInfo= new ArrayList<>();





    public LoadFromFlickrTask(GettingResults fragment){
        this.fragment = fragment;
        StringBuilder sb = new StringBuilder(protocol);
        sb.append("&per_page=20&page=");
        protocol = sb.toString();
        //photoUrls ;
        //photosInfo = new ArrayList<>();
    }

    private int countLoadingPhotos = 0;


    @Override
    protected Void doInBackground(Void... voids) {


        JSONObjects jsonObjects;

        if(!isCancelled()) {

            try {
                if(page == 1) {
                    jsonObjects = new JSONObjects(protocol, page);
                    pages = jsonObjects.getPages();
                }

                while(countLoadingPhotos < loadingPhotosPerOnce){
                    Log.d("PAGE", "" + page);
                    if(page <= pages) {
                        jsonObjects = new JSONObjects(protocol, page++);
                        countLoadingPhotos += jsonObjects.getCountPhotosPerPage();
                        int size = photoUrls.size();
                        for (int i = size; i < countLoadingPhotos; i++) {
                            photoUrls.add(i, jsonObjects.getUrl(i));
                            photosInfo.add(i, jsonObjects.getPhotoInfo(i)[4]);
                        }
                    }else {
                        photoEnds = true;
                        break;
                    }
                }
                countLoadingPhotos = 0;
            } catch (IOException ioe) {
                Log.d(errorTag, ioe.getMessage());
            } catch (JSONException je) {
                Log.d(errorTag, je.getMessage());
            }
        }else {
            Thread.currentThread().interrupt();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(fragment != null && photoUrls.size() != 0) {
            fragment.onGettingResult(photoUrls, photosInfo, photoEnds);
        }
    }

    public void setFragment(GettingResults fragment){
        this.fragment = fragment;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if(fragment != null) {
            fragment.getProgress(values[0]);
        }
    }
}