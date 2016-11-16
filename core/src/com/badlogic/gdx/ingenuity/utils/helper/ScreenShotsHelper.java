
package com.badlogic.gdx.ingenuity.utils.helper;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * @作者 Mitkey
 * @时间 2016年5月17日 下午7:53:21
 * @类说明:截屏
 * @版本 xx
 */
public class ScreenShotsHelper {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss.SSS");
	private static final AtomicInteger Atommic_Counter = new AtomicInteger(0);
	private static final String File_Name_Format = "screenshot-%s-%s.png";

	public static void saveScreenShot(String fileName, int x, int y, int w, int h) {
		try {
			FileHandle fh;
			do {
				fh = new FileHandle(fileName);
			} while (fh.exists());
			Pixmap pixmap = getScreenshot(x, y, w, h, true);
			PixmapIO.writePNG(fh, pixmap);
			pixmap.dispose();
		} catch (Exception e) {
			Gdx.app.error("screenshot", e.getLocalizedMessage());
		}
	}

	public static void saveScreenShot() {
		String fileName = String.format(File_Name_Format, DATE_FORMAT.format(new Date()), Atommic_Counter.getAndIncrement());
		saveScreenShot(fileName, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	private static Pixmap getScreenshot(int x, int y, int w, int h, boolean yDown) {
		final Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(x, y, w, h);
		if (yDown) {
			// Flip the pixmap upside down
			ByteBuffer pixels = pixmap.getPixels();
			int numBytes = w * h * 4;
			byte[] lines = new byte[numBytes];
			int numBytesPerLine = w * 4;
			for (int i = 0; i < h; i++) {
				pixels.position((h - i - 1) * numBytesPerLine);
				pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
			}
			pixels.clear();
			pixels.put(lines);
		}
		return pixmap;
	}

}
