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

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

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
public class RecyclerViewFragment extends Fragment{

    private OnRecyclerViewClickListener listener;
    private RecyclerView recyclerView;

    private GridLayoutManager recyclerGridLayout;

    public interface OnRecyclerViewClickListener {
        void doAction(ListContent object);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    static int p = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        listener = (OnRecyclerViewClickListener) this.getActivity();

        View view = inflater.inflate(R.layout.fragment, null);

        if (landOrientation()) {
            recyclerGridLayout = new GridLayoutManager(view.getContext(), 3);
        } else {
            recyclerGridLayout = new GridLayoutManager(view.getContext(), 2);
        }
        recyclerView = setRecyclerView(view);

        setHasOptionsMenu(true);

        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("PROCESS", "connection established");
            ((RecyclerViewAdapter) recyclerView.getAdapter()).loadMore();
        } else {
            Log.d("ERROR 0", "connection error");
        }




        return view;
    }


    @Override
    public void onDestroy() {
        RecyclerViewAdapter ra = (RecyclerViewAdapter) recyclerView.getAdapter();
        ra.beforeDelete();
        super.onDestroy();

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.my_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        RecyclerViewAdapter ra = (RecyclerViewAdapter) recyclerView.getAdapter();
        ra.getMyImageLoader().getDiskCashing().cleanDisk();
        return super.onOptionsItemSelected(item);

    }

    public boolean landOrientation() {
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return true;
        } else {
            return false;
        }
    }

    public class MyOnScrollListener extends RecyclerView.OnScrollListener {
        GridLayoutManager recyclerGridLayout;
        RecyclerViewAdapter adapter;

        private MyOnScrollListener(GridLayoutManager manager, RecyclerViewAdapter adapter) {
            this.adapter = adapter;
            this.recyclerGridLayout = manager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            if (dy > 0 && recyclerGridLayout.findLastVisibleItemPosition() >= adapter.getItemCount()-3 && adapter.loadingFinish && !adapter.lastTaskTerminated) {
                ((RecyclerViewAdapter)recyclerView.getAdapter()).loadMore();
            }else if(adapter.lastTaskTerminated && recyclerGridLayout.findLastCompletelyVisibleItemPosition() == adapter.getItemCount()-1){
                Toast.makeText(getActivity(), "all photos uploaded", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public RecyclerView setRecyclerView(View view){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rl);

        recyclerView.setLayoutManager(recyclerGridLayout);
        recyclerView.setAdapter(new RecyclerViewAdapter(getActivity()));

        recyclerView.addOnScrollListener(new MyOnScrollListener(recyclerGridLayout, (RecyclerViewAdapter) recyclerView.getAdapter()));
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                listener.doAction(((RecyclerViewAdapter)recyclerView.getAdapter()).getItem(position));
            }
        });
        return recyclerView;

    }
}
