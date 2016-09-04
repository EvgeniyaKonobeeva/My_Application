package com.example.appwithfragment;

import java.io.Serializable;

/**
 * Created by e.konobeeva on 28.07.2016.
 */
public class PhotoObjectInfo implements Serializable{
    private String fullTitle;
    private String imgUrl;


    public PhotoObjectInfo(String imgUrl, String title){
        this.fullTitle = title;
        this.imgUrl = imgUrl;

    }
    public PhotoObjectInfo(){}

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

    @Override
    public boolean equals(Object obj) {
        PhotoObjectInfo lc = (PhotoObjectInfo)obj;
        if(this.getImgUrl().equals(lc.getImgUrl()) && this.getFullTitle().equals(lc.getFullTitle()))
            return true;
        else return false;
    }

    @Override
    public int hashCode() {
        return this.getImgUrl().hashCode();
    }
}
