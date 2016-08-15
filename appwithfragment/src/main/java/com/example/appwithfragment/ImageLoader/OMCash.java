package com.example.appwithfragment.ImageLoader;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by e.konobeeva on 09.08.2016.
 */
public class OMCash{
    private Map<Integer, Bitmap> storage;
    private Object lock1 = new Object();
    private Object lock2 = new Object();
    private int size;

    public OMCash(int size){
        this.size = size;
        storage = new WeakHashMap<Integer, Bitmap>(100);

    }
    public void putImage(int keyUrl, Bitmap bitmap){
        synchronized (lock1) {
            Log.d("OMCash", "putting img into");
            //WeakReference<Bitmap> weakRef= new WeakReference<>(bitmap);
            storage.put(keyUrl, bitmap);
            lock1.notifyAll();
        }


    }
    public boolean getImageTo(int keyUrl, ImageView im){
        synchronized (lock2) {
            if (storage.containsKey(keyUrl)) {
                Log.d("OMCash", "getting img out");
                im.setImageBitmap(storage.get(keyUrl));
                lock2.notifyAll();
                return true;
            } else {
                Log.d("OMCash", "doesn't exist");
                lock2.notifyAll();
                return false;
            }
        }
    }
    public Bitmap getImg(String keyUrl){
        return storage.get(keyUrl.hashCode());
    }


}
