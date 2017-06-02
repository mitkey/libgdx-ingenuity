
package com.badlogic.gdx.ingenuity.extend.android;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * @作者 Mitkey
 * @时间 2016-2-17 下午7:47:13
 * @类说明:自定义的 异常处理类 , 实现了 UncaughtExceptionHandler接口
 * @版本 xx
 */
class ExceptionCaughHandler implements UncaughtExceptionHandler {
	static final SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM dd-HH-mm-ss", Locale.getDefault());

	private static ExceptionCaughHandler handler;

	// 系统默认的UncaughtException处理类
	Thread.UncaughtExceptionHandler defaultHandler;

	GdxApplication gdxApplication;

	// 1.私有化构造方法
	private ExceptionCaughHandler() {
	}

	public static synchronized ExceptionCaughHandler getInstance() {
		if (handler == null) {
			handler = new ExceptionCaughHandler();
		}
		return handler;
	}

	public void init(GdxApplication gdxApplication) {
		this.gdxApplication = gdxApplication;
		defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// 1.获取当前程序的版本号. 版本的id
		String versioninfo = getVersionInfo();

		// 2.获取手机的硬件信息.
		String mobileInfo = getMobileInfo();

		// 3.把错误的堆栈信息 获取出来
		String errorinfo = getErrorInfo(ex);

		// 4.把所有的信息 还有信息对应的时间 提交到服务器
		try {
			JSONObject object = new JSONObject();
			object.put("time", dataFormat.format(new Date()));
			object.put("versioninfo", versioninfo);
			object.put("mobileInfo", mobileInfo);
			object.put("errorinfo", errorinfo);
			gdxApplication.handlerCaughtException(ex);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		defaultHandler.uncaughtException(thread, ex); // 不加本语句会导致ANR

		// 干掉当前的程序
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}

	/** 获取错误的信息 */
	private String getErrorInfo(Throwable arg1) {
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		arg1.printStackTrace(pw);
		pw.close();
		return writer.toString();
	}

	/** 获取手机的硬件信息 */
	private String getMobileInfo() {
		StringBuffer sb = new StringBuffer();
		// 通过反射获取系统的硬件信息
		try {
			Field[] fields = Build.class.getDeclaredFields();
			for (Field field : fields) {
				// 暴力反射 ,获取私有的信息
				field.setAccessible(true);
				String name = field.getName();
				String value = field.get(null).toString();
				sb.append(name + "=" + value);
				sb.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/** 获取手机的版本信息 */
	private String getVersionInfo() {
		try {
			PackageManager pm = gdxApplication.getApplicationContext().getPackageManager();
			PackageInfo info = pm.getPackageInfo(gdxApplication.getApplicationContext().getPackageName(), 0);
			return info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			return "版本号未知";
		}
	}

}
