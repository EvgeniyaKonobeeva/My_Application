package com.example.appwithfragment.RecyclerViewFragment.adapterClasses;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appwithfragment.DiskCashing;
import com.example.appwithfragment.ListContent;
import com.example.appwithfragment.MyImageLoader;
import com.example.appwithfragment.R;
import com.example.appwithfragment.OMCash;

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
    private List<ListContent> list;

    private GridLayoutManager gridLayoutManager;

    private DiskCashing dc;
    private OMCash omCash;
    private MyImageLoader iml;


    public RecyclerViewAdapter(List<ListContent> list, GridLayoutManager layoutManager, Context ctx){
        this.list = list;
        iml = new MyImageLoader(ctx);
        this.gridLayoutManager = layoutManager;

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
        holder.imageView.setImageDrawable(null);
        if(listContent.getImgUrl() != null ) {
            String keyUrl = listContent.getImgUrl();

            iml.setResourceUrl(keyUrl).setImgInto(holder.imageView);
            if (iml.getResult()) {
                listContent.setImg(holder.imageView.getDrawable());
            }
        }

        holder.textView.setText(listContent.getShortTitle());

        /*if(position >= 11) {
            for (int pos : mapTask.keySet()) {

                int firstVisibleItemPos = gridLayoutManager.findFirstVisibleItemPosition();

                if (pos < firstVisibleItemPos || pos > firstVisibleItemPos+gridLayoutManager.getItemCount()) {

                    mapTask.get(pos).cancel(true);
                    mapLoadingImg.remove(pos);
                }
            }
        }*/
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


}
