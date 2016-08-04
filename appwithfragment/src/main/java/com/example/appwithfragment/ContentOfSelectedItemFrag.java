package com.example.appwithfragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by e.konobeeva on 02.08.2016.
 */
public class ContentOfSelectedItemFrag extends Fragment {
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag2, null);
        ((TextView)view.findViewById(R.id.frag_text_view)).setText(getArguments().get("key").toString());
        return view;
    }


}
