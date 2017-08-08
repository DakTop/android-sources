package com.toolproject.runtop.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 拦截器载体
 * 
 * @author bazengliang
 * 
 */
public class IntentInvoker implements Parcelable {
	public String mTargetAction;
	public Bundle mbundle;

	public IntentInvoker(String target, Bundle bundle) {
		mTargetAction = target;
		mbundle = bundle;
	}

	/**
	 * 目标activity
	 * 
	 * @param ctx
	 */
	public void invoke(Context ctx) {
		Intent intent = new Intent(mTargetAction);
		intent.putExtras(mbundle);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ctx.startActivity(intent);
	}

	public IntentInvoker(Parcel parcel) {
		// 按变量定义的顺序读取
		mTargetAction = parcel.readString();
		mbundle = parcel.readParcelable(Bundle.class.getClassLoader());
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		// 按变量定义的顺序写入
		parcel.writeString(mTargetAction);
		parcel.writeParcelable(mbundle, flags);
	}

	public static final Creator<IntentInvoker> CREATOR = new Creator<IntentInvoker>() {

		@Override
		public IntentInvoker createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new IntentInvoker(source);
		}

		@Override
		public IntentInvoker[] newArray(int arg0) {
			// TODO Auto-generated method stub
			return new IntentInvoker[arg0];
		}
	};
}
