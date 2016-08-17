package com.example.appwithfragment.example_navigation_bar;

import android.app.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.appwithfragment.R;

/**
 * Created by e.konobeeva on 17.08.2016.
 */
public class ExMainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ListView listView;
    private FrameLayout frameLayout;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Log.d("CREATe", "create");
        setContentView(R.layout.ex_navigatorlayout);
        drawerLayout = (DrawerLayout) findViewById(R.id.DrawerLay);
        listView = (ListView) findViewById(R.id.navigationList);
        frameLayout = (FrameLayout) findViewById(R.id.frameLay);
        String str[] = new String[]{
                "menu1",
                "menu2",
                "menu3",
                "menu4"
        };
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.list_view_items, getResources().getStringArray(R.array.list_items)));
    }

}
