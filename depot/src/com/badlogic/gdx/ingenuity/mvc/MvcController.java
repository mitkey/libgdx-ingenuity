package com.badlogic.gdx.ingenuity.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import com.badlogic.gdx.Gdx;

/**
 * @作者 Mitkey
 * @时间 2015-7-30 下午03:50:44
 * @类说明:
 * @版本 xx
 */
public class MvcController {
	private static final String tag = MvcController.class.getSimpleName();

	/** 指令对应的处理者 */
	private static final Map<Integer, List<ICmdHandler>> cmdMapHandlers = new HashMap<Integer, List<ICmdHandler>>();

	/** 预处理指令动作列表 */
	private static final Vector<CmdAction<?>> cmdActions = new Vector<CmdAction<?>>();

	/** 锁状态。若为 true 则不处理 cmdActions 列表，直到状态为 false */
	private static boolean locked = false;

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

	public static void handleCmds() {
		if (locked) {
			Gdx.app.log(tag, "消息处理被锁定");
			return;
		}
		if (!cmdActions.isEmpty()) {
			CmdAction<?> cmdAction = cmdActions.remove(0);
			handleCmds(cmdAction);
		}
	}

	public static void publishCmdAction(CmdAction<?> cmdAction) {
		cmdActions.add(cmdAction);
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

	private static void handleCmds(CmdAction<?> cmdAction) {
		if (cmdMapHandlers.containsKey(cmdAction.getCmd())) {
			for (ICmdHandler cmdHandler : cmdMapHandlers.get(cmdAction.getCmd())) {
				String formatStr = " %s 处理 %s 指令%s -->  %s";
				try {
					cmdHandler.handle(cmdAction);
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

}
