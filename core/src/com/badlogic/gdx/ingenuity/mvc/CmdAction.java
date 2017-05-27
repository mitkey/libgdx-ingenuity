package com.badlogic.gdx.ingenuity.mvc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

/**
 * @作者 mitkey
 * @时间 2017年5月26日 上午11:42:45
 * @类说明 CmdAction.java <br/>
 * @版本 0.0.1
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
