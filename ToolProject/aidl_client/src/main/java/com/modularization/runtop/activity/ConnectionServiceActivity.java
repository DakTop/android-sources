package com.modularization.runtop.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
        intent.setClassName("com.modularization.runtop.aidl.server", "com.modularization.runtop.service.BookService");
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

    /**
     * 服务端会收到这个b对象，也会解析对象的信息到服务端，但是当服务端收到这个对象后，修改任何属性的话，
     * 这里的b对象还是原来的信息，不会做出任何修改。
     *
     * @param v
     */
    public void inBook(View v) {
        Book b = new Book(1, "in新书");
        try {
            binder.inBook(b);
            Log.i("in添加一本书", b.getId() + "，" + b.getName());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务端会收到这个b对象，但是不会解析这个对象，会新建一个对象，然后会将新建的对象信息
     * 返回到客户端。
     *
     * @param v
     */
    public void outBook(View v) {
        Book b = new Book(2, "out新书");
        try {
            binder.outBook(b);
            Log.i("out添加一本书", b.getId() + "，" + b.getName());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务端会收到这个b对象，也会解析这个对象的信息到服务端，如果服务端修改对象的任何属性这里的b对象也会被修改。
     *
     * @param v
     */
    public void inoutBook(View v) {
        Book b = new Book(3, "inout新书");
        try {
            binder.inoutBook(b);
            Log.i("inout添加一本书", b.getId() + "，" + b.getName());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void addNewItemView(Book book) {
        TextView textView = new TextView(this);
        textView.setText("id：" + book.getId() + "，name：" + book.getName());
        llBook.addView(textView);
    }
}
