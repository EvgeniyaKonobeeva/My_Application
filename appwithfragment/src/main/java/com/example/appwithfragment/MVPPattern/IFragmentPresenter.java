package com.example.appwithfragment.MVPPattern;

import android.os.AsyncTask;

/**
 * Created by e.konobeeva on 23.08.2016.
 */
public interface IFragmentPresenter {
    void onCreateView();
    void onRecViewScroll(int dy, int lastVisibleItemPosition);
    void onCreateFragment();
    void onFragmentDestroy();
    void runLoadImageUrlsTask();
    void setTag(String tag);
    void setTask(AsyncTask task);
    void onPauseFragment();
    void onResume();
}
