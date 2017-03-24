package com.badlogic.gdx.ingenuity.protocol;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;

/**
 * @作者 Mitkey
 * @时间 2017年3月24日 下午2:18:51
 * @类说明:
 * @版本 xx
 */
public class Cmd {

	private static final Map<Integer, String> cmd2Names = new HashMap<Integer, String>();

	@FieldMeta("测试")
	public static final int test = 1;

	@FieldMeta("登录")
	public static final int login = 2;

	@FieldMeta("注册")
	public static final int register = 3;

	static {
		try {
			for (Field field : ClassReflection.getFields(Cmd.class)) {
				if (field.isAnnotationPresent(FieldMeta.class)) {
					FieldMeta fieldMeta = field.getDeclaredAnnotation(FieldMeta.class).getAnnotation(FieldMeta.class);
					if (fieldMeta != null) {
						Integer cmd = (Integer) field.get(null);
						if (cmd2Names.containsKey(cmd)) {
							throw new RuntimeException("cmd 重复 " + cmd);
						} else {
							cmd2Names.put(cmd, fieldMeta.value());
						}
					}
				}
			}
		} catch (ReflectionException e) {
			e.printStackTrace();
		}
	}

	public static String cmd2Name(int cmd) {
		String name = cmd2Names.get(cmd);
		return name != null ? name : ("未定义指令 " + cmd);
	}

	// =====================================

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD, ElementType.METHOD})
	@Documented
	public @interface FieldMeta {
		/** 字段描述 */
		String value();
	}

	// =====================================

}
