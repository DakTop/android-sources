package com.modularization.runtop.http;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.modularization.runtop.http.runable.GetRequestRunnable;
import com.modularization.runtop.http.runable.PostRequestRunnable;


public class MainActivity extends AppCompatActivity {
    public static final int GetRequest = 1;
    public static final String GetResponse = "GetResponse";
    public static final int PostRequest = 2;
    public static final String PostResponse = "PostResponse";
    private String response;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GetRequest:
                    Toast.makeText(MainActivity.this, msg.getData().getString(GetResponse), Toast.LENGTH_SHORT).show();
                    break;
                case PostRequest:
                    Toast.makeText(MainActivity.this, msg.getData().getString(PostResponse), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * post请求示例
     *
     * @param view
     */
    public void onPost(View view) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new PostRequestRunnable(handler));
    }

    /**
     * get请求示例
     *
     * @param view
     */
    public void onGet(View view) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new GetRequestRunnable(handler));
    }

    /**
     * 文件下载示例
     *
     * @param view
     */
    public void onDownloadFile(View view) {
        startActivity(new Intent(this, MultipleThreadDownloadActivity.class));
    }
}
