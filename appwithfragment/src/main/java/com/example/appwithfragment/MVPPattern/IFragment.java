package com.example.appwithfragment.MVPPattern;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.example.appwithfragment.PhotoObjectInfo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by e.konobeeva on 23.08.2016.
 */
public interface IFragment extends Serializable {
    RecyclerView getRecyclerView();
    Activity getActivity();
    ArrayList<PhotoObjectInfo> getList();
    void setList(ArrayList<PhotoObjectInfo> list);

}
