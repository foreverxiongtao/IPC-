package com.desperado.xt.messengerclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv_main_result;
    private static final int MSG_NUM = 0x110;
    private Messenger serviceMessenger;
    private Messenger messenger = new Messenger(new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_NUM:
                    tv_main_result.append(msg.arg1+"");
                    break;
            }
        }
    });
    private boolean connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_main_result = (TextView) findViewById(R.id.tv_main_result);
        connectService();
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("onServiceConnected0", "onServiceConnected");
            serviceMessenger = new Messenger(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("onServiceDisconnected", "onServiceDisconnected");
        }
    };

    private void setResult() {

    }

    private void connectService() {
        Intent intent = new Intent();
        intent.setAction("com.desperado.xt.messengerserver");
        connected = bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void remoteCal(View view) {
        Message message = Message.obtain();
        message.what = MSG_NUM;
        message.arg1 = 10;
        message.arg2 = 20;
        message.replyTo = messenger;
        if(connected){
            try {
                serviceMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
