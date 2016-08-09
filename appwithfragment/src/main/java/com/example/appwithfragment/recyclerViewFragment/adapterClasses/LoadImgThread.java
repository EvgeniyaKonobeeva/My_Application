package com.example.appwithfragment.RecyclerViewFragment.adapterClasses;

import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.appwithfragment.MyImageLoader.*;
import com.example.appwithfragment.RecyclerViewFragment.adapterClasses.RecyclerViewAdapter.*;


/**
 * Created by e.konobeeva on 08.08.2016.
 */
public class LoadImgThread implements Runnable {
    private static final String errorTag = "ERROR LoadImgThread";
    private MyHandler handler;
    String url;

    public LoadImgThread(MyHandler handler, String url){
        this.handler = handler;
        this.url = url;
    }

    @Override
    public void run() {
        if(!Thread.currentThread().isInterrupted()) {
            try {
                InputStream is = (InputStream) new URL(url.replace("_m", "")).getContent();
                Drawable drawable = Drawable.createFromStream(is, "" + url.hashCode() + ".jpeg");
                is.close();
                if (!Thread.currentThread().isInterrupted())
                    handler.sendMessage(handler.obtainMessage(1, drawable));
            } catch (MalformedURLException me) {
                Log.d(errorTag, me.getMessage());
            } catch (IOException ioe) {
                Log.d(errorTag, ioe.getMessage());
            }/*catch (InterruptedException ie){
            Log.d(errorTag, "interrupted");
        }*/
        }
    }

}
