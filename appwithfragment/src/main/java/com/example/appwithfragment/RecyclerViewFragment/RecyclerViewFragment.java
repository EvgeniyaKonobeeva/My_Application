package com.example.appwithfragment.RecyclerViewFragment;

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

import com.example.appwithfragment.DataBasePack.DBHelper;
import com.example.appwithfragment.MVPPattern.IFragment;
import com.example.appwithfragment.MVPPattern.IFragmentPresenter;
import com.example.appwithfragment.MVPPattern.RecViewFragPresenter;
import com.example.appwithfragment.MyActivity;
import com.example.appwithfragment.RecyclerViewFragment.Tasks.LoadFromFlickrTask;
import com.example.appwithfragment.RecyclerViewFragment.Tasks.LoadTask;
import com.example.appwithfragment.TabsFragments.IOnLikePhotoListener;
import com.example.appwithfragment.TabsFragments.OnLikePhotoListener;
import com.example.appwithfragment.ListContent;
import com.example.appwithfragment.R;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by e.konobeeva on 02.08.2016.
 * фрагмент, содержащий RecyclerView.
 * выполняет задания по загрузке url изображений - LoadFromFlickrTask
 */
public class RecyclerViewFragment extends ARecyclerViewFragment implements IFragment, Serializable {

    private ArrayList<ListContent> list;
    private RecyclerView recyclerView;

    private String tag;
    private String protocol;
    private AsyncTask task;
    private DBHelper dbHelper;

    private IOnLikePhotoListener likePhotoListener;

    private IFragmentPresenter presenter;

    public static RecyclerViewFragment getNewInstance(String tag, DBHelper dbHelper){
        Log.d("RecyclerViewFragment", "getNewInstance tag " + tag);
        if(tag.isEmpty()){
            Log.d("RecyclerViewFragment", "getNewInstance if tag " + tag);
            RecyclerViewFragment fragment = new RecyclerViewFragment().setProtocol(MyActivity.protocolInterestingness)
                    .setTask(new LoadFromFlickrTask()).setDbHelper(dbHelper);
            //fragment.likePhotoListener = new OnLikePhotoListener(fragment.getActivity());
            return fragment;
        }else {
            Log.d("RecyclerViewFragment", "getNewInstance else tag " + tag);
            RecyclerViewFragment fragment = new RecyclerViewFragment().setProtocol(MyActivity.protocol)
                    .setTask(new LoadTask()).setTag(tag).setDbHelper(dbHelper);
            //fragment.likePhotoListener = new OnLikePhotoListener(fragment.getActivity());
            return fragment;
        }

    }

    public RecyclerViewFragment setProtocol(String protocol){
        Log.d("setProtocol ", this.getClass().getName());
        this.protocol = protocol;
        return this;
    }

    public RecyclerViewFragment setTag(String _tag){
        this.tag = _tag;
        return this;
    }

    public RecyclerViewFragment setTask(AsyncTask task){
        this.task = task;
        return this;
    }

    public RecyclerViewFragment setDbHelper(DBHelper dbHelper){
        this.dbHelper = dbHelper;
        return this;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("RecyclerViewFragment", "onActivityCreated");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("RecyclerViewFragment", "onCreate");
        list = new ArrayList<>();
        if(likePhotoListener == null){
            likePhotoListener = new OnLikePhotoListener(dbHelper, tag);
        }



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
        if(presenter != null){
            presenter.onFragmentDestroy();
        }

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

        setOnItemClickListener(recyclerView, list, likePhotoListener);

        return recyclerView;

    }


    //метод менеджера - установка в зависимости от позиции viewholder - его определенный тип - колонки или строка

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

    public IOnLikePhotoListener getLikePhotoListener(){
        likePhotoListener = new OnLikePhotoListener(dbHelper, tag);
        return likePhotoListener;
    }

}
