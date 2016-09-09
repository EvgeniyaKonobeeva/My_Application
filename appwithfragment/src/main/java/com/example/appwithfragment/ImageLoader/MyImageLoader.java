package com.example.appwithfragment.ImageLoader;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
    private static final int threadPoolSize = 50;

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
        queue = new LIFOQueue(500);
        executorService = new ThreadPoolExecutor(threadPoolSize, threadPoolSize, 500L, TimeUnit.SECONDS, queue);
    }

    public MyImageLoader setResourceUrl(String url){
        this.resUrl = url;
        return this;
    }

    public MyImageLoader setImgInto(final ImageView iv) {
        //Log.d("MyImageLoader", "call MyImageLoader");
        iv.setTag(resUrl);

        //if (!oc.setImageTo(resUrl.hashCode(), iv)) {
           if (!mapLoadingImg.containsKey(resUrl.hashCode())) {
               //Log.d("HERE", "running thread");
                mapLoadingImg.put(resUrl.hashCode(), resUrl);
                LoadImgRunnable loadImgRunnable = new LoadImgRunnable(handler, resUrl, iv, oc, dc);
                executorService.execute(loadImgRunnable);
           }
        //}

        return this;
    }


/*msg.arg1 == 1 фото есть в дисковом кэше и мы удаляем из карты загрузок
* msg.arg1 == 2 фото загрузилось из интернета и мы удаляем из карты загрузок
* msg.arg1 == -1 фото не загрузилось и мы удаляем его из карты загрузок
* пока в любом случае получения сообщения удаляем из карты загрузок*/
    public class MyHandler extends Handler {
        @Override
        public void handleMessage(final Message msg) {
            mapLoadingImg.remove(msg.what);
        }

    }


    public void terminateAllProcess(){
        //Log.d("MyImageLoader", "terminateAllProcess");
        queue.removeAll(queue);
        executorService.shutdownNow();
    }


    public DiskCashing getDiskCashing(){
        return dc;
    }


}
