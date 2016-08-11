package com.example.appwithfragment.ImageLoader;

import android.util.Log;

import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by e.konobeeva on 11.08.2016.
 */
public class LIFOQueue extends LinkedBlockingDeque<Runnable> {
    public LIFOQueue(int size){
        super(size);
    }

    @Override
    public boolean offer(Runnable o) {
        //Log.d("LIFOQueue", "offer-push the thread"  + o.getClass().getSimpleName());
        try {
            push(o);
        }catch (IllegalStateException ie){
            remove();
            push(o);
        }
        return true;
    }


    @Override
    public Runnable remove() {
        //Log.d("LIFOQueue", "remove the thread");
        return super.remove();
    }

    @Override
    public Runnable take() throws InterruptedException {
        //Log.d("LIFOQueue", "take-pop the thread");
        if(!this.isEmpty()){
            try {
                return pop();
            }catch (NoSuchElementException nsee){
                Log.d("LILOQueue", nsee.toString());
                return null;
            }
        }
        else return null;
    }
}
