package com.toolproject.runtop.tool_logstatistics.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import android.os.Handler;
import android.os.Message;
import cn.caifuqiao.database.DBService;
import cn.caifuqiao.database.DBService.OnDataBasseLogIncrease;
import cn.caifuqiao.database.model.Statistics;
import cn.caifuqiao.mode.LogInfoCommand;
import cn.caifuqiao.statistics.DeleteLog.DeleteLogType;
import cn.caifuqiao.statistics.SubmitLog.SubmitPath;
import cn.caifuqiao.tool.HelpUtil;

/**
 * 统计管理
 * 
 * @author bazengliang
 *
 */
public class StatisticsManager implements OnDataBasseLogIncrease {

	/** 线程池 */
	private ExecutorService mExecutorService;
	/** 统计管理实例 */
	private static StatisticsManager managerIntance;
	/** Activity对应的key-value */
	private Map<String, Integer> activityNameVal;
	/** Umeng统计Activity对应的名称 */
	private static Map<String, String> activityForName;
	/** 默认日志类型 */
	public static final int LOG_TYPE_DEFAULT = 0;
	/** 页面跳转类型 */
	public static final int LOG_TYPE_ACTIVITYJUMP = 1;
	/** 页面加载类型 */
	public static final int LOG_TYPE_ACTIVITYLOADING = 2;
	/** 崩溃异常日志类型 */
	public static final int LOG_TYPE_ACTIVITYEXCEPTION = 3;
	/** 数据库日志数目 */
	private long DATABASE_LOG_NUMLIMIT = 0;
	/** 数据库日志数目 */
	private final long DATABASE_LOG_NUM = 25;
	/** 日志提交 */
	private final int LOG_SUBMIT = 0;
	/** 日志开关 */
	private final boolean logSwitch = false;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case LOG_SUBMIT:
				// 提交日志
				submitAllLog(SubmitPath.NumLimit_Submit);
				break;
			default:
				break;
			}
		}
	};

	private StatisticsManager() {
		mExecutorService = Executors.newFixedThreadPool(6);
		// 解析LogPageId文件中Activity名称的所对应的value
		mExecutorService.execute(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				activityNameVal = JSONObject.parseObject(HelpUtil.getJsonFile("LogPageId.json").toString(), Map.class);
				activityForName = JSONObject.parseObject(HelpUtil.getJsonFile("LogPageName.json").toString(),
						Map.class);

			}
		});
		// 注册数据库日志插入监听
		DBService.getInstance().setDatabasselogincrease(this);
	}

	public static StatisticsManager getInstance() {
		if (managerIntance == null) {
			synchronized (StatisticsManager.class) {
				if (managerIntance == null) {
					managerIntance = new StatisticsManager();
				}
			}
		}
		return managerIntance;
	}

	/**
	 * 获取Umeng统计Activity对应的名称
	 * 
	 * @return
	 */
	public static String getUmengActivityForName(String activityName) {
		return activityForName.get(activityName);
	}

	/**
	 * 添加一条日志
	 */
	public void addLog(LogInfoCommand entity) {
		if (entity == null)
			return;
		switch (entity.getLogType()) {
		case LOG_TYPE_ACTIVITYEXCEPTION:// 崩溃异常日志
			DBService.getInstance().addStatistics(new Statistics(JSON.toJSONString(entity)));
			break;
		default:
			mExecutorService.execute(new WriterLog(entity));
			break;
		}
	}

	/**
	 * 提交所有日志到服务器
	 */
	public synchronized void submitAllLog(SubmitPath submitpath) {
		if (logSwitch)
			mExecutorService.execute(new SubmitLog(submitpath));
	}

	/**
	 * 删除提交的日志
	 */
	public void deleteSubmitLog(List<Statistics> statisticsList) {
		mExecutorService.execute(new DeleteLog(statisticsList, DeleteLogType.delete_byList));
	}

	/**
	 * 获取activity集合
	 */
	public Map<String, Integer> getActivityMap() {
		if (activityNameVal == null)
			return new HashMap<String, Integer>();
		return activityNameVal;
	}

	/**
	 * 数据库添加日志回调方法
	 * 
	 * @param num
	 *            插入数据后数据库日志条数
	 */
	@Override
	public void dataBaseLogNum(long num) {
		setDatabaseLognumLimit(num);
	}

	/**
	 * 更新数据库日志数目 ，并提交数据库
	 * 
	 * @param num
	 *            插入数据后数据库日志条数
	 */
	private synchronized void setDatabaseLognumLimit(long num) {
		if (num >= DATABASE_LOG_NUMLIMIT) {// 如果数据库日志满足提交条件，则上传日志到数据库
			DATABASE_LOG_NUMLIMIT = DATABASE_LOG_NUM + num;
			handler.sendEmptyMessage(LOG_SUBMIT);
		}
	}

}
