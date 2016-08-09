package com.example.appwithfragment.supportLib;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by e.konobeeva on 09.08.2016.
 */
public class OMCash{
    private Map<Integer, Drawable> storage;
    private Object lock1 = new Object();
    private Object lock2 = new Object();
    private int size;

    public OMCash(int size){
        this.size = size;
        storage = new LinkedHashMap<>(size, 0.75f, true);

    }
    public void putImage(int key, Drawable bitmap){
        synchronized (lock1) {
            storage.put(key, bitmap);
            lock1.notifyAll();
        }


    }
    public boolean getImageTo(int key, ImageView im){
        synchronized (lock2) {
            if (storage.containsKey(key)) {
                im.setImageDrawable(storage.get(key));
                lock2.notifyAll();
                return true;
            } else {
                lock2.notifyAll();
                return false;
            }
        }
    }
    public Drawable getImg(int key){
        return storage.get(key);
    }


}
