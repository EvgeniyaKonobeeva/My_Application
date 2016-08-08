package com.example.appwithfragment.recyclerViewFragment.adapterClasses;

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

import com.example.appwithfragment.ListContent;
import com.example.appwithfragment.R;

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

    private ExecutorService executorSPool;
    private LoadImgThread loadImgThread;
    private MyHandler handler;

    public RecyclerViewAdapter(List<ListContent> list, GridLayoutManager layoutManager){
        executorSPool= Executors.newFixedThreadPool(threadPoolSize);
        this.list = list;
        this.gridLayoutManager = layoutManager;
        this.handler = new MyHandler();
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

        holder.imageView.setImageDrawable(null);

        if(listContent.getImgSmall()!= null)
            holder.imageView.setImageDrawable(listContent.getImgSmall());
        else if(listContent.getImgUrl() != null && !mapLoadingImg.containsKey(position))
        {
            mapLoadingImg.put(position, listContent);
            loadImgThread = new LoadImgThread(handler,listContent);
            mapTask.put(position, executorSPool.submit(loadImgThread) );


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
            if(msg.what == 1){
                notifyDataSetChanged();
            }
        }
    }


}
