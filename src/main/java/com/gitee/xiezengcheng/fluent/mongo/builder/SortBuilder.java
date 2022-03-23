package com.gitee.xiezengcheng.fluent.mongo.builder;

import cn.hutool.core.collection.CollectionUtil;
import com.gitee.xiezengcheng.fluent.mongo.reflection.ReflectionUtil;
import com.gitee.xiezengcheng.fluent.mongo.reflection.SerializableFunction;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xiezengcheng
 */
public class SortBuilder {

	private List<Order> orderList = new ArrayList<>();

	public SortBuilder() {

	}

	public SortBuilder(List<Order> orderList) {
		this.orderList.addAll(orderList);
	}

	public <T, R> SortBuilder(SerializableFunction<T, R> key, Direction direction) {
		Order order = new Order(direction, ReflectionUtil.getFieldName(key));
		orderList.add(order);
	}

	@SafeVarargs
	public <T, R> SortBuilder(Direction direction, SerializableFunction<T, R>... keys) {

		if (keys != null && keys.length > 0) {
			for (SerializableFunction<T, R> key : keys) {
				Order order = new Order(direction, ReflectionUtil.getFieldName(key));
				orderList.add(order);
			}
		}
	}


	public <T, R> SortBuilder(List<SerializableFunction<T, R>> keys, Direction direction) {

		if (CollectionUtil.isNotEmpty(keys)) {
			for (SerializableFunction<T, R> key : keys) {
				Order order = new Order(direction, ReflectionUtil.getFieldName(key));
				orderList.add(order);
			}

		}
	}

	public <T, R> SortBuilder add(SerializableFunction<T, R> key, Direction direction) {
		Order order = new Order(direction, ReflectionUtil.getFieldName(key));
		orderList.add(order);
		return this;
	}

	public <T, R> SortBuilder addAll(List<SerializableFunction<T, R>> keys, Direction direction) {
		if (CollectionUtil.isEmpty(keys)) {
			return this;
		}

		for (SerializableFunction<T, R> key : keys) {
			Order order = new Order(direction, ReflectionUtil.getFieldName(key));
			orderList.add(order);
		}

		return this;
	}


	@SafeVarargs
	public final <T, R> SortBuilder addAll(Direction direction, SerializableFunction<T, R>... keys) {

		if (keys == null || keys.length == 0) {
			return this;
		}

		for (SerializableFunction<T, R> key : keys) {
			Order order = new Order(direction, ReflectionUtil.getFieldName(key));
			orderList.add(order);
		}

		return this;
	}

	public Sort build() {
		return Sort.by(orderList);
	}

}
