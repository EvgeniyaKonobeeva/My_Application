package com.example.appwithfragment.FullScreenPicture;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appwithfragment.ImageLoader.MyImageLoader;
import com.example.appwithfragment.ListContent;
import com.example.appwithfragment.MyActivity;
import com.example.appwithfragment.R;

/**
 * Created by e.konobeeva on 02.08.2016.
 */
public class FragmentFullScreenPicture extends Fragment {
    public static final String TAG = "FrgFullScreenPicture";
    private static final String keyContext = "Context";
    private static final String keyListContent = "ListContent";


    public static FragmentFullScreenPicture newInstance(ListContent lc, MyActivity ctx ){
        Log.d("FragmentFullPicture", "Create single pic");
        FragmentFullScreenPicture f = new FragmentFullScreenPicture();
        Bundle b = new Bundle();
        b.putSerializable(keyListContent, lc);
        b.putSerializable(keyContext, ctx);
        f.setArguments(b);
        return f;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.single_pic_frag, null);

        setViews(view, R.id.fullImage, R.id.title);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.i(TAG,"onCreatonDestroyeView");
    }

    @Override
    public void onDestroyView() {
        //Log.i(TAG,"onDestroyView");
        super.onDestroyView();
    }

    public void setViews(View view, int imageViewId, int textViewId){

        ImageView imgView = (ImageView)view.findViewById(imageViewId);

        final TextView txt =(TextView)view.findViewById(textViewId);

        MyImageLoader iml = new MyImageLoader((Context) getArguments().get(keyContext));

        final ListContent lc = (ListContent) getArguments().get(keyListContent);
        String url = lc.getImgUrl();

        iml.setResourceUrl(url.replace("_m", ""));
        iml.setImgInto(imgView);

        txt.post(new Runnable() {
            @Override
            public void run() {
                txt.setText(lc.getFullTitle());
            }
        });
    }

}
