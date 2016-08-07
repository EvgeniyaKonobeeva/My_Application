package com.example.appwithfragment;

import android.graphics.drawable.Drawable;

/**
 * Created by e.konobeeva on 28.07.2016.
 */
public class ListContent {
    private String string;
    private String imRes;
    private Drawable img;
    public ListContent(String str){
        string = str;
    }
    public String getString(){
        return string;
    }
    public String getImRes(){
        return imRes;
    }

    public Drawable getImg() {
        return img;
    }


    public void setImg(Drawable img) {
        this.img = img;
    }
    public void setImRes(String url){
        imRes = url;
    }
    public void setTitle(String title){
        this.string = title;
    }
}
