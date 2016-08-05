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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by e.konobeeva on 02.08.2016.
 */
public class MyFragment extends Fragment implements GettingResults{
    private View view;
    private int n = 5;
    private Button conBut;
    private myOnClickListener listener;
    private Activity activity;
    private RecyclerView recyclerView;
    private List<ListContent> list;
    LoadFromFlickrTask task1;
    LoadFromFlickrTask task;


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
            task = new LoadFromFlickrTask(this, 10, 1, 5);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } else {
            Log.d("ERROR 0", "connection error");
        }

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

        final GridLayoutManager recyclerGridLayout = new GridLayoutManager(view.getContext(), 2);

        recyclerView.setLayoutManager(recyclerGridLayout);
        recyclerView.setAdapter(new RLAdapter(list));

        final GettingResults fragment = this;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastPositionMarker = 45;

            int pageE = 5;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

               if(dy > 0 && recyclerGridLayout.findLastVisibleItemPosition() == lastPositionMarker){
                    Log.d("POSITION", Integer.toString(recyclerGridLayout.findLastVisibleItemPosition()));
                    int pageB = pageE+1;
                    pageE = pageB+4;
                    task = new LoadFromFlickrTask(fragment, 10, pageB, pageE);
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    lastPositionMarker+=50;
                }
            }
        });

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                listener.doAction("element " + (position+1));
            }
        });

        return view;

    }


    private static int countLoaders = 0;

    @Override
    public void onGettingResult(ArrayList<String> photoUrls) {
        if(countLoaders == 0) {
            countLoaders++;
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setImRes(photoUrls.get(i));
            }
            if(list.size() < photoUrls.size()){
                int size = list.size();
                for(int i = size; i < photoUrls.size(); i++){
                    ListContent listContent = new ListContent("text" + (i+1));
                    listContent.setImRes(photoUrls.get(i));
                    list.add(i, listContent);
                }
            }
        }else {
            int size = list.size();
            for(int i = size; i < photoUrls.size(); i++){
                ListContent listContent = new ListContent("text" + (i+1));
                listContent.setImRes(photoUrls.get(i));
                list.add(i, listContent);
            }
        }

        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        task1.setFragment(null);
        task.setFragment(null);
        recyclerView.addOnScrollListener(null);

    }

}
