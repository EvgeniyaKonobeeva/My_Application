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
    private WeakReference<Drawable> img;
    private Drawable imgSmallSize;
    private Drawable imgBigSize;

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

    public Drawable getImgSmall() {
        return imgSmallSize;
    }

    public void setImgSmall(Drawable img) {
        this.imgSmallSize = img;
    }

    public Drawable getImgBigSize() {
        return imgBigSize;
    }

    public void setImgBigSize(Drawable imgBigSize) {
        this.imgBigSize = imgBigSize;
    }

    public Drawable getImg(){
        if(img != null) {
            return img.get();
        }else return null;
    }
    public void setImg(Drawable img){
        this.img = new WeakReference<Drawable>(img);
    }


}
