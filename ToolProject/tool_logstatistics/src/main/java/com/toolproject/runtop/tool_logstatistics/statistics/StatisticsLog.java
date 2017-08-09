package com.toolproject.runtop.tool_logstatistics.statistics;

import java.util.HashMap;
import java.util.Map;

import cn.caifuqiao.mode.LogInfoCommand;
import cn.caifuqiao.request.ParameterTimelyManager;
import cn.caifuqiao.tool.HelpString;
import cn.caifuqiao.tool.HelpUtil;
import cn.oak.log.LogUtils;

/**
 * 统计埋点类
 * 
 * @author bazengliang
 *
 */
public class StatisticsLog {
	/** 统计埋点实例 */
	private static StatisticsLog intance;
	/** 缓存动态参数 */
	private Map<String, Object> mapParam;
	/** 日志实体类 */
	private LogInfoCommand logInfoCommand;

	private StatisticsLog() {
		mapParam = new HashMap<String, Object>();
	}

	/** 获取实例（单例模式） */
	public static StatisticsLog getIntance() {
		if (intance == null) {
			synchronized (StatisticsLog.class) {
				if (intance == null) {
					intance = new StatisticsLog();
				}
			}
		}
		return intance;
	}

	/**
	 * 添加普通日志
	 * 
	 * @param eventId
	 *            事件ID
	 * @param pageId
	 *            当前页面id
	 */
	private StatisticsLog addLog(int eventId, int pageId, String pageValue, int logType) {
		logInfoCommand = null;
		logInfoCommand = new LogInfoCommand(eventId, pageId, pageValue, logType);
		if (mapParam != null)
			mapParam.clear();
		return intance;
	}

	/**
	 * 添加普通日志不需要传递当前页面id（底层已处理）
	 * 
	 * @param eventId
	 *            事件ID
	 */
	public StatisticsLog addLogActivityLoading(int eventId) {
		return addLog(eventId, -1, null, StatisticsManager.LOG_TYPE_ACTIVITYLOADING);
	}

	/**
	 * 添加普通日志需要传递当前页面id,例如：Fragment
	 * 
	 * @param eventId
	 *            事件ID
	 * @param pageClass
	 *            当前页面Class
	 */
	public StatisticsLog addLogActivityLoading(int eventId, Class<?> pageClass) {
		return addLog(eventId, -1, pageClass != null ? pageClass.getSimpleName() : null,
				StatisticsManager.LOG_TYPE_ACTIVITYLOADING);
	}

	/**
	 * 添加普通日志不需要传递当前页面id（底层已处理）
	 * 
	 * @param eventId
	 *            事件ID
	 */
	public StatisticsLog addLog(int eventId) {
		return addLog(eventId, -1, null, StatisticsManager.LOG_TYPE_DEFAULT);
	}

	/**
	 * 添加普通日志需要传递当前页面id
	 * 
	 * @param eventId
	 *            事件ID
	 */
	public StatisticsLog addLog(int eventId, Class<?> pageClass) {
		return addLog(eventId, -1, pageClass != null ? pageClass.getSimpleName() : null,
				StatisticsManager.LOG_TYPE_DEFAULT);
	}

	/**
	 * 添加请求结果日志自动填充当前页面
	 * 
	 * @param eventId
	 *            事件ID
	 * @param isSuccess
	 *            请求是否成功(1:成功2失败)
	 * @param failure
	 *            失败原因
	 */
	public StatisticsLog addLog(int eventId, int isSuccess, String failure) {
		logInfoCommand = null;
		logInfoCommand = new LogInfoCommand(eventId, -1, isSuccess, failure);
		if (mapParam != null)
			mapParam.clear();
		return intance;
	}

	/**
	 * 添加app启动日志
	 * 
	 * @param startApp
	 *            启动方式 1.主动点击;2后台唤起;3消息唤起，4被其它应用调用
	 */
	public StatisticsLog addAppStartLog(int startApp) {
		logInfoCommand = null;
		logInfoCommand = new LogInfoCommand(1, -1, null, StatisticsManager.LOG_TYPE_DEFAULT);
		if (mapParam != null)
			mapParam.clear();
		mapParam.put("isLogin", HelpUtil.getLoginStateInt());
		mapParam.put("startApp", startApp);
		return intance;
	}

	/**
	 * 添加应用崩溃异常日志
	 * 
	 * @param eventId
	 *            事件ID
	 */
	public StatisticsLog addExceptionLog(int eventId) {
		logInfoCommand = null;
		logInfoCommand = new LogInfoCommand(true, eventId, -1, null, StatisticsManager.LOG_TYPE_ACTIVITYEXCEPTION);
		if (mapParam != null)
			mapParam.clear();
		return intance;
	}

	/**
	 * 添加页面跳转日志
	 * 
	 * @param eventId
	 *            事件ID
	 * @param pageId
	 *            当前页面id
	 * @param pageClassName
	 *            当前页面页面名称
	 * @param targetPage
	 *            将要跳转到那个页面id
	 * @param targetPageClassName
	 *            将要跳转到哪个页面名称
	 * @param logType
	 *            跳转类型
	 */
	private StatisticsLog addJumpPage(int eventId, int pageId, String pageClassName, int targetPage,
			String targetPageClassName, int logType) {
		logInfoCommand = null;
		logInfoCommand = new LogInfoCommand(eventId, pageId, pageClassName, targetPage, targetPageClassName, logType);
		if (HelpString.isEmpty(logInfoCommand.getPageValue())) {
			// 当前页面
			logInfoCommand.setPageValue(PublicParameter.getPublicParameter().getCurrentActivity());
		}
		if (mapParam != null)
			mapParam.clear();
		return intance;
	}

	/**
	 * 添加页面跳转日志，跳出事件，指跳出当前页面，需要记录跳转到页面的id
	 * 
	 * @param eventId
	 *            事件ID
	 * @param targetPageClass
	 *            将要跳转到哪个页面Class
	 */
	public StatisticsLog addJumpPage(int eventId, Class<?> targetPageClass) {
		return addJumpPage(eventId, -1, null, -1, targetPageClass == null ? "" : targetPageClass.getSimpleName(),
				StatisticsManager.LOG_TYPE_ACTIVITYJUMP);
	}

	/**
	 * 添加页面跳转日志，跳出事件，指跳出当前页面，需要记录跳转到页面的id
	 * 
	 * @param eventId
	 *            事件ID
	 * @param targetPageClass
	 *            将要跳转到哪个页面Class
	 * @param pageClass
	 *            当前页面Class,假如为null则传递当前activity的名称
	 */
	public StatisticsLog addJumpPage(int eventId, Class<?> pageClass, Class<?> targetPageClass) {
		return addJumpPage(eventId, -1, pageClass == null ? "" : pageClass.getSimpleName(), -1,
				targetPageClass == null ? "" : targetPageClass.getSimpleName(),
				StatisticsManager.LOG_TYPE_ACTIVITYJUMP);
	}

	/**
	 * 添加页面跳转日志，跳出事件，指跳出当前页面，需要记录跳转到页面的id
	 * 
	 * @param eventId
	 *            事件ID
	 * @param targetPageName
	 *            将要跳转到哪个页面页面名称
	 * @param pageClass
	 *            当前页面Class
	 */
	public StatisticsLog addJumpPage(int eventId, Class<?> pageClass, String targetPageName) {
		return addJumpPage(eventId, -1, pageClass == null ? "" : pageClass.getSimpleName(), -1, targetPageName,
				StatisticsManager.LOG_TYPE_ACTIVITYJUMP);
	}

	/**
	 * 添加页面跳转日志，跳出事件，指跳出当前页面，需要记录跳转到页面的id
	 * 
	 * @param eventId
	 *            事件ID
	 * @param targetPageClass
	 *            将要跳转到哪个页面页面名称
	 */
	public StatisticsLog addJumpPage(int eventId, String targetPage) {
		return addJumpPage(eventId, -1, null, -1, targetPage, StatisticsManager.LOG_TYPE_ACTIVITYJUMP);
	}

	/**
	 * 给mapParam赋值
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public StatisticsLog putMap(String key, Object value) {
		if (mapParam != null)
			mapParam.put(key, value);
		return intance;
	}

	/**
	 * 提交数据
	 */
	public void SubmitData() {
		logInfoCommand.setParameters(mapParam);
		StatisticsManager.getInstance().addLog(logInfoCommand);
	}

	/**
	 * WebView的URL拼接数据参数
	 * 
	 * @param baseUrl
	 *            WebView的链接
	 * @return 拼接好基本参数的链接
	 */
	public static String splicingWebUrl(String baseUrl) {
		if (HelpString.isEmpty(baseUrl))
			return baseUrl;
		StringBuffer strUrl = new StringBuffer(baseUrl);
		if (baseUrl.contains("?")) {
			strUrl.append("&");
		} else {
			strUrl.append("?");
		}
		strUrl.append("faId=").append(ParameterTimelyManager.getFaId()).append("&platform=3").append("&device_name=")
				.append(ParameterTimelyManager.getDeviceInfo()).append("&uuid=")
				.append(ParameterTimelyManager.getUUID()).append("&transform_from=1").append("&version="+ParameterTimelyManager.getVersion())
				.append("&loginToken="+ParameterTimelyManager.getFatoken());
		LogUtils.i("webUrl", strUrl.toString());
		return strUrl.toString();
	}

}
