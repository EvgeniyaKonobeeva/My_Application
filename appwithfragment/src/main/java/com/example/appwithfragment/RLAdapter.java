package com.example.appwithfragment;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.graphics.drawable.DrawableWrapper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



/**
 * Created by e.konobeeva on 29.07.2016.
 */
public class RLAdapter extends RecyclerView.Adapter<RLAdapter.ViewHolder> {

    private List<ListContent> list;
    private Map<Integer, Object> mapLoadingImg = new HashMap<>();
    private Map<Integer, AsyncTask> mapTask = new HashMap<>();
    private GridLayoutManager gridLayoutManager;

    public RLAdapter(List<ListContent> list, GridLayoutManager layoutManager){
        this.list = list;
        this.gridLayoutManager = layoutManager;
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
        LoaDImageFromUrlTask task = new LoaDImageFromUrlTask();

        holder.imageView.setImageDrawable(null);

        if(listContent.getImg()!= null)
            holder.imageView.setImageDrawable(listContent.getImg());
        else if(listContent.getImRes() != null && !mapLoadingImg.containsKey(position))
        {
            mapLoadingImg.put(position, listContent);
            mapTask.put(position, task);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, listContent);

        }


        holder.textView.setText(listContent.getString());

        if(position >= 11) {
            for (int pos : mapTask.keySet()) {

                int firstVisibleItemPos = gridLayoutManager.findFirstVisibleItemPosition();
                if (pos < firstVisibleItemPos || pos > firstVisibleItemPos+gridLayoutManager.getItemCount()) {
                    //Log.d("POSITION PP", Integer.toString(pos) + " " + (gridLayoutManager.findLastVisibleItemPosition()));
                    mapTask.get(pos).cancel(false);
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

    class LoaDImageFromUrlTask extends AsyncTask<ListContent, Integer, Void>{
        private Drawable drawable;
        private ListContent listContent;

        @Override
        protected Void doInBackground(ListContent... listContents) {
            listContent = listContents[0];
            if(!isCancelled()) {
                try {
                    //Log.d("HERE ", "here 0000");
                    InputStream is = (InputStream) new URL(listContent.getImRes()).getContent();
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
            listContent.setImg(drawable);
            notifyDataSetChanged();
        }

        @Override
        protected void onCancelled() {
            Log.d("FFF", "fffff ");
        }
    }


}
