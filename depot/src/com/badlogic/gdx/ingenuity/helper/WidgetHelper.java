package com.badlogic.gdx.ingenuity.helper;

import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.MINUS;
import static com.badlogic.gdx.Input.Keys.O;
import static com.badlogic.gdx.Input.Keys.P;
import static com.badlogic.gdx.Input.Keys.PERIOD;
import static com.badlogic.gdx.Input.Keys.PLUS;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.Z;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ingenuity.GdxData;
import com.badlogic.gdx.ingenuity.scene2d.SimpleScreen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import net.mwplay.nativefont.NativeFont;
import net.mwplay.nativefont.NativeLabel;

/**
 * @作者 mitkey
 * @时间 2017年6月9日 下午6:06:29
 * @类说明 WidgetHelper.java <br/>
 * @版本 0.0.1
 */
public final class WidgetHelper {

	private static final String tag = WidgetHelper.class.getSimpleName();

	/** 默认步长 */
	private static final int DEFAULT_STEP_XY = 1;

	/** 日志格式:初始x、初始y、当前x、当前y、步长 */
	private static final String LOG_FORMAT = "x[%s] -- y[%s] -- curX[%s] -- curY[%s] -- step[%s]";

	/** 重复任务初始间隔时间，单位秒 */
	private static final float KEY_REPEAT_INITIAL_TIME = 0.4f;

	/** 重复任务间隔时间，单位秒 */
	private static final float KEY_REPEAT_TIME = 0.1f;

	private static Set<Actor> widgets = new HashSet<>();
	private static Table root;

	private WidgetHelper() {
	}

	/** 注册 input 监听 */
	public static <T extends Actor> T register(T actor) {
		Objects.requireNonNull(actor, "register actor must not be null");
		if (Gdx.app.getType() != ApplicationType.Desktop) {
			return actor;
		}
		if (actor.isTouchable()) {
			actor.addListener(new InputListenerExtension(actor));
			widgets.add(actor);
		}
		return actor;
	}

	/** 显示帮助面板 ---> 如按了 F12 显示 */
	public static void showHelpOnOff() {
		if (Gdx.app.getType() != ApplicationType.Desktop) {
			return;
		}

		generateRoot();
		if (root.getUserObject() == null) {
			// 首次默认为显示
			root.setUserObject(root);
		} else if (root.getUserObject() == root) {
			// 后续为不显示
			root.setUserObject(null);
		}
	}

	/** 渲染绘制 */
	public static void renderHelp() {
		if (Gdx.app.getType() != ApplicationType.Desktop) {
			return;
		}

		if (root != null && root.getUserObject() == root) {
			SpriteBatch spriteBatch = SimpleScreen.spriteBatch();
			if (spriteBatch != null) {
				spriteBatch.begin();
				root.draw(spriteBatch, 1);
				root.act(Gdx.graphics.getDeltaTime());
				spriteBatch.end();
			}
		}
	}

	/** 清理无效的 */
	public static void clearInvalids() {
		Iterator<Actor> iterator = widgets.iterator();
		while (iterator.hasNext()) {
			Actor actor = iterator.next();
			if (actor.getStage() == null) {
				iterator.remove();
			}
		}
	}

	private static void generateRoot() {
		if (root != null) {
			return;
		}
		NativeFont font = GdxData.getInstance().getFont(20);
		root = new Table();
		root.setBackground(PixmapHelper.getInstance().newRectangleDrawable(Color.valueOf("343434"), 10, 10));
		root.pad(15).defaults().left().space(10);
		root.add(new NativeLabel("↑", font)).getTable().add(new NativeLabel("上移", font)).row();
		root.add(new NativeLabel("↓", font)).getTable().add(new NativeLabel("下移", font)).row();
		root.add(new NativeLabel("←", font)).getTable().add(new NativeLabel("左移", font)).row();
		root.add(new NativeLabel("→", font)).getTable().add(new NativeLabel("右移", font)).row();

		root.add(new NativeLabel("P", font)).getTable().add(new NativeLabel("打印坐标和步长", font)).row();
		root.add(new NativeLabel(".", font)).getTable().add(new NativeLabel("重置步长", font)).row();
		root.add(new NativeLabel("+", font)).getTable().add(new NativeLabel("增加步长", font)).row();
		root.add(new NativeLabel("-", font)).getTable().add(new NativeLabel("减少步长", font)).row();

		root.add(new NativeLabel("Z", font)).getTable().add(new NativeLabel("还原为初始位置", font)).row();
		root.add(new NativeLabel("S", font)).getTable().add(new NativeLabel("更新为初始位置", font)).row();
		root.add(new NativeLabel("O", font)).getTable().add(new NativeLabel("原点", font)).row();
		root.layout();
		root.pack();
		root.setPosition(GdxData.WIDTH - root.getWidth(), GdxData.HEIGHT - root.getHeight());
	}

	private static final class InputListenerExtension extends InputListener {
		private final Actor actor;
		boolean debug;
		int stepXy = DEFAULT_STEP_XY;
		float x, y;
		float startx, starty;
		KeyRepeatTask keyRepeatTask = new KeyRepeatTask(this);

		private InputListenerExtension(Actor actor) {
			this.actor = actor;
			x = actor.getX();
			y = actor.getY();
		}

		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			// 获取键盘焦点
			Stage stage = actor.getStage();
			if (stage != null)
				stage.setKeyboardFocus(actor);

			startx = x;
			starty = y;
			debug = actor.getDebug();
			actor.setDebug(true);
			return true;
		}

		@Override
		public void touchDragged(InputEvent event, float x, float y, int pointer) {
			super.touchDragged(event, x, y, pointer);
			float roundX = Math.round(x - startx);
			float roundY = Math.round(y - starty);
			actor.moveBy(roundX, roundY);
		}

		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
			super.touchUp(event, x, y, pointer, button);
			actor.setDebug(debug);
		}

		@Override
		public boolean keyDown(InputEvent event, int keycode) {
			if (!hasFocus()) {
				return true;
			}

			actor.setDebug(true);
			boolean repeatMove = false;

			switch (keycode) {
				case UP:
					// 方向键 ---> 上
					actor.moveBy(0, stepXy);
					repeatMove = true;
					break;
				case DOWN:
					// 方向键 ---> 下
					actor.moveBy(0, -stepXy);
					repeatMove = true;
					break;
				case LEFT:
					// 方向键 ---> 左
					actor.moveBy(-stepXy, 0);
					repeatMove = true;
					break;
				case RIGHT:
					// 方向键 ---> 右
					actor.moveBy(stepXy, 0);
					repeatMove = true;
					break;

				// ============

				case P:
					// 打印位置和步长
					Gdx.app.log(tag, String.format(LOG_FORMAT, x, y, actor.getX(), actor.getY(), stepXy));
					break;
				case PERIOD:
					// . 符号重置步长
					stepXy = DEFAULT_STEP_XY;
					break;
				case PLUS:
					// + 符号增加步长
					stepXy += DEFAULT_STEP_XY;
					break;
				case MINUS:
					// - 符号减少步长
					stepXy -= DEFAULT_STEP_XY;
					break;

				// ============

				case Z:
					// 还原位置
					actor.setPosition(x, y);
					break;
				case S:
					// 重新设置初始位置
					x = actor.getX();
					y = actor.getY();
					break;
				case O:
					// 原点
					actor.setPosition(0, 0);
					break;
				default:
					break;
			}

			if (repeatMove) {
				scheduleKeyRepeatTask(keycode);
			}
			return true;
		}

		@Override
		public boolean keyUp(InputEvent event, int keycode) {
			Set<Integer> keycodes = keyRepeatTask.keycodes;
			if (keycodes.contains(keycode)) {
				keycodes.remove(keycode);
			}
			if (keycodes.size() == 0) {
				keyRepeatTask.cancel();
				actor.setDebug(debug);
			}
			return true;
		}

		private void scheduleKeyRepeatTask(int keycode) {
			keyRepeatTask.keycodes.add(keycode);
			// 该任务未调度过
			if (!keyRepeatTask.isScheduled()) {
				Timer.schedule(keyRepeatTask, KEY_REPEAT_INITIAL_TIME, KEY_REPEAT_TIME);
			}
		}

		/** 若当前焦点是不是自己 */
		private boolean hasFocus() {
			Stage stage = actor.getStage();
			if (stage == null) {
				return false;
			}
			if (stage.getKeyboardFocus() != actor) {
				return false;
			}
			return true;
		}
	}

	private static final class KeyRepeatTask extends Task {
		InputListener inputListener;
		Set<Integer> keycodes = new HashSet<>();

		private KeyRepeatTask(InputListener inputListener) {
			this.inputListener = inputListener;
		}

		public void run() {
			keycodes.forEach(new Consumer<Integer>() {
				@Override
				public void accept(Integer keycode) {
					inputListener.keyDown(null, keycode);
				}
			});
		}
	}

}
