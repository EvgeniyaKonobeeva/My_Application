package com.example.appwithfragment.MVPPattern;

import android.app.Activity;
import android.os.AsyncTask;

/**
 * Created by e.konobeeva on 23.08.2016.
 */
public interface IFragmentPresenter {
    void onAttachedToView();
    void onRecViewScroll(int dy, int lastVisibleItemPosition);
    void onCreateFragment();
    void onFragmentDestroy();
    void runLoadImageUrlsTask();
    IFragmentPresenter setTag(String tag);
    IFragmentPresenter setProtocol(String protocol);
    IFragmentPresenter setTask(AsyncTask task);
}
