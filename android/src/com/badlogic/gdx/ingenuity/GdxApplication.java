package com.badlogic.gdx.ingenuity;

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
		// TODO Auto-generated method stub
		super.onCreate();
		ExceptionCaughHandler caughHandler = ExceptionCaughHandler.getInstance();
		caughHandler.init(getApplicationContext());
	}

}
