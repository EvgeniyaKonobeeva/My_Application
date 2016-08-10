package com.example.appwithfragment;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
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
    public void putImage(int keyUrl, Drawable bitmap){
        synchronized (lock1) {
            Log.d("OMCash", "putting img into");
            storage.put(keyUrl, bitmap);
            lock1.notifyAll();
        }


    }
    public boolean getImageTo(int keyUrl, ImageView im){
        synchronized (lock2) {
            if (storage.containsKey(keyUrl)) {
                Log.d("OMCash", "getting img out");
                im.setImageDrawable(storage.get(keyUrl));
                lock2.notifyAll();
                return true;
            } else {
                Log.d("OMCash", "doesn't exist");
                lock2.notifyAll();
                return false;
            }
        }
    }
    public Drawable getImg(String keyUrl){
        return storage.get(keyUrl.hashCode());
    }


}
