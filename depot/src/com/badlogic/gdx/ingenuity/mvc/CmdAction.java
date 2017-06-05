package com.badlogic.gdx.ingenuity.mvc;

/**
 * @作者 mitkey
 * @时间 2017年5月26日 上午11:42:45
 * @类说明 CmdAction.java <br/>
 * @版本 0.0.1
 */
public class CmdAction<T> {

	private Integer cmd;
	private T msg;

	public CmdAction(Integer cmd, T msg) {
		super();
		this.cmd = cmd;
		this.msg = msg;
	}

	public Integer getCmd() {
		return cmd;
	}

	public T getMsg() {
		return msg;
	}

	@Override
	public String toString() {
		return "CmdAction [cmd=" + cmd + ", msg=" + msg + "]";
	}

}
