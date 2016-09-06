package com.example.appwithfragment.RetrofitPack;

/**
 * Created by e.konobeeva on 06.09.2016.
 */
public class Photos {
    private String pages;
    private int perpage;
    private Photo[] photo = new Photo[perpage];

    public int getPerpage() {
        return perpage;
    }

    public Photo[] getPhoto() {
        return photo;
    }



    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    public void setPhoto(Photo[] photo) {
        this.photo = photo;
    }



    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }



}
