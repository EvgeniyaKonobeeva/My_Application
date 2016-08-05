package com.example.appwithfragment;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by e.konobeeva on 05.08.2016.
 */
public class LoadFromFlickrTask extends AsyncTask<Void, Integer, Void> {
    private String protocol = " https://api.flickr.com/services/rest/?method=flickr.interestingness.getList&api_key=b14e644ffd373999f625f4d2ba244522" +
            "&format=json&nojsoncallback=1";
    private ArrayList<String> photoUrls;
    public MyFragment fragment;
    private int page = 2;



    public LoadFromFlickrTask(){
        this.photoUrls = new ArrayList<>();
    }


    @Override
    protected Void doInBackground(Void... voids) {


        try {
            Log.d("HERE ", "here 1");
            HttpURLConnection connection = setConnection(protocol+1);
            connection.connect();
            JSONObject photos = getJSONInfo(connection).getJSONObject("photos");

            //int pages = photos.getInt("pages");

            String query2 = "https://farm[farm_id].staticflickr.com/[server_id]/[ID]_[id_secret].jpg";

            for(int k = 1; k <= page/2; k++) {
                Log.d("HERE ", "here " + k);
                connection = setConnection(protocol+k);
                connection.connect();
                photos = getJSONInfo(connection).getJSONObject("photos");
                JSONArray jsa = photos.getJSONArray("photo");
                for (int i = 0; i < jsa.length(); i++) {
                    JSONObject obj = jsa.getJSONObject(i);
                    String farmId = obj.getString("farm");
                    String serverId = obj.getString("server");
                    String id = obj.getString("id");
                    String secret = obj.getString("secret");

                    String qq = query2.replace("[farm_id]", farmId).replace("[server_id]", serverId).replace("[ID]", id).replace("[id_secret]", secret);

                    photoUrls.add(qq);
                }
            }

        } catch (IOException ioe) {
            Log.d("ERROR 1", ioe.getMessage());
        } catch (JSONException je) {
            Log.d("ERROR 2", je.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(fragment != null) {
            fragment.onGettingResult(photoUrls);
        }
    }

    public LoadFromFlickrTask setParams(int per_page){
        StringBuilder sb = new StringBuilder(protocol);
        sb.append("&per_page=" + per_page + "&page=");
        protocol = sb.toString();
        return this;
    }

    public JSONObject getJSONInfo(HttpURLConnection connection) throws IOException, JSONException {

        InputStream is = connection.getInputStream();
        StringBuffer buf = new StringBuffer();
        String photosData;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        while ((photosData = reader.readLine()) != null) {
            buf.append(photosData);
        }

        //Log.d("STRING 1", buf.toString());
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
}