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
    private Map<Integer, WeakReference<Bitmap>> storage;
    private Object lock1 = new Object();
    private Object lock2 = new Object();
    private Object lock3 = new Object();
    private int size;

    public OMCash(int size){
        this.size = size;
        storage = new LinkedHashMap<>(300);

    }
    public void putImage(int keyUrl, Bitmap bitmap){
        synchronized (lock1) {
            //Log.d("OMCash", "putting img into");
            WeakReference<Bitmap> weakRef= new WeakReference<>(bitmap);
            storage.put(keyUrl, weakRef);
            lock1.notifyAll();
        }


    }
    public void setImageTo(int keyUrl, ImageView im){
        synchronized (lock2) {
            Log.d("OMCash", "setImageTo into");
            im.setImageBitmap(storage.get(keyUrl).get());
            lock2.notifyAll();
        }
    }

    public boolean containsBitmap(int keyUrl){
        synchronized (lock3) {
            if (storage.containsKey(keyUrl) && storage.get(keyUrl).get() != null) {
                lock3.notifyAll();
                return true;
            } else if(storage.get(keyUrl) == null){
                storage.remove(keyUrl);
                lock3.notifyAll();
                return false;
            } else {
                //Log.d("OMCash", "doesn't exist");
                lock3.notifyAll();
                return false;
            }
        }
    }


}
