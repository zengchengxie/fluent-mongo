package fluent.mongo.reflection;

import cn.hutool.core.util.StrUtil;
import fluent.mongo.constant.FluentConstant;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.Introspector;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ReflectionUtil {

	private static Map<SerializableFunction<?, ?>, Field> cache = new ConcurrentHashMap<>();

	public static <E, R> String getFieldName(SerializableFunction<E, R> function) {
		Field field = ReflectionUtil.getField(function);

		// @Filed
		org.springframework.data.mongodb.core.mapping.Field fieldAnnotation = field.getAnnotation(org.springframework.data.mongodb.core.mapping.Field.class);
		if (null != fieldAnnotation) {
			String filedValue = fieldAnnotation.value();

			if (StrUtil.isNotBlank(filedValue)) {
				return filedValue;
			}
		}

		return field.getName();
	}

	public static Field getField(SerializableFunction<?, ?> function) {
		return cache.computeIfAbsent(function, ReflectionUtil::findField);
	}


	public static Field findField(SerializableFunction<?, ?> function) {
		Field field = null;
		String fieldName = null;
		try {
			// 第1步 获取SerializedLambda
			Method method = function.getClass().getDeclaredMethod(FluentConstant.WRITE_REPLACE);
			if (null == method) {
				throw new NoSuchMethodException(FluentConstant.SERIALIZABLE_ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE);
			}

			method.setAccessible(Boolean.TRUE);
			SerializedLambda serializedLambda = (SerializedLambda) method.invoke(function);
			// 第2步 implMethodName 即为Field对应的Getter方法名
			String implMethodName = serializedLambda.getImplMethodName();
			if (implMethodName.startsWith(FluentConstant.GET) && implMethodName.length() > FluentConstant.GET_LENGTH) {
				fieldName = Introspector.decapitalize(implMethodName.substring(FluentConstant.GET_LENGTH));
			} else if (implMethodName.startsWith(FluentConstant.IS) && implMethodName.length() > FluentConstant.IS_LENGTH) {
				fieldName = Introspector.decapitalize(implMethodName.substring(FluentConstant.IS_LENGTH));
			} else if (implMethodName.startsWith(FluentConstant.LANBDA$)) {
				throw new IllegalArgumentException(FluentConstant.LANBDA_ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE);
			} else {
				throw new IllegalArgumentException(implMethodName + FluentConstant.GETTER_ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE);
			}
			// 第3步 获取的Class是字符串，并且包名是“/”分割，需要替换成“.”，才能获取到对应的Class对象
			String declaredClass = serializedLambda.getImplClass().replace("/", ".");

			Class<?> aClass = Class.forName(declaredClass, false, ClassUtils.getDefaultClassLoader());

			// 第4步 Spring 中的反射工具类获取Class中定义的Field
			field = ReflectionUtils.findField(aClass, fieldName);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 第5步 如果没有找到对应的字段应该抛出异常
		if (field != null) {
			return field;
		}

		throw new NoSuchFieldError(fieldName);
	}
	
	
}