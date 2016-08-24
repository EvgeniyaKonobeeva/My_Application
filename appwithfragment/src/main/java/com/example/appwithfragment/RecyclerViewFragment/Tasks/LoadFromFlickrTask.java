package com.example.appwithfragment.RecyclerViewFragment.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.appwithfragment.RecyclerViewFragment.GettingResults;
import com.example.appwithfragment.RecyclerViewFragment.JSONObjects;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by e.konobeeva on 05.08.2016.
 */
public class LoadFromFlickrTask extends AsyncTask<Object, Integer, Map> {

    private static final String errorTag = "LoadFFTask";
    private boolean photoEnds;
    private String protocol;
    private GettingResults fragment;


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
    protected Map doInBackground(Object... voids) {

        JSONObjects jsonObjects;
        int page = fragment.getCurCluster_id();
        Log.d(errorTag, "doInBackground PAGE:" + page );
        int countLoadingPhotos = 0;
        int loadingPhotosPerOnce = 50;
        int pages;
        Map<String, String> photosInfo = new HashMap<>();

        if(!isCancelled()) {
            Log.d(errorTag, "doInBackground");

            try {
                jsonObjects = new JSONObjects(protocol, ++page);
                pages = jsonObjects.getPages();

                while(countLoadingPhotos < loadingPhotosPerOnce){
                    if(page <= pages) {
                        jsonObjects = new JSONObjects(protocol, page++);
                        for (int i = 0; i <jsonObjects.getCountPhotosPerPage(); i++) {
                            photosInfo.put(jsonObjects.getUrl(i), jsonObjects.getPhotoInfo(i)[4]);
                        }
                        countLoadingPhotos += jsonObjects.getCountPhotosPerPage();
                    }else {
                        photoEnds = true;
                        break;
                    }
                }
                fragment.setCurCluster_id(page);

            } catch (IOException ioe) {
                Log.d(errorTag, ioe.toString());
            } catch (JSONException je) {
                Log.d(errorTag, je.toString());
            }
        }else {
            Thread.currentThread().interrupt();
        }

        return photosInfo;
    }

    @Override
    protected void onPostExecute(Map aVoid) {
        if(fragment != null && aVoid.size() != 0) {
            Log.d(errorTag, "onPostExecute");
            fragment.onGettingResult(aVoid, photoEnds);
        }
    }

    public void setFragment(GettingResults fragment){
        this.fragment = fragment;
    }

}