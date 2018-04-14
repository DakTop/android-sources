package com.modularization.runtop.http.model;

import java.util.Map;
import java.util.Set;

/**
 * 文件下载信息
 * Created by runTop on 2018/3/15.
 */
public class DownloadFile {
    //下载文件名称
    private String downloadUrl;
    //文件存储路径
    private String fileName;
    //文件存储名称
    private String filePath;
    //下载线程集合信息
    private Set threadSet;
    //下载文件总大小
    private int fileSize;

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public DownloadFile() {

    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Set getThreadSet() {
        return threadSet;
    }

    public void setThreadSet(Set threadSet) {
        this.threadSet = threadSet;
    }
}
