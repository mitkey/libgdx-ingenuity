package com.badlogic.gdx.ingenuity.utils.helper;

import static com.badlogic.gdx.Net.HttpMethods.POST;
import static com.badlogic.gdx.net.HttpStatus.SC_OK;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpStatus;

/**
 * @作者 Mitkey
 * @时间 2016年5月27日 上午11:28:23
 * @类说明:
 * @版本 xx
 */
public final class HttpHelper {
	private static final HttpRequestBuilder Builder = new HttpRequestBuilder();
	private static final String URL = "http://127.0.0.1:9090/bill";
	private static final String TAG = HttpHelper.class.getSimpleName();

	public static HttpRequest post(final Object content, final OnHttpCall onCall) {
		Gdx.app.log("发送网络请求", content.toString());

		HttpRequest httpRequest = Builder.newRequest().url(URL).method(POST).timeout(5000).content(content.toString()).build();
		Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				HttpStatus status = httpResponse.getStatus();
				if (status.getStatusCode() == SC_OK) {
					String resultAsString = null;
					try {
						resultAsString = httpResponse.getResultAsString();
						onCall.response(resultAsString);
					} catch (Exception e) {
						Gdx.app.error(TAG, "处理失败：" + resultAsString, e);
					}
				} else {
					onCall.failed();
				}
			}
			@Override
			public void failed(Throwable t) {
				Gdx.app.error(TAG, "网络请求失败：" + content.toString(), t);
				onCall.failed();
			}
			@Override
			public void cancelled() {
				Gdx.app.debug(TAG, "取消网络请求");
				onCall.cancelled();
			}
		});
		return httpRequest;
	}

	public static abstract class OnHttpCall {
		public abstract void response(String data);
		public abstract void failed();
		public void cancelled() {
		}
	}
}
