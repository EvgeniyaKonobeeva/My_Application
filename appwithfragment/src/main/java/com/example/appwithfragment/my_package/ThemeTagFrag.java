package com.example.appwithfragment.my_package;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.appwithfragment.ListContent;
import com.example.appwithfragment.RecyclerViewFragment.LoadFromFlickrTask;
import com.example.appwithfragment.RecyclerViewFragment.RecyclerViewAdapter;
import com.example.appwithfragment.RecyclerViewFragment.RecyclerViewFragment;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

/**
 * Created by Евгения on 18.08.2016.
 * класс - наследник класса RecyclerViewFragment
 * пердставляет собой фрагмент загружающий в RecyclerView картинки по тегам
 * нужно переопределить методы getImgUrlTask
 * oncreate
 * onDestroy
 */
/*public class ThemeTagFrag extends RecyclerViewFragment {
    private LoadTask task;
    private String tag;
    private static ArrayList<ListContent> list;
    private static int curCluster_id = 0;// нужно знать
    private static ArrayList<String> photoUrls = new ArrayList<>();//
    private static ArrayList<String> photoTitles = new ArrayList<>();//

    public void setTag(String _tag){
        tag = _tag;
        StringBuilder sb = new StringBuilder(protocol);
        sb.append("&tag=").append(tag);
        protocol = sb.toString();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ThemeTagFrag", "onCreate");
        list = new ArrayList<>();
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
    public void loadImageUrlsTask() {
        this.loadingFinished = false;
        this.task = new LoadTask(this, protocol, photoUrls, photoTitles, curCluster_id, tag);
        this.task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}*/
