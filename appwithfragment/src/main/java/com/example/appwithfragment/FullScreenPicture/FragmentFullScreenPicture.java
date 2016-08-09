package com.example.appwithfragment.fullScreenPicture;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appwithfragment.ListContent;
import com.example.appwithfragment.R;

/**
 * Created by e.konobeeva on 02.08.2016.
 */
public class FragmentFullScreenPicture extends Fragment {
    private View view;
    private ImageView imgView;
    private static final String key = "KEY";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag2, null);

        imgView = (ImageView) view.findViewById(R.id.fullImage);
        final ListContent listContent = (ListContent) getArguments().get(key);
        imgView.setImageDrawable(listContent.getImg());

        final TextView txt =(TextView)view.findViewById(R.id.title);

        txt.post(new Runnable() {
            @Override
            public void run() {
                txt.setText(listContent.getFullTitle());
            }
        });

        return view;
    }






}
