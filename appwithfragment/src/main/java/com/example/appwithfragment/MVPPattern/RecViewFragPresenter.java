package com.example.appwithfragment.MVPPattern;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.appwithfragment.ListContent;
import com.example.appwithfragment.RecyclerViewFragment.GettingResults;
import com.example.appwithfragment.RecyclerViewFragment.Tasks.LoadFromFlickrTask;
import com.example.appwithfragment.RecyclerViewFragment.Tasks.LoadTask;
import com.example.appwithfragment.RecyclerViewFragment.RecyclerViewAdapter;


import java.util.ArrayList;
import java.util.Map;

/**
 * Created by e.konobeeva on 23.08.2016.
 */
public class RecViewFragPresenter implements GettingResults, IFragmentPresenter{
    private IFragment fragment;
    private AsyncTask task;
    private boolean loadingFinished;
    private boolean lastTaskTerminated;
    private String protocol;
    private String tag;
    private int curCluster_id;

    private RecyclerView recyclerView;

    public RecViewFragPresenter(IFragment frag){
        this.fragment = frag;
    }

    public void setTask(AsyncTask task){
        this.task = task;
    }

    public void setProtocol(String protocol){
        this.protocol = protocol;

    }

    public void setTag(String tag){
        this.tag = tag;
        if(tag != null){
            if(!tag.isEmpty()) {
                StringBuilder sb = new StringBuilder(protocol);
                sb.append("&tag=").append(tag);
                protocol = sb.toString();
                this.tag = tag;
            }
        }
    }

    @Override
    public void runLoadImageUrlsTask() {
        loadingFinished = false;
        if(task instanceof LoadFromFlickrTask){
            task = new LoadFromFlickrTask(this, protocol);
            //Log.d("RecVfrag", "task LoadFromFlickrTask ");
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }else{
            //Log.d("cluster current", " " + curCluster_id);
            //Log.d("RecVfrag", "task LoadFromFlickrTask ");
            task = new LoadTask(this, protocol,tag);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    public void onGettingResult(Map<String, String> photosInfo, boolean isEnded) {
        ArrayList<ListContent> list = fragment.getList();
        int size = list.size();
        for (Map.Entry<String,String> entry : photosInfo.entrySet()){
            //Log.d("RecvFrag" , "onGettingResult key : " + entry.getKey() + " value : " + entry.getValue());
            ListContent listContent = new ListContent(entry.getKey(), entry.getValue());
            list.add(size, listContent);
            fragment.getRecyclerView().getAdapter().notifyItemChanged(size);
            size++;
        }
        lastTaskTerminated = isEnded;
        loadingFinished = true;

        fragment.setList(list);
    }

    public int getCurCluster_id(){
        return curCluster_id;
    }

    public void setCurCluster_id(int id){
        curCluster_id = id;
    }

    public void onFragmentDestroy(){
        if (task != null) {
            if(task instanceof LoadFromFlickrTask){
                ((LoadFromFlickrTask)task).setFragment(null);
            }else
                ((LoadTask)task).setFragment(null);

            task.cancel(false);
        }

        RecyclerViewAdapter ra = (RecyclerViewAdapter) recyclerView.getAdapter();
        ra.getMyImageLoader().terminateAllProcess();
    }

    public void onAttachedToView(){
        getInternetConnection();
        recyclerView = fragment.getRecyclerView();
    }

    public void onCreateFragment(){
        curCluster_id = 0;
    }

    @Override
    public void onRecViewScroll(int dy, int lastVisibleItemPosition){
        GridLayoutManager recyclerGridLayout = (GridLayoutManager)recyclerView.getLayoutManager();
        if (dy > 0 && lastVisibleItemPosition >= recyclerView.getAdapter().getItemCount()-1 && loadingFinished && !lastTaskTerminated) {
            runLoadImageUrlsTask();
        }else if(lastTaskTerminated && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount()-1){
            Toast.makeText(fragment.getActivity(), "all photos uploaded", Toast.LENGTH_SHORT).show();
            recyclerView.getAdapter().notifyItemRemoved(recyclerGridLayout.findLastCompletelyVisibleItemPosition()+1);
        }
    }

    protected void getInternetConnection(){
        ConnectivityManager connMgr = (ConnectivityManager) fragment.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("PROCESS", "connection established");
            this.runLoadImageUrlsTask();

        } else {
            Log.d("ERROR 0", "connection error");

            final AlertDialog alertDialog = new AlertDialog.Builder(fragment.getActivity()).create();
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
}
