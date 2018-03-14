package com.modularization.runtop.http;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.modularization.runtop.http.runable.MultipleThreadDownload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

/**
 * 多线程下载文件
 */
public class MultipleThreadDownloadActivity extends AppCompatActivity {
    private static final String DownloadUrl = "http://d1.music.126.net/dmusic/CloudMusic_official_5.0.0.559922.apk";
    //下载文件名称
    private static final String DownloadFileName = "qangyiyiyue.apk";
    //多线程下载线程数量
    private final int ThreadCount = 3;
    //多线程下载文件存放路径
    private static String DownloadPath = "";
    private TextView tvSingleDown;
    private TextView tvMultipleDown;
    //多线程下载文件进度
    private long multipleDownloadSize = 0;
    //多线程下载文件总大小
    private long fileSize = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    tvSingleDown.setText("单线程下载:" + msg.getData().getString("singprogress") + "%");
                    break;
                case 2:
                    multipleDownloadSize += msg.getData().getLong("downloadSize");
                    tvMultipleDown.setText("多线程下载:" + ((float) multipleDownloadSize / (float) fileSize) * 100 + "%");
                    if (multipleDownloadSize >= fileSize) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(new File(DownloadPath)), "application/vnd.android.package-archive");
                        startActivity(intent);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_thread_download);
        tvSingleDown = findViewById(R.id.tv_singledownload);
        tvMultipleDown = findViewById(R.id.tv_multipledownload);
        //初始化文件下载路径
        DownloadPath = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + DownloadFileName;
    }

    /**
     * 单线程下载
     *
     * @param view
     */
    public void singleDownload(View view) {
        AsyncTask.execute(singleDownloadRun);
    }

    public Runnable singleDownloadRun = new Runnable() {
        @Override
        public void run() {
            try {
                HttpURLConnection connection = (HttpURLConnection) (new URL(DownloadUrl)).openConnection();
                OutputStream outputStream = MultipleThreadDownloadActivity.this.openFileOutput(DownloadFileName, MODE_PRIVATE);
                InputStream inputStream = connection.getInputStream();
                byte[] buffer = new byte[1024];
                int len;
                float fileLength = connection.getContentLength();
                float downLength = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(len);
                    String progressSingleDown = String.valueOf((downLength += len) / fileLength * 100);
                    Message message = new Message();
                    message.what = 1;
                    Bundle b = new Bundle();
                    b.putString("singprogress", progressSingleDown);
                    message.setData(b);
                    handler.sendEmptyMessage(1);
                }
                outputStream.flush();
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


    /**
     * 多线程下载
     *
     * @param view
     */
    public void multipleDownload(View view) {
        multipleDownloadSize = 0;
        fileSize = 0;
        AsyncTask.execute(MultipleDownloadRun);
    }

    Runnable MultipleDownloadRun = new Runnable() {
        @Override
        public void run() {
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) (new URL(DownloadUrl)).openConnection();
                fileSize = httpURLConnection.getContentLength();
                RandomAccessFile randomAccessFile = new RandomAccessFile(DownloadPath, "rw");
                //设置一个与资源文件大小相等的空文件
                randomAccessFile.setLength(fileSize);
                //取余
                long surplus = (fileSize % ThreadCount);
                //去掉余数后，计算每个线程平均下载文件大小。
                long average = (fileSize - surplus) / ThreadCount;
                for (int i = 0; i < ThreadCount; i++) {
                    //线程平均下载文件大小
                    long downloadLength = average;
                    if (i + 1 == ThreadCount) {
                        //把文件平均份以外的下载量放到最后一个线程中。
                        downloadLength += surplus;
                    }
                    //线程下载的开始位置
                    long downloadStartPosition = average * i;
                    AsyncTask.THREAD_POOL_EXECUTOR.execute(new MultipleThreadDownload(DownloadPath, downloadStartPosition, downloadLength, DownloadUrl, handler));
                }
                httpURLConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

}
