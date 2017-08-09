package com.toolproject.runtop.tool_logstatistics.statistics;

import java.util.List;

import android.os.Process;
import cn.caifuqiao.database.DBService;
import cn.caifuqiao.database.model.Statistics;

/**
 * 异步删除数据库中日志
 * 
 * @author bazengliang
 *
 */
public class DeleteLog implements Runnable {
	private DeleteLogType deleteType;
	private List<Statistics> statisticsList;

	/**
	 * 删除方式
	 * 
	 * @author bazengliang
	 *
	 */
	public enum DeleteLogType {
		/**
		 * 删除全部
		 */
		delete_all,
		/**
		 * 通过列表删除
		 */
		delete_byList;
	}

	/**
	 * 构造方法
	 * 
	 * @param type
	 *            删除方式
	 */
	public DeleteLog(DeleteLogType type) {
		this.deleteType = type;
	}

	/**
	 * 构造方法
	 * 
	 * @param statisticsList
	 *            日志列表
	 * @param type
	 *            删除方式
	 */
	public DeleteLog(List<Statistics> statisticsList, DeleteLogType type) {
		this(type);
		this.statisticsList = statisticsList;
	}

	@Override
	public void run() {
		// 设置后台工作
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

		switch (deleteType) {
		case delete_all:// 删除全部
			DBService.getInstance().deleteAllStatistics();
			break;
		case delete_byList:// 根据传入日志列表删除
			if (statisticsList == null || statisticsList.size() <= 0)
				return;
			DBService.getInstance().deleteAllStatisticsByList(statisticsList);
			break;
		default:
			break;
		}
	}

}
