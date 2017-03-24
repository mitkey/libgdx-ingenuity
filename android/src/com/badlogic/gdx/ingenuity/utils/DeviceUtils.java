package com.badlogic.gdx.ingenuity.utils;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import com.badlogic.gdx.ingenuity.utils.common.StrUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/8/1
 *     desc  : 设备相关工具类
 * </pre>
 */
public class DeviceUtils {

	private static final String prefs_file = "device_id.xml";
	private static final String key = "deviceId";
	private static String device_id = null;
	private DeviceUtils() {
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	public synchronized static String uniqueId(Context context) {
		initDerviceUuid(context);
		return device_id;
	}

	/** 获取设备型号:如Che2-TL00 */
	public static String getModel() {
		String model = Build.MODEL;
		if (StrUtil.isNotBlank(model)) {
			model = model.trim().replaceAll("\\s*", "");
		}
		if (StrUtil.isBlank(model)) {
			model = Build.MANUFACTURER;
		}
		return model;
	}

	public static String getVersionName(Context context) {
		String version = "unknow";
		try {
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			version = packInfo.versionName;
			if (StrUtil.isBlank(version)) {
				version = "unknow";
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}

	private static void initDerviceUuid(Context context) {
		if (device_id == null) {
			synchronized (DeviceUtils.class) {
				if (device_id == null) {
					// 从文件中获取
					final SharedPreferences prefs = context.getSharedPreferences(prefs_file, 0);
					final String id = prefs.getString(key, null);
					if (StrUtil.isNotBlank(id)) {
						device_id = id;
						return;
					}

					// 生成
					// ANDROID_ID 是设备第一次启动时产生和存储的64bit的一个数，当设备被wipe后该数重置
					// 它在Android <=2.1 or Android >=2.3的版本是可靠、稳定的，但在2.2的版本并不是100%可靠的
					// 在主流厂商生产的设备上，有一个很经常的bug，就是每个设备都会产生相同的ANDROID_ID：9774d56d682e549c
					final String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
					try {
						// 若不为 null 且不是重复的 ANDROID_ID
						if (StrUtil.isNotBlank(androidId) && !"9774d56d682e549c".equals(androidId)) {
							device_id = UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
						} else {
							// 获取 Sim Serial Number
							final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
							UUID uuid = StrUtil.isNotBlank(deviceId) ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
							device_id = uuid.toString();
						}
					} catch (UnsupportedEncodingException e) {
						// 若出错，使用随意的 uuid
						device_id = UUID.randomUUID().toString();
					}
					prefs.edit().putString(key, device_id).commit();
				}
			}
		}
	}

}