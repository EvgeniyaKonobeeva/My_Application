package com.example.appwithfragment.recyclerViewFragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appwithfragment.supportLib.ItemClickSupport;
import com.example.appwithfragment.ListContent;
import com.example.appwithfragment.R;
import com.example.appwithfragment.recyclerViewFragment.adapterClasses.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by e.konobeeva on 02.08.2016.
 */
public class RecyclerViewFragment extends Fragment implements GettingResults {

    private int listSize = 5;
    private static final int maxPages = 10;
    private static final int maxPerPage = 50;

    private View view;
    private OnRecyclerViewClickListener listener;
    private Activity activity;
    private RecyclerView recyclerView;
    private List<ListContent> list;
    private LoadFromFlickrTask task;


    public interface OnRecyclerViewClickListener {
        void doAction(ListContent object);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        listener = (OnRecyclerViewClickListener) this.getActivity();
        activity = this.getActivity();
        view = inflater.inflate(R.layout.fragment, null);

        ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            Log.d("PROCESS", "connection established");
            task = new LoadFromFlickrTask(this, maxPerPage, 1);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } else {
            Log.d("ERROR 0", "connection error");
        }

        String[] text = new String[listSize];
        for(int i = 0; i< listSize; i++){
            text[i] = "photo " + (i+1);
        }
        if(list == null) {
            list = new ArrayList();
            ListContent lc;
            for (int i = 0; i < listSize; i++) {
                lc = new ListContent(null, "photo" + i);
                list.add(lc);
            }
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.rl);

        final GridLayoutManager recyclerGridLayout = new GridLayoutManager(view.getContext(), 2);

        recyclerView.setLayoutManager(recyclerGridLayout);
        recyclerView.setAdapter(new RecyclerViewAdapter(list, recyclerGridLayout, activity));

        final GettingResults fragment = this;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastPositionMarker = 45;

            int page = 2;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

               if(dy > 0 && recyclerGridLayout.findLastVisibleItemPosition() >= lastPositionMarker && page <= maxPages){
                    Log.d("POSITION", Integer.toString(recyclerGridLayout.findLastVisibleItemPosition()));
                    task = new LoadFromFlickrTask(fragment, maxPerPage, page++);
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    lastPositionMarker+=maxPerPage;
               }else if(page > maxPages && recyclerGridLayout.findLastCompletelyVisibleItemPosition() == 499){
                   final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
                   dialogBuilder.setMessage("There is no photo anymore");
                   dialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                       }
                   });
                   AlertDialog alertDialog = dialogBuilder.create();
                   alertDialog.show();
               }
            }
        });

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                listener.doAction(list.get(position));
            }
        });

        return view;

    }


    private static int countLoaders = 0;

    @Override
    public void onGettingResult(ArrayList<String> photoUrls, ArrayList<String> photosInfo) {
        if(countLoaders == 0) {
            countLoaders++;
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setImgUrl(photoUrls.get(i));
                list.get(i).setFullTitle(photosInfo.get(i));

            }
            if(list.size() < photoUrls.size()){
                int size = list.size();
                for(int i = size; i < photoUrls.size(); i++){
                    ListContent listContent = new ListContent(photoUrls.get(i),photosInfo.get(i));
                    list.add(i, listContent);
                }
            }
        }else {
            int size = list.size();
            for(int i = size; i < photoUrls.size(); i++){
                ListContent listContent = new ListContent(photoUrls.get(i),photosInfo.get(i));
                list.add(i, listContent);
            }
        }

        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        task.setFragment(null);
        task.cancel(false);
        recyclerView.addOnScrollListener(null);

    }


}
