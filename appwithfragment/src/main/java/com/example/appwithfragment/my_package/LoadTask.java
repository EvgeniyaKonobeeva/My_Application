package com.example.appwithfragment.my_package;

import android.os.AsyncTask;
import android.util.Log;

import com.example.appwithfragment.RecyclerViewFragment.GettingResults;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Евгения on 18.08.2016.
 * таск , который соответствует одному фрагменту,для загрузки данных с сервера
 * перед первым использованием в фрагменте необходимо вызвать инициализацию всех статических параметров методом setTaskParams
 * дальнейшие экземпляры тасков создаются обычным конструктором
 */
public class LoadTask extends AsyncTask<Object, Integer, Void>{
    private static final String errorTag = "ERROR Task";
    private int loadingPhotosPerOnce = 50;
    private boolean photoEnds = false;
    private int countLoadingPhotos = 0;


    private String protocol;
    private GettingResults fragment;
    private int curCluster_id;
    private ArrayList<String> photoUrls;
    private ArrayList<String> photosInfo;
    private String tag;
    private ArrayList<String> clustersId;




    public LoadTask(GettingResults fragment, String protocol, ArrayList<String> photoUrls, ArrayList<String> photosInfo, int curCluster_id, String tag ){
        this.fragment = fragment;
        this.protocol = protocol;
        this.photoUrls = photoUrls;
        this.photosInfo = photosInfo;
        this.curCluster_id = curCluster_id;
        this.tag = tag;
        Log.d("LoadTask ", tag);
    }

    public LoadTask()
    {}
    @Override
    protected Void doInBackground(Object... voids) {
        if(!isCancelled()) {
            try {
                clustersId = clustersIdQuery(protocol);
                photoArrQuery(clustersId, tag);
            } catch (IOException ioEx) {
                Log.d("LoadTask ER ", ioEx.toString());
            } catch (JSONException jsonEx) {
                Log.d("LoadTask ER ", jsonEx.toString());
            }
        }else Thread.currentThread().interrupt();
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
    public  ArrayList<String> clustersIdQuery(String protocol)throws IOException{
        ArrayList<String> clusters_id = new ArrayList<>();
        try {
            JSONObject clusters = getJSONRootPoint(protocol).getJSONObject("clusters"); // clusters
            JSONArray cluster = clusters.getJSONArray("cluster");
            for (int i = 0; i < cluster.length(); i++) {
                JSONArray tag = cluster.getJSONObject(i).getJSONArray("tag");
                for (int j = 0; j < tag.length(); j++) {
                    clusters_id.add(tag.getJSONObject(j).getString("_content"));
                }
            }
        }catch (JSONException jsonEx){
            Log.d("LoadTask", "clustersIdQuery " +jsonEx.toString());
        }
        return clusters_id;
    }

    public void photoArrQuery(ArrayList<String> clusters_id, String tag)throws IOException, JSONException{

        String protocol = " https://api.flickr.com/services/rest/?method=flickr.tags.getClusterPhotos&" +
                "api_key=b14e644ffd373999f625f4d2ba244522&format=json&nojsoncallback=1&tag=";
        StringBuilder sb = new StringBuilder(protocol);
        sb.append(tag).append("&cluster_id=");
        protocol = sb.toString();
        while(countLoadingPhotos < loadingPhotosPerOnce){
            if(curCluster_id <  clusters_id.size()) {
                //sb.append(clusters_id.get(curCluster_id++));
                String str = protocol+clusters_id.get(curCluster_id++);
                Log.d("cl tag  ", clusters_id.get(curCluster_id));
                JSONObject photos = getJSONRootPoint(str).getJSONObject("photos");
                Object[] urlArr = ParserJSONToPhotoUrl.getUrlArray(photos.getJSONArray("photo"));
                Log.d("URL ", urlArr.length + "");
                Object[] titles =  ParserJSONToPhotoUrl.getTitleArray(photos.getJSONArray("photo"));
                int size = photoUrls.size();
                for (int i = 0; i <urlArr.length; i++) {
                    photoUrls.add(i+size, urlArr[i].toString());
                    photosInfo.add(i+size, titles[i].toString());
                }
                countLoadingPhotos += urlArr.length;
            }else {
                photoEnds = true;
                break;
            }
        }

    }
    public int getCurClusterId(){
        return curCluster_id;
    }

    private HttpURLConnection setConnection(String protocol)throws IOException {
        URL url = new URL(protocol);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(20000);
        connection.setConnectTimeout(20000);
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        return connection;
    }
    public JSONObject getJSONRootPoint(String protocol) throws IOException, JSONException {

        HttpURLConnection connection = setConnection(protocol);
        connection.connect();
        InputStream is = connection.getInputStream();
        StringBuffer buf = new StringBuffer();
        String photosData;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        while ((photosData = reader.readLine()) != null) {
            buf.append(photosData);
        }
        is.close();
        connection.disconnect();
        JSONObject obj = new JSONObject(buf.toString());
        Log.d("LoadTask jsonObject", obj.toString());
        return obj;

    }
}
