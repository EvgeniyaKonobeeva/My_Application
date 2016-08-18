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

/**
 * Created by Евгения on 18.08.2016.
 * таск , который соответствует одному фрагменту,для загрузки данных с сервера
 * перед первым использованием в фрагменте необходимо вызвать инициализацию всех статических параметров методом setTaskParams
 * дальнейшие экземпляры тасков создаются обычным конструктором
 */
public class LoadTask extends AsyncTask<Void, Integer, Void>{
    private static final String errorTag = "ERROR Task";
    private static String protocol;
    private static GettingResults fragment;
    private static int loadingPhotosPerOnce;
    private static int curCluster_id;
    private static boolean photoEnds = false;
    private static ArrayList<String> photoUrls;
    private static ArrayList<String> photosInfo;
    private static String tag;
    private int countLoadingPhotos;


    public static LoadTask setTaskParams(String protocol, GettingResults fragment, String tag){
        LoadTask.loadingPhotosPerOnce = 50;
        LoadTask.curCluster_id = 0;
        LoadTask.photoEnds = false;
        LoadTask.photoUrls = new ArrayList<>();
        LoadTask.photosInfo = new ArrayList<>();
        LoadTask.protocol = protocol;
        LoadTask.fragment = fragment;
        LoadTask.tag = tag;
        return new LoadTask();
    }
    public LoadTask(){
        if(loadingPhotosPerOnce == 0 || curCluster_id == 0 || photoEnds !=  false || photoUrls == null ||
                photosInfo == null || protocol==null){
            Log.e("ERROR Task ", "you must call setTaskParams() method at first");
            return;
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if(!isCancelled()) {
            try {
                photoArrQuery(clustersIdQuery(protocol), tag);
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
    public  ArrayList<String> clustersIdQuery(String protocol)throws IOException, JSONException{
        ArrayList<String> clusters_id = new ArrayList<>();
        JSONObject clusters = getJSONRootPoint(protocol).getJSONObject("clusters"); // clusters
        JSONArray cluster = clusters.getJSONArray("cluster");
        for(int i = 0; i < cluster.length(); i++){
            JSONArray tag = cluster.getJSONArray(i);
            for(int j = 0; j < tag.length(); j++){
                clusters_id.add(tag.getString(i));
            }
        }
        return clusters_id;
    }

    public void photoArrQuery(ArrayList<String> clusters_id, String tag)throws IOException, JSONException{

        protocol = " https://api.flickr.com/services/rest/?method=flickr.tags.getClusterPhotos&" +
                "api_key=b14e644ffd373999f625f4d2ba244522&format=json&nojsoncallback=1&tag=";
        StringBuilder sb = new StringBuilder(protocol);
        sb.append(tag);
        while(countLoadingPhotos < loadingPhotosPerOnce){
            if(curCluster_id <  clusters_id.size()) {
                sb.append("&cluster_id=").append(clusters_id.get(curCluster_id));
                JSONObject photos = getJSONRootPoint(protocol);
                String[] urlArr = ParserJSONToPhotoUrl.getUrlArray(photos.getJSONArray("photo"));
                String[] titles = ParserJSONToPhotoUrl.getTitleArray(photos.getJSONArray("photo"));
                int size = photoUrls.size();
                //Log.d("SIZE URL", "" + size);
                //Log.d("SIZE OBJECTS", "" + jsonObjects.getCountPhotosPerPage());
                for (int i = 0; i <urlArr.length; i++) {
                    photoUrls.add(i+size, urlArr[i]);
                    photosInfo.add(i+size, titles[i]);
                }
                countLoadingPhotos += urlArr.length;
            }else {
                photoEnds = true;
                break;
            }
        }

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
        return new JSONObject(buf.toString());

    }
}
