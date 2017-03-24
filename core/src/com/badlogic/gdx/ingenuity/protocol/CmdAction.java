package com.badlogic.gdx.ingenuity.protocol;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

/**
 * @作者 Mitkey
 * @时间 2017年3月24日 下午2:21:14
 * @类说明:
 * @版本 xx
 */
public class CmdAction {

	private Integer cmd;
	private JSONArray msg;

	public CmdAction(Integer cmd, JSONArray msg) {
		super();
		this.cmd = cmd;
		this.msg = msg;
	}

	public Integer getCmd() {
		return cmd;
	}
	public JSONArray getMsg() {
		return msg;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
