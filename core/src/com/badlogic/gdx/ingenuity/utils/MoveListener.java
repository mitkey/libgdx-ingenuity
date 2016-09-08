package com.badlogic.gdx.ingenuity.utils;

import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.MINUS;// -
import static com.badlogic.gdx.Input.Keys.P;
import static com.badlogic.gdx.Input.Keys.PERIOD;// .
import static com.badlogic.gdx.Input.Keys.PLUS;// +
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.UP;

import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * @作者 Mitkey
 * @时间 2016年4月11日 上午9:35:08
 * @类说明:可以拖动改变坐标、可以使用键盘的方向键改变坐标。若需要打印当前 actor 的坐标，按下【P】键
 * @版本 xx
 */
public class MoveListener extends InputListener {
	private static final Timer TIMER = new Timer();
	private static final String TAG = MoveListener.class.getSimpleName();
	private static final String format = "%s actor 当前 X 坐标为 %s、Y坐标为 %s";
	private static int STEP_XY = 1;

	private Actor actor;
	private String name;

	// 当前键盘的状态
	float startX, startY;
	TimerTask taskUp, taskDown, taskLeft, taskRight;

	public MoveListener(Actor actor) {
		this.actor = actor;
		this.name = actor.getName() == null ? actor.getClass().getSimpleName() : actor.getName();
	}

	public MoveListener(Actor actor, String name) {
		this.actor = actor;
		this.name = name;
	}

	@Override
	public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
		// 在该 actor 获取到焦点时，通知 stage 该 actor 需要处理“键盘点击”事件
		actor.getStage().setKeyboardFocus(actor);
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		// 不处理多个触点时间
		if (pointer != 0) {
			return false;
		}
		// x y 参数坐标的原点是基于构造方法传入的 actor 左下角
		startX = x;
		startY = y;
		return true;
	}

	@Override
	public void touchDragged(InputEvent event, float x, float y, int pointer) {
		// 不处理多个触点时间
		if (pointer != 0) {
			return;
		}
		// x y 参数坐标的原点是基于构造方法传入的 actor 左下角
		float xMoveSpace = x - startX;
		float yMoveSpace = y - startY;
		actor.moveBy(xMoveSpace, yMoveSpace);
	}

	@Override
	public boolean keyUp(InputEvent event, int keycode) {
		// 按键抬起时，取消对应的 task
		if (keycode == UP)
			cancelTask(taskUp);
		else if (keycode == DOWN)
			cancelTask(taskDown);
		else if (keycode == LEFT)
			cancelTask(taskLeft);
		else if (keycode == RIGHT)
			cancelTask(taskRight);
		else if (keycode == P)
			printPosition();
		else if (keycode == PERIOD)//
			STEP_XY = 1;// .
		else if (keycode == PLUS)
			STEP_XY += 1;// +
		else if (keycode == MINUS)
			STEP_XY -= 1; // -
		else {
			// 其他按键抬起不处理。
			System.err.println();
		}
		return false;
	}

	@Override
	public boolean keyDown(InputEvent event, int keycode) {
		if (keycode == UP) {
			createUpMoveTask();
		} else if (keycode == DOWN) {
			createDownMoveTask();
		} else if (keycode == LEFT) {
			createLeftMoveTask();
		} else if (keycode == RIGHT) {
			createRightMoveTask();
		} else {
			// 不处理
		}
		return false;
	}

	private void createUpMoveTask() {
		cancelTask(taskUp);
		taskUp = new TimerTask() {
			@Override
			public void run() {
				actor.moveBy(0, STEP_XY);
			}
		};
		TIMER.schedule(taskUp, 0, 20);
	}

	private void createDownMoveTask() {
		cancelTask(taskDown);
		taskDown = new TimerTask() {
			@Override
			public void run() {
				actor.moveBy(0, -STEP_XY);
			}
		};
		TIMER.schedule(taskDown, 0, 20);
	}

	private void createLeftMoveTask() {
		cancelTask(taskLeft);
		taskLeft = new TimerTask() {
			@Override
			public void run() {
				actor.moveBy(-STEP_XY, 0);
			}
		};
		TIMER.schedule(taskLeft, 0, 20);
	}

	private void createRightMoveTask() {
		cancelTask(taskRight);
		taskRight = new TimerTask() {
			@Override
			public void run() {
				actor.moveBy(STEP_XY, 0);
			}
		};
		TIMER.schedule(taskRight, 0, 20);
	}

	private void cancelTask(TimerTask task) {
		if (task != null) {
			task.cancel();
		}
	}

	private void printPosition() {
		Gdx.app.log(TAG, String.format(format, name, actor.getX(), actor.getY()));
	}
}
