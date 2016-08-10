package com.example.appwithfragment.FullScreenPicture;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appwithfragment.R;

/**
 * Created by e.konobeeva on 02.08.2016.
 */
public class FragmentFullScreenPicture extends Fragment {
    private View view;
    private ImageView imgView;
    private static final String keyTitle = "Title";
    private static final String keyUrl = "URL";
    private static final String keyContext = "Context";
    private Activity activity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag2, null);

        imgView = (ImageView) view.findViewById(R.id.fullImage);


       // MyImageLoader.load((Context) getArguments().get(keyContext), (String) getArguments().get(keyUrl), imgView);

        final TextView txt =(TextView)view.findViewById(R.id.title);

        txt.post(new Runnable() {
            @Override
            public void run() {
                txt.setText((String)getArguments().get(keyTitle));
            }
        });

        return view;
    }






}
