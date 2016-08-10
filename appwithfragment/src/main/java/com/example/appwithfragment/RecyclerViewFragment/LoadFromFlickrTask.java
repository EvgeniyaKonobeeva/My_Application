package com.example.appwithfragment.recyclerViewFragment;

import android.os.AsyncTask;
import android.util.Log;

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
 * Created by e.konobeeva on 05.08.2016.
 */
public class LoadFromFlickrTask extends AsyncTask<Void, Integer, Void> {
    private static final String errorTag = "ERROR";
    private String protocol = " https://api.flickr.com/services/rest/?method=flickr.interestingness.getList&api_key=b14e644ffd373999f625f4d2ba244522" +
            "&format=json&nojsoncallback=1";
    private static ArrayList<String> photoUrls = new ArrayList<>();
    private static ArrayList<String> photosInfo = new ArrayList<>();
    private GettingResults fragment;

    private static int loadingPhotosPerOnce = 20;

    private static int page = 1;




    public LoadFromFlickrTask(GettingResults fragment){
        this.fragment = fragment;
        StringBuilder sb = new StringBuilder(protocol);
        sb.append("&per_page=20&page=");
        protocol = sb.toString();
    }

    private int countLoadingPhotos = 0;


    @Override
    protected Void doInBackground(Void... voids) {


        Log.d("HERE", "here 1");

        if(!isCancelled()) {
            try {
                HttpURLConnection connection = setConnection(protocol + page);
                connection.connect();
                String imgUrl = "https://farm[farm_id].staticflickr.com/[server_id]/[ID]_[id_secret]_m.jpg";

                JSONObject photos = getJSONInfo(connection).getJSONObject("photos");
                int pages = photos.getInt("pages");

                while(countLoadingPhotos < loadingPhotosPerOnce){
                    if(page <= pages) {
                        connection = setConnection(protocol + page++);
                        connection.connect();
                        photos = getJSONInfo(connection).getJSONObject("photos");
                        JSONArray jsa = photos.getJSONArray("photo");
                        countLoadingPhotos += jsa.length();
                        int size = photoUrls.size();
                        for (int i = 0; i < jsa.length(); i++) {
                            Log.d("HERE", "here 2");
                            JSONObject photo = jsa.getJSONObject(i);
                            String farmId = photo.getString("farm");
                            String serverId = photo.getString("server");
                            String id = photo.getString("id");
                            String secret = photo.getString("secret");
                            String title = photo.getString("title");

                            String qq = imgUrl.replace("[farm_id]", farmId).replace("[server_id]", serverId).replace("[ID]", id).replace("[id_secret]", secret);

                            photoUrls.add(size + i, qq);
                            photosInfo.add(size + i, title);
                        }
                    }
                }
                publishProgress(countLoadingPhotos);
            } catch (IOException ioe) {
                Log.d(errorTag, ioe.getMessage());
            } catch (JSONException je) {
                Log.d(errorTag, je.getMessage());
            }
        }else {
            Log.d("HERE", "here 3");
            Thread.currentThread().interrupt();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(fragment != null && photoUrls.size() != 0) {
            fragment.onGettingResult(photoUrls, photosInfo);
        }
    }


    public JSONObject getJSONInfo(HttpURLConnection connection) throws IOException, JSONException {

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
    private HttpURLConnection setConnection(String protocol)throws IOException{
        URL url = new URL(protocol);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        return connection;
    }
    public GettingResults getFragment(){
        return fragment;
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