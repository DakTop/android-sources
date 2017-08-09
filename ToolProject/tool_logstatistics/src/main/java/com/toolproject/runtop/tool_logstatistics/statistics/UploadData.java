package com.toolproject.runtop.tool_logstatistics.statistics;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.TextHttpResponseHandler;

import cn.caifuqiao.request.StaticParametr;
import cn.oak.log.LogUtils;


public class UploadData {
	public static final int SUCCEED = 201;//上传成功
	public static final int ERROR = 202;;//上传失败
	public static final String DEFAULT_URl = "logstore/v1/services/logService/save";
	private static UploadData uploadData;

	private UploadData() {

	}

	public static UploadData getInstance() {
		if (uploadData == null) {
			uploadData = new UploadData();
		}
		return uploadData;
	}

	public interface onUploadDataListenter {
		/**
		 * 上传失败
		 */
		public void onUploadError(int statusCode, Header[] headers,
								  String responseString, Throwable throwable);

		/**
		 * 上传成功
		 */
		public void onUploadSucceed(int statusCode, Header[] headers,
									String responseString);
	}
    /**
     * 上传
     * @param array  Json数组
     */
	public void upload(JSONArray array)  {
		this.uploadLog(null, array, null, null);
	}
    /**
     * 
     * @param url 上传地址
     * @param array  Json数组
     */
	public void upload(String url, JSONArray array) {
		this.uploadLog(url, array, null, null);
	}
     
	/**
	 * 
	 * @param array  Json数组
	 * @param listenter  是否上传成功回调接口
	 */
	public void upload(JSONArray array, onUploadDataListenter listenter) {
		this.uploadLog(null, array, null, listenter);
	}

	/**
	 * 
	 * @param url 上传地址
	 * @param httpEntity 封装上传数据
	 * @param listenter  是否上传成功回调接口
	 */
	public void upload(String url, HttpEntity httpEntity,
			final onUploadDataListenter listenter) {
		this.uploadLog(url, null, httpEntity, listenter);
	}
    /**
     * 
     * @param url 上传地址
     * @param array  Json数组
     * @param listenter 是否上传成功回调接口
     */
	public void upload(String url, JSONArray array,
			final onUploadDataListenter listenter) {
		this.uploadLog(url, array, null, listenter);
	}

	private void uploadLog(String url, JSONArray array, HttpEntity httpEntity,
			final onUploadDataListenter listenter) {
		LogUtils.i("上传日志信息",array.size()+"条 "+ array.toString());
		try {
			if(httpEntity==null){
				httpEntity=new ByteArrayEntity(array.toString()
						.getBytes("UTF-8"));
			}
			if (url == null) {
				url = StaticParametr.LOG+DEFAULT_URl;
			}
			CustomAsyncHttp.getInstance().postJson(url, httpEntity,
					new TextHttpResponseHandler() {

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								String responseString) {
							// TODO Auto-generated method stub
							try {
								JSONObject jsonObject=JSONObject.parseObject(responseString);
								int status = jsonObject.getIntValue("statusCode");
								if (listenter != null) {
									switch (status) {
									case SUCCEED://成功
										listenter.onUploadSucceed(status, headers,
												responseString);
										break;
									case ERROR://失败
										listenter.onUploadError(status, headers, responseString, null);
										break;
									}
									
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								String responseString, Throwable throwable) {
							// TODO Auto-generated method stub
							if (listenter != null) {
								listenter.onUploadError(statusCode, headers,
										responseString, throwable);
							}

						}
					});
		} catch (Exception e) {
			LogUtils.e("UploadData.class", e.getMessage());
		}
	}
}
