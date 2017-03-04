package com.badlogic.gdx.ingenuity.utils.scene2d;

import java.security.MessageDigest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;

/**
 * @作者 Mitkey
 * @时间 2016年10月31日 下午3:14:20
 * @类说明:
 * @版本 xx
 */
public class RemoteImage extends Image implements Disposable {

	private static final String tag = RemoteImage.class.getSimpleName();
	private static final int ReCount = 5;

	public static final String saveRemoteImage2lDir = "abcsResource";

	String url;
	Texture texture;
	HttpRequest httpRequest;

	int count;

	public RemoteImage(Texture defaultTexture, String url) {
		this(new TextureRegion(defaultTexture), url);
	}

	public RemoteImage(TextureRegion defaultRegion, String url) {
		super(defaultRegion);
		this.url = url;
		// 处理下载网络图片
		handleDownloadRemoteImage();
	}

	void handleDownloadRemoteImage() {
		if (texture != null) {
			Gdx.app.debug(tag, url + " read form app memory");
			changeDrawable();
			return;
		}
		final String md5FileName = md5FileName(url);
		// 从本地读
		Texture textureLocal = readTextureFromLocal(md5FileName);
		if (textureLocal != null) {
			Gdx.app.debug(tag, url + " read form local");
			texture = textureLocal;
			changeDrawable();
			return;
		}
		// 从网络读
		httpRequest = new HttpRequest(HttpMethods.GET);
		httpRequest.setUrl(url);
		Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				int statusCode = httpResponse.getStatus().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					// 最多尝试 ReCount 次
					if (count++ < ReCount) {
						handleDownloadRemoteImage();
					} else {
						Gdx.app.error(tag, "download remote image response statusCode no is 200,try again!(" + url + ")");
					}
					return;
				}
				final byte[] rawImageBytes = httpResponse.getResult();
				Gdx.app.postRunnable(new Runnable() {
					public void run() {
						Gdx.app.debug(tag, url + " read form remote");
						Pixmap pixmap = new Pixmap(rawImageBytes, 0, rawImageBytes.length);
						// 保存到本地
						PixmapIO.writeCIM(generalImageFileHandle(md5FileName), pixmap);

						texture = new Texture(pixmap);
						pixmap.dispose();
						changeDrawable();
					}
				});
			}
			@Override
			public void failed(Throwable t) {
				Gdx.app.error(tag, "download remote image failed:" + url, t);
			}
			@Override
			public void cancelled() {
				Gdx.app.debug(tag, "cancel download remote image:" + url);
			}
		});
	}

	void changeDrawable() {
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
		setSize(getWidth(), getHeight());
	}

	Texture readTextureFromLocal(String md5FileName) {
		Texture tempTexture;
		try {
			Pixmap readCIM = PixmapIO.readCIM(generalImageFileHandle(md5FileName));
			tempTexture = new Texture(readCIM);
			readCIM.dispose();
		} catch (Exception e) {
			tempTexture = null;
		}
		return tempTexture;
	}

	FileHandle generalImageFileHandle(String md5FileName) {
		FileHandle external = Gdx.files.external(saveRemoteImage2lDir);
		if (!external.exists()) {
			external.mkdirs();
		}
		return external.child(md5FileName);
	}

	String md5FileName(String url) {
		String returnStr;
		try {
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.update(url.getBytes());
			returnStr = byte2HexString(localMessageDigest.digest());
			return returnStr;
		} catch (Exception e) {
			return url;
		}
	}

	String byte2HexString(byte[] b) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			buffer.append(hex.toUpperCase());
		}
		return buffer.toString();
	}

	@Override
	public void dispose() {
		if (httpRequest != null) {
			Gdx.net.cancelHttpRequest(httpRequest);
		}
		if (texture != null) {
			texture.dispose();
			texture = null;
		}
	}

}
