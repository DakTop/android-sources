package com.toolproject.runtop.tool_logstatistics.statistics;

import com.alibaba.fastjson.JSONArray;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import cn.oak.log.LogUtils;
/**
 * 统计服务
 * @author hao
 *
 */
public class StatisticsServers extends Service {
    private static JSONArray array;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		//前台服务  不会被系统干死
		Notification notification=new Notification();
		startForeground(10, notification);
		stopForeground(true);
	  new Thread(){
		public void run() {
			while (true) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LogUtils.i("-------------------", "启动服务");
				
			}
		};
	}.start();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if(array!=null){
			  UploadData.getInstance().upload(array);
		}
		return super.onStartCommand(intent, flags, startId);
		
		
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
   public synchronized static void startServiceAndUpload(Context context,JSONArray array){
	   StatisticsServers.array=array;
	   context.startService(new Intent(context, StatisticsServers.class));
	 
   }
   
}
