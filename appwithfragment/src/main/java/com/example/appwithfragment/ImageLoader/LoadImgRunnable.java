package com.example.appwithfragment.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.appwithfragment.BroadcastReciever.InternetStateReceiver;
import com.example.appwithfragment.ImageLoader.MyImageLoader.*;
import com.example.appwithfragment.MVPPattern.IFragment;
import com.example.appwithfragment.MyActivity;


/**
 * Created by e.konobeeva on 08.08.2016.
 */
public class LoadImgRunnable implements Runnable {
    private static final String errorTag = "ERROR LoadImgRunnable";
    //private static final String KEYDrawable = "Drawable";
    //private static final String KEYUrl = "URL";
    private MyHandler handler;
    private String url;
    private ImageView iv;
    private DiskCashing dc;
    private OMCash oc;



    public LoadImgRunnable(MyHandler handler, String url, ImageView iv, OMCash oc){
        this.handler = handler;
        this.url = url;
        this.iv = iv;
        this.dc = DiskCashing.getInstance(MyActivity.context);
        this.oc = oc;

    }

    @Override
    public void run() {
        Thread.currentThread().setPriority(1);
        if(!Thread.currentThread().isInterrupted()) {
            if(!oc.containsBitmap(url.hashCode())) {
                final Bitmap bitmap = dc.getImg(url.hashCode());
                if (bitmap != null) {
                    handler.sendMessage(handler.obtainMessage(url.hashCode(), 1, 0, bitmap));
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (iv.getTag().equals(url))
                                iv.setImageBitmap(bitmap);
                        }
                    });
                } else {/*internet connection here req*/
                    try {
                        InputStream is = (InputStream) new URL(url).getContent();
                        final Bitmap bitmap1 = BitmapFactory.decodeStream(is);
                        is.close();
                        dc.saveOnDisk(url.hashCode(), bitmap1);
                        oc.putImage(url.hashCode(), bitmap1);
                        if (!Thread.currentThread().isInterrupted()) {
                            handler.sendMessage(handler.obtainMessage(url.hashCode(), 2, 0, bitmap1));
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (iv.getTag().equals(url)) {
                                        //Log.d("THREAD OPERATION", iv.getTag().toString());
                                        iv.setImageBitmap(bitmap1);

                                    }
                                }
                            });
                        } else {
                            handler.sendMessage(handler.obtainMessage(url.hashCode(), -1));
                        }


                    } catch (MalformedURLException me) {
                        Log.d(errorTag, "MalformedURLException " + me.getMessage());
                        handler.sendMessage(handler.obtainMessage(url.hashCode(), -1));
                    } catch (InterruptedIOException ioe) {
                        handler.sendMessage(handler.obtainMessage(url.hashCode(), -1));
                        Log.d(errorTag, "InterruptedIOException " + ioe.getMessage());
                        handler.sendMessage(handler.obtainMessage(url.hashCode(), -1));
                        Thread.currentThread().interrupt();
                    } catch (IOException ioe2) {
                        Log.d(errorTag, "IOException " + ioe2.getMessage());
                        handler.sendMessage(handler.obtainMessage(url.hashCode(), -1));
                        Thread.currentThread().interrupt();
                    }
                }
            }else {
                handler.sendMessage(handler.obtainMessage(url.hashCode(), -1));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        oc.setImageTo(url.hashCode(), iv);
                    }
                });
            }
        }else{
            handler.sendMessage(handler.obtainMessage(-1));
        }

    }

}
