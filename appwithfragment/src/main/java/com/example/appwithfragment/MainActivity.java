package com.example.appwithfragment;

import android.app.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MyFragment.myOnClickListener {

    private ContentOfSelectedItemFrag fragment2;
    private Bundle bundle;

    @Override
    public void doAction(String object) {
       if(fragment2 == null){
           fragment2 = new ContentOfSelectedItemFrag();
       }
        FragmentTransaction fragTrans = getFragmentManager().beginTransaction();
        fragTrans.replace(R.id.LL, fragment2);
        fragTrans.addToBackStack(null);
        fragTrans.commit();
        bundle = new Bundle();
        bundle.putCharSequence("key",object);
        fragment2.setArguments(bundle);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyFragment fragment = new MyFragment();
        FragmentTransaction fragTrans= getFragmentManager().beginTransaction();
        fragTrans.add(R.id.LL, fragment);
        fragTrans.addToBackStack(null);
        fragTrans.commit();
    }


}
