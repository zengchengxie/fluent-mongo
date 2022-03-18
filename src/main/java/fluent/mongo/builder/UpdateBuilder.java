package fluent.mongo.builder;

import fluent.mongo.common.FluentPageRequest;
import fluent.mongo.reflection.ReflectionUtil;
import fluent.mongo.reflection.SerializableFunction;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;


public class UpdateBuilder {

	Update update = new Update();

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
