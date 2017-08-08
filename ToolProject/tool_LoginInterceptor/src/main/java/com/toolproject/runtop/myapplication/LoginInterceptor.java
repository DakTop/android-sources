package com.toolproject.runtop.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * 登录拦截器
 * 
 * @author bazengliang
 * 
 */
public class LoginInterceptor {
	public static final String mINVOKER = "INTERCEPTOR_INVOKER";

	/**
	 * 拦截处理
	 * 
	 * @param ctx
	 *            当前activity的上下文
	 * @param target
	 *            目标activity的target
	 * @param params
	 *            目标activity所需要的参数
	 * @param intent
	 *            目标activity
	 * 
	 */
	public static void interceptor(Context ctx, String target, Bundle bundle,
			Intent intent) {
		if (TextUtils.isEmpty(target))
			throw new RuntimeException("No target activity.");
		IntentInvoker invoker = new IntentInvoker(target, bundle);
		if (getLogin()) {
			invoker.invoke(ctx);
		} else {
			if (intent == null) {
				intent = new Intent(ctx, LoginActivity.class);
			}
			login(ctx, invoker, intent);
		}
	}

	/**
	 * 登录拦截
	 * 
	 * @param ctx
	 *            当前activity的上下文
	 * @param target
	 *            目标activity的target
	 * @param params
	 *            目标activity所需要的参数
	 */
	public static void interceptor(Context ctx, String target, Bundle bundle) {
		interceptor(ctx, target, bundle, null);
	}

	private static boolean getLogin() {
		return MainActivity.is_login;
	}

	private static void login(Context context, IntentInvoker invoker,
			Intent intent) {
		intent.putExtra(mINVOKER, invoker);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
}
