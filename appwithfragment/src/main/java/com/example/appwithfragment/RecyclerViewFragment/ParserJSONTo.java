package com.example.appwithfragment.RecyclerViewFragment;

import com.example.appwithfragment.PhotoObjectInfo;
import com.example.appwithfragment.RetrofitPack.Photo;

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
 */
public class ParserJSONTo {

    public static String[] getPhotoInfo(Photo array) throws JSONException {
        return new String[]{
                Integer.toString(array.getFarm()),
                array.getServer(),
                array.getId(),
                array.getSecret(),
                array.getTitle()
        };
    }
    public static String getUrl(Photo array)throws JSONException{
        String imgUrl = "https://farm[0].staticflickr.com/[1]/[2]_[3]_m.jpg";

        String[] info = getPhotoInfo(array);
        return imgUrl.replace("[0]", info[0]).replace("[1]", info[1]).replace("[2]", info[2]).replace("[3]", info[3]);
    }

    private static String[] getUrlArray(Photo[] array) throws JSONException{
        String urlArr[] = new String[array.length];
        for(int i = 0; i < array.length; i++){
            urlArr[i] = getUrl(array[i]);
        }
        return urlArr;
    }
    private static String[] getTitleArray(Photo[] array)throws JSONException{
        String urlArr[] = new String[array.length];
        for(int i = 0; i < array.length; i++){
            urlArr[i] = getPhotoInfo(array[i])[4];
        }
        return urlArr;
    }

    public static ArrayList PhotoArrayList(Photo[] jsonArray)throws IOException, JSONException{

        String[] url = getUrlArray(jsonArray);
        String[] titles = getTitleArray(jsonArray);

        ArrayList arrayList = new ArrayList<>();

        for(int i = 0; i < url.length; i++){

            arrayList.add(new PhotoObjectInfo(url[i],titles[i]));
        }

        removeRepeatingElements(arrayList);
        return arrayList;
    }

    private static void removeRepeatingElements(ArrayList arrayList){
        ArrayList arrayListDuplicate = arrayList;

        for(int i = 0; i< arrayListDuplicate.size(); i++){

            for(int j = i+1; j < arrayListDuplicate.size(); j++){

                if(arrayList.get(j).equals(arrayListDuplicate.get(i))){

                    arrayList.remove(j--);
                }
            }
        }
    }

    public static HttpURLConnection setConnection(String protocol)throws IOException {
        URL url = new URL(protocol);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(20000);
        connection.setConnectTimeout(20000);
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        return connection;
    }

    public static JSONObject getJSONRootPoint(String protocol) throws IOException, JSONException {

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
        return obj;

    }


}
