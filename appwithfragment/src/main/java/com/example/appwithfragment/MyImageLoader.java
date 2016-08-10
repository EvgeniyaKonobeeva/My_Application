package com.example.appwithfragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.example.appwithfragment.RecyclerViewFragment.adapterClasses.LoadImgThread;

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

    private static int countThreads = 0;

    //private boolean result = false;




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
        executorSPool= Executors.newFixedThreadPool(threadPoolSize);
    }

    public MyImageLoader load(Context ctx, String url, ImageView iv){
        return new MyImageLoader(ctx).setResourceUrl(url).setImgInto(iv);
    }

    public static MyImageLoader setContext(Context ctx){

        return new MyImageLoader(ctx);
    }

    public MyImageLoader setResourceUrl(String url){
        this.resUrl = url;
        return this;
    }

    public MyImageLoader setImgInto(ImageView iv) {

        Drawable drawable;
        iv.setTag(resUrl);
        if (!oc.getImageTo(resUrl.hashCode(), iv)) {
            if ((drawable = dc.getImg(resUrl.hashCode())) != null) {
                iv.setImageDrawable(drawable);
                //oc.putImage(resUrl.hashCode(), drawable);
            } else if (!mapLoadingImg.containsKey(resUrl.hashCode())) {
                mapLoadingImg.put(resUrl.hashCode(), resUrl);
                LoadImgThread loadImgThread = new LoadImgThread(handler, resUrl,iv);
                executorSPool.submit(loadImgThread);
                mapTask.put(resUrl.hashCode(), executorSPool.submit(loadImgThread));
            }
        }
        return this;
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Drawable drawable = (Drawable) msg.obj;
            oc.putImage(msg.what, drawable);
            //Bitmap bitmap = Bitmap.createBitmap(drawable.getMinimumWidth(), drawable.getMinimumHeight(), Bitmap.Config.ARGB_8888);
            //dc.saveOnDisk(msg.what, bitmap);
            //bitmap.recycle();
        }
    }



}
