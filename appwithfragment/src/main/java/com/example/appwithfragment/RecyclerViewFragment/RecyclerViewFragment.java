package com.example.appwithfragment.RecyclerViewFragment;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.appwithfragment.RecyclerViewFragment.adapterClasses.RecyclerViewAdapter;
import com.example.appwithfragment.supportLib.ItemClickSupport;
import com.example.appwithfragment.ListContent;
import com.example.appwithfragment.R;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by e.konobeeva on 02.08.2016.
 * фрагмент, содержащий RecyclerView.
 * выполняет задания по загрузке url изображений - LoadFromFlickrTask
 */
public class RecyclerViewFragment extends Fragment implements GettingResults {

    private int listSize = 1;

    private View view;

    private OnRecyclerViewClickListener listener;
    private Activity activity;
    private RecyclerView recyclerView;
    private List<ListContent> list;
    private LoadFromFlickrTask task;

    private boolean loadingFinished = false;
    private boolean lastTaskTerminated = false;

    private int progress;


    public interface OnRecyclerViewClickListener {
        void doAction(ListContent object);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        listener = (OnRecyclerViewClickListener) this.getActivity();
        activity = this.getActivity();
        view = inflater.inflate(R.layout.fragment, null);
        setHasOptionsMenu(true);
        ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            Log.d("PROCESS", "connection established");
            task = new LoadFromFlickrTask(this);
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
            //int lastPositionMarker = 0;

            //int page = 2;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

               if(dy > 0 && recyclerGridLayout.findLastVisibleItemPosition() >= progress-2  && loadingFinished){
                    loadingFinished = false;
                    //Log.d("POSITION", Integer.toString(recyclerGridLayout.findLastVisibleItemPosition()));
                    task = new LoadFromFlickrTask(fragment);
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    //lastPositionMarker+=maxPerPage;
               }else if(lastTaskTerminated && recyclerGridLayout.findLastVisibleItemPosition() >= progress-2){
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
    public void onGettingResult(ArrayList<String> photoUrls, ArrayList<String> photosInfo, boolean isTheLast) {
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

        if(isTheLast){
            this.lastTaskTerminated = isTheLast;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        task.setFragment(null);
        task.cancel(false);
        recyclerView.addOnScrollListener(null);
        recyclerView.setAdapter(null);


    }

    @Override
    public void getProgress(int loadingPhotos) {

        loadingFinished = true;
        progress += loadingPhotos;
        //Log.d("PROGRESS", Integer.toString(progress));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add("clean disk cash");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        RecyclerViewAdapter ra = (RecyclerViewAdapter)recyclerView.getAdapter();
        ra.getMyImageLoader().getDiskCashing().cleanDisk();
        return super.onOptionsItemSelected(item);

    }
}
