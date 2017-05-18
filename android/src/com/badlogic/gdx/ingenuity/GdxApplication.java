package com.badlogic.gdx.ingenuity;

import com.badlogic.gdx.ingenuity.extend.AndroidCoreHelper;
import com.badlogic.gdx.ingenuity.helper.ExceptionCaughHandler;

import android.app.Application;

/**
 * @作者 Mitkey
 * @时间 2016年9月7日 下午7:30:39
 * @类说明:
 * @版本 xx
 */
public class GdxApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		GdxData.getInstance().initRuntime(new AndroidCoreHelper(getApplicationContext()));
		ExceptionCaughHandler.getInstance().init(getApplicationContext());
	}

}
