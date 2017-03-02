
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
import com.badlogic.gdx.ingenuity.utils.common.StrUtil;

public class RHelper {
	private static final String TAG = RHelper.class.getSimpleName();
	private static final String ResRoot = "assets";
	private static HashMap<String, String> resMap = new HashMap<String, String>();

	/** 自动读写资源路径 */
	public static void generated() {
		// 加载 assets 资源下所有文件的 name 和 path。
		loadResNames("");

		// 读取模板
		String rPath = RHelper.class.getName().replaceAll("\\.", "/").replace(RHelper.class.getSimpleName(), "") + "R.template";
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
			Gdx.app.error(TAG, "读取操作模板文件错误", e);
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

		Gdx.app.debug(TAG, "generated GdxR.java finished");
	}

	/** 读取assets目录下全部资源 */
	private static void loadResNames(String path) {
		// 处理 path 为 C:\development\git-repository\company-project\libgdx-ingenuity\android\assets 这种路径，转换为 assets 目录下相对 path
		int idxAssetEnd = path.contains(ResRoot) ? (path.lastIndexOf(ResRoot) + ResRoot.length() + 1) : -1;
		if (idxAssetEnd > 6) {
			path = path.substring(idxAssetEnd);
		}
		// 获取该相对 path 下的所有 file handle
		FileHandle[] fileHandles = getFileHandles(path);
		for (FileHandle fileHandle : fileHandles) {
			String pathAbsolute = fileHandle.path();
			if (fileHandle.isDirectory()) { // 是目录，继续遍历
				loadResNames(pathAbsolute);
			} else {// 是文件
				// 处理文件名中包含 【.】 的字符替换为 【_】
				String fileName = fileHandle.name().replaceAll("[\\.\\-]", "_");
				// 若是 mac 下生成的 .DS_Store 文件则跳过
				if (fileName.contains("DS_Store")) {
					continue;
				}
				// 数字打头的文件，需要处理下
				if (Character.isDigit(fileName.toCharArray()[0])) {
					fileName = "_" + fileName;
				}

				// 路径转换为 assets 目录下的相对 path
				int assetEnd = pathAbsolute.lastIndexOf(ResRoot);

				// 文件夹路径基于 assets
				String folderPath = pathAbsolute.substring(assetEnd + ResRoot.length() + 1, pathAbsolute.indexOf(fileHandle.name()));
				folderPath = folderPath.replaceAll("/", "");

				String key = folderPath + (StrUtil.isBlank(folderPath) ? "" : "_") + fileName;
				String value = pathAbsolute.substring(assetEnd + ResRoot.length() + 1);
				resMap.put(key, value);
			}
		}
	}

	/** 读取文件下所有文件 */
	private static FileHandle[] getFileHandles(String fileName) {
		if (Gdx.app.getType() == ApplicationType.Desktop) {
			try {
				File directory = new File("");// 参数为空
				String courseFile = directory.getCanonicalPath();
				courseFile = courseFile.replace("desktop", "android");
				File assetFile = new File(courseFile + File.separator + ResRoot + File.separator + fileName);
				File[] files = assetFile.listFiles();
				if (files == null)
					return new FileHandle[]{};
				FileHandle[] handles = new FileHandle[files.length];
				for (int i = 0, l = files.length; i < l; i++) {
					handles[i] = new FileHandle(files[i]);
				}
				return handles;
			} catch (IOException ioException) {
				Gdx.app.error(TAG, "load assets error :" + ioException.getLocalizedMessage());
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
}
