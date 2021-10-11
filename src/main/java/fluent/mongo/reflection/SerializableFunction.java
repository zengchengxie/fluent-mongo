package fluent.mongo.reflection;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author xiezengcheng
 */
@FunctionalInterface
public interface SerializableFunction<E, R> extends Function<E, R>, Serializable {


}