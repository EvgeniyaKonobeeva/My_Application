package com.example.appwithfragment.RecyclerViewFragment;

import java.util.ArrayList;

/**
 * Created by e.konobeeva on 05.08.2016.
 * интерфейс получения и обработки результата после выполнения AsyncTask для загрузки url фотографий
 */
public interface GettingResults {
    void onGettingResult(ArrayList<String> photoUrls, ArrayList<String> photosInfo, boolean isEnded);
    void getProgress(int loadingPhotos);
}
