package com.modularization.runtop.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.interfaces.IBookInterface;
import com.model.Book;
import com.modularization.runtop.aidl.client.R;

import java.util.List;

/**
 * 客户端连接服务端 & 向服务端发送数据
 */
public class ConnectionServiceActivity extends AppCompatActivity {

    private LinearLayout llBook;

    private IBookInterface binder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = IBookInterface.Stub.asInterface(service);
            Toast.makeText(ConnectionServiceActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_service);
        llBook = findViewById(R.id.ll_books);
    }

    /**
     * 连接服务端
     */
    public void connectService(View v) {
        Intent intent = new Intent();
        intent.setAction("com.modularization.runtop.service.BookService");
        intent.setPackage("com.modularization.runtop.aidl.server");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    /**
     * 获取图书列表
     */
    public void getBookList(View v) {
        llBook.removeAllViews();
        try {
            List<Book> list = binder.getBookList();
            for (Book book : list) {
                addNewItemView(book);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void inBook(View v) {
        Book b = new Book(1, "新书");
        try {
            binder.inBook(b);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void outBook(View v) {
        Intent intent = new Intent();
        intent.setAction("com.modularization.runtop.service.BookService");
        intent.setPackage("com.modularization.runtop.aidl.server");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void inoutBook(View v) {
        Intent intent = new Intent();
        intent.setAction("com.modularization.runtop.service.BookService");
        intent.setPackage("com.modularization.runtop.aidl.server");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void addNewItemView(Book book) {
        TextView textView = new TextView(this);
        textView.setText("id：" + book.getId() + "，name：" + book.getName());
        llBook.addView(textView);
    }
}
