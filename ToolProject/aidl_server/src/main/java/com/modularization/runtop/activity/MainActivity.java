package com.modularization.runtop.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.interfaces.IBookInterface;
import com.model.Book;
import com.modularization.runtop.aidl.server.R;
import com.modularization.runtop.service.BookService;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void toUpdateData(View v) {
        startActivity(new Intent(this, UpdateDataActivity.class));
    }
}
