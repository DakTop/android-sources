package com.toolproject.runtop.tool_logstatistics.statistics;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * 手机Log日志
 * @author hao
 *
 */
public class LogCatCollect {

	 public static StringBuffer collectLog() throws Exception{
		
		    //执行命令
		 new Thread(){
			 public void run() {
				 try {
					StringBuffer buffer=new StringBuffer();
					 ArrayList<String> cmdLine=new ArrayList<String>();
					 cmdLine.add("logcat");
					 cmdLine.add("-s");
					 cmdLine.add("*:E");
					 
					 String [] strs=new String[cmdLine.size()];
					 while (true ){
						 Process process = Runtime.getRuntime().exec(cmdLine.toArray(strs));
							InputStream is = process.getInputStream();
							BufferedReader br = new BufferedReader(new InputStreamReader(is));
							String str = null;
							while((str = br.readLine())!=null){
								buffer.append(str);
								buffer.append("\n");
							}
							buffer.toString();
							sleep(5000);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				   
			 };
		 }.start();
			
			//LogUtils.i("日志", buffer.toString());
		return null;
		 
	 }


}
