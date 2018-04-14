package com.modularization.runtop.http.help;

import android.content.Context;
import android.content.SharedPreferences;

import com.modularization.runtop.http.APPApplication;
import com.modularization.runtop.http.model.DownloadFile;

/**
 * 文件下载信息帮助类
 * Created by runTop on 2018/3/15.
 */
public class SPHelper {
    private static final String ConfigureFileName = "BreakFile";
    //
    private static final String DownloadUrl = "downloadUrl";
    private static final String FileName = "fileName";
    private static final String FilePath = "filePath";
    private static final String ThreadSet = "threadSet";


    /**
     * 存储断点下载文件信息
     *
     * @param downloadFile
     */
    public static void saveBreakDownload(DownloadFile downloadFile) {
        SharedPreferences sp = APPApplication.instance.getSharedPreferences(ConfigureFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(DownloadUrl, downloadFile.getDownloadUrl());
        editor.putString(FileName, downloadFile.getFileName());
        editor.putString(FilePath, downloadFile.getFilePath());
        editor.putStringSet(ThreadSet, downloadFile.getThreadSet());
        editor.commit();
    }

    /**
     * 获取断点下载文件信息
     */
    public static DownloadFile getBreakDownload() {
        SharedPreferences sp = APPApplication.instance.getSharedPreferences(ConfigureFileName, Context.MODE_PRIVATE);
        DownloadFile downloadFile = new DownloadFile();
        downloadFile.setDownloadUrl(sp.getString(DownloadUrl, ""));
        downloadFile.setFileName(sp.getString(FileName, ""));
        downloadFile.setFilePath(sp.getString(FilePath, ""));
        downloadFile.setThreadSet(sp.getStringSet(ThreadSet, null));
        return downloadFile;
    }
}
