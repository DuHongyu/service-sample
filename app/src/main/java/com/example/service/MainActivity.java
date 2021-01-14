package com.example.service;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.service.binder.DownloadBinder;
import com.example.service.broadcast.MyBroadcastReceiver;
import com.example.service.service.MyIntentService;
import com.example.service.thread.MyThread;
import com.example.service.utils.Constant;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView tvText;

    private static final int UPDATE_TEXT = 1;

    private static final String TAG = "MainActivity";

    private MyBroadcastReceiver myBroadcastReceiver;

    private Handler handler;

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DownloadBinder downloadBinder = (DownloadBinder) service;
            downloadBinder.startDownload();
            downloadBinder.getProgress();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        Button btnExchangeText = findViewById(R.id.btn_exchange_text);
        btnExchangeText.setOnClickListener(this);

        Button btnStartService = findViewById(R.id.btn_start_service);
        btnStartService.setOnClickListener(this);

        Button btnStopService = findViewById(R.id.btn_stop_service);
        btnStopService.setOnClickListener(this);

        Button btnBindService = findViewById(R.id.btn_bind_service);
        btnBindService.setOnClickListener(this);

        Button btnUnbindService = findViewById(R.id.btn_unbind_service);
        btnUnbindService.setOnClickListener(this);

        Button btnIntentService = findViewById(R.id.btn_intent_service);
        btnIntentService.setOnClickListener(this);

        tvText = findViewById(R.id.tv_text);

        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == UPDATE_TEXT) {
                    tvText.setText("Nice to meet you");
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.BROADCAST_MY_BROADCAST);
        myBroadcastReceiver = new MyBroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);
                Toast.makeText(context, "received in MyBroadcastReceiver", Toast.LENGTH_SHORT).show();
                tvText.setText(R.string.you_are_baby);
            }
        };
        registerReceiver(myBroadcastReceiver, intentFilter);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        Log.d(TAG, "onClick");
        switch (id) {
            case R.id.btn_exchange_text:
                Log.d(TAG, "exchange text");
                MyThread myThread = new MyThread() {
                    @Override
                    public void run() {
                        Log.d(TAG, "myThread run");
                        Message message = new Message();
                        message.what = UPDATE_TEXT;
                        handler.sendMessage(message);
                    }
                };
                Thread thread = new Thread(myThread);
                thread.start();
                break;
            case R.id.btn_start_service:
                Intent startIntent = new Intent(this, MyService.class);
                startService(startIntent);
                break;
            case R.id.btn_stop_service:
                Intent stopIntent = new Intent(this, MyService.class);
                stopService(stopIntent);
                break;
            case R.id.btn_bind_service:
                Intent bindIntent = new Intent(this, MyService.class);
                bindService(bindIntent, connection, BIND_AUTO_CREATE);
                break;
            case R.id.btn_unbind_service:
                unbindService(connection);
                break;
            case R.id.btn_intent_service:
                Log.d(TAG, "Thread id is" + Thread.currentThread().getId());
                Intent intentService = new Intent(this, MyIntentService.class);
                startService(intentService);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        unregisterReceiver(myBroadcastReceiver);
    }
}