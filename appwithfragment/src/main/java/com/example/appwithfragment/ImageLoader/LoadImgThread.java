package com.example.appwithfragment.ImageLoader;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.appwithfragment.ImageLoader.MyImageLoader.*;


/**
 * Created by e.konobeeva on 08.08.2016.
 */
public class LoadImgThread implements Runnable {
    private static final String errorTag = "ERROR LoadImgThread";
    private static final String KEYDrawable = "Drawable";
    private static final String KEYUrl = "URL";
    private MyHandler handler;
    private String url;
    private ImageView iv;

    public LoadImgThread(MyHandler handler, String url, ImageView iv){
        this.handler = handler;
        this.url = url;
        this.iv = iv;

    }

    @Override
    public void run() {
        if(!Thread.currentThread().isInterrupted()) {
            Log.d("IN THE THREAD", "LoadImgThread here 1");
            try {
                InputStream is = (InputStream) new URL(url).getContent();
                final Drawable drawable = Drawable.createFromStream(is, "" + url.hashCode() + ".png");
                is.close();
                if (!Thread.currentThread().isInterrupted())
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            handler.sendMessage(handler.obtainMessage(url.hashCode(), drawable));
                            if(iv.getTag().equals(url))
                                iv.setImageDrawable(drawable);
                        }
                    });

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
