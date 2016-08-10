package com.example.appwithfragment;

import android.content.Context;
import android.graphics.Bitmap;
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
    private static final String errorTag = "ERROR DiskCashing";
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

    public void saveOnDisk(int keyUrl, Bitmap bitmap){

        synchronized (lock1){
            File file = new File(fileDir.getAbsolutePath(), keyUrl + ".jpeg");
            if(!file.exists()) {
                try {
                    Log.d("PROCESS", "file creating");
                    FileOutputStream fileOutputStream = new FileOutputStream(file);

                    if(hasFreeDiskSpace(fileDir, bitmap)){
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fileOutputStream);
                        bitmap.recycle();
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }else{
                        while (!hasFreeDiskSpace(fileDir, bitmap)){
                            deleteLatestFile(fileDir);
                        }
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fileOutputStream);
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
    public Drawable getImg(int keyUrl){
        synchronized (lock2) {
            File file = new File(fileDir.getAbsolutePath(), keyUrl + ".JPG");
            if (file.exists()) {
                Log.d("PROCESS ", "file exists");
                lock2.notifyAll();
                return Drawable.createFromPath(file.getPath());
            } else {
                Log.d(errorTag, "file does not exists");
                lock2.notifyAll();
                return null;
            }

        }
    }
    public boolean hasFreeDiskSpace(File file, Bitmap bitmap){
        Log.d("DiskCashing", "hasFreeDiskSpace");
        int maxSizeMb = 50;
        int usableSpace = (int) file.getUsableSpace()/1024;
        int bitmapSize = bitmap.getByteCount()/1024;
        if(maxSizeMb-usableSpace >= bitmapSize){
            return true;
        }else return false;
    }
    public void deleteLatestFile(File dir){
        Log.d("DiskCashing", "deleting");
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

}
