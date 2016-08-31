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
    protected Map doInBackground(Object... voids) {

        JSONObjects jsonObjects;
        int curPage = fragment.getCurCluster_id();
        startPageNum = curPage;
        Log.d(errorTag, "doInBackground PAGE:" + curPage );
        int countLoadingPhotos = 0;
        int loadingPhotosPerOnce = 50;
        int pages;
        Map<String, String> photosInfo = new HashMap<>();

        if(!isCancelled()) {
            Log.d(errorTag, "doInBackground");

            try {
                jsonObjects = new JSONObjects(protocol, ++curPage);
                pages = jsonObjects.getPages();

                while(countLoadingPhotos < loadingPhotosPerOnce){
                    if(curPage <= pages) {
                        jsonObjects = new JSONObjects(protocol, curPage++);
                        for (int i = 0; i <jsonObjects.getCountPhotosPerPage(); i++) {
                            photosInfo.put(jsonObjects.getUrl(i), jsonObjects.getPhotoInfo(i)[4]);
                        }
                        countLoadingPhotos += jsonObjects.getCountPhotosPerPage();
                    }else {
                        photoEnds = true;
                        break;
                    }
                }
                fragment.setCurCluster_id(curPage);

            } catch (IOException ioe) {
                Log.d(errorTag, "IOException " + ioe.getMessage());
                Log.d("LoadFromFlickrTask", "Exception interrapted");
                this.cancel(true);
            } catch (JSONException je) {
                Log.d(errorTag, "JSONException " + je.getMessage());
                Log.d("LoadFromFlickrTask", "Exception interrapted");
                this.cancel(true);
            }
        }else {
            Log.d("LoadFromFlickrTask", "interrapted");
            this.cancel(true);
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

}