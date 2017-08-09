package com.toolproject.runtop.tool_logstatistics.statistics;

import java.util.HashMap;
import java.util.Map;

import cn.caifuqiao.statistics.StatisticsManager;

/**
 * 日志参数表
 * 
 * @author bazengliang
 *
 */
public class LogInfoCommand extends BaseLog {

	/** 事件ID */
	private int eventId;
	/** 页面ID：不同端的的同一页面需要不同的id */
	private int pageId = -1;
	/** 是否成功：1:成功2失败 */
	private int isSuccess = -1;
	/** 失败原因:如果isSuccess=1则为空？？对于特殊事件要写对应例外返回原因 */
	private String failure = "";
	/** 从那一个页面跳转过来的，如果是本页面功能就写本页面 */
	private int refererId = -1;
	/** 将要跳转到哪个页面 */
	private int targetPage = -1;
	/** 每个事件特有的参数key-value数组 */
	private Map<String, Object> parameters;
	// ---------------一下自定义参数不能提交到服务器，提交前需要删掉---------------------------
	/** 当前页面名称 (主要是给不能使用默认的方法获取到当前页面id的方法使用) */
	private String pageValue;
	/** 将要跳转到哪个页面名称 */
	private String targetPageValue;
	/** 日志类型 */
	private int logType = StatisticsManager.LOG_TYPE_DEFAULT;

	/**
	 * 构造方法 基本参数
	 * 
	 * @param isInitData
	 * @param eventId
	 * @param isSuccess
	 * @param failure
	 * @param refererId
	 * @param targetPage
	 * @param targetPageValue
	 * @param logType
	 */
	public LogInfoCommand(boolean isInitData, int eventId, int isSuccess, String failure, int refererId, int targetPage,
			int logType) {
		super(isInitData);
		this.eventId = eventId;
		this.isSuccess = isSuccess;
		this.failure = failure;
		this.refererId = refererId;
		this.targetPage = targetPage;
		this.logType = logType;
	}

	/**
	 * 页面跳转统计构造方法
	 * 
	 * @param eventId
	 * @param pageId
	 * @param pageValue
	 * @param targetPage
	 * @param targetPageValue
	 * @param logType
	 */
	public LogInfoCommand(int eventId, int pageId, String pageValue, int targetPage, String targetPageValue,
			int logType) {
		super();
		this.eventId = eventId;
		this.pageId = pageId;
		this.pageValue = pageValue;
		this.targetPage = targetPage;
		this.targetPageValue = targetPageValue;
		this.logType = logType;
	}

	/**
	 * 不需要统计跳转页的构造方法
	 * 
	 * @param eventId
	 * @param pageId
	 * @param refererId
	 * @param parameters
	 */
	public LogInfoCommand(int eventId, int pageId, int isSuccess, String failure) {
		super();
		this.eventId = eventId;
		this.pageId = pageId;
		this.isSuccess = isSuccess;
		this.failure = failure;
	}

	/**
	 * 统计带有页面信息和事件ID
	 * 
	 * @param eventId
	 * @param pageId
	 * @param logType
	 */
	public LogInfoCommand(int eventId, int pageId, String pageValue, int logType) {
		super();
		this.logType = logType;
		this.pageValue = pageValue;
		this.eventId = eventId;
		this.pageId = pageId;
	}

	/**
	 * 统计带有页面信息和事件ID
	 * 
	 * @param isInitData
	 * @param eventId
	 * @param pageId
	 * @param logType
	 * @param isInitData
	 *            是否需要初始化基本参数
	 */
	public LogInfoCommand(boolean isInitData, int eventId, int pageId, String pageValue, int logType) {
		super(isInitData);
		this.logType = logType;
		this.pageValue = pageValue;
		this.eventId = eventId;
		this.pageId = pageId;
	}

	public int getLogType() {
		return logType;
	}

	public String getTargetPageValue() {
		return targetPageValue;
	}

	public void setTargetPageValue(String targetPageValue) {
		this.targetPageValue = targetPageValue;
	}

	public void setLogType(int logType) {
		this.logType = logType;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String getPageValue() {
		return pageValue;
	}

	public void setPageValue(String pageValue) {
		this.pageValue = pageValue;
	}

	public int getTargetPage() {
		return targetPage;
	}

	public void setTargetPage(int targetPage) {
		this.targetPage = targetPage;
	}

	public int getPageId() {
		return pageId;
	}

	public void setPageId(int pageId) {
		this.pageId = pageId;
	}

	public int getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(int isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getFailure() {
		return failure;
	}

	public void setFailure(String failure) {
		this.failure = failure;
	}

	public int getRefererId() {
		return refererId;
	}

	public void setRefererId(int refererId) {
		this.refererId = refererId;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> mParameters) {
		this.parameters = new HashMap<String, Object>();
		this.parameters.putAll(mParameters);
	}

	@Override
	public void toJson() {
		// TODO Auto-generated method stub
		super.toJson();
	}

	@Override
	public String toString() {
		return "LogInfoCommand [eventId=" + eventId + ", pageId=" + pageId + ", isSuccess=" + isSuccess + ", failure="
				+ failure + ", refererId=" + refererId + ", parameters=" + parameters + "]";
	}

}
