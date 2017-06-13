package com.badlogic.gdx.ingenuity.extend.desktop;

import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ingenuity.extend.ICoreHelper;
import com.badlogic.gdx.ingenuity.utils.Storage;
import com.badlogic.gdx.ingenuity.utils.common.StrUtil;

/**
 * @作者 Mitkey
 * @时间 2017年3月24日 下午2:27:15
 * @类说明:
 * @版本 xx
 */
public class DesktopCoreHelper implements ICoreHelper {

	// 存储 pc 端唯一设备码的 key
	private static final String DEVICE_KEY = "deviceId";

	@Override
	public String getVersion() {
		return "0.1.1";
	}

	@Override
	public String getDeviceUniqueId() {
		String curStr = Storage.getStr(DEVICE_KEY, null);
		if (StrUtil.isNotBlank(curStr)) {
			return curStr;
		} else {
			curStr = UUID.randomUUID().toString();
			Storage.saveStr(DEVICE_KEY, curStr);
			return curStr;
		}
	}

	@Override
	public String getDeviceType() {
		return "PC";
	}

	@Override
	public void existApp() {
		Gdx.app.exit();
		System.exit(0);
		System.gc();
	}

}
