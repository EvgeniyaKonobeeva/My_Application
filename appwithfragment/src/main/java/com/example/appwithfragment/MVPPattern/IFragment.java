package com.example.appwithfragment.MVPPattern;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;

import com.example.appwithfragment.ListContent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by e.konobeeva on 23.08.2016.
 */
public interface IFragment extends Serializable {
    RecyclerView getRecyclerView();
    Activity getActivity();
    ArrayList<ListContent> getList();
    void setList(ArrayList<ListContent> list);

}