package com.modularization.runtop.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.interfaces.IRegisterDataListenerInterface;
import com.interfaces.IServerDataChangeListener;
import com.model.Book;
import com.modularization.runtop.aidl.client.R;

import java.util.List;

/**
 * 服务端数据变化监听接口
 */
public class ServerDataListenerActivity extends AppCompatActivity {

    private List<Book> books;
    //由于client监听方法是在内核空间中dBinder线程池中运行，所以不能更新UI，需要借助handler
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            llBooks.removeAllViews();
            int c = books.size();
            for (int i = 0; i < c; i++) {
                addNewItemView(books.get(i));
            }
        }
    };

    private LinearLayout llBooks;
    private IRegisterDataListenerInterface registerBinder;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            registerBinder = IRegisterDataListenerInterface.Stub.asInterface(service);
            try {
                registerBinder.registerServerDataChangeListener(client);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    /**
     * 客户端监听接口，client对象的本质也是一个Binder，通过接口注册的方式将这个Binder对象传递到服务端，
     * 服务端通过操作这个Binder对象，来通知客户端数据变化。
     */
    private IServerDataChangeListener.Stub client = new IServerDataChangeListener.Stub() {
        @Override
        public void deleteBook(int position, Book b) throws RemoteException {
            if (books != null && books.size() > position) {
                books.remove(position);
                handler.sendEmptyMessage(0);
            }
        }

        @Override
        public void updateBook(int position, Book b) throws RemoteException {
            if (books != null && books.size() > position) {
                books.remove(position);
                books.add(position, b);
                handler.sendEmptyMessage(0);
            }
        }

        @Override
        public void initBookList(List<Book> list) throws RemoteException {
            books = list;
            if (books != null) {
                handler.sendEmptyMessage(0);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_data_listener);
        llBooks = findViewById(R.id.ll_books);
    }

    /**
     * 连接服务端
     */
    public void connectService(View v) {
        Intent intent = new Intent();
        intent.setClassName("com.modularization.runtop.aidl.server", "com.modularization.runtop.service.DataUpdateListenerService");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    public void addNewItemView(Book book) {
        TextView textView = new TextView(this);
        textView.setText("id：" + book.getId() + "，name：" + book.getName());
        llBooks.addView(textView);
    }

    @Override
    protected void onDestroy() {
        if (registerBinder != null) {
            try {
                registerBinder.unRegisterServerDataChangeListener(client);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
}
