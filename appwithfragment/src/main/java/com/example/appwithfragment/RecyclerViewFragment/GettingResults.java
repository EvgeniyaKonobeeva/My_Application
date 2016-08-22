package com.example.appwithfragment.RecyclerViewFragment;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by e.konobeeva on 05.08.2016.
 * интерфейс получения и обработки результата после выполнения AsyncTask для загрузки url фотографий
 */
public interface GettingResults {
    void onGettingResult(Map<String, String> photosInfo,  boolean isEnded);
}
