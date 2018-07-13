package com.modularization.runtop.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.interfaces.IBookInterface;
import com.model.Book;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BookService extends Service {
    //存储图书列表信息，CopyOnWriteArrayList支持线程同步。
    private CopyOnWriteArrayList<Book> bookList = new CopyOnWriteArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        bookList.add(new Book(1, "压箱底"));
    }

    //IMyAidlInterface.Stub继承Binder类和在aidl中定义的接口。
    // 在服务端实现aidl接口中的定义的方法，当进行跨进程通信的时候，
    // 在onBind方法中将bookManager对象返回给客户端，客户端就可以调用
    //这里实现的方法，操作服务端的数据完成通信。
    private IBookInterface.Stub binder = new IBookInterface.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return bookList;
        }

        @Override
        public Book inBook(Book book) throws RemoteException {
            return null;
        }

        @Override
        public Book outBook(Book book) throws RemoteException {
            return null;
        }

        @Override
        public Book inoutBook(Book book) throws RemoteException {
            return null;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
