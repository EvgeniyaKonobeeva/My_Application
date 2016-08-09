package com.example.appwithfragment.recyclerViewFragment.adapterClasses;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appwithfragment.DiskCashing;
import com.example.appwithfragment.ListContent;
import com.example.appwithfragment.R;
import com.example.appwithfragment.supportLib.OMCash;

import java.io.IOException;
import java.io.InputStream;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Created by e.konobeeva on 29.07.2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final int threadPoolSize = 500;
    private List<ListContent> list;
    private Map<Integer, Object> mapLoadingImg = new HashMap<>();
    private Map<Integer, Future> mapTask = new HashMap<>();
    private GridLayoutManager gridLayoutManager;
    private DiskCashing dc;
    private OMCash omCash;

    private ExecutorService executorSPool;
    private LoadImgThread loadImgThread;
    private MyHandler handler;


    public RecyclerViewAdapter(List<ListContent> list, GridLayoutManager layoutManager, Context ctx){
        executorSPool= Executors.newFixedThreadPool(threadPoolSize);
        this.list = list;
        this.gridLayoutManager = layoutManager;
        this.handler = new MyHandler();

        if(dc == null){
            dc = new DiskCashing(ctx);
        }

        if (omCash == null){
            omCash = new OMCash(50);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_view_layout, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ListContent listContent = list.get(position);
        Drawable drawable;

        holder.imageView.setImageDrawable(null);

            if (!omCash.getImageTo(position, holder.imageView)) {
                if ((drawable = dc.getImg(position)) != null) {
                    holder.imageView.setImageDrawable(drawable);
                    listContent.setImg(drawable);
                    omCash.putImage(position, drawable);
                }else if (listContent.getImgUrl() != null && !mapLoadingImg.containsKey(position)) {
                    mapLoadingImg.put(position, listContent);
                    loadImgThread = new LoadImgThread(handler, listContent, dc, omCash, position);
                    mapTask.put(position, executorSPool.submit(loadImgThread));
                }

                //MyLib.loadImagetTo(imageView, imgUrl);
                /*if (listContent.getImg() != null)
                    holder.imageView.setImageDrawable(listContent.getImg());
                else if (listContent.getImgUrl() != null && !mapLoadingImg.containsKey(position)) {
                    mapLoadingImg.put(position, listContent);
                    loadImgThread = new LoadImgThread(handler, listContent, dc, position);
                    mapTask.put(position, executorSPool.submit(loadImgThread));
                }*/

            }else {
                listContent.setImg(holder.imageView.getDrawable());
            }

        holder.textView.setText(listContent.getShortTitle());

        if(position >= 11) {
            for (int pos : mapTask.keySet()) {

                int firstVisibleItemPos = gridLayoutManager.findFirstVisibleItemPosition();

                if (pos < firstVisibleItemPos || pos > firstVisibleItemPos+gridLayoutManager.getItemCount()) {

                    mapTask.get(pos).cancel(true);
                    mapLoadingImg.remove(pos);
                }
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;

        public ViewHolder(View view){
            super(view);
            textView = (TextView) view.findViewById(R.id.IDTextView);
            imageView = (ImageView) view.findViewById(R.id.IDImageView);
        }


    }

    //unused class
    /**
     * class LoaDImageFromUrlTask extends AsyncTask<ListContent, Integer, Void>{
     private Drawable drawable;
     private ListContent listContent;

     @Override
     protected Void doInBackground(ListContent... listContents) {
     listContent = listContents[0];
     if(!isCancelled()) {
     try {
     //Log.d("HERE ", "here 0000");
     InputStream is = (InputStream) new URL(listContent.getImgUrl()).getContent();
     drawable = Drawable.createFromStream(is, "img" + listContent.hashCode() + ".png");
     is.close();
     } catch (MalformedURLException me) {
     Log.d("ERROR 3", me.getMessage());
     } catch (IOException ioe) {
     Log.d("ERROR 4", ioe.getMessage());
     }
     return null;
     }else {

     return null;
     }
     }

     @Override
     protected void onPostExecute(Void aVoid) {
     listContent.setImgSmall(drawable);
     notifyDataSetChanged();
     }

     @Override
     protected void onCancelled() {
     Log.d("FFF", "fffff ");
     Thread.currentThread().interrupt();
     }
     }
     */

    public class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            notifyDataSetChanged();
            mapLoadingImg.remove(msg.what);
        }
    }


}
