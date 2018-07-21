package com.modularization.runtop.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;

import com.interfaces.IBookInterface;
import com.model.Book;

import java.util.List;

/**
 * 客户端权限验证两种方式：
 * 1、在Stub实现类的onTransact方法中对客户端的包名进行验证、权限验证。
 * 这种方式客户端是可以绑定到服务端的，但是如果在onTransact方法中验证失败则不能调用服务端方法。
 * <p>
 * 2、在onBinder方法中进行权限验证，这种方式直接决定客户端是否可以获取到服务端的Binder。
 */
public class AuthenticationService extends Service {
    //指定客户端的包名
    private final String CLIENT_PACKAGE_NAME = "com.modularization.runtop.aidl.client";

    IBookInterface.Stub binder = new IBookInterface.Stub() {
        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            //包名验证
            String[] pn = getPackageManager().getPackagesForUid(getCallingUid());
            if (pn != null && getPackageName().length() > 0) {
                if (pn[0].startsWith("com.modularization.runtop.aidl")) {
                    return super.onTransact(code, data, reply, flags);
                }
                Log.i("setvicetag", "客户端无权限访问onTransact");
                return false;
            }
            return super.onTransact(code, data, reply, flags);
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            return null;
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
