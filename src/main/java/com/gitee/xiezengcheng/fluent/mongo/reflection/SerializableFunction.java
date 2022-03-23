package com.gitee.xiezengcheng.fluent.mongo.reflection;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author xiezengcheng
 */
@FunctionalInterface
public interface SerializableFunction<T, R> extends Function<T, R>, Serializable {


}