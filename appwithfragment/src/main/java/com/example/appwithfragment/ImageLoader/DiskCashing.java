package com.example.appwithfragment.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by e.konobeeva on 09.08.2016.
 */
public class DiskCashing {
    private static final String errorTag = "ErDiskCashing";
    private File fileDir;
    private Object lock1 = new Object();
    private Object lock2 = new Object();
    private static DiskCashing diskCashing;

    public DiskCashing(Context ctx){
        fileDir = new File(ctx.getCacheDir(), "bitmapCash");
        if(!fileDir.exists()){
            fileDir.mkdirs();
            //Log.d("CREATING DIR", fileDir.mkdirs() + "");
        }

    }

    public static DiskCashing getInstance(Context ctx){
        if(diskCashing == null){
            return new DiskCashing(ctx);
        }else return diskCashing;
    }

    public void saveOnDisk(int keyUrl, Bitmap bitmap){

        synchronized (lock1){
            File file = new File(fileDir.getAbsolutePath(), keyUrl + ".png");
            if(!file.exists()) {
                try {
                    Log.d("PROCESS", "file creating");

                    FileOutputStream fileOutputStream = new FileOutputStream(file);

                    if(hasFreeDiskSpace(fileDir, bitmap)){
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }else{
                        while (!hasFreeDiskSpace(fileDir, bitmap)){
                            deleteLatestFile(fileDir);
                        }
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                        bitmap.recycle();
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                } catch (FileNotFoundException fnfe) {
                    Log.d(errorTag, fnfe.getMessage());
                }catch (IOException ioe){
                    Log.d(errorTag, ioe.getMessage());
                }
            }else{
                Log.d(errorTag, "file exists");
            }
            lock1.notifyAll();
        }

    }
    public Bitmap getImg(int keyUrl){

        synchronized (lock2) {
            File file = new File(fileDir.getAbsolutePath(), keyUrl + ".png");
            if (file.exists()) {
                Log.d("PROCESS ", "file exists");
                lock2.notifyAll();
                return BitmapFactory.decodeFile(file.getPath());
            } else {
                Log.d(errorTag, "file does not exists");
                lock2.notifyAll();
                return null;
            }

        }
    }
    public boolean hasFreeDiskSpace(File file, Bitmap bitmap){
        //Log.d("DiskCashing", "hasFreeDiskSpace");
        double maxSizeByte = 50000000; //50Mb
        double bitmapSize = bitmap.getByteCount();
        if(maxSizeByte - getDirSize(file) >= bitmapSize){
            return true;
        }else
            return false;
    }
    public void deleteLatestFile(File dir){
        //Log.d("DiskCashing", "deleting");
        File[] files = dir.listFiles();
        if(files.length != 0) {
            long now = new Date().getTime();
            File latest = null;
            for (File f : files) {
                if (now > f.lastModified()) {
                    now = f.lastModified();
                    latest = f;
                }
            }
            latest.delete();
        }
    }
    public void cleanDisk(){
        //Log.d("before delete ", "" + getDirSize(fileDir));
        File[] files = fileDir.listFiles();
        if(files.length != 0) {
            for (File f : files) {
                if(f.exists())
                    f.delete();
            }

        }
        //Log.d("after delete ", "" + getDirSize(fileDir));



    }
    public double getDirSize(File dir){
        double size = 0;
        File[] files = dir.listFiles();
        if(files.length != 0) {
            for (File f : files) {
                size += f.length();
            }

        }
        return size;
    }

}
