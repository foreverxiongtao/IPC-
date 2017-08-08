package com.desperado.xt.messengerserver;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by xt on 2017/8/8.
 */

public class MessaengerService extends Service {
    public static final int MSG_NUM = 0x110;
    private Handler mHander;
    private Messenger messenger;
    private HandlerThread thread;

    @Override
    public void onCreate() {
        super.onCreate();
        thread = new HandlerThread("messgae_handle");
        thread.start();
        mHander = new Handler(thread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Message clientMsg = Message.obtain(msg);
                switch (msg.what) {
                    case MSG_NUM:
                        clientMsg.what = MSG_NUM;
                        int result = msg.arg1 + msg.arg2;
                        clientMsg.arg1 = result;
                        clientMsg.replyTo = msg.replyTo;
                        try {
                            msg.replyTo.send(clientMsg);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;

                }
            }
        };
        messenger = new Messenger(mHander);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thread.quit();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
