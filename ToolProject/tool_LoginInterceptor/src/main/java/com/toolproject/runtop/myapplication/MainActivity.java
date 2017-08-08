package com.toolproject.runtop.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
	/**
	 * 这里模拟登录标志
	 */
	public static boolean is_login = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	/*
	 * 跳转到登录页面
	 */
	public void starIntent(View v) {
		Bundle bun=new Bundle();
		bun.putString("Type", "login test");
		LoginInterceptor.interceptor(this, "com.example.logininterceptor.SecondActivity", bun);
	}

	/*
	 * 退出登录
	 */
	public void exitSign(View v) {
		is_login = false;
	}

}
