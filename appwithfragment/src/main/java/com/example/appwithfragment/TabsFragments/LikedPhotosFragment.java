package com.example.appwithfragment.TabsFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appwithfragment.ListContent;
import com.example.appwithfragment.R;
import com.example.appwithfragment.RecyclerViewFragment.ARecyclerViewFragment;
import com.example.appwithfragment.RecyclerViewFragment.OnRecyclerViewClickListener;
import com.example.appwithfragment.RecyclerViewFragment.RecyclerViewAdapter;
import com.example.appwithfragment.RecyclerViewFragment.RecyclerViewDecorator;
import com.example.appwithfragment.RecyclerViewFragment.RecyclerViewFragment;
import com.example.appwithfragment.supportLib.ItemClickSupport;

import java.util.List;

/**
 * Created by e.konobeeva on 23.08.2016.
 */
public class LikedPhotosFragment extends ARecyclerViewFragment {
    public static final String keyLikeListener = "keyLikeListener";
    private RecyclerView recyclerView;
    private IOnLikePhotoListener likePhotoListener;
    private List<ListContent> list;

    public static LikedPhotosFragment getNewInstance(IOnLikePhotoListener likePhotoListener){
        Bundle bundle = new Bundle();
        bundle.putSerializable(keyLikeListener, likePhotoListener);
        LikedPhotosFragment frag = new LikedPhotosFragment();
        frag.setArguments(bundle);
        return frag;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("LikedPhotosFragment", "onCreate");
        likePhotoListener = (IOnLikePhotoListener)getArguments().get(keyLikeListener);

        this.setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("LikedPhotosFragment", "onCreateView");
        View view = inflater.inflate(R.layout.recycler_view_frag, null);
        list = likePhotoListener.getLikedPhotos();

        recyclerView = (RecyclerView) view.findViewById(R.id.rl);
        recyclerView.setAdapter(new RecyclerViewAdapter(list, getActivity()));
        recyclerView.addItemDecoration(new RecyclerViewDecorator(5));
        GridLayoutManager recyclerGridLayout;
        if (landOrientation()) {
            recyclerGridLayout = new GridLayoutManager(view.getContext(), 3);
        } else {
            recyclerGridLayout = new GridLayoutManager(view.getContext(), 2);
        }
        recyclerView.setLayoutManager(recyclerGridLayout);
        recyclerView.getAdapter().notifyItemRemoved(recyclerGridLayout.findLastVisibleItemPosition());
        setOnItemClickListener(recyclerView, list, likePhotoListener);

        return view;
    }


}
