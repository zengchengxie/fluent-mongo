package com.gitee.xiezengcheng.fluent.mongo.builder;

import com.gitee.xiezengcheng.fluent.mongo.reflection.ReflectionUtil;
import com.gitee.xiezengcheng.fluent.mongo.reflection.SerializableFunction;
import org.springframework.data.mongodb.core.query.Update;


/**
 * @author xiezengcheng
 */
public class UpdateBuilder {

	private Update update = new Update();

	public UpdateBuilder() {

	}

	public <T, R> UpdateBuilder(SerializableFunction<T, R> key, Object value) {
		update.set(ReflectionUtil.getFieldName(key), value);
	}


	public <T, R> UpdateBuilder set(SerializableFunction<T, R> key, Object value) {
		update.set(ReflectionUtil.getFieldName(key), value);
		return this;
	}

	public <T, R> UpdateBuilder inc(SerializableFunction<T, R> key, Number count) {
		update.inc(ReflectionUtil.getFieldName(key), count);
		return this;
	}

	public Update build() {
		return update;
	}

}
