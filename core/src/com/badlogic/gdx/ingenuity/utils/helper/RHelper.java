
package com.badlogic.gdx.ingenuity.utils.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.ingenuity.IngenuityGdx;

public class RHelper {

	private static HashMap<String, String> resMap = new HashMap<String, String>();

	/** 自动读写资源路径 */
	public static void autoWriteRes() {
		loadRes("");// FIXME 为什么要在这调用

		// 读取模板
		String rPath = RHelper.class.getName().replaceAll("\\.", "/") + "R.template";
		FileHandle resFileHandle = Gdx.files.classpath(rPath);
		String line = null;
		StringBuffer contentBuffer = new StringBuffer();
		try (BufferedReader bufferedReader = new BufferedReader(resFileHandle.reader())) {
			while ((line = bufferedReader.readLine()) != null) {
				if (line.contains("$")) {
					// 替换变量
					String demo = line.replace("$", "");
					for (Map.Entry<String, String> entry : resMap.entrySet()) {
						String a = String.format(demo, entry.getKey(), entry.getValue()) + "\n";
						contentBuffer.append(a);
					}
				} else if (line.contains("#")) {
					// 替换包名
					line = String.format(line.replace("#", ""), getGameClassPath());
					line = line.replaceAll("/", ".");
					contentBuffer.append(line + "\n");
				} else {
					contentBuffer.append(line + "\n");
				}
			}
		} catch (IOException e) {
			Gdx.app.error(RHelper.class.getSimpleName(), "", e);
		}

		if (contentBuffer.length() > 0) {
			// 工程名称
			String projectname = System.getProperty("user.dir").replace("desktop", "core");
			// =/Users/haocao/git/CHGame/my-gdx-game-core
			projectname = projectname.substring(projectname.lastIndexOf("/") + 1);
			// 在相应包目录下创建类文件
			String coreProjectSrc = String.format("%s/src/", projectname);
			String resJavaFile = coreProjectSrc + getGameClassPath();
			File resFile = new File(resJavaFile);
			if (!resFile.exists()) {
				resFile.mkdir();
			}
			FileHandle fileHandle = new FileHandle(resFile + "/GdxR.java");
			fileHandle.writeBytes(contentBuffer.toString().getBytes(), false);
		}
	}

	/** 读取文件下所有文件 */
	private static FileHandle[] getFileHandles(String fileName) {
		if (Gdx.app.getType() == ApplicationType.Desktop) {
			try {
				File directory = new File("");// 参数为空
				String courseFile = directory.getCanonicalPath();
				courseFile = courseFile.replace("desktop", "android");
				File assetFile = new File(courseFile + File.separator + "assets/" + fileName);
				File[] files = assetFile.listFiles();
				if (files == null)
					return new FileHandle[]{};
				FileHandle[] handles = new FileHandle[files.length];
				for (int i = 0, l = files.length; i < l; i++) {
					handles[i] = new FileHandle(files[i]);
				}
				return handles;
			} catch (IOException ioException) {
				Gdx.app.error("CHFileHandler", "load assets error :" + ioException.getLocalizedMessage());
			}
		}

		FileHandle parentFileHandle = Gdx.files.internal(fileName);
		return parentFileHandle.list();
	}

	/** 获取 game 类的继续类所在包路径 */
	private static String getGameClassPath() {
		Class<?> gameclass = IngenuityGdx.class;
		String gameClassPath = gameclass.getName().replaceAll("\\.", "/");
		gameClassPath = gameClassPath.substring(0, gameClassPath.lastIndexOf("/"));
		return gameClassPath;
	}

	/** 读取assets目录下全部资源 */
	private static void loadRes(String path) {
		// 处理path
		int idxAssetEnd = path.contains("assets") ? path.lastIndexOf("assets") + 7 : -1;
		if (idxAssetEnd > 6) {
			path = path.substring(idxAssetEnd);
		}
		FileHandle[] fileHandles = RHelper.getFileHandles(path);
		for (FileHandle fileHandle : fileHandles) {
			if (fileHandle.isDirectory()) {
				// 是目录，继续遍历
				loadRes(fileHandle.path());
			} else {
				// 是文件
				String key = fileHandle.name().replaceAll("\\.", "_");
				if (key.contains("DS_Store")) {
					continue;
				}
				if (Character.isDigit(key.toCharArray()[0])) {
					// 数字打头的文件，需要处理下
					key = "_" + key;
				}
				String value = fileHandle.path();
				int assetEnd = value.lastIndexOf("assets");
				resMap.put(key, value.substring(assetEnd + 7));
			}
		}
	}
}
