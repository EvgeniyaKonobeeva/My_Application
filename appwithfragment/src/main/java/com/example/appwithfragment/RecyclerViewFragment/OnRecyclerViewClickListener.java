package com.example.appwithfragment.RecyclerViewFragment;

import android.support.v4.app.Fragment;

import com.example.appwithfragment.ListContent;

import java.util.ArrayList;

/**
 * Created by e.konobeeva on 16.08.2016.
 */
public interface OnRecyclerViewClickListener {
    void doAction(int object, ArrayList<ListContent> urls);
}