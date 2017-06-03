package com.badlogic.gdx.ingenuity.test.screen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.ingenuity.test.protocol.PacketBuffer;
import com.badlogic.gdx.ingenuity.utils.GdxUtil;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Pools;

import io.socket.nativeclient.IO;
import io.socket.nativeclient.IO.Options;
import io.socket.nativeclient.OnSocketCall;
import io.socket.nativeclient.SocketClient;
import io.socket.nativeclient.SocketIOException;
import net.mwplay.nativefont.NativeButton;

/**
 * @作者 mitkey
 * @时间 2017年5月21日 下午5:25:03
 * @类说明 TestScreen.java <br/>
 * @版本 0.0.1
 */
public class TestScreen extends BaseTestScreen {

	private NativeButton btnToast;
	private SocketClient socket;

	private boolean isConnect = false;
	private NativeButton btnConnectSocket;
	private NativeButton btnSend;
	private NativeButton btnDisconnect;

	@Override
	public void show() {
		super.show();

		NativeButton btnLoading = newNativeButton("加载进度", dabUp, dabDown, null, 25);
		btnLoading.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				showLoading();
			}
		});

		btnToast = newNativeButton("提示信息", dabUp, dabDown, null, 25);
		btnToast.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				showMesssage(stage().toString());
			}
		});

		btnConnectSocket = newNativeButton("连接服务器", dabUp, dabDown, null, 25);
		btnConnectSocket.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);

				String host = "192.168.1.147";
				int port = 6141;

				socket = IO.socket(host, port, new Options(), new OnSocketCall() {
					@Override
					public void onMessage(byte[] data) {
						System.err.println(JSON.toJSONString(PacketBuffer.decode(data), true));
					}

					@Override
					public void onError(SocketIOException socketIOException) {
						socketIOException.printStackTrace();
					}

					@Override
					public void onDisconnect() {
						isConnect = false;
					}

					@Override
					public void onConnect() {
						isConnect = true;
					}
				});
			}
		});

		btnSend = newNativeButton("发数据", dabUp, dabDown, null, 25);
		btnSend.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);

				StringBuffer temp = new StringBuffer("PushService.pi0ng");

				JSONObject body = new JSONObject();
				for (int i = 0; i < 5000; i++) {
					body.put(""+i, "mm" + i);
				}
				socket.sendData(PacketBuffer.encode(temp.toString(), body));
			}
		});

		btnDisconnect = newNativeButton("断开连接", dabUp, dabDown, null, 25);
		btnDisconnect.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (socket != null) {
					socket.dispose();
				}
			}
		});

		VerticalGroup verticalGroup = new VerticalGroup();
		verticalGroup.addActor(btnLoading);
		verticalGroup.addActor(btnToast);
		verticalGroup.addActor(btnConnectSocket);
		verticalGroup.addActor(btnSend);
		verticalGroup.addActor(btnDisconnect);
		verticalGroup.space(20);
		verticalGroup.pack();
		GdxUtil.center(verticalGroup);
		stage().addActor(verticalGroup);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		if (isConnect) {
			disalbeButton(btnConnectSocket, true);
			disalbeButton(btnSend, false);
			disalbeButton(btnDisconnect, false);
		} else {
			disalbeButton(btnConnectSocket, false);
			disalbeButton(btnSend, true);
			disalbeButton(btnDisconnect, true);
		}
	}

	private void disalbeButton(NativeButton button, boolean disable) {
		if (disable) {
			button.setColor(Color.GRAY);
			button.setTouchable(Touchable.disabled);
		} else {
			button.setColor(Color.WHITE);
			button.setTouchable(Touchable.enabled);
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.SPACE) {
			hideLoading();
		} else if (keycode == Keys.T) {
			// 程序代码触发监听事件
			InputEvent obtain = Pools.obtain(InputEvent.class);
			obtain.setStage(stage());
			obtain.setTarget(btnToast);
			obtain.setType(Type.touchUp);
			obtain.setBubbles(false);
			btnToast.fire(obtain);
		}

		return super.keyDown(keycode);
	}

}
