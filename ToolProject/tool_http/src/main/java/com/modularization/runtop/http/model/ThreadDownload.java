package com.modularization.runtop.http.model;

/**
 * 下载线程信息
 * Created by runTop on 2018/3/16.
 */
public class ThreadDownload {
    //线程名称
    private String threadName;
    //线程下载大小
    private int downloadDataSize;
    //线程下载进度
    private int downloadProgress;

    public ThreadDownload(String threadName, int downloadDataSize, int downloadProgress) {
        this.threadName = threadName;
        this.downloadDataSize = downloadDataSize;
        this.downloadProgress = downloadProgress;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public int getDownloadDataSize() {
        return downloadDataSize;
    }

    public void setDownloadDataSize(int downloadDataSize) {
        this.downloadDataSize = downloadDataSize;
    }

    public int getDownloadProgress() {
        return downloadProgress;
    }

    public void setDownloadProgress(int downloadProgress) {
        this.downloadProgress = downloadProgress;
    }
}
