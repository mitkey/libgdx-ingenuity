
package com.badlogic.gdx.ingenuity.utils.helper;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.ingenuity.GdxGame;

/**
 * @作者 mitkey
 * @时间 2017年5月19日 下午12:30:41
 * @类说明 RHelper.java <br/>
 * @版本 0.0.1
 */
public class RHelper {

	private static final String TAG = RHelper.class.getSimpleName();

	// 资源文件 R 映射类名
	private static final String R_JAVA_NAME = "GdxR";

	// 包名格式化字符
	private static final String STR_FORMAT_PACKAGE = "package %s;\n\n";

	// 类声明格式化字符
	private static final String STR_FORMAT_CLASS = "public static final class %s {\n\n";

	// 资源映射字段格式化字符
	private static final String STR_FORMAT_FIELD = "public static final String %s = \"%s\";\n";

	/** 自动读写资源路径 */
	public static void generated() {
		if (checkCanCreateGdxR()) {
			String content = generateRjavaContent();
			writeRjava(content);
			Gdx.app.debug(TAG, "generated GdxR.java finished");
		}
	}

	/** 检测是否能且有必要生成 GdxR.java 文件 */
	private static boolean checkCanCreateGdxR() {
		try {
			String protocol = RHelper.class.getResource("").getProtocol();
			if ("file".equals(protocol)) {
				return true;
			}
		} catch (Exception e) {
			Gdx.app.error(TAG, "check can create GdxR.java fail", e);
		}
		return false;
	}

	private static String generateRjavaContent() {
		StringBuffer stringBuffer = new StringBuffer();
		// 包名
		stringBuffer.append(String.format(STR_FORMAT_PACKAGE, GdxGame.class.getPackage().getName()));
		// 类声明
		stringBuffer.append(String.format(STR_FORMAT_CLASS, R_JAVA_NAME));

		// 加载所有的资源名
		Map<String, List<AssetsNames>> result = loadAssetsDirectory(true, new LinkedHashMap<String, List<AssetsNames>>(), parseAssetsRootPath());
		Iterator<Entry<String, List<AssetsNames>>> iterator = result.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, List<AssetsNames>> entry = iterator.next();
			for (AssetsNames element : entry.getValue()) {
				stringBuffer.append("\t").append(String.format(STR_FORMAT_FIELD, element.name, element.filePath));
			}
			if (entry.getValue().size() > 0) {
				stringBuffer.append("\n");
			}
		}

		// 类结束
		stringBuffer.append("}");
		return stringBuffer.toString();
	}

	private static Map<String, List<AssetsNames>> loadAssetsDirectory(boolean isRoot, Map<String, List<AssetsNames>> result, String path) {
		File file = new File(path);
		// 该路径不存在
		if (!file.exists()) {
			Gdx.app.error(TAG, "loadAssetsNames fail, file no exists:" + file.getAbsolutePath());
			return result;
		}
		// 不是目录
		if (!file.isDirectory()) {
			Gdx.app.error(TAG, "loadAssetsNames fail, file no directory:" + file.getAbsolutePath());
			return result;
		}

		List<AssetsNames> list = new LinkedList<AssetsNames>();
		// 文件夹名
		String strFolderName = isRoot ? "def" : path.substring(path.lastIndexOf("\\") + 1);
		for (File temp : file.listFiles()) {
			if (temp.isHidden()) {
				// 隐藏文件跳过
				continue;
			}
			if (temp.isDirectory()) {
				// 文件夹则递归
				loadAssetsDirectory(false, result, temp.getAbsolutePath());
			} else {
				// 文件
				String fileName = temp.getName();
				if (fileName.equals(".DS_Store")) {
					continue;
				}
				// 文件名中【.】 和 【-】 字符替换为 【_】
				fileName = fileName.replaceAll("[\\.\\-]", "_");
				// 数字打头的文件，需要处理下
				if (Character.isDigit(fileName.toCharArray()[0])) {
					fileName = "_" + fileName;
				}

				// 资源对应的字段名
				String name = strFolderName + "_" + fileName;
				// E:\git-repository\libgdx-ingenuity\android\assets\asdasd\xxx.txt 替换为 asdasd\xxx.txt
				String filePath = temp.getAbsolutePath().replace(parseAssetsRootPath() + "\\", "");
				filePath = filePath.replaceAll("\\\\", "/");
				list.add(new AssetsNames(name, filePath));
			}
		}
		result.put(strFolderName, list);
		return result;
	}

	private static String parseAssetsRootPath() {
		// core 中启动调用 E:\git-repository\libgdx-ingenuity\core
		// desktop 中启动调用 E:\git-repository\libgdx-ingenuity\desktop
		String absolutePath = new File("").getAbsolutePath();
		// E:\git-repository\libgdx-ingenuity
		absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("\\"));
		// E:\git-repository\libgdx-ingenuity\android\assets
		absolutePath = absolutePath + "/android/assets";
		return absolutePath;
	}

	private static String parseGdxRjavaPath() {
		// core 中启动调用 E:\git-repository\libgdx-ingenuity\core
		// desktop 中启动调用 E:\git-repository\libgdx-ingenuity\desktop
		String srcPath = new File("").getAbsolutePath();
		// E:\git-repository\libgdx-ingenuity
		srcPath = srcPath.substring(0, srcPath.lastIndexOf("\\"));
		// E:\git-repository\libgdx-ingenuity\core\src
		srcPath = srcPath + "/core/src";
		// E:/git-repository/libgdx-ingenuity/core/src
		srcPath = srcPath.replaceAll("\\\\", "/");

		// com.badlogic.gdx.ingenuity 转换为 com/badlogic/gdx/ingenuity
		String packPath = GdxGame.class.getPackage().getName().replaceAll("\\.", "/");
		// E:/git-repository/libgdx-ingenuity/core/src/com/badlogic/gdx/ingenuity/GdxR.java
		return srcPath + "/" + packPath + "/" + R_JAVA_NAME + ".java";
	}

	private static void writeRjava(String content) {
		// 写入
		FileHandle fileHandle = new FileHandle(new File(parseGdxRjavaPath()));
		if (fileHandle.exists() && content.equals(fileHandle.readString())) {
			Gdx.app.debug(TAG, "write cancel(content the same) " + R_JAVA_NAME + " --> " + fileHandle);
		} else {
			fileHandle.writeString(content, false, "UTF-8");
			Gdx.app.debug(TAG, "write success " + R_JAVA_NAME + " --> " + fileHandle);
		}
	}

	private static class AssetsNames {
		public String name;
		public String filePath;

		public AssetsNames(String name, String filePath) {
			super();
			this.name = name;
			this.filePath = filePath;
		}
	}

}
