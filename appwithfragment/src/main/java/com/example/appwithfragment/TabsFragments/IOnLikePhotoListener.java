package com.example.appwithfragment.TabsFragments;

import com.example.appwithfragment.ListContent;
import com.example.appwithfragment.RecyclerViewFragment.Categories;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Stack;

/**
 * Created by Евгения on 24.08.2016.
 */
public interface IOnLikePhotoListener extends Serializable{
    void onLikePhotoListener(ListContent lc);
    Stack getLikedPhotos();
    boolean isLikedPhoto(ListContent lc);
    void removePhoto(ListContent lc);
    void setCategory(String cat);
}
