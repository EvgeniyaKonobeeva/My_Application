package com.example.appwithfragment.RecyclerViewFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.*;
import android.content.Context;
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
import android.widget.Toast;

import com.example.appwithfragment.supportLib.ItemClickSupport;
import com.example.appwithfragment.ListContent;
import com.example.appwithfragment.R;


import java.util.ArrayList;

/**
 * Created by e.konobeeva on 02.08.2016.
 * фрагмент, содержащий RecyclerView.
 * выполняет задания по загрузке url изображений - LoadFromFlickrTask
 */
public class RecyclerViewFragment extends Fragment implements GettingResults {

    protected RecyclerView recyclerView;
    protected LoadFromFlickrTask task;
    protected boolean loadingFinished;
    protected boolean lastTaskTerminated;
    protected static ArrayList<ListContent> list;
    private String protocol = "https://api.flickr.com/services/rest/?method=flickr.interestingness.getList&" +
            "api_key=b14e644ffd373999f625f4d2ba244522&format=json&nojsoncallback=1";

    public void setProtocol(String protocol){
        this.protocol = protocol;
    }
    public void setTag(String tag){
        StringBuilder sb = new StringBuilder(protocol);
        sb.append("&tag=").append(tag);
        protocol = sb.toString();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        loadingFinished = false;
        lastTaskTerminated = false;
        task = LoadFromFlickrTask.setTaskParams(protocol, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recycler_view_frag, null);
        recyclerView = setRecyclerView(view, R.id.rl);

        setHasOptionsMenu(true);
        tryGetInternetConnection();

        return view;
    }

    @Override
    public void onGettingResult(ArrayList<String> photoUrls, ArrayList<String> photosInfo, boolean isTheLast) {

        int size = list.size();
        for (int i = size; i < photoUrls.size(); i++) {
            ListContent listContent = new ListContent(photoUrls.get(i), photosInfo.get(i));
            list.add(i, listContent);
            recyclerView.getAdapter().notifyItemChanged(i);
        }
        lastTaskTerminated = isTheLast;
        loadingFinished = true;

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

        private MyOnScrollListener(GridLayoutManager manager) {
            this.recyclerGridLayout = manager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            if (dy > 0 && recyclerGridLayout.findLastVisibleItemPosition() >= recyclerView.getAdapter().getItemCount()-1 && loadingFinished && !lastTaskTerminated) {
                loadImageUrlsTask(task);
                //Log.d("COUNT COLS", "" + recyclerGridLayout.getSpanCount());
            }else if(lastTaskTerminated && recyclerGridLayout.findLastCompletelyVisibleItemPosition() == recyclerView.getAdapter().getItemCount()-1){
                Toast.makeText(getActivity(), "all photos uploaded", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public RecyclerView setRecyclerView(View view, int id){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(id);
        GridLayoutManager recyclerGridLayout;
        if (landOrientation()) {
            recyclerGridLayout = new GridLayoutManager(view.getContext(), 3);
        } else {
            recyclerGridLayout = new GridLayoutManager(view.getContext(), 2);
        }
        recyclerView.setLayoutManager(recyclerGridLayout);

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(list, getActivity());
        recyclerView.setAdapter(recyclerViewAdapter);


        setSpanSize(recyclerGridLayout, recyclerViewAdapter);

        recyclerView.addOnScrollListener(new MyOnScrollListener(recyclerGridLayout));

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                ((OnRecyclerViewClickListener)getActivity()).doAction(position);
            }
        });

        return recyclerView;

    }


    //метод менеджера - установка в зависимости от позиции viewholder - его определенный тип - колонки или нет
    public void setSpanSize(final GridLayoutManager gridMan, final RecyclerView.Adapter adapter){
        gridMan.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(adapter.getItemViewType(position) == 1)
                    return gridMan.getSpanCount();
                else if(adapter.getItemViewType(position) == 0){
                    return 1;
                }else return -1;
            }
        });
    }

    public void loadImageUrlsTask(LoadFromFlickrTask task){
        loadingFinished = false;
        task = new LoadFromFlickrTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void tryGetInternetConnection(){
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("PROCESS", "connection established");
            loadImageUrlsTask(task);
        } else {
            Log.d("ERROR 0", "connection error");
            final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialog.dismiss();
                }
            });

            alertDialog.show();
        }
    }

    public static ArrayList<ListContent> getList(){
        return list;
    }

}
