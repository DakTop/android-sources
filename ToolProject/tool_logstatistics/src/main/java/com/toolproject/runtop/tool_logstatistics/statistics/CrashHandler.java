package com.toolproject.runtop.tool_logstatistics.statistics;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import cn.caifuqiao.tool.HelpDate;
import cn.caifuqiao.tool.SystemInstance;
import cn.oak.log.LogUtils;

/**
 * 捕获全局异常
 * 
 * @author hao
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {
	private static CrashHandler instance;
	private Context context;
	private UncaughtExceptionHandler mDefaultHandler;

	public static CrashHandler getInstance() {
		if (instance == null) {
			instance = new CrashHandler();
		}
		return instance;
	}

	public void init(Context context) {
		this.context = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, final Throwable ex) {

		try {
			LogUtils.e("caifuqiao", "app crash!", ex);
			saveErrorLog(ex, true);
			SystemInstance.getInstance().exitApp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 保存错误日志
	 * 
	 * @param ex
	 * @param isSaveStackTrace
	 *            是否保堆栈信息
	 */
	private void saveErrorLog(Throwable ex, boolean isSaveStackTrace) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("时间："
				+ HelpDate.stampToTime(System.currentTimeMillis() + ""));
		buffer.append(" 异常信息：" + ex.getMessage());
		if (isSaveStackTrace) {
			buffer.append("堆栈信息：" + getErrorInfo(ex));
		}
		LogUtils.e("异常日志信息", buffer.toString());
		StatisticsLog.getIntance().addExceptionLog(46)
				.putMap("errInfo", buffer.toString()).SubmitData();
		// StatisticsLog.getIntance().addLog(46).putMap("errInfo",
		// buffer.toString()).SubmitData();

	}

	private String getErrorInfo(Throwable arg1) {
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		arg1.printStackTrace(pw);
		pw.close();
		String error = writer.toString();
		return error;
	}

}
