package com.example.appwithfragment.RecyclerViewFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Евгения on 18.08.2016.
 */
public class ParserJSONToPhotoUrl {

    public static String[] getPhotoInfo(JSONArray array, int i) throws JSONException {

        return new String[]{
                array.getJSONObject(i).getString("farm"),
                array.getJSONObject(i).getString("server"),
                array.getJSONObject(i).getString("id"),
                array.getJSONObject(i).getString("secret"),
                array.getJSONObject(i).getString("title")
        };
    }
    public static String getUrl(JSONArray array, int i)throws JSONException{
        String imgUrl = "https://farm[0].staticflickr.com/[1]/[2]_[3]_m.jpg";

        String[] info = getPhotoInfo(array,i);
        return imgUrl.replace("[0]", info[0]).replace("[1]", info[1]).replace("[2]", info[2]).replace("[3]", info[3]);
    }

    public static String[] getUrlArray(JSONArray array) throws JSONException{
        String urlArr[] = new String[array.length()];
        for(int i = 0; i < array.length(); i++){
            urlArr[i] = getUrl(array, i);
        }
        return urlArr;
    }
    public static String[] getTitleArray(JSONArray array)throws JSONException{
        String urlArr[] = new String[array.length()];
        for(int i = 0; i < array.length(); i++){
            urlArr[i] = getPhotoInfo(array, i)[4];
        }
        return urlArr;
    }
}
