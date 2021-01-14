package com.example.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.service.binder.DownloadBinder;
import com.example.service.thread.MyThread;

/**
 * @author Administrator
 */
public class MyService extends Service {

    private static final String TAG = "MyService";

    private final DownloadBinder mBinder = new DownloadBinder();

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("This is content title")
                .setContentText("This is content text")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pi)
                .build();
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        MyThread myThread = new MyThread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    Intent intent2 = new Intent("com.example.broadcast.MY_BROADCAST");
                    sendOrderedBroadcast(intent2,null);
                    Log.d(TAG, "sendOrderedBroadcast");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(myThread);
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}