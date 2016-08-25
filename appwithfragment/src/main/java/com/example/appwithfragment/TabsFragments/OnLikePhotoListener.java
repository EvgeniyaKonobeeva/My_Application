package com.example.appwithfragment.TabsFragments;

import android.util.Log;

import com.example.appwithfragment.ListContent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * Created by Евгения on 24.08.2016.
 */
public class OnLikePhotoListener implements IOnLikePhotoListener{
    private Stack<ListContent> likedList;

    public OnLikePhotoListener(){
        Log.d("OnLikePhotoListener", "onLikePhotoListener create");
        likedList = new Stack<>();
    }

    //метод вызывается фрагментом FragmentFullScreenPicture по нажатию кнопки
    public void onLikePhotoListener(ListContent lc){
        likedList.push(lc);
        Log.d("OnLikePhotoListener", "onLikePhotoListener " + Arrays.toString(likedList.toArray()));
    }

    //метод вызывается для получения списка фотографий фрагментом LikedPhotosFragment
    public Stack getLikedPhotos(){
        return likedList;
    }

    @Override
    public boolean isLikedPhoto(ListContent lc) {
        Log.d("OnLikePhotoListener", "isLikedPhoto");
        if(likedList.contains(lc)){
            Log.d("OnLikePhotoListener", "isLikedPhoto true");
            return true;
        }else {
            Log.d("OnLikePhotoListener", "isLikedPhoto false");
            return false;
        }
    }

    public void removePhoto(ListContent lc){
        Log.d("OnLikePhotoListener", "removePhoto " + lc);
        likedList.remove(lc);
    }


}
