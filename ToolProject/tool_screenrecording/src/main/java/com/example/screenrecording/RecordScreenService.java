package com.example.screenrecording;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.display.DisplayManager;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * 屏幕录制服务
 * Created by runTop on 2018/1/10.
 */
public class RecordScreenService extends Service {
    private final int notificationID = 1;
    private RecordBinder recordBinder;
    private MediaProjectionManager mediaProjectionManager;
    private DisplayMetrics metrics = new DisplayMetrics();
    private NotificationCompat.Builder mBuilder;
    private Notification notification;
    private NotificationManager mNotificationManager;
    private final String PendingIntent_Tag = "PendingIntent_Tag";
    private final int PendingIntent_Val_Start = 1;
    private final int PendingIntent_Val_Close = 2;
    private OnServiceRequestCallback onServiceRequestCallback;

    @Override
    public void onCreate() {
        super.onCreate();
        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        recordBinder = new RecordBinder();
        mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);

    }


    /**
     * 初始化通知
     */
    public void initNotification() {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
        //开始按钮
        Intent intent = new Intent(this, RecordScreenService.class);
        intent.putExtra(PendingIntent_Tag, PendingIntent_Val_Start);
        PendingIntent switchBtn = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btn_switch, switchBtn);
        //关闭按钮
        Intent intentClose = new Intent(this, RecordScreenService.class);
        intentClose.putExtra(PendingIntent_Tag, PendingIntent_Val_Close);
        PendingIntent closeBtn = PendingIntent.getService(this, 2, intentClose, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.tv_close, closeBtn);

        //第一步：实例化通知构造器Notification.Builder
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setTicker("打开录屏软件了") //通知首次出现在通知栏，带上升动画效果的
                .setContent(remoteViews)//设置内容布局
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_MAX) //设置该通知优先级
                .setAutoCancel(false)//设置这个标志为true当用户单击面板就可以让通知将自动取消
                .setOngoing(true)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_LIGHTS)//向通知添加声音、闪灯和振动效果，可以组合Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音requires VIBRATE permission
                .setSmallIcon(R.mipmap.ic_launcher).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));//设置通知小ICON

        notification = mBuilder.build();
        //设置为前台服务，这里就已经发送通知了，省去了通过获取NotificationManager服务执行 mNotificationManager.notify(notificationID, notification);发送通知了。
        startForeground(notificationID, notification);
    }

    /**
     * 屏幕录制操作
     */
    class RecordBinder extends Binder {
        //录制屏幕类
        private MediaProjection mediaProjection;
        //进行数据编码工作
        private MediaRecorder mediaRecorder;
        //输出文件名称
        private String videoFileName = "default";
        //文件保存路径
        private String videoFilePath = "";
        private boolean isRecording;

        public RecordBinder() {
            mediaRecorder = new MediaRecorder();
            videoFilePath = getsaveDirectory();
        }

        /**
         * 初始化设置编码器的参数信息
         *
         * @param outPath
         */
        private void initRecorder(String outPath) {
            //设置音频源
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置视频源：Surface和Camera两种
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            //设置视频输出格式
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            //设置视频输出路径
            mediaRecorder.setOutputFile(outPath);
            //设置视频尺寸大小
            mediaRecorder.setVideoSize(metrics.widthPixels, metrics.heightPixels);
            //设置视频编码格式
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            //设置音频编码格式
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            //设置视频编码的码率
            mediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1024);
            //设置视频编码的帧率
            mediaRecorder.setVideoFrameRate(30);

        }

        /**
         * 开始录制
         *
         * @param resultCode
         * @param data
         */
        public void startRecord(int resultCode, Intent data) {
            if (isRecording) {
                return;
            }
            isRecording = true;
            mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
            initRecorder(videoFilePath + videoFileName + ".mp4");
            try {
                // 调用prepare方法做准备，不然后续再调用getSurface会报错
                mediaRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            /**
             *  将图像渲染到 Surface中
             * 第一个参数：虚拟画面名称
             * 第二个参数：虚拟画面的宽度
             * 第三个参数：虚拟画面的高度
             * 第四个参数：虚拟画面的标志
             * 第五个参数：虚拟画面输出的Surface
             * 第六个参数：虚拟画面回调接口
             */
            mediaProjection.createVirtualDisplay("MyVideo", metrics.widthPixels, metrics.heightPixels, metrics.densityDpi,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mediaRecorder.getSurface(), null, null);
            //更新通知栏状态
            notification.contentView.setTextViewText(R.id.tv_title, "正在录制...");
            notification.contentView.setTextViewText(R.id.btn_switch, "暂停");
            mNotificationManager.notify(notificationID, notification);
            Toast.makeText(RecordScreenService.this, "开始录屏", Toast.LENGTH_SHORT).show();
            mediaRecorder.start();
        }

        /**
         * 停止录制
         */
        public void stopRecord() {
            if (!isRecording) {
                return;
            }
            isRecording = false;
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaProjection.stop();
            notification.contentView.setTextViewText(R.id.tv_title, "录屏准备");
            notification.contentView.setTextViewText(R.id.btn_switch, "开始");
            mNotificationManager.notify(notificationID, notification);
            Toast.makeText(RecordScreenService.this, "停止录屏", Toast.LENGTH_SHORT).show();
            if (onServiceRequestCallback != null) {
                onServiceRequestCallback.onRecorderStop();
            }
        }

        private String getsaveDirectory() {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "ScreenVideo" + "/";

                File file = new File(rootDir);
                if (!file.exists()) {
                    if (!file.mkdirs()) {
                        return null;
                    }
                }
                return rootDir;
            } else {
                return null;
            }
        }

        public String getVideoFilePath() {
            return videoFilePath;
        }

        public void setVideoFileName(String videoFileName) {
            this.videoFileName = videoFileName;
        }

        public boolean isRecording() {
            return isRecording;
        }

        public void setOnServiceRequestCallback(OnServiceRequestCallback mOnServiceRequestCallback) {
            onServiceRequestCallback = mOnServiceRequestCallback;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return recordBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mNotificationManager == null) {
            initNotification();
        } else {
            //设置为前台服务，这里就已经发送通知了，省去了通过获取NotificationManager服务执行 mNotificationManager.notify(notificationID, notification);发送通知了。
            startForeground(notificationID, notification);
        }
        if (intent != null) {
            int code = intent.getIntExtra(PendingIntent_Tag, 0);
            switch (code) {
                case PendingIntent_Val_Start://开始or停止
                    if (recordBinder.isRecording()) {
                        recordBinder.stopRecord();
                    } else {
                        if (onServiceRequestCallback != null) {
                            onServiceRequestCallback.onRecorderStart(mediaProjectionManager.createScreenCaptureIntent());
                        }
                    }
                    break;

                case PendingIntent_Val_Close://关闭
                    stopForeground(true);
                    break;

            }
        }

        return START_STICKY;
    }

    public interface OnServiceRequestCallback {
        boolean onRecorderStart(Intent intent);

        void onRecorderStop();
    }

}
