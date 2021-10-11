package fluent.mongo.builder;

import fluent.mongo.reflection.ReflectionUtil;
import fluent.mongo.reflection.SerializableFunction;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiezengcheng
 */
public class SortBuilder {

	List<Order> orderList = new ArrayList<>();

	public SortBuilder() {
		
	}

	public SortBuilder(List<Order> orderList) {
		this.orderList.addAll(orderList);
	}

	public <E, R> SortBuilder(SerializableFunction<E, R> column, Direction direction) {
		Order order = new Order(direction, ReflectionUtil.getFieldName(column));
		orderList.add(order);
	}

	public <E, R> SortBuilder add(SerializableFunction<E, R> column, Direction direction) {
		Order order = new Order(direction, ReflectionUtil.getFieldName(column));
		orderList.add(order);
		return this;
	}

	public Sort toSort() {
		return Sort.by(orderList);
	}
}
