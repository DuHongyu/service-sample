package com.example.service.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.service.MainActivity;

public class MyIntentService extends IntentService {

    private static final String TAG = "MyIntentService";

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.d(TAG, "Thread id is"+Thread.currentThread().getId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
