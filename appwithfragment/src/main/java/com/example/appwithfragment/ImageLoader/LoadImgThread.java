package com.example.appwithfragment.ImageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
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
    private DiskCashing dc;

    public LoadImgThread(MyHandler handler, String url, ImageView iv, DiskCashing dc){
        this.handler = handler;
        this.url = url;
        this.iv = iv;
        this.dc = dc;

    }

    @Override
    public void run() {
        if(!Thread.currentThread().isInterrupted()) {
            Bitmap bitmap;
            if ((bitmap = dc.getImg(url.hashCode())) != null) {
                //if (iv.getTag().equals(url))
                final Bitmap bm = bitmap;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iv.setImageBitmap(bm);
                    }
                });
            } else {
                try {
                    InputStream is = (InputStream) new URL(url).getContent();
                    final Bitmap bitmap1 = BitmapFactory.decodeStream(is);
                    is.close();
                    dc.saveOnDisk(url.hashCode(), bitmap1);
                    if (!Thread.currentThread().isInterrupted()) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                handler.sendMessage(handler.obtainMessage(url.hashCode(), bitmap1));
                                if (iv.getTag().equals(url)) {
                                    Log.d("THREAD OPERATION", "operation done " + (Looper.myLooper()));
                                    iv.setImageBitmap(bitmap1);
                                }
                            }
                        });
                    }
                } catch (MalformedURLException me) {
                    Log.d(errorTag, me.getMessage());
                } catch (InterruptedIOException ioe) {

                    Log.d(errorTag, ioe.getMessage());
                    Thread.currentThread().interrupt();
                } catch (IOException ioe2) {

                    Log.d(errorTag, ioe2.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        }

    }

}
