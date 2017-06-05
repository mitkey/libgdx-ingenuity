package com.badlogic.gdx.ingenuity.mvc;

/**
 * @作者 mitkey
 * @时间 2017年5月27日 上午10:37:05
 * @类说明 ICmdHandler.java <br/>
 * @版本 0.0.1
 */
public interface ICmdHandler {

	void handle(CmdAction<?> cmdAction) throws Exception;

}