package com.toolproject.runtop.tool_logstatistics.statistics;

import java.util.List;

import org.apache.http.Header;

import com.alibaba.fastjson.JSONArray;

import android.os.Handler;
import android.os.Message;
import android.os.Process;
import cn.caifuqiao.database.DBService;
import cn.caifuqiao.database.model.Statistics;
import cn.caifuqiao.statistics.UploadData.onUploadDataListenter;

/**
 * 异步查询数据库中日志
 * 
 * @author bazengliang
 *
 */
public class SubmitLog implements Runnable {

	public enum SubmitPath {
		/**
		 * 进入后台提交数据
		 */
		Backstage_Submit,
		/**
		 * 正常提交，满足一定条数后提交
		 */
		NumLimit_Submit;
	}

	/** 提交方式 */
	private SubmitPath submitpath;

	private List<Statistics> statisticsList;
	/** 查询数据库成功 */
	private final int DBQUERY_STATISTICS_RESULT = 0;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case DBQUERY_STATISTICS_RESULT:
				/* 提交数据到服务器 */
				UploadData.getInstance().upload(JSONArray.parseArray(statisticsList.toString()),
						new onUploadDataListenter() {
					@Override
					public void onUploadSucceed(int statusCode, Header[] headers, String responseString) {
						// Log.i("--------日志提交服务器成功", statusCode + "；" +
						// responseString);
						StatisticsManager.getInstance().deleteSubmitLog(statisticsList);
						statisticsList = null;
					}

					@Override
					public void onUploadError(int statusCode, Header[] headers, String responseString,
							Throwable throwable) {
						// Log.i("--------日志提交服务器失败", statusCode + "；" +
						// responseString);
					}
				});
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 构造方法
	 */
	public SubmitLog(SubmitPath submitpath) {
		this.submitpath = submitpath;
	}

	@Override
	public void run() {
		if (handler == null)
			return;
		switch (submitpath) {
		case Backstage_Submit:
			try {
				Thread.sleep(1000 * 8);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case NumLimit_Submit:
			// 设置后台工作
			Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
			break;
		default:
			break;
		}
		statisticsList = DBService.getInstance().loadAllStatistics();
		if (statisticsList != null && statisticsList.size() > 0) {
			handler.sendEmptyMessage(DBQUERY_STATISTICS_RESULT);
		}

	}
}
