package com.example.ekonobeeva.myapplication_1;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.*;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by e.konobeeva on 29.07.2016.
 */
public class RLAdapter extends RecyclerView.Adapter<RLAdapter.ViewHolder> {

    private List<ListContent> list;
    public RLAdapter(List<ListContent> list){
        this.list = list;
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
        ListContent listContent = list.get(position);
        holder.imageView.setImageResource(listContent.getImRes());
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


}
