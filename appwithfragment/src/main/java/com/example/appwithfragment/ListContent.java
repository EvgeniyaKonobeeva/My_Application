package com.example.appwithfragment;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.lang.ref.WeakReference;

/**
 * Created by e.konobeeva on 28.07.2016.
 */
public class ListContent implements Serializable{
    private String fullTitle;
    private String imgUrl;

    public ListContent(String imgUrl, String title){
        this.fullTitle = title;
        this.imgUrl = imgUrl;

    }

    public void setFullTitle(String fullTitle) {
        this.fullTitle = fullTitle;
    }

    public String getFullTitle() {
        return fullTitle;
    }

    public String getShortTitle(){
        return fullTitle.substring(0, fullTitle.length() > 20 ? 20 : fullTitle.length())+"...";
    }

    public void setImgUrl(String url){
        imgUrl = url;
    }

    public String getImgUrl(){
        return imgUrl;
    }


}
