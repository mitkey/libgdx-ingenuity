package com.badlogic.gdx.ingenuity.extend.android;

import com.badlogic.gdx.ingenuity.GdxData;

import android.app.Application;
import android.util.Log;

/**
 * @作者 mitkey
 * @时间 2017年6月2日 下午6:37:14
 * @类说明 GdxApplication.java <br/>
 * @版本 0.0.1
 */
public class GdxApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		GdxData.getInstance().initRuntime(new AndroidCoreHelper(getApplicationContext()));
		ExceptionCaughHandler.getInstance().init(this);
	}

	public void handlerCaughtException(Throwable ex) {
		// 自己处理数据，发送到 服务器、保存到本地、发邮件通知等...
		Log.e("捕获到异常", "################## UncaughtException ################## " + ex.getMessage());
	}

}
