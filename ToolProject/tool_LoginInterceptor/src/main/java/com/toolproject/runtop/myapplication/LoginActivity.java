package com.toolproject.runtop.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends Activity {
	private IntentInvoker invoker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	/*
	 * 点击登录
	 */
	public void login(View v) {
		invoker = (IntentInvoker) getIntent().getParcelableExtra(
				LoginInterceptor.mINVOKER);
		invoker.invoke(this);
		MainActivity.is_login = true;
		finish();
	}
}
