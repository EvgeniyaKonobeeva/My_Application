package com.example.appwithfragment;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.example.appwithfragment.RecyclerViewFragment.adapterClasses.LoadImgThread;
import com.example.appwithfragment.RecyclerViewFragment.adapterClasses.RecyclerViewAdapter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Евгения on 10.08.2016.
 */
public class MyImageLoader {
    private static final String errorTag = "ERROR MyImageLoader";
    private static final int threadPoolSize = 500;

    private String resUrl;

    private static OMCash oc;
    private static DiskCashing dc;

    private MyHandler handler;

    private Map<Integer, Object> mapLoadingImg;
    private Map<Integer, Future> mapTask;
    private ExecutorService executorSPool;

    private ImageView iv;
    private boolean result = false;




    public MyImageLoader(Context ctx){
        if(oc == null){
            oc = new OMCash(50);
        }
        if(dc == null){
            dc = new DiskCashing(ctx);
        }
        handler = new MyHandler();
        mapLoadingImg = new HashMap<>();
        mapTask = new HashMap<>();
        //executorSPool= Executors.newFixedThreadPool(threadPoolSize);
    }


    public static MyImageLoader setContext(Context ctx){

        return new MyImageLoader(ctx);
    }

    public MyImageLoader setResourceUrl(String url){
        this.resUrl = url;
        return this;
    }

    public void setImgInto(ImageView iv) {

        this.iv = iv;
        Drawable drawable;
        if (!oc.getImageTo(resUrl, iv)) {
            if ((drawable = dc.getImg(resUrl)) != null) {
                iv.setImageDrawable(drawable);
                oc.putImage(resUrl, drawable);
                result = true;
            } else if (!mapLoadingImg.containsKey(resUrl.hashCode())) {
                mapLoadingImg.put(resUrl.hashCode(), resUrl);
                LoadImgThread loadImgThread = new LoadImgThread(handler, resUrl);
                //mapTask.put(resUrl.hashCode(), executorSPool.submit(loadImgThread));
            }
        }else result = true;
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            iv.setImageDrawable((Drawable) msg.obj);
            //dc.saveOnDisk("",(BitmapDrawable) msg.obj);
            //oc.putImage("",(Drawable) msg.obj);
            //put in cash
            //TODO put all received data to cash, set the url-property to listContent obj
            result = true;
        }
    }
    public boolean getResult(){
        return result;
    }



}
