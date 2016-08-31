package com.example.appwithfragment.RecyclerViewFragment.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.appwithfragment.MVPPattern.RecViewFragPresenter;
import com.example.appwithfragment.RecyclerViewFragment.GettingResults;
import com.example.appwithfragment.RecyclerViewFragment.ParserJSONToPhotoUrl;

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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Евгения on 18.08.2016.
 * таск , который соответствует одному фрагменту,для загрузки данных с сервера
 * перед первым использованием в фрагменте необходимо вызвать инициализацию всех статических параметров методом setTaskParams
 * дальнейшие экземпляры тасков создаются обычным конструктором
 */
public class LoadTask extends AsyncTask<Object, Integer, Map>{
    private static final String errorTag = "ERROR Task";
    private int loadingPhotosPerOnce = 50;
    private boolean photoEnds = false;



    private String protocol;
    private GettingResults fragment;
    private String tag;

    private int startPageNum;


    public LoadTask(GettingResults fragment, String protocol, String tag ){
        this.fragment = fragment;
        this.protocol = protocol;
        this.tag = tag;
        //Log.d("LoadTask ", tag);
    }

    public LoadTask() {}

    @Override
    protected Map doInBackground(Object... voids) {
        Map<String, String> map = new HashMap<>();
        if(!isCancelled()) {
            try {
                ArrayList<String> clustersId = clustersIdQuery(protocol);
                map = photoArrQuery(clustersId, tag);
            } catch (IOException ioEx) {
                Log.d("LoadTask ER ", ioEx.toString());
            } catch (JSONException jsonEx) {
                Log.d("LoadTask ER ", jsonEx.toString());
            }
        }else Thread.currentThread().interrupt();
        return map;
    }

    @Override
    protected void onPostExecute(Map aVoid) {
        if(fragment != null && aVoid.size() != 0) {
            fragment.onGettingResult(aVoid, photoEnds);
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
                clusters_id.add(tag.getJSONObject(0).getString("_content"));
                /*for (int j = 0; j < tag.length(); j++) {
                    clusters_id.add(tag.getJSONObject(j).getString("_content"));
                }*/
            }
        }catch (JSONException jsonEx){
            Log.d("LoadTask", "clustersIdQuery " +jsonEx.toString());
        }
        return clusters_id;
    }

    public Map photoArrQuery(ArrayList<String> clusters_id, String tag)throws IOException, JSONException{

        String protocol = " https://api.flickr.com/services/rest/?method=flickr.tags.getClusterPhotos&" +
                "api_key=b14e644ffd373999f625f4d2ba244522&format=json&nojsoncallback=1&tag=";

        StringBuilder sb = new StringBuilder(protocol);
        sb.append(tag).append("&cluster_id=");
        protocol = sb.toString();

        int curCluster_id = fragment.getCurCluster_id();
        startPageNum = curCluster_id;

        Map<String,String> map = new HashMap<>();

        int countLoadingPhotos = 0;

        while(countLoadingPhotos < loadingPhotosPerOnce){
            if(curCluster_id <  clusters_id.size()) {
                String str = protocol+clusters_id.get(curCluster_id++);
                JSONObject photos = getJSONRootPoint(str).getJSONObject("photos");
                Object[] urlArr = ParserJSONToPhotoUrl.getUrlArray(photos.getJSONArray("photo"));
                Object[] titles =  ParserJSONToPhotoUrl.getTitleArray(photos.getJSONArray("photo"));
                for (int i = 0; i <urlArr.length; i++) {
                    map.put(urlArr[i].toString(), titles[i].toString());
                }
                countLoadingPhotos += urlArr.length;
            }else {
                photoEnds = true;
                break;
            }
        }
        fragment.setCurCluster_id(curCluster_id);
        return map;

    }

    @Override
    protected void onCancelled() {
        Log.d("LoadTask", "onCancelled");
        if(fragment != null) {
            fragment.setCurCluster_id(startPageNum);
        }
        super.onCancelled();
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
        //Log.d("LoadTask jsonObject", obj.toString());
        return obj;

    }
}
