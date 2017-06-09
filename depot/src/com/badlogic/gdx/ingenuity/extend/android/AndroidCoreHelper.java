package com.badlogic.gdx.ingenuity.extend.android;

import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ingenuity.extend.ICoreHelper;

import android.content.Context;

/**
 * @作者 Mitkey
 * @时间 2017年3月24日 下午2:41:42
 * @类说明:
 * @版本 xx
 */
class AndroidCoreHelper implements ICoreHelper {

	private Context ctx;

	public AndroidCoreHelper(Context ctx) {
		this.ctx = ctx;
	}

	@Override
	public String getVersion() {
		return DeviceUtils.getVersionName(ctx);
	}

	@Override
	public String getDeviceUniqueId() {
		String uniqueId;
		try {
			uniqueId = DeviceUtils.uniqueId(ctx);
		} catch (Exception e) {
			e.printStackTrace();
			uniqueId = UUID.randomUUID().toString();
		}
		return uniqueId;
	}

	@Override
	public String getDeviceType() {
		return DeviceUtils.getModel();
	}

	@Override
	public void existApp() {
		Gdx.app.exit();
		System.exit(0);
		android.os.Process.killProcess(android.os.Process.myPid());
		System.gc();
	}

}
