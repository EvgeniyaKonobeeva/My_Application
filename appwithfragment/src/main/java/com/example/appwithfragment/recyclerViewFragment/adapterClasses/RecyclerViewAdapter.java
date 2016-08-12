package com.example.appwithfragment.RecyclerViewFragment.adapterClasses;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appwithfragment.ImageLoader.DiskCashing;
import com.example.appwithfragment.ListContent;
import com.example.appwithfragment.ImageLoader.MyImageLoader;
import com.example.appwithfragment.R;
import com.example.appwithfragment.ImageLoader.OMCash;

import java.util.List;


/**
 * Created by e.konobeeva on 29.07.2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<ListContent> list;

    private GridLayoutManager gridLayoutManager;
    private MyImageLoader iml;
    Context ctx;


    public RecyclerViewAdapter(List<ListContent> list, GridLayoutManager layoutManager, Context ctx){
        this.list = list;
        iml = new MyImageLoader(ctx);
        this.gridLayoutManager = layoutManager;

        this.ctx = ctx;
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
        if(listContent.getImgUrl() != null ) {
            String keyUrl = listContent.getImgUrl();
            iml.setResourceUrl(keyUrl).setImgInto(holder.imageView);
            listContent.setImg(holder.imageView.getDrawable());
        }

        holder.textView.setText(listContent.getShortTitle());

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

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        Log.d("RecyclerViewAdapter", "onDetachedFromRecyclerView");
        //iml.terminateAllProcess();
    }

    public MyImageLoader getMyImageLoader(){
        return iml;
    }
}
