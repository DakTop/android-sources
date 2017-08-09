package com.toolproject.runtop.tool_logstatistics.statistics;

import org.apache.http.HttpEntity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ResponseHandlerInterface;

import cn.caifuqiao.tool.SystemInstance;

public class CustomAsyncHttp  {
	private static CustomAsyncHttp asyncHttp;
    private CustomAsyncHttp(){
    	
    }
	public static  CustomAsyncHttp getInstance(){
		if(asyncHttp==null){
			asyncHttp=new CustomAsyncHttp();
		}
		return asyncHttp;
	}
	
	public AsyncHttpClient postJson(String url,HttpEntity entity,ResponseHandlerInterface responseHandler){
		AsyncHttpClient asyncHttpClient = getAsyncHttpClient();
		asyncHttpClient.post(SystemInstance.getInstance().getApplication(), url, entity, "application/json", responseHandler);
		return asyncHttpClient;
	}
	
	private AsyncHttpClient postFile(){
		AsyncHttpClient asyncHttpClient = getAsyncHttpClient();
		return asyncHttpClient;
	}
	
	private AsyncHttpClient getAsyncHttpClient(){
		return new AsyncHttpClient();
	}
}
