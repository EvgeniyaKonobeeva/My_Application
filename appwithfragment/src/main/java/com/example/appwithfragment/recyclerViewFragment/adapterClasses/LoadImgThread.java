package com.example.appwithfragment.recyclerViewFragment.adapterClasses;

import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.appwithfragment.ListContent;
import com.example.appwithfragment.recyclerViewFragment.adapterClasses.RecyclerViewAdapter.*;


/**
 * Created by e.konobeeva on 08.08.2016.
 */
public class LoadImgThread implements Runnable {
    private static final String errorTag = "ERROR LoadImgThread";
    private MyHandler handler;
    private ListContent data;
    public LoadImgThread(MyHandler handler, ListContent data){

        this.handler = handler;
        this.data = data;
    }

    @Override
    public void run() {
        if(!Thread.currentThread().isInterrupted()) {
            try {
                InputStream is = (InputStream) new URL(data.getImgUrl()).getContent();
                Drawable drawable = Drawable.createFromStream(is, "img" + data.hashCode() + ".png");

                is = (InputStream) new URL(data.getImgUrl().replace("_m", "")).getContent();
                Drawable drawable1 = Drawable.createFromStream(is, "imgH" + data.hashCode() + ".png");
                is.close();


                data.setImgSmall(drawable);
                data.setImgBigSize(drawable1);

                if (!Thread.currentThread().isInterrupted())
                    handler.sendEmptyMessage(1);

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
