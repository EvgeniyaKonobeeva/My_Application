package com.example.appwithfragment;

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


/**
 * Created by e.konobeeva on 03.08.2016.
 */
public class ConnectionToFlickr1 implements Runnable{


    private String protocol = " https://api.flickr.com/services/rest/?method=flickr.interestingness.getList&api_key=b14e644ffd373999f625f4d2ba244522" +
            "&format=json&nojsoncallback=1";

    private String key = "b14e644ffd373999f625f4d2ba244522";
    private String secret = "4610da2f4d81bcb9";

    private String per_page = "&per_page=";
    private String page = "&page=";
    private String date = "&date=2016-08-02";

    private String resultStr;

    public ConnectionToFlickr1(int per_page, int page){

        StringBuilder sb = new StringBuilder(protocol);
        sb.append(this.page+page);
        sb.append(this.per_page+per_page);
        sb.append(date);

        protocol = sb.toString();
    }
    public ConnectionToFlickr1(){};

    @Override
    public void run() {
        try {
            URL url = new URL(protocol);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            JSONArray jsa = getJSONInfo(connection).getJSONObject("photos").getJSONArray("photo");

            String query = " https://api.flickr.com/services/rest/?method=flickr.photos.getInfo&api_key=10c6cbb1124a050c3bd98a03ab1aaf33" +
                    "&format=json&nojsoncallback=1";

            for(int i = 0; i < jsa.length(); i++){
                JSONObject obj = jsa.getJSONObject(i);

                String id = obj.getString("id");
                String secret = obj.getString("secret");

                StringBuilder sb = new StringBuilder();
                sb.append(query);
                sb.append("&photo_id=" + id);
                sb.append("&secret=" + secret);


                HttpURLConnection conn = (HttpURLConnection)new URL(sb.toString()).openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                JSONObject photo = getJSONInfo(conn).getJSONObject("photo").getJSONObject("urls");
                String photosUrl = photo.getJSONArray("url").getJSONObject(0).getString("_content");
                Log.d("RESULT", photosUrl);

            }
        }catch (MalformedURLException mue){
            Log.d("ERROR 1", mue.getMessage());
        }catch (IOException ioe) {
            Log.d("ERROR 2", ioe.getMessage());
        }catch (JSONException jse){
            Log.d("ERROR 3", jse.toString());
        }
    }

    public JSONObject getJSONInfo(HttpURLConnection connection)throws IOException, JSONException{

        InputStream is = connection.getInputStream();
        StringBuffer buf = new StringBuffer();
        String photosData;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        while ((photosData = reader.readLine()) != null) {
            buf.append(photosData);
        }

        Log.d("STRING 1", buf.toString());
        is.close();
        connection.disconnect();
        return new JSONObject(buf.toString());

    }

}
