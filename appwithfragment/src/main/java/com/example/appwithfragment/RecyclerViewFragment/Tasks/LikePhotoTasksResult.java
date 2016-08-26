package com.example.appwithfragment.RecyclerViewFragment.Tasks;

import com.example.appwithfragment.ListContent;

import java.util.ArrayList;

/**
 * Created by e.konobeeva on 26.08.2016.
 */
public interface LikePhotoTasksResult {
    void getRemoveResult(int i, ListContent lc);
    void getPhotoResult(ArrayList<ListContent> arr);
}
