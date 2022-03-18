package fluent.mongo.common;

import fluent.mongo.reflection.ReflectionUtil;
import fluent.mongo.reflection.SerializableFunction;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiezengcheng
 */
public class FluentPageRequest implements Serializable {

    private final Sort sort;
    private final int page;
    private final int size;
    // 是否查询全部数据个数
    private final boolean queryTotalCount;

    private FluentPageRequest(int page, int size, boolean queryTotalCount, Sort sort) {
        if (page < 1) {
            throw new IllegalArgumentException("Page index must not be less than one!");
        } else if (size < 1) {
            throw new IllegalArgumentException("Page size must not be less than one!");
        } else {
            this.page = page;
            this.size = size;
        }
        Assert.notNull(sort, "Sort must not be null!");
        this.sort = sort;
        this.queryTotalCount = queryTotalCount;
    }

    public static FluentPageRequest of(int page, int size) {
        return new FluentPageRequest(page, size, true, Sort.unsorted());
    }

    public static FluentPageRequest of(int page, int size, boolean queryTotalCount) {
        return new FluentPageRequest(page, size, queryTotalCount, Sort.unsorted());
    }

    public static FluentPageRequest of(int page, int size, Sort sort) {
        return new FluentPageRequest(page, size, true,sort);
    }

    public static FluentPageRequest of(int page, int size, boolean queryTotalCount, Sort sort) {
        return new FluentPageRequest(page, size, queryTotalCount, sort);
    }

    public static FluentPageRequest of(int page, int size, boolean queryTotalCount, Sort.Direction direction, String... properties) {
        return of(page, size, queryTotalCount, Sort.by(direction, properties));
    }

    public static FluentPageRequest of(int page, int size, Sort.Direction direction, String... properties) {
        return of(page, size, Sort.by(direction, properties));
    }

    public static <T, R> FluentPageRequest of(int page, int size, Sort.Direction direction, SerializableFunction<T, R>... properties) {

        List<Sort.Order> orderList = new ArrayList<>();
        for (SerializableFunction<T, R> property : properties) {
            Sort.Order order = new Sort.Order(direction, ReflectionUtil.getFieldName(property));
            orderList.add(order);
        }

        return of(page, size, Sort.by(orderList));
    }


    public static <T, R> FluentPageRequest of(int page, int size, boolean queryTotalCount, Sort.Direction direction, SerializableFunction<T, R>... properties) {

        List<Sort.Order> orderList = new ArrayList<>();
        for (SerializableFunction<T, R> property : properties) {
            Sort.Order order = new Sort.Order(direction, ReflectionUtil.getFieldName(property));
            orderList.add(order);
        }

        return of(page, size, queryTotalCount, Sort.by(orderList));
    }

    public static FluentPageRequest ofSize(int pageSize) {
        return of(0, pageSize);
    }

    public static FluentPageRequest ofSize(int pageSize, boolean queryTotalCount) {
        return of(0, pageSize, queryTotalCount);
    }

    public Sort getSort() {
        return this.sort;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public boolean isQueryTotalCount() {
        return queryTotalCount;
    }
}
