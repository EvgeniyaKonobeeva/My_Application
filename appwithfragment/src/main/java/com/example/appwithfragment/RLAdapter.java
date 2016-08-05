package com.example.appwithfragment;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.graphics.drawable.DrawableWrapper;
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
    public RLAdapter(List<ListContent> list){
        this.list = list;
    }
    private Map<Integer, Object> map = new HashMap<>();
    private Map<Integer, Object> seeMap = new HashMap<>();

    private Handler handler = new Handler();


    private ExecutorService executor = Executors.newFixedThreadPool(5);

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        seeMap.put(holder.getAdapterPosition(), list.get(holder.getAdapterPosition()));
    }



    @Override
    public void onViewRecycled(ViewHolder holder) {
        if(seeMap.containsKey(holder.getAdapterPosition()))
            seeMap.remove(list.get(holder.getAdapterPosition()));

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

            if(listContent.getImg()!= null)
                holder.imageView.setImageDrawable(listContent.getImg());
            else if(listContent.getImRes() != null && !map.containsKey(position))
            {
                holder.imageView.setImageDrawable(null);
                map.put(position, listContent);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, listContent);

            }




        holder.textView.setText(listContent.getString());
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
            try {
                Log.d("HERE ", "here 0000");
                InputStream is = (InputStream) new URL(listContent.getImRes()).getContent();
                drawable = Drawable.createFromStream(is, "img" + listContent.hashCode() + ".png");
                is.close();
            }catch (MalformedURLException me){

            }catch (IOException ioe){

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            listContent.setImg(drawable);
            notifyDataSetChanged();
        }

        @Override
        protected void onCancelled() {
            Log.d("FFF", "fffff");
        }
    }


}
