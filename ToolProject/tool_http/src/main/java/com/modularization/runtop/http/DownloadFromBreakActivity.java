package com.modularization.runtop.http;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/**
 * 多线程文件下载断点续传
 */
public class DownloadFromBreakActivity extends AppCompatActivity {
    //true表示正在下载，false表示停止下载
    private boolean downloadSwich = false;
    private Button btnDownloadSwich;
    private String[] permissionArray = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_from_break);
        btnDownloadSwich = findViewById(R.id.btn_downloadswich);
        openPermissions();
    }

    public boolean openPermissions() {
        int permissionArrayCount = permissionArray.length;
        for (int i = 0; i < permissionArrayCount; i++) {
            if (ContextCompat.checkSelfPermission(this, permissionArray[i]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissionArray, 100);
                return false;
            }
        }
        return true;
    }


    /**
     * 处理权限请求结果
     *
     * @param requestCode  请求权限时传入的请求码，用于区别是哪一次请求的
     * @param permissions  所请求的所有权限的数组
     * @param grantResults 权限授予结果，和 permissions 数组参数中的权限一一对应，元素值为两种情况，如下:
     *                     授予: PackageManager.PERMISSION_GRANTED
     *                     拒绝: PackageManager.PERMISSION_DENIED
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //授予
        if (requestCode == 100 && openPermissions()) {
        } else {//拒绝
        }
    }

    /**
     * 下载开关
     *
     * @param view
     */
    public void onDownloadSwich(View view) {
        if (downloadSwich) {
            downloadSwich = false;
            btnDownloadSwich.setText("下载");
        } else {
            downloadSwich = true;
            btnDownloadSwich.setText("停止");
        }
    }


}
