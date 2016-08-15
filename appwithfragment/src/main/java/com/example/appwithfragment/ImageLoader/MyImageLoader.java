package com.example.appwithfragment.ImageLoader;

import android.content.Context;

import android.graphics.Bitmap;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    private BlockingQueue<Runnable> queue;
    private ExecutorService executorService;


    public MyImageLoader(Context ctx){
        if(oc == null){
            oc = new OMCash(50);
        }
        dc = DiskCashing.getInstance(ctx);
        handler = new MyHandler();
        mapLoadingImg = new HashMap<>();
        queue = new LIFOQueue(10);
        executorService = new ThreadPoolExecutor(threadPoolSize, threadPoolSize, 50L, TimeUnit.SECONDS, queue);
    }

    public MyImageLoader setResourceUrl(String url){
        this.resUrl = url;
        return this;
    }

    public MyImageLoader setImgInto(final ImageView iv) {
        Log.d("HERE", "call MyImageLoader");
        iv.setTag(resUrl);
        if (!oc.getImageTo(resUrl.hashCode(), iv)) {
           if (!mapLoadingImg.containsKey(resUrl.hashCode())) {
               Log.d("HERE", "running thread");
                mapLoadingImg.put(resUrl.hashCode(), resUrl);
                LoadImgRunnable loadImgRunnable = new LoadImgRunnable(handler, resUrl, iv, dc);
                executorService.submit(loadImgRunnable);
           }
        }

        return this;
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(final Message msg) {
            Log.d("HERE","terminating thread");
                if(msg.arg1 == 2){
                    oc.putImage(msg.what, (Bitmap) msg.obj);
                    mapLoadingImg.remove(msg.what);
                }else{
                    mapLoadingImg.remove(msg.what);
                }


        }

    }


    public void terminateAllProcess(){
        Log.d("MyImageLoader", "terminateAllProcess");
        queue.removeAll(queue);
        executorService.shutdownNow();
    }


    public DiskCashing getDiskCashing(){
        return dc;
    }


}
