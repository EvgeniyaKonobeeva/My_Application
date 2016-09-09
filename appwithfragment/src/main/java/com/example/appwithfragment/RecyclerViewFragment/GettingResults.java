package com.example.appwithfragment.RecyclerViewFragment;

import com.example.appwithfragment.MVPPattern.IFragment;

import java.util.ArrayList;

/**
 * Created by e.konobeeva on 05.08.2016.
 * интерфейс получения и обработки результата после выполнения AsyncTask для загрузки url фотографий
 */
public interface GettingResults {
    void onGettingResult(ArrayList photosInfo,  boolean isEnded);
    int getCurCluster_id();
    void setCurCluster_id(int clusters_id);
    IFragment getFragment();
}
