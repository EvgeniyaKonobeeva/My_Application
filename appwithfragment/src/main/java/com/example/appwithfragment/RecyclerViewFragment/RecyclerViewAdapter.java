package com.example.appwithfragment.RecyclerViewFragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.appwithfragment.ListContent;
import com.example.appwithfragment.ImageLoader.MyImageLoader;
import com.example.appwithfragment.R;

import java.util.List;


/**
 * Created by e.konobeeva on 29.07.2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ListContent> list;
    private MyImageLoader iml;
    private Context ctx;
    private static int PROGRESS_TYPE = 1;
    private static int IMAGE_TYPE = 0;


    public RecyclerViewAdapter(List<ListContent> list, Context ctx){
        this.list = list;
        iml = new MyImageLoader(ctx);
        this.ctx = ctx;
    }

    @Override
    public int getItemCount() {
        if(list.size() == 0){
            return -1;
        }
        return list.size()+1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == IMAGE_TYPE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
            return new ImageViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
            //int height  = parent.getMinimumHeight()/6;
            //view.setMinimumHeight(height);
            return new ProgressViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImageViewHolder) {
            ImageViewHolder ivh = ((ImageViewHolder) holder);
            ivh.imageView.setImageDrawable(null);
            String keyUrl = list.get(position).getImgUrl();
            iml.setResourceUrl(keyUrl).setImgInto(ivh.imageView);
            ivh.textView.setText(list.get(position).getShortTitle());
        } else if (holder instanceof ProgressViewHolder) {
            ProgressViewHolder pvh = (ProgressViewHolder) holder;
            pvh.progressBar.setIndeterminate(true);
        }

    }



    class ImageViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;

        public ImageViewHolder(View view){
            super(view);
            textView = (TextView) view.findViewById(R.id.IDTextView);
            imageView = (ImageView) view.findViewById(R.id.IDImageView);
        }


    }
    class ProgressViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public ProgressViewHolder(View view){
            super(view);
             progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        Log.d("RecyclerViewAdapter", "onDetachedFromRecyclerView");
    }

    public MyImageLoader getMyImageLoader(){
        return iml;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == getItemCount()-1){
            return PROGRESS_TYPE;
        }else return IMAGE_TYPE;
    }
}
