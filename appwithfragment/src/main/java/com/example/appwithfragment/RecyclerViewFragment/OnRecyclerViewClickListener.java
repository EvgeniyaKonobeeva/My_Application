package com.example.appwithfragment.RecyclerViewFragment;

import com.example.appwithfragment.ListContent;
import com.example.appwithfragment.TabsFragments.IOnLikePhotoListener;

import java.util.List;

/**
 * Created by e.konobeeva on 16.08.2016.
 */
public interface OnRecyclerViewClickListener {
    void doAction(int object, List<ListContent> urls, IOnLikePhotoListener onLPListener);
}