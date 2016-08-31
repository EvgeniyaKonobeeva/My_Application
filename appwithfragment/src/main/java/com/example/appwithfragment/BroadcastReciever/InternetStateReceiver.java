package com.example.appwithfragment.BroadcastReciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by e.konobeeva on 31.08.2016.
 */
public class InternetStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("InternetStateReceiver", " internet state changed");

    }
}
