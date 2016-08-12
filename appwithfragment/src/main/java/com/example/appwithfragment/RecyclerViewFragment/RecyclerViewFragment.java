package com.example.appwithfragment.RecyclerViewFragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
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

    private GridLayoutManager recyclerGridLayout;
    private List<ListContent> list;
    private LoadFromFlickrTask task;

    private boolean loadingFinished = false;
    private boolean lastTaskTerminated = false;

    private int progress;


    public interface OnRecyclerViewClickListener {
        void doAction(ListContent object);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this.getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        listener = (OnRecyclerViewClickListener) this.getActivity();

        view = inflater.inflate(R.layout.fragment, null);

        if (landOrientation()) {
            recyclerGridLayout = new GridLayoutManager(view.getContext(), 3);
        } else {
            recyclerGridLayout = new GridLayoutManager(view.getContext(), 2);
        }
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
        for (int i = 0; i < listSize; i++) {
            text[i] = "photo " + (i + 1);
        }
        if (list == null) {
            list = new ArrayList();
            ListContent lc;
            for (int i = 0; i < listSize; i++) {
                lc = new ListContent(null, "photo" + i);
                list.add(lc);
            }
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.rl);

        //recyclerGridLayout = new GridLayoutManager(view.getContext(), 3);
        recyclerView.setLayoutManager(recyclerGridLayout);
        recyclerView.setAdapter(new RecyclerViewAdapter(list, recyclerGridLayout, activity));

        recyclerView.addOnScrollListener(new MyOnScrollListener(recyclerGridLayout, this));

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                listener.doAction(list.get(position));
            }
        });

        return view;

    }


    private int countLoaders = 0;

    @Override
    public void onGettingResult(ArrayList<String> photoUrls, ArrayList<String> photosInfo, boolean isTheLast) {
        if (countLoaders == 0) {
            countLoaders++;
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setImgUrl(photoUrls.get(i));
                recyclerView.getAdapter().notifyItemChanged(i);
                list.get(i).setFullTitle(photosInfo.get(i));

            }
            if (list.size() < photoUrls.size()) {
                int size = list.size();
                for (int i = size; i < photoUrls.size(); i++) {
                    ListContent listContent = new ListContent(photoUrls.get(i), photosInfo.get(i));
                    list.add(i, listContent);
                    recyclerView.getAdapter().notifyItemChanged(i);
                }
            }
        } else {
            int size = list.size();
            for (int i = size; i < photoUrls.size(); i++) {
                ListContent listContent = new ListContent(photoUrls.get(i), photosInfo.get(i));
                list.add(i, listContent);
                recyclerView.getAdapter().notifyItemChanged(i);
            }
        }


        if (isTheLast) {
            Log.d("LAST TASK", "last task");
            this.lastTaskTerminated = isTheLast;
        }
    }

    @Override
    public void onDestroy() {
        if (task != null) {
            task.setFragment(null);
            task.cancel(false);
        }

        RecyclerViewAdapter ra = (RecyclerViewAdapter) recyclerView.getAdapter();
        ra.getMyImageLoader().terminateAllProcess();
        super.onDestroy();


    }

    @Override
    public void getProgress(int loadingPhotos) {

        loadingFinished = true;
        progress += loadingPhotos;

        //Log.d("PROGRESS", Integer.toString(progress));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu.size() == 0)
            menu.add("clean disk cash");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        RecyclerViewAdapter ra = (RecyclerViewAdapter) recyclerView.getAdapter();
        ra.getMyImageLoader().getDiskCashing().cleanDisk();
        return super.onOptionsItemSelected(item);

    }

    public boolean landOrientation() {
        Log.d("ORIENTATION ", "" + activity.getResources().getConfiguration().orientation);
        Log.d("ORIENTATION PORTRAIT", "" + Configuration.ORIENTATION_PORTRAIT);
        Log.d("ORIENTATION LAND", "" + Configuration.ORIENTATION_LANDSCAPE);
        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return true;
        } else {
            return false;
        }
    }

    public class MyOnScrollListener extends RecyclerView.OnScrollListener {
        GridLayoutManager recyclerGridLayout;
        GettingResults fragment;

        private MyOnScrollListener(GridLayoutManager manager, GettingResults fragment) {
            this.recyclerGridLayout = manager;
            this.fragment = fragment;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            if (dy > 0 && recyclerGridLayout.findLastVisibleItemPosition() >= progress - 5 && loadingFinished && !lastTaskTerminated) {
                loadingFinished = false;
                task = new LoadFromFlickrTask(fragment);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }

    }
}
