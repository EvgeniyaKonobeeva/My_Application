package com.example.ekonobeeva.myapplication_1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by e.konobeeva on 28.07.2016.
 */
public class MyAdapter extends ArrayAdapter<Object>{

    private List<Object> list;
    private int resource;
    private Context ctx;
    LayoutInflater lInflater;
    private static int count = 0;

    public MyAdapter(Context ctx, int resource, List<Object> list){
        super(ctx, resource);
        this.ctx = ctx;
        this.resource = resource;
        this.list = list;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
   public MyAdapter(Context context, int resource){
       super(context, resource);
       ctx = context;
       this.resource = resource;
       lInflater = (LayoutInflater) ctx
               .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

   }

    @Override
    public int getCount() {
        if(list.size()%2 == 0){
            return list.size()/2;
        }else{
            return list.size()/2+1;
        }

    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position%2;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    static class ViewHolder{
        TextView textViewFirst;
        ImageView imageViewFirst;
        TextView textViewSecond;
        ImageView imageViewSecond;


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Log.d("MY_INFO", "count = " + (count));
        count++;
        final ViewHolder viewHolder;


        /*if(position%2==0){
            resource = R.layout.right_layout;

        }else{
            resource = R.layout.image_view_layout;
        }*/




        if (convertView == null){
            //Log.d("MY_INFO", "convertView == null");

             convertView = lInflater.inflate(resource, parent, false);
            viewHolder = new ViewHolder();
            /*viewHolder.textViewFirst = (TextView)convertView.findViewById(R.id.IDTextViewFirst);
            viewHolder.imageViewFirst = (ImageView)convertView.findViewById(R.id.IDImageViewFirst);
            viewHolder.textViewSecond = (TextView)convertView.findViewById(R.id.IDTextViewSecond);
            viewHolder.imageViewSecond = (ImageView)convertView.findViewById(R.id.IDImageViewSecond);
            convertView.setTag(viewHolder);
            */


        }else{
            //Log.d("MY_INFO", "convertView != null");
            viewHolder = (ViewHolder) convertView.getTag();
        }







        int n = position*2;
        ListContent listContent1 = (ListContent) getItem(n);
        viewHolder.textViewFirst.setText(listContent1.getString());
        viewHolder.textViewFirst.setTag("tvf" + (n+1));
        viewHolder.imageViewFirst.setImageResource(listContent1.getImRes());
        viewHolder.imageViewFirst.setTag("ivf" + (n+1));



       if(n+1 <list.size()){
           ListContent listContent2 = (ListContent) getItem(n + 1);
           viewHolder.textViewSecond.setText(listContent2.getString());
           viewHolder.textViewSecond.setTag("ivs" + (n+2));
           viewHolder.imageViewSecond.setImageResource(listContent2.getImRes());
           viewHolder.imageViewSecond.setTag("ivf" + (n+2));
        }else{
           viewHolder.textViewSecond.setVisibility(View.INVISIBLE);
           viewHolder.imageViewSecond.setVisibility(View.INVISIBLE);
       }


        viewHolder.imageViewFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ctx, viewHolder.imageViewFirst.getTag().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.imageViewSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ctx, viewHolder.imageViewSecond.getTag().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.textViewFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ctx, viewHolder.textViewFirst.getTag().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.textViewSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ctx, viewHolder.textViewSecond.getTag().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(ctx, view.getTag().toString(), Toast.LENGTH_SHORT).show();
        }
    };

}
