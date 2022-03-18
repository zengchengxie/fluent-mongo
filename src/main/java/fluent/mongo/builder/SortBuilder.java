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

	public <T, R> SortBuilder add(SerializableFunction<T, R> key, Direction direction) {
		Order order = new Order(direction, ReflectionUtil.getFieldName(key));
		orderList.add(order);
		return this;
	}

	public Sort build() {
		return Sort.by(orderList);
	}

}
