package com.modularization.runtop.Binder;

import android.os.Binder;

import com.model.Book;
import com.modularization.runtop.service.DataUpdateListenerService;

import java.util.List;

/**
 * 修改服务中图书列表的Binder
 * Created by runTop on 2018/7/18.
 */
public class UpdateServerDataBinder extends Binder {
    private DataUpdateListenerService service;

    public UpdateServerDataBinder(DataUpdateListenerService service) {
        this.service = service;
    }

    public void deleteBook(int id) {
        service.deleteBook(id);
    }

    public void updateBook(int id, Book book) {
        service.updateBook(id,book);
    }

    public List<Book> getBookList() {
        return service.getList();
    }

}
