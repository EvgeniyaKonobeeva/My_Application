package com.example.appwithfragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by e.konobeeva on 02.08.2016.
 */
public class MyFragment extends Fragment implements GettingResults{
    private View view;
    private int n = 10;
    private Button conBut;
    private myOnClickListener listener;
    private Activity activity;
    private RecyclerView recyclerView;
    private List<ListContent> list;
    private LoadFromFlickrTask task;


    interface myOnClickListener{
        void doAction(String object);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        listener = (myOnClickListener) this.getActivity();
        activity = this.getActivity();
        view = inflater.inflate(R.layout.fragment, null);




        ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("PROCESS", "connection established");


            task = new LoadFromFlickrTask();
            task.fragment = this;
            task.setParams(10);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            Log.d("ERROR 0", "connection error");
        }

        //conBut = (Button)view.findViewById(R.id.getConnectionBut);
        //conBut.setOnClickListener(new MyListener());

        String[] text = new String[n];
        for(int i = 0; i< n; i++){
            text[i] = "text " + (i+1);
        }
        list = new ArrayList();
        ListContent lc;
        for(int i = 0; i < n; i++){
            lc = new ListContent(text[i]);
            list.add(lc);
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.rl);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        recyclerView.setAdapter(new RLAdapter(list));
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                listener.doAction("element " + (position+1));
            }
        });

        return view;

    }


    @Override
    public void onGettingResult(ArrayList<String> photoUrls) {
        int count = 0;
        ListContent listContent;
        for(ListContent lc : list){
            lc.setImRes(photoUrls.get(count++));
        }
        if(list.size() < photoUrls.size()){
            int size = list.size();
            for(int i = size; i < photoUrls.size(); i++){
                listContent = new ListContent("text" + i);
                listContent.setImRes(photoUrls.get(i));
                list.add(i, listContent);
            }
        }
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        task.fragment = null;

    }

}
