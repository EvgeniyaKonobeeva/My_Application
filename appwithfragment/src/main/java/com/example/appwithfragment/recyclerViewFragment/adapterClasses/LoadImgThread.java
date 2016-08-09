package com.example.appwithfragment.recyclerViewFragment.adapterClasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.appwithfragment.DiskCashing;
import com.example.appwithfragment.ListContent;
import com.example.appwithfragment.recyclerViewFragment.GettingResults;
import com.example.appwithfragment.recyclerViewFragment.adapterClasses.RecyclerViewAdapter.*;
import com.example.appwithfragment.supportLib.OMCash;


/**
 * Created by e.konobeeva on 08.08.2016.
 */
public class LoadImgThread implements Runnable {
    private static final String errorTag = "ERROR LoadImgThread";
    private MyHandler handler;
    private ListContent data;
    private DiskCashing dc;
    private OMCash omCash;
    private int position;

    public LoadImgThread(MyHandler handler, ListContent data,DiskCashing dc, OMCash omCash, int position){
        this.omCash = omCash;
        this.position = position;
        this.dc = dc;
        this.handler = handler;
        this.data = data;
    }

    @Override
    public void run() {
        if(!Thread.currentThread().isInterrupted()) {
            try {
                InputStream is = (InputStream) new URL(data.getImgUrl().replace("_m", "")).getContent();
                Drawable drawable = Drawable.createFromStream(is, "" + data.hashCode());

                omCash.putImage(position, drawable);
                dc.cashingImg(position, BitmapFactory.decodeStream(is));
                is.close();
                if (!Thread.currentThread().isInterrupted())
                    handler.sendEmptyMessage(position);

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
