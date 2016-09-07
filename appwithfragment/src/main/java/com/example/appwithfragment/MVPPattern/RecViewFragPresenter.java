package com.example.appwithfragment.MVPPattern;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.appwithfragment.BroadcastReciever.InternetStateReceiver;
import com.example.appwithfragment.MyActivity;
import com.example.appwithfragment.RecyclerViewFragment.GettingResults;
import com.example.appwithfragment.RecyclerViewFragment.RecyclerViewAdapter;
import com.example.appwithfragment.RecyclerViewFragment.Tasks.InterestingnessTask;
import com.example.appwithfragment.RecyclerViewFragment.Tasks.TaggedPhotosTask;

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
    private Map protocol;
    private String tag;
    private int curCluster_id;
    private boolean firstTaskExecution = true;
    private String tagg = "RecViewFragPresenter ";
    boolean isShownDialog = false;

    private RecyclerView recyclerView;
    private InternetStateReceiver receiver;

    /*init presenter*/
    public RecViewFragPresenter(IFragment frag){
        Log.d("RecViewFragPresenter", "constructor");
        this.fragment = frag;


    }

    public void setTask(AsyncTask task){
        this.task = task;
    }


    public void setTag(String tag){
        this.tag = tag;
    }


    @Override
    public void runLoadImageUrlsTask() {
        loadingFinished = false;
        if(task instanceof InterestingnessTask){
            task = new InterestingnessTask(this);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }else{
            task = new TaggedPhotosTask(this,tag);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        if(firstTaskExecution)
            firstTaskExecution = false;
    }

    @Override
    public void onGettingResult(ArrayList photosInfo, boolean isEnded) {
        ArrayList list = fragment.getList();
        int size = list.size();
        for(int i = 0; i < photosInfo.size(); i++){
            list.add(photosInfo.get(i));
            fragment.getRecyclerView().getAdapter().notifyItemChanged(size++);
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
            if(task instanceof InterestingnessTask) {
                ((InterestingnessTask) task).setFragment(null);
            }else
                ((TaggedPhotosTask)task).setFragment(null);

            task.cancel(false);
        }

        RecyclerViewAdapter ra = (RecyclerViewAdapter) recyclerView.getAdapter();
        //ra.getMyImageLoader().terminateAllProcess();
    }

    public void onCreateView(){
        Log.d("RecViewFragPresenter", "onCreateView presenter");
        //runFirstTask();
        recyclerView = fragment.getRecyclerView();
    }

    public void onCreateFragment(){
        if(receiver == null) {
            receiver = getReceiverInstance();
        }
        if(firstTaskExecution)
            runLoadImageUrlsTask();
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


    public void onResume(){
        Log.d(tagg, "onResume");
        fragment.getActivity().registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void onPauseFragment(){
        Log.d(tagg, "onPauseFragment");
        fragment.getActivity().unregisterReceiver(receiver);
    }



    public InternetStateReceiver getReceiverInstance(){
        receiver = new InternetStateReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //super.onReceive(context, intent);
                Log.d("RecViewFragPresenter", " internet state changed " + tag);

                ConnectivityManager connMgr = (ConnectivityManager) fragment.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    Log.d("PROCESS", "connection established");
                    //isShownDialog= false;
                    if (task.isCancelled() || firstTaskExecution) {
                        Log.d("RecViewFragPresenter", " run task again ");
                        runLoadImageUrlsTask();
                    }
                } else {
                    if (task.getStatus() == AsyncTask.Status.RUNNING) {
                        task.cancel(true);
                        Log.d("RecViewFragPresenter", " cancel task ");
                    }
                    Log.d("ERROR 0", "connection error");
                    showAlertDialog();

                }
            }
        };
        return receiver;
    }

    protected void showAlertDialog(){
        if(!isShownDialog){
            final AlertDialog alertDialog = new AlertDialog.Builder(fragment.getActivity()).create();
            alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialog.dismiss();
                    isShownDialog = false;
                }
            });
            alertDialog.show();
            isShownDialog = true;
        }
    }
}
