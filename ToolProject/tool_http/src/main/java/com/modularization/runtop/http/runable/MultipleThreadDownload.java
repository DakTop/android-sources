package com.modularization.runtop.http.runable;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by runTop on 2018/3/13.
 */
public class MultipleThreadDownload implements Runnable {

    //下载开始位置
    private long downloadStartPosition;
    //下载问价大小
    private long downloadLength;
    private RandomAccessFile randomAccessFile;
    private String downloadURL;
    private Handler handler;

    public MultipleThreadDownload(String filePath, long downloadStartPosition, long downloadLength, String downloadURL, Handler handler) {
        try {
            randomAccessFile = new RandomAccessFile(filePath, "rw");
            //把文件指针移到下载的开始位置。
            randomAccessFile.seek(downloadStartPosition);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.downloadStartPosition = downloadStartPosition;
        this.downloadLength = downloadLength;
        this.downloadURL = downloadURL;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            HttpURLConnection connection = (HttpURLConnection) (new URL(downloadURL)).openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            //下载结束位置
            long endP = downloadLength + downloadStartPosition;

            // bytes=0-499	    表示头500个字节
            // bytes=500-999	表示第二个500字节
            // bytes=-500	    表示最后500个字节
            // bytes=500-	    表示500字节以后的范围
            // bytes=0-0,-1	    第一个和最后一个字节
            connection.setRequestProperty("Range", "bytes=" + downloadStartPosition + "-" + endP);
            connection.connect();
            int code = connection.getResponseCode();
            if (code == 206 || code == 200) {
                byte[] buffer = new byte[1024];
                InputStream is = connection.getInputStream();
                int readLength = 0;
                int len = -1;
                while (readLength < downloadLength && (len = is.read(buffer)) != -1) {
                    randomAccessFile.write(buffer, 0, len);
                    readLength += len;
                    //
                    Message msg = new Message();
                    msg.what = 2;
                    Bundle bundle = new Bundle();
                    bundle.putLong("downloadSize", len);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
                is.close();
                randomAccessFile.close();
                connection.disconnect();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
