package com.badlogic.gdx.ingenuity.protocol;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.badlogic.gdx.Gdx;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

/**
 * @作者 Mitkey
 * @时间 2015-7-30 下午03:50:44
 * @类说明:
 * @版本 xx
 */
public class SocketIOMgr {
	private static final String tag = SocketIOMgr.class.getSimpleName();

	private static final String URL = "http://182.92.153.129:9007";

	/** 指令对应的处理者 */
	private static final Map<Integer, List<ICmdHandler>> cmdMapHandlers = new HashMap<Integer, List<ICmdHandler>>();
	/** 预处理指令动作列表 */
	private static final Vector<CmdAction> cmdActions = new Vector<CmdAction>();

	/** 锁状态。若为 true 则不处理 cmdActions 列表，直到状态为 false */
	private static boolean locked = false;

	private static SocketIO socketIO;
	private static boolean connected = false;

	public static boolean bind(ICmdHandler cmdHandler, int... cmds) {
		if (cmdHandler == null || cmds == null || cmds.length == 0) {
			return false;
		}

		List<String> bindCmdNames = new ArrayList<String>();
		boolean isTag = true;
		for (int cmd : cmds) {
			boolean success = bind(cmd, cmdHandler);
			if (!success) {
				// 不成功是更新
				isTag = success;
			} else {
				bindCmdNames.add(Cmd.cmd2Name(cmd));
			}
		}
		Gdx.app.log(tag, cmdHandler.getClass().getSimpleName() + " 绑定指令 " + bindCmdNames);
		return isTag;
	}

	public static void unBind(ICmdHandler target) {
		if (target == null) {
			return;
		}
		List<String> unBindCmdNames = new ArrayList<String>();

		Iterator<Entry<Integer, List<ICmdHandler>>> iterator = cmdMapHandlers.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, List<ICmdHandler>> entry = iterator.next();
			Iterator<ICmdHandler> iteratorCmdHandlers = entry.getValue().iterator();
			while (iteratorCmdHandlers.hasNext()) {
				ICmdHandler cmdHandler = iteratorCmdHandlers.next();
				if (target == cmdHandler) {
					iteratorCmdHandlers.remove();
					unBindCmdNames.add(Cmd.cmd2Name(entry.getKey()));
				}
			}
			if (entry.getValue().size() == 0) {
				iterator.remove();
			}
		}
		Gdx.app.log(tag, target.getClass().getSimpleName() + " 解除绑定指令 " + unBindCmdNames);
	}

	public static void emitCmd(int cmd, Object... objects) {
		if (socketIO == null && !connected) {
			Gdx.app.error(tag, "socket 未连接");
			return;
		}
		String cmdMsg = generalSendCmdMsg(cmd, objects);
		socketIO.send(cmdMsg);
		Gdx.app.log(tag, "◆◆ " + Cmd.cmd2Name(cmd) + " ◆◆\t" + cmdMsg);
	}

	public static boolean isLocked() {
		return locked;
	}

	public static void lock() {
		locked = true;
	}

	public static void unlock() {
		locked = false;
	}

	public static void connectServer() {
		if (socketIO != null && socketIO.isConnected()) {
			return;
		}

		try {
			socketIO = new SocketIO(URL);
		} catch (MalformedURLException e) {
			Gdx.app.error(tag, "实例化 SocketIO 对象失败 " + URL, e);
			return;
		}

		Gdx.app.log(tag, "开始连接到 socket 服务器 " + URL);
		socketIO.connect(new IOCallback() {
			@Override
			public void onMessage(JSONObject json, IOAcknowledge ack) {
				Gdx.app.log(tag, "接收到 socket 的 onMessage(JSONObject json, IOAcknowledge ack) 回调未处理 json:" + json);
			}

			@Override
			public void onMessage(String data, IOAcknowledge ack) {
				parseCmd(data);
			}

			@Override
			public void onError(SocketIOException e) {
				Gdx.app.log(tag, "socket 连接错误", e);
				connected = false;
			}

			@Override
			public void onDisconnect() {
				Gdx.app.log(tag, "断开 socket 服务器");
				connected = false;
			}

			@Override
			public void onConnect() {
				Gdx.app.log(tag, "连接 socket 服务器成功");
				connected = true;
			}
			@Override
			public void on(String event, IOAcknowledge ack, Object... args) {
				Gdx.app.log(tag, "接收到 socket 的 on(String event, IOAcknowledge ack, Object... args) 回调未处理 event:" + event + " args:" + JSON.toJSONString(args));
			}
		});

	}

	public static boolean isConnected() {
		return connected;
	}

	public static void handleCmds() {
		if (locked) {
			Gdx.app.log(tag, "消息处理被锁定");
			return;
		}
		if (!cmdActions.isEmpty()) {
			CmdAction cmdAction = cmdActions.remove(0);
			handleCmds(cmdAction);
		}
	}

	private static void handleCmds(CmdAction cmdAction) {
		if (cmdMapHandlers.containsKey(cmdAction.getCmd())) {
			for (ICmdHandler cmdHandler : cmdMapHandlers.get(cmdAction.getCmd())) {
				String formatStr = " %s 处理 %s 指令%s -->  %s";
				try {
					cmdHandler.handle(cmdAction.getCmd(), cmdAction.getMsg());
					Gdx.app.log(tag, String.format(formatStr, cmdHandler.getClass().getSimpleName(), Cmd.cmd2Name(cmdAction.getCmd()), "成功", cmdAction));
				} catch (Exception e) {
					Gdx.app.error(tag, String.format(formatStr, cmdHandler.getClass().getSimpleName(), Cmd.cmd2Name(cmdAction.getCmd()), "失败", cmdAction), e);
				}
			}
		} else {
			String name = Cmd.cmd2Name(cmdAction.getCmd());
			Gdx.app.log(tag, name + " 未注册处理者:" + cmdAction);
		}
	}

	private static void parseCmd(String data) {
		try {
			JSONArray array = JSON.parseArray(data);
			// 判断是否为指令队列
			if (array.get(0).getClass() == JSONArray.class) {
				for (int i = 0; i < array.size(); i++) {
					parseCmd(array.getString(i));
				}
				return;
			}

			addCmdAction(array);
		} catch (Exception e) {
			Gdx.app.error(tag, "socket 服务器返回数据解析失败 " + data, e);
		}
	}

	private static void addCmdAction(JSONArray cmdArray) {
		cmdActions.add(new CmdAction(cmdArray.getInteger(0), cmdArray));
	}

	private static boolean bind(int cmd, ICmdHandler cmdHandler) {
		List<ICmdHandler> listCmdHandlers = cmdMapHandlers.get(cmdHandler);
		if (listCmdHandlers != null) {
			if (listCmdHandlers.contains(cmdHandler)) {
				return false;
			} else {
				listCmdHandlers.add(cmdHandler);
			}
		} else {
			listCmdHandlers = new ArrayList<ICmdHandler>();
			listCmdHandlers.add(cmdHandler);
			cmdMapHandlers.put(cmd, listCmdHandlers);
		}
		return true;
	}

	private static String generalSendCmdMsg(int cmd, Object... objects) {
		JSONArray cmdMsg = new JSONArray();
		cmdMsg.add(cmd);
		for (Object object : objects) {
			cmdMsg.add(object);
		}
		return cmdMsg.toString();
	}

	/**
	 * @作者 Mitkey
	 * @时间 2017年3月3日 下午4:01:07
	 * @类说明:
	 * @版本 xx
	 */
	public interface ICmdHandler {
		void handle(int cmd, JSONArray msg) throws Exception;
	}
}
