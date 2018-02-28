package com.example.screenrecording;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 屏幕录制
 */
public class MainActivity extends AppCompatActivity implements RecordScreenService.OnServiceRequestCallback {
    //录制权限
    private String[] permissionArray = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final int permissionRequestCode = 1001;
    private RecordScreenService.RecordBinder recordBinder;
    //    private boolean isOpenJurisdiction = false;
    private EditText etFileName;
    private TextView tvFileNameShow;
    private String fileName = "";
    private String oldfileName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //录制文件名称
        etFileName = (EditText) findViewById(R.id.et_filename);
        tvFileNameShow = (TextView) findViewById(R.id.tv_filename_show);
        openPermissions();
        bindScreenService();
    }

    /**
     * 绑定服务
     */
    private void bindScreenService() {
        Intent intent = new Intent(this, RecordScreenService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    /**
     * 初始化录制
     *
     * @param view
     */
    public void launchScreenTran(View view) {
        if (recordBinder != null && recordBinder.isRecording()) {
            Toast.makeText(this, "正在录屏", Toast.LENGTH_SHORT).show();
            return;
        }
        if (recordBinder == null) {
            bindScreenService();
            Toast.makeText(this, "正在绑定服务...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(fileName)) {
            Toast.makeText(this, "文件名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, RecordScreenService.class);
        this.startService(intent);
        Toast.makeText(this, "请在通知栏中开启或暂停录屏", Toast.LENGTH_LONG).show();
    }

    /**
     * 确认文件名称
     *
     * @param view
     */
    public void setFileName(View view) {
        if (recordBinder == null) {
            bindScreenService();
            Toast.makeText(this, "正在绑定服务获取文件路径...", Toast.LENGTH_SHORT).show();
            return;
        }
        fileName = etFileName.getText().toString().trim();
        if (TextUtils.isEmpty(fileName)) {
            Toast.makeText(this, "文件名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        tvFileNameShow.setText("输出文件位置：" + recordBinder.getVideoFilePath() + fileName + ".mp4");
        recordBinder.setVideoFileName(fileName);

    }

    /**
     * 打开录制权限
     */
    public boolean openPermissions() {
        int permissionArrayCount = permissionArray.length;
        for (int i = 0; i < permissionArrayCount; i++) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permissionArray[i]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissionArray, permissionRequestCode);
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
        if (requestCode == permissionRequestCode && openPermissions()) {
        } else {//拒绝
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            moveTaskToBack(true);
            recordBinder.startRecord(resultCode, data);
        } else {
            Toast.makeText(this, "录制失败！", Toast.LENGTH_SHORT).show();
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(MainActivity.this, "服务绑定成功！", Toast.LENGTH_SHORT).show();
            recordBinder = (RecordScreenService.RecordBinder) service;
            recordBinder.setOnServiceRequestCallback(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public boolean onRecorderStart(Intent intent) {
        boolean result = fileName.equals(oldfileName);
        if (result) {
            Toast.makeText(this, "输出文件名称不能重复！", Toast.LENGTH_SHORT).show();
        } else {
            startActivityForResult(intent, 100);
        }
        return result;
    }

    @Override
    public void onRecorderStop() {
        oldfileName = fileName;
    }

}
