package com.example.appwithfragment.RecyclerViewFragment;

import java.util.ArrayList;

/**
 * Created by e.konobeeva on 05.08.2016.
 */
public interface GettingResults {
    void onGettingResult(ArrayList<String> photoUrls, ArrayList<String> photosInfo);
    void getProgress(int loadingPhotos);
}
