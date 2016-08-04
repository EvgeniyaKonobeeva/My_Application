package com.example.ekonobeeva.myapplication_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    String[] str = new String[]{"one","two", "three"};
    private int n= 21;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // ListView listView = (ListView) findViewById(R.id.listView);
        /*ArrayAdapter arrAdapter = new ArrayAdapter(this, R.layout.my_list_view, str);
        listView.setAdapter(arrAdapter);*/


        String[] colNames = new String[]{"Image", "Text"};
        int img = R.mipmap.ic_launcher;
        String[] text = new String[n];
        for(int i = 0; i< n; i++){
            text[i] = "text " + (i+1);
        }

        List<ListContent> list = new ArrayList();
        ListContent lc;
        for(int i = 0; i < n; i++){
            lc = new ListContent(text[i], img);
            list.add(lc);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rl);
        recyclerView.setAdapter(new RLAdapter(list));
        //recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Toast.makeText(getApplicationContext(), "toast" + position, Toast.LENGTH_SHORT).show();
            }
        });
        //MyAdapter ad = new MyAdapter(this, R.layout.image_view_layout, list);
        //listView.setAdapter(ad);










    }
}
