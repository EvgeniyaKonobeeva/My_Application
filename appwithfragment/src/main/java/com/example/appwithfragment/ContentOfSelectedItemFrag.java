package com.example.appwithfragment;

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

/**
 * Created by e.konobeeva on 02.08.2016.
 */
public class ContentOfSelectedItemFrag extends Fragment {
    private View view;
    private ImageView imgView;
    private static final String key = "KEY";
    private static int s = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        s++;
        view = inflater.inflate(R.layout.frag2, null);
        imgView = (ImageView) view.findViewById(R.id.fullImage);
        final ListContent listContent = (ListContent) getArguments().get(key);
        imgView.setImageDrawable(listContent.getImgBigSize());

        Log.d("LOOPER", Looper.myLooper() == Looper.getMainLooper() ? "true" : "false");
        final TextView txt =(TextView)view.findViewById(R.id.title);


        txt.post(new Runnable() {
            @Override
            public void run() {
                txt.setText(listContent.getFullTitle());
            }
        });

        ((Button) view.findViewById(R.id.button)).setText(listContent.getFullTitle());

        return view;
    }






}
