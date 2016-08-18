package com.example.appwithfragment.RecyclerViewFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by e.konobeeva on 15.08.2016.
 */
public class JSONObjects {

    JSONObject headObject;
    int pages;
    JSONArray array;

    public JSONObjects(String protocol, int page)throws IOException, JSONException{
        HttpURLConnection connection = setConnection(protocol + page);
        connection.connect();
        headObject = getJSONRootPoint(connection).getJSONObject("photos"); // clusters
        pages = headObject.getInt("pages");// total
        array = headObject.getJSONArray("photo");//cluster
        // in cluster get tag(array)
        // form new protocol

    }


    public JSONObject getPhotoObj(int i)throws JSONException{
        return array.getJSONObject(i);
    }
    public String[] getPhotoInfo(int i) throws JSONException{

        return new String[]{
                array.getJSONObject(i).getString("farm"),
                array.getJSONObject(i).getString("server"),
                array.getJSONObject(i).getString("id"),
                array.getJSONObject(i).getString("secret"),
                array.getJSONObject(i).getString("title")
        };
    }

    public String getUrl(int i)throws JSONException{
        String imgUrl = "https://farm[0].staticflickr.com/[1]/[2]_[3]_m.jpg";

        String[] info = getPhotoInfo(i);
        return imgUrl.replace("[0]", info[0]).replace("[1]", info[1]).replace("[2]", info[2]).replace("[3]", info[3]);
    }

    public int getPages(){
        return pages;
    }

    public int getCountPhotosPerPage() {
        return array.length();
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
    public JSONObject getJSONRootPoint(HttpURLConnection connection) throws IOException, JSONException {

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
