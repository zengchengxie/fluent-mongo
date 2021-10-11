package fluent.mongo.builder;

import fluent.mongo.reflection.ReflectionUtil;
import fluent.mongo.reflection.SerializableFunction;
import org.springframework.data.mongodb.core.query.Update;

public class UpdateBuilder {

	Update update = new Update();

	public UpdateBuilder() {

	}

	public <E, R> UpdateBuilder(SerializableFunction<E, R> key, R value) {
		update.set(ReflectionUtil.getFieldName(key), value);
	}


	public <E, R> UpdateBuilder set(SerializableFunction<E, R> key, R value) {
		update.set(ReflectionUtil.getFieldName(key), value);
		return this;
	}

	public <E, R> UpdateBuilder inc(SerializableFunction<E, R> key, Number count) {
		update.inc(ReflectionUtil.getFieldName(key), count);
		return this;
	}

	public Update toUpdate() {
		return update;
	}

}
