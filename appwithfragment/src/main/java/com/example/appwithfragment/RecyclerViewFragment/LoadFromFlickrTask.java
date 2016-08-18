package com.example.appwithfragment.RecyclerViewFragment;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by e.konobeeva on 05.08.2016.
 */
public class LoadFromFlickrTask extends AsyncTask<Object, Integer, Void> {
    private static final String errorTag = "ERROR Task";
    private int loadingPhotosPerOnce = 50;
    private boolean photoEnds = false;
    private int countLoadingPhotos = 0;


    private String protocol;
    private GettingResults fragment;
    private int page;
    private ArrayList<String> photoUrls;
    private ArrayList<String> photosInfo;
    private int pages;


    public LoadFromFlickrTask(GettingResults fragment, String protocol, ArrayList<String> photoUrls, ArrayList<String> photosInfo, int page){
        StringBuilder sb = new StringBuilder(protocol);
        sb.append("&per_page=20&page=");
        protocol = sb.toString();
        this.fragment = fragment;
        this.protocol = protocol;
        this.photoUrls = photoUrls;
        this.photosInfo = photosInfo;
        this.page = page;

    }

    public LoadFromFlickrTask(){}


    @Override
    protected Void doInBackground(Object... voids) {


        JSONObjects jsonObjects;

        if(!isCancelled()) {

            try {
                jsonObjects = new JSONObjects(protocol, page);
                pages = jsonObjects.getPages();

                while(countLoadingPhotos < loadingPhotosPerOnce){
                    //Log.d("PAGE", "" + page);
                    if(page <= pages) {
                        jsonObjects = new JSONObjects(protocol, page++);
                        int size = photoUrls.size();
                        //Log.d("SIZE URL", "" + size);
                        //Log.d("SIZE OBJECTS", "" + jsonObjects.getCountPhotosPerPage());
                        for (int i = 0; i <jsonObjects.getCountPhotosPerPage(); i++) {
                            photoUrls.add(i+size, jsonObjects.getUrl(i));
                            photosInfo.add(i+size, jsonObjects.getPhotoInfo(i)[4]);
                        }
                        countLoadingPhotos += jsonObjects.getCountPhotosPerPage();
                    }else {
                        photoEnds = true;
                        break;
                    }
                }
                //publishProgress(countLoadingPhotos);
                countLoadingPhotos = 0;
            } catch (IOException ioe) {
                Log.d(errorTag, ioe.toString());
            } catch (JSONException je) {
                Log.d(errorTag, je.toString());
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
    public int getCurClusterId(){
        return page;
    }

    public void setFragment(GettingResults fragment){
        this.fragment = fragment;
    }

}