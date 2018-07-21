package com.modularization.runtop.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.interfaces.IBookInterface;
import com.model.Book;
import com.modularization.runtop.aidl.client.R;

/**
 * 服务端的身份验证
 */
public class AuthenticationActivity extends AppCompatActivity {
    private static final String TAG = "AuthenticationActivity";
    private IBookInterface binderProxy;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binderProxy = IBookInterface.Stub.asInterface(service);
            if (binderProxy == null) {
                Log.i(TAG, "无权限连接！");
                Toast.makeText(AuthenticationActivity.this, "无权限连接", Toast.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "连接成功！");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
    }

    /**
     * 连接服务
     *
     * @param v
     */
    public void connectServer(View v) {
        Intent intent = new Intent();
        intent.setAction("com.connection.action.server");
        String className = "com.modularization.runtop.service.AuthenticationService";
        String packageName = "com.modularization.runtop.aidl.server";
        intent.setClassName(packageName, className);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 发送消息
     *
     * @param v
     */
    public void sendBook(View v) {
        if (binderProxy != null) {
            try {
                binderProxy.inBook(new Book(1, ""));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return;
        }
        Toast.makeText(AuthenticationActivity.this, "无权限连接！", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }
}
