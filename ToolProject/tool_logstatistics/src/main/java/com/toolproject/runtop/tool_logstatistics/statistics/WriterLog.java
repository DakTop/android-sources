package com.toolproject.runtop.tool_logstatistics.statistics;

import com.alibaba.fastjson.JSON;

import android.os.Process;
import cn.caifuqiao.database.DBService;
import cn.caifuqiao.database.model.Statistics;
import cn.caifuqiao.mode.LogInfoCommand;
import cn.caifuqiao.tool.HelpString;

/**
 * 向数据库中异步添加一日志条数据
 * 
 * @author bazengliang
 *
 */
public class WriterLog implements Runnable {
	private LogInfoCommand logInfoCommand;

	public WriterLog(LogInfoCommand logInfoCommand) {
		this.logInfoCommand = logInfoCommand;
	}

	@Override
	public void run() {
		// 设置后台工作
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
		// 设置基本数据
		logInfoCommand.setChangingValue();
		logInfoCommand.setFixValue();
		// 当前页面
		if (!HelpString.isEmpty(logInfoCommand.getPageValue())) {// 判断是否是手动传入当前页面
			// 根据传入页面名称获取当前页面
			logInfoCommand.setPageId(HelpString.StrToInteger(
					String.valueOf(StatisticsManager.getInstance().getActivityMap().get(logInfoCommand.getPageValue())),
					-1));
		} else if (logInfoCommand.getPageId() <= 0) {
			// 根据当前Activity队列获取当前页面
			logInfoCommand
					.setPageId(
							HelpString
									.StrToInteger(
											String.valueOf(StatisticsManager.getInstance().getActivityMap()
													.get(PublicParameter.getPublicParameter().getCurrentActivity())),
									-1));
		}
		// 判断日志类型
		switch (logInfoCommand.getLogType()) {

		case StatisticsManager.LOG_TYPE_ACTIVITYJUMP:// activity页面跳转,所有获取当前页面pageId的值在StatisticsLog层已经实现，假如在异步中处理pageId会取到目标页的值。
			// 将要跳转的页面
			logInfoCommand.setTargetPage(HelpString.StrToInteger(
					String.valueOf(
							StatisticsManager.getInstance().getActivityMap().get(logInfoCommand.getTargetPageValue())),
					-1));
			break;

		case StatisticsManager.LOG_TYPE_ACTIVITYLOADING:// 页面加载
			// 当前页面的上一个页面
			logInfoCommand
					.setRefererId(
							HelpString
									.StrToInteger(
											String.valueOf(StatisticsManager.getInstance().getActivityMap()
													.get(PublicParameter.getPublicParameter().getBeforeActivity())),
									-1));
			break;
		case StatisticsManager.LOG_TYPE_DEFAULT:// 默认值
			break;
		default:

			break;
		}
		// 插入到数据库
		DBService.getInstance().addStatistics(new Statistics(JSON.toJSONString(logInfoCommand)));
	}

}
