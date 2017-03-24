package com.badlogic.gdx.ingenuity;

import com.badlogic.gdx.ingenuity.extend.ICoreHelper;

/**
 * @作者 Mitkey
 * @时间 2017年3月24日 下午2:38:43
 * @类说明:
 * @版本 xx
 */
public class GlobalData {
	private static GlobalData ourInstance = new GlobalData();

	private ICoreHelper coreHelper;

	private GlobalData() {
	}

	public static GlobalData getInstance() {
		return ourInstance;
	}

	public void initRuntime(ICoreHelper coreHelper) {
		this.coreHelper = coreHelper;
	}

	public ICoreHelper getCoreHelper() {
		return coreHelper;
	}

}
