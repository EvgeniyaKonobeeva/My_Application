package com.example.appwithfragment.RecyclerViewFragment;

import android.content.Context;
import android.net.wifi.WifiManager;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.appwithfragment.DataBasePack.DBHelper;
import com.example.appwithfragment.MVPPattern.IFragment;
import com.example.appwithfragment.MVPPattern.IFragmentPresenter;
import com.example.appwithfragment.MVPPattern.RecViewFragPresenter;
import com.example.appwithfragment.MyActivity;
import com.example.appwithfragment.PhotoObjectInfo;
import com.example.appwithfragment.RecyclerViewFragment.Tasks.LoadFromFlickrTask;
import com.example.appwithfragment.RecyclerViewFragment.Tasks.LoadTask;
import com.example.appwithfragment.TabsFragments.IOnLikePhotoListener;
import com.example.appwithfragment.TabsFragments.OnLikePhotoListener;
import com.example.appwithfragment.R;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by e.konobeeva on 02.08.2016.
 * фрагмент, содержащий RecyclerView.
 * выполняет задания по загрузке url изображений - LoadFromFlickrTask
 */
public class RecyclerViewFragment extends ARecyclerViewFragment implements IFragment, Serializable {

    private ArrayList<PhotoObjectInfo> list;
    private RecyclerView recyclerView;
    private static String logTag = "RecyclerViewFragment";

    private String tag;
    private String protocol;
    private AsyncTask task;
    private DBHelper dbHelper;

    private IOnLikePhotoListener likePhotoListener;

    private IFragmentPresenter presenter;
    private WifiManager wifiManager;
    private CheckBox checkBox;

    public static RecyclerViewFragment getNewInstance(String tag, DBHelper dbHelper){
        Log.d("RecyclerViewFragment", "getNewInstance tag " + tag);
        if(tag.equals("interestingness")){
            Log.d("RecyclerViewFragment", "getNewInstance LoadFromFlickrTask " + tag);
            RecyclerViewFragment fragment = new RecyclerViewFragment().setProtocol(MyActivity.protocolInterestingness)
                    .setTask(new LoadFromFlickrTask()).setDbHelper(dbHelper);
            return fragment;
        }else {
            Log.d("RecyclerViewFragment", "getNewInstance LoadTask " + tag);
            RecyclerViewFragment fragment = new RecyclerViewFragment().setProtocol(MyActivity.protocol)
                    .setTask(new LoadTask()).setTag(tag).setDbHelper(dbHelper);
            return fragment;
        }

        //Log.d("RecyclerViewFragment", "getNewInstance else tag " + tag);

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

        presenter = new RecViewFragPresenter(this);
        presenter.setTask(this.task);
        presenter.setProtocol(this.protocol);
        presenter.setTag(this.tag);


        presenter.onCreateFragment();

        wifiManager = (WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);
        this.setRetainInstance(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("RecyclerViewFragment ", "onCreateView fragment");


        View view = inflater.inflate(R.layout.recycler_view_frag, null);
        recyclerView = setRecyclerView(view, R.id.rl);
        this.setHasOptionsMenu(true);
        presenter.onCreateView();
        if(checkBox != null && wifiManager != null){
            checkBox.setChecked(wifiManager.isWifiEnabled());
        }
        return view;
    }

    @Override
    public void onDestroy() {
        Log.d("RecyclerViewFragment", " onDestroy");
        if(presenter != null){
            presenter.onFragmentDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.my_menu, menu);
        MenuItem item = menu.findItem(R.id.wifi);
        checkBox = (CheckBox)item.getActionView();
        checkBox.setChecked(wifiManager.isWifiEnabled());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                wifiManager.setWifiEnabled(checkBox.isChecked());
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.item1){
            RecyclerViewAdapter ra = (RecyclerViewAdapter) recyclerView.getAdapter();
            ra.getMyImageLoader().getDiskCashing().cleanDisk();
        }else{
            Log.d(logTag, "item " + ((CheckBox)item.getActionView()).isChecked());
        }
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
    public ArrayList<PhotoObjectInfo> getList()
    {
        return list;
    }

    public RecyclerView getRecyclerView(){
        return recyclerView;
    }


    @Override
    public void setList(ArrayList<PhotoObjectInfo> list) {
        this.list = list;
    }

    public IOnLikePhotoListener getLikePhotoListener(){
        Bundle bundle = new Bundle();
        bundle.putString("category", tag);
        bundle.putSerializable("dbHelper", dbHelper);
        likePhotoListener = new OnLikePhotoListener(bundle);
        return likePhotoListener;
    }

    @Override
    public void onPause() {
        Log.d("RecyclerViewFragment", "onPause");
        presenter.onPauseFragment();
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d("RecyclerViewFragment", "onResume");
        presenter.onResume();
        super.onResume();
    }
}
