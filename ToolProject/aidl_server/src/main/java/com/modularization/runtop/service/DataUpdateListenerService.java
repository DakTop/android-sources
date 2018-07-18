package com.modularization.runtop.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.interfaces.IRegisterDataListenerInterface;
import com.interfaces.IServerDataChangeListener;
import com.model.Book;
import com.modularization.runtop.Binder.UpdateServerDataBinder;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 数据更新服务
 */
public class DataUpdateListenerService extends Service {
    public static final String LOCAL_BINDER_ACTION = "LOCAL_BINDER_ACTION";
    private CopyOnWriteArrayList<Book> list = new CopyOnWriteArrayList<>();
    private UpdateServerDataBinder serverBinder;
    //客户端集合
    private RemoteCallbackList<IServerDataChangeListener> clientList = new RemoteCallbackList<>();
    /**
     * 注册服务数据监听的Binder
     */
    private IRegisterDataListenerInterface.Stub binder = new IRegisterDataListenerInterface.Stub() {
        @Override
        public void registerServerDataChangeListener(IServerDataChangeListener listener) throws RemoteException {
            clientList.beginBroadcast();
            clientList.register(listener);
            listener.initBookList(list);
            clientList.finishBroadcast();
        }

        @Override
        public void unRegisterServerDataChangeListener(IServerDataChangeListener listener) throws RemoteException {
            clientList.beginBroadcast();
            clientList.unregister(listener);
            clientList.finishBroadcast();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        list.add(new Book(1, "压箱底"));
    }

    @Override
    public IBinder onBind(Intent intent) {
        //如果修改服务中数据则返回ServerBinder
        if (LOCAL_BINDER_ACTION.equals(intent.getAction())) {
            if (serverBinder == null) {
                serverBinder = new UpdateServerDataBinder(this);
            }
            return serverBinder;
        } else {
            //如果监听服务中数据修改则返回IRegisterDataListenerInterface.Stub
            return binder.asBinder();
        }
    }

    /**
     * 删除一本书
     *
     * @param id
     */
    public void deleteBook(int id) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Book b = list.get(i);
            if (b.getId() == id) {
                list.remove(i);
                int c = clientList.beginBroadcast();
                for (int j = 0; j < c; j++) {
                    try {
                        clientList.getBroadcastItem(j).deleteBook(i, b);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                clientList.finishBroadcast();
                return;
            }
        }
    }

    /**
     * 修改或者添加一本书
     *
     * @param id
     * @param book
     */
    public void updateBook(int id, Book book) {
        //修改一本书
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (list.get(i).getId() == id) {
                list.remove(i);
                list.add(i, book);
                int c = clientList.beginBroadcast();
                for (int j = 0; j < c; j++) {
                    try {
                        clientList.getBroadcastItem(j).updateBook(i, book);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                clientList.finishBroadcast();
                return;
            }
        }
        //添加一本书
        list.add(book);
        int n = clientList.beginBroadcast();
        for (int k = 0; k < n; k++) {
            try {
                clientList.getBroadcastItem(k).initBookList(list);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        clientList.finishBroadcast();
    }

    public CopyOnWriteArrayList<Book> getList() {
        return list;
    }
}
