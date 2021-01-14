package com.example.service.binder;

import android.os.Binder;
import android.util.Log;

/**
 * @author Administrator
 */
public class DownloadBinder extends Binder {
    private static final String TAG = "MyService";
    public void startDownload(){
        Log.d(TAG, "startDownload");
    }

    public void getProgress(){
        Log.d(TAG, "getProgress");
    }
}
