package com.example.appwithfragment.FullScreenPicture;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appwithfragment.ImageLoader.MyImageLoader;
import com.example.appwithfragment.R;

/**
 * Created by e.konobeeva on 02.08.2016.
 */
public class FragmentFullScreenPicture extends Fragment {
    public static final String TAG = "FrgFullScreenPicture";
    private static final String keyTitle = "Title";
    private static final String keyUrl = "URL";
    private static final String keyContext = "Context";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.frag2, null);

        setViews(view, R.id.fullImage, R.id.title);

        Log.i(TAG,"onCreateView");

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onCreatonDestroyeView");
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG,"onDestroyView");
        super.onDestroyView();
    }

    public void setViews(View view, int imageViewId, int textViewId){

        ImageView imgView = (ImageView)view.findViewById(imageViewId);
        final TextView txt =(TextView)view.findViewById(textViewId);
        MyImageLoader iml = new MyImageLoader((Context) getArguments().get(keyContext));
        String url = (String) getArguments().get(keyUrl);
        iml.setResourceUrl(url.replace("_m", ""));
        iml.setImgInto(imgView);


        txt.post(new Runnable() {
            @Override
            public void run() {
                txt.setText((String)getArguments().get(keyTitle));
            }
        });
    }

}
