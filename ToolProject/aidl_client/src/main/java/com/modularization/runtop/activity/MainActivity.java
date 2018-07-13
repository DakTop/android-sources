package com.modularization.runtop.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.modularization.runtop.aidl.client.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 客户端连接服务端 & 向服务端发送数据
     *
     * @param v
     */
    public void toConnectionService(View v) {
        startActivity(new Intent(this, ConnectionServiceActivity.class));
    }
}
