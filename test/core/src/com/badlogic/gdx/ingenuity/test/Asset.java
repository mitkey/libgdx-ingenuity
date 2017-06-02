package com.badlogic.gdx.ingenuity.test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;

/**
 * @作者 mitkey
 * @时间 2017年5月20日 下午6:23:48
 * @类说明 Assets.java <br/>
 * @版本 0.0.1
 */
public enum Asset {

	none() {
		@Override
		public Set<String> names() {
			Set<String> emptySet = Collections.emptySet();
			return Collections.unmodifiableSet(emptySet);
		}
	},

	common() {
		@Override
		public Set<String> names() {
			return Collections.unmodifiableSet(parseAssetsByGdxR("def"));
		}
	};

	/** 资源名集（包含路径如：data/loading.bg */
	public abstract Set<String> names();

	/** 根据给定 {@link GdxR} 中字段名的前缀解析资源（资源路径） */
	public static Set<String> parseAssetsByGdxR(String... fieldPrefixs) {
		Set<String> result = new HashSet<String>();
		try {
			Field[] fields = ClassReflection.getFields(GdxR.class);
			for (Field field : fields) {
				String name = field.getName();
				String path = field.get(null).toString();
				for (String prefixs : fieldPrefixs) {
					if (name.startsWith(prefixs)) {
						result.add(path);
					}
				}
			}
		} catch (ReflectionException e) {
			e.printStackTrace();
		}
		return result;
	}

}
