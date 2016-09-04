package com.example.appwithfragment.RecyclerViewFragment.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.appwithfragment.PhotoObjectInfo;
import com.example.appwithfragment.RecyclerViewFragment.GettingResults;
import com.example.appwithfragment.RecyclerViewFragment.ParserJSONTo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Евгения on 18.08.2016.
 * таск , который соответствует одному фрагменту,для загрузки данных с сервера
 *
 */
public class LoadTask extends AsyncTask<Object, Integer, ArrayList> {
    private static final String errorTag = "ERROR Task";
    private int loadingPhotosPerOnce = 50;
    private boolean photoEnds = false;


    private String protocol;
    private GettingResults fragment;
    private String tag;

    private int startPageNum;


    public LoadTask(GettingResults fragment, String protocol, String tag) {
        this.fragment = fragment;
        this.protocol = protocol;
        this.tag = tag;
        //Log.d("LoadTask ", tag);
    }

    public LoadTask() {
    }

    @Override
    protected ArrayList doInBackground(Object... voids) {
        ArrayList<PhotoObjectInfo> wholePhotosList = new ArrayList<>();
        if (!isCancelled()) {
            try {
                ArrayList<String> clustersId = getClustersIdArrayList(protocol);
                wholePhotosList = getPhotoArrayList(clustersId, tag);
            } catch (IOException ioEx) {
                Log.d("LoadTask ER ", ioEx.toString());
            } catch (JSONException jsonEx) {
                Log.d("LoadTask ER ", jsonEx.toString());
            }
        } else Thread.currentThread().interrupt();
        return wholePhotosList;
    }



    public void setFragment(GettingResults fragment) {
        this.fragment = fragment;
    }


    public ArrayList<String> getClustersIdArrayList(String protocol) throws IOException {
        ArrayList<String> clusters_id = new ArrayList<>();
        try {
            JSONObject clusters = ParserJSONTo.getJSONRootPoint(protocol).getJSONObject("clusters"); // clusters
            JSONArray cluster = clusters.getJSONArray("cluster");
            for (int i = 0; i < cluster.length(); i++) {
                JSONArray tag = cluster.getJSONObject(i).getJSONArray("tag");
                clusters_id.add(tag.getJSONObject(0).getString("_content"));
                /*for (int j = 0; j < tag.length(); j++) {
                    clusters_id.add(tag.getJSONObject(j).getString("_content"));
                }*/
            }
        } catch (JSONException jsonEx) {
            Log.d("LoadTask", "getClustersIdArrayList " + jsonEx.toString());
        }
        return clusters_id;
    }

    public ArrayList getPhotoArrayList(ArrayList<String> clusters_id, String tag) throws IOException, JSONException {

        String protocol = " https://api.flickr.com/services/rest/?method=flickr.tags.getClusterPhotos&" +
                "api_key=b14e644ffd373999f625f4d2ba244522&format=json&nojsoncallback=1&tag=";

        StringBuilder sb = new StringBuilder(protocol);
        sb.append(tag).append("&cluster_id=");
        protocol = sb.toString();

        int curCluster_id = fragment.getCurCluster_id();
        startPageNum = curCluster_id;

        ArrayList<PhotoObjectInfo> wholePhotosList = new ArrayList<>();
        ArrayList<PhotoObjectInfo> onePagePhotosList = new ArrayList<>();

        int countLoadingPhotos = 0;

        while (countLoadingPhotos < loadingPhotosPerOnce) {
            if (curCluster_id < clusters_id.size()) {
                String str = protocol + clusters_id.get(curCluster_id++);
                JSONObject photos = ParserJSONTo.getJSONRootPoint(str).getJSONObject("photos");
                JSONArray photo = photos.getJSONArray("photo");
                onePagePhotosList = ParserJSONTo.PhotoArrayList(photo);
                addWHoleArrayToAnotherArray(onePagePhotosList, wholePhotosList);
                countLoadingPhotos += onePagePhotosList.size();
            } else {
                photoEnds = true;
                break;
            }
        }
        fragment.setCurCluster_id(curCluster_id);

        return wholePhotosList;

    }

    @Override
    protected void onPostExecute(ArrayList aVoid) {
        if (fragment != null && aVoid.size() != 0) {
            fragment.onGettingResult(aVoid, photoEnds);
        }
    }

    @Override
    protected void onCancelled() {
        Log.d("LoadTask", "onCancelled");
        if (fragment != null) {
            fragment.setCurCluster_id(startPageNum);
        }
        super.onCancelled();
    }

    public void addWHoleArrayToAnotherArray(ArrayList putArr, ArrayList addToArr){
        for(int i = 0; i < putArr.size(); i++){
            addToArr.add(putArr.get(i));
        }
    }
}