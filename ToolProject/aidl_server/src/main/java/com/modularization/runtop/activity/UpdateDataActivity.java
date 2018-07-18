package com.modularization.runtop.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.interfaces.IServerDataChangeListener;
import com.model.Book;
import com.modularization.runtop.Binder.UpdateServerDataBinder;
import com.modularization.runtop.aidl.server.R;
import com.modularization.runtop.service.DataUpdateListenerService;

import java.util.List;

/**
 * 更新数据服务接口
 */
public class UpdateDataActivity extends AppCompatActivity {

    private EditText etUpdateName;
    private EditText etUpdateId;
    private EditText etDeleteId;
    private LinearLayout llBookList;

    private UpdateServerDataBinder binder;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (UpdateServerDataBinder) service;
            refreshBookList(binder.getBookList());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);
        //
        etUpdateName = findViewById(R.id.et_name);
        etUpdateId = findViewById(R.id.et_id);
        etDeleteId = findViewById(R.id.et_delete_id);
        llBookList = findViewById(R.id.ll_booklist);

        //绑定服务
        Intent intent = new Intent(this, DataUpdateListenerService.class);
        intent.setAction(DataUpdateListenerService.LOCAL_BINDER_ACTION);
        this.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 添加或更新一本书，如果图书列表中有这本书的id则更新这本书，
     * 如果没有则添加。
     *
     * @param v
     */
    public void updateBook(View v) {
        String idStr = etUpdateId.getText().toString().trim();
        if (TextUtils.isEmpty(idStr)) {
            return;
        }
        int id = Integer.valueOf(idStr);
        Book book = new Book(id, etUpdateName.getText().toString().trim());
        binder.updateBook(id, book);
        refreshBookList(binder.getBookList());
    }

    /**
     * 删除一本书
     *
     * @param v
     */
    public void deleteBook(View v) {
        String id = etDeleteId.getText().toString().trim();
        if (TextUtils.isEmpty(id)) {
            return;
        }
        binder.deleteBook(Integer.valueOf(id));
        refreshBookList(binder.getBookList());
    }

    /**
     * 刷新图书列表视图
     *
     * @param list
     */
    public void refreshBookList(List<Book> list) {
        llBookList.removeAllViews();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            TextView textView = new TextView(this);
            textView.setText("id：" + list.get(i).getId() + "，name：" + list.get(i).getName());
            llBookList.addView(textView);
        }
    }
}
