package com.example.appwithfragment.RecyclerViewFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.*;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.appwithfragment.MVPPattern.IFragment;
import com.example.appwithfragment.MVPPattern.IFragmentPresenter;
import com.example.appwithfragment.MVPPattern.RecViewFragPresenter;
import com.example.appwithfragment.MyActivity;
import com.example.appwithfragment.supportLib.ItemClickSupport;
import com.example.appwithfragment.ListContent;
import com.example.appwithfragment.R;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by e.konobeeva on 02.08.2016.
 * фрагмент, содержащий RecyclerView.
 * выполняет задания по загрузке url изображений - LoadFromFlickrTask
 */
public class RecyclerViewFragment extends Fragment implements IFragment {

    private ArrayList<ListContent> list;
    private RecyclerView recyclerView;
    private String tag;
    private String protocol;
    private AsyncTask task;

    private IFragmentPresenter presenter;

    public static RecyclerViewFragment getNewInstance(String tag){
        Log.d("RecyclerViewFragment", "getNewInstance tag " + tag);
        if(tag.isEmpty()){
            Log.d("RecyclerViewFragment", "getNewInstance if tag " + tag);
            return new RecyclerViewFragment().setProtocol(MyActivity.protocolInterestingness).setTask(new LoadFromFlickrTask());
        }else {
            Log.d("RecyclerViewFragment", "getNewInstance else tag " + tag);
            return new RecyclerViewFragment().setProtocol(MyActivity.protocol).setTask(new LoadTask()).setTag(tag);
        }

    }

    public RecyclerViewFragment setProtocol(String protocol){
        Log.d("setProtocol ", this.getClass().getName());
        //presenter = new RecViewFragPresenter(this);
        this.protocol = protocol;
        //presenter.setProtocol(protocol);
        return this;
    }

    public RecyclerViewFragment setTag(String _tag){
        this.tag = _tag;
        //presenter.setTag(_tag);
        return this;
    }

    public RecyclerViewFragment setTask(AsyncTask task){
        this.task = task;
        //presenter.setTask(task);
        return this;
    }
   
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("RecyclerViewFragment", "onCreate");
        list = new ArrayList<>();
        this.setRetainInstance(true);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = new RecViewFragPresenter(this);
        presenter.onCreateFragment();
        presenter.setTask(this.task);
        presenter.setProtocol(this.protocol);
        presenter.setTag(this.tag);

        Log.d("RecyclerViewFragment ", "onCreateView ");
        View view = inflater.inflate(R.layout.recycler_view_frag, null);
        recyclerView = setRecyclerView(view, R.id.rl);
        this.setHasOptionsMenu(true);
        presenter.onAttachedToView();
        return view;
    }

    @Override
    public void onDestroy() {
        presenter.onFragmentDestroy();
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.my_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        RecyclerViewAdapter ra = (RecyclerViewAdapter) recyclerView.getAdapter();
        ra.getMyImageLoader().getDiskCashing().cleanDisk();
        return super.onOptionsItemSelected(item);

    }

    public boolean landOrientation() {
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return true;
        } else {
            return false;
        }
    }

    public class MyOnScrollListener extends RecyclerView.OnScrollListener {
        GridLayoutManager recyclerGridLayout;
        private MyOnScrollListener(GridLayoutManager manager) {
            this.recyclerGridLayout = manager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            presenter.onRecViewScroll(dy, recyclerGridLayout.findLastVisibleItemPosition());
        }

    }

    public RecyclerView setRecyclerView(View view, int id){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(id);
        GridLayoutManager recyclerGridLayout;
        if (landOrientation()) {
            recyclerGridLayout = new GridLayoutManager(view.getContext(), 3);
        } else {
            recyclerGridLayout = new GridLayoutManager(view.getContext(), 2);
        }
        recyclerView.setLayoutManager(recyclerGridLayout);

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(list, getActivity());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addItemDecoration(new RecyclerViewDecorator(5));


        this.setSpanSize(recyclerGridLayout, recyclerViewAdapter);

        recyclerView.addOnScrollListener(new MyOnScrollListener(recyclerGridLayout));

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                ((OnRecyclerViewClickListener)getActivity()).doAction(position, list);
            }
        });

        return recyclerView;

    }


    //метод менеджера - установка в зависимости от позиции viewholder - его определенный тип - колонки или строка
    public void setSpanSize(final GridLayoutManager gridMan, final RecyclerView.Adapter adapter){
        gridMan.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(adapter.getItemViewType(position) == 1)
                    return gridMan.getSpanCount();
                else if(adapter.getItemViewType(position) == 0){
                    return 1;
                }else return -1;
            }
        });
    }
    @Override
    public ArrayList<ListContent> getList()
    {
        return list;
    }

    public RecyclerView getRecyclerView(){
        return recyclerView;
    }


    @Override
    public void setList(ArrayList<ListContent> list) {
        this.list = list;
    }



}
