package com.badlogic.gdx.ingenuity.extend;

/**
 * @作者 Mitkey
 * @时间 2017年3月24日 下午2:18:05
 * @类说明:
 * @版本 xx
 */
public interface ICoreHelper {

	/** 获取客户端版本 */
	String getVersion();

	/** 设备唯一标识ID */
	String getDeviceUniqueId();

	/** 设备类型 */
	String getDeviceType();
	
	/** 退出应用 */
	void existApp();

}
