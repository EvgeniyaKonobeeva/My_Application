package com.example.appwithfragment.TabsFragments;

import com.example.appwithfragment.PhotoObjectInfo;
import com.example.appwithfragment.MVPPattern.IFragment;

import java.io.Serializable;

/**
 * Created by Евгения on 24.08.2016.
 */
public interface IOnLikePhotoListener extends Serializable{
    void onLikePhotoListener(PhotoObjectInfo lc);
    boolean isLikedPhoto(PhotoObjectInfo lc);
    void removePhoto(PhotoObjectInfo lc);
    void setCategory(String cat);
    void setFragment(IFragment f);

}
