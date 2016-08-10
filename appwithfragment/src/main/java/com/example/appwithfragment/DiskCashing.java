package com.example.appwithfragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

/**
 * Created by e.konobeeva on 09.08.2016.
 */
public class DiskCashing {
    private File fileDir;
    private Object lock1 = new Object();
    private Object lock2 = new Object();

    public DiskCashing(Context ctx){
        fileDir = new File(ctx.getCacheDir() + "bitmapCash");
        if(!fileDir.exists()){
            fileDir.mkdir();
        }
        Log.d("FILE", fileDir.getPath());

    }

    public void cashingImg(String key, Bitmap bitmap){

        synchronized (lock1){
            File file = new File(fileDir.getAbsolutePath(), key.hashCode() + ".png");
            if(!file.exists()) {
                try {
                    Log.d("PROCESS", "file creating");
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.close();
                } catch (FileNotFoundException fne) {
                    Log.d("ERROR", "file not found");
                }catch (IOException ioe){
                    Log.d("ERROR", ioe.getMessage());
                }
            }else{
                Log.d("ERROR ", "file exists");
            }
            lock1.notifyAll();
        }

    }
    public Drawable getImg(String key){
        synchronized (lock2) {
            File file = new File(fileDir.getAbsolutePath(), key.hashCode() + ".png");
            if (file.exists()) {
                Log.d("PROCESS ", "file exists");
                lock2.notifyAll();
                return Drawable.createFromPath(file.getPath());
            } else {
                Log.d("ERROR ", "file does not exists");
                lock2.notifyAll();
                return null;
            }

        }
    }
    public void computingSpaceOptions(File file, Bitmap bitmap){

        long totalSpace = file.getTotalSpace();
        long freeSpace = file.getFreeSpace();
        long usableSpace = file.getUsableSpace();
        long bitmapSize = bitmap.getAllocationByteCount();



    }

}
