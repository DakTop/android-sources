package com.modularization.runtop.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.ConnectionService;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.interfaces.IBookInterface;
import com.modularization.runtop.aidl.client.R;

/**
 * 服务死亡重连机制，共有两种方式：
 * 1、ServiceConnection的onServiceDisconnected方法中监听。
 * 2、设置服务端DeathRecipient死亡代理。
 * 这两种方式不需要服务端做任何处理，只需要在客户端进行设置即可。
 * 如果两种方式都设置了，当服务端的进程被杀死，那么会先回调ServiceConnection中的监听，在回调DeathRecipient中的监听。
 */
public class ReconnectActivity extends AppCompatActivity {
    private static final String tag = "ReconnectActivity";
    private IBookInterface binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reconnection);
    }

    /**
     * 死亡代理
     */
    IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.i(tag, "服务死亡");
            if (binder != null) {
                Log.i(tag, "注销死亡代理");
                binder.asBinder().unlinkToDeath(deathRecipient, 0);
                binder = null;
                //从新连接
                connectServer(null);
            }
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = IBookInterface.Stub.asInterface(service);
            Toast.makeText(ReconnectActivity.this, "服务连接成功！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //第一种方式,只有在意外断开连接时才会调用这个方法，主动方式断开连接不会调用这个方法。
            Log.i(tag, "onServiceDisconnected服务死亡");
            connectServer(null);
        }
    };

    /**
     * 连接服务
     */
    public void connectServer(View v) {
        Log.i(tag, "连接服务");
        Intent intent = new Intent();
        intent.setClassName("com.modularization.runtop.aidl.server", "com.modularization.runtop.service.BookService");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 设置死亡代理
     */
    public void setDeathRecipient(View v) {
        if (binder == null) {
            Toast.makeText(this, "请先连接服务", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            binder.asBinder().linkToDeath(deathRecipient, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


}
