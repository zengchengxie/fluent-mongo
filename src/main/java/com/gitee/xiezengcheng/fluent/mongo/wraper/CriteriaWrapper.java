package com.gitee.xiezengcheng.fluent.mongo.wraper;

import com.gitee.xiezengcheng.fluent.mongo.reflection.ReflectionUtil;
import com.gitee.xiezengcheng.fluent.mongo.reflection.SerializableFunction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 查询语句生成器
 * 
 * @author CYM
 *
 */
public abstract class CriteriaWrapper {
	boolean andLink = true;

	Criteria criteria;
	List<Criteria> list = new ArrayList<Criteria>();

	/**
	 * 将Wrapper转化为Criteria
	 * 
	 * @return Criteria
	 */
	public Criteria build() {
		criteria = new Criteria();
		if (list.size() > 0) {
			if (andLink) {
				criteria.andOperator(listToArray(list));
			} else {
				criteria.orOperator(listToArray(list));
			}
		}
		return criteria;
	}

	/**
	 * 将Wrapper转化为Query
	 *
	 * @return Criteria
	 */
	public Query buildQuery() {
		criteria = new Criteria();
		if (list.size() > 0) {
			if (andLink) {
				criteria.andOperator(listToArray(list));
			} else {
				criteria.orOperator(listToArray(list));
			}
		}
		return new Query(criteria);
	}


	private Criteria[] listToArray(List<Criteria> list) {
		return list.toArray(new Criteria[list.size()]);
	}



	/**
	 * 等于
	 * 
	 * @value key 字段
	 * @value value 值
	 * @return CriteriaWrapper
	 */
	public <T, R> CriteriaWrapper is(SerializableFunction<T, R> key, Object value) {
		list.add(Criteria.where(ReflectionUtil.getFieldName(key)).is(value));
		return this;
	}

	/**
	 * 不等于
	 * 
	 * @value key 字段
	 * @value value 值
	 * @return CriteriaWrapper
	 */
	public <T, R> CriteriaWrapper ne(SerializableFunction<T, R> key, Object value) {
		list.add(Criteria.where(ReflectionUtil.getFieldName(key)).ne(value));
		return this;
	}

	/**
	 * 小于
	 * 
	 * @value key 字段
	 * @value value 值
	 * @return CriteriaWrapper
	 */
	public <T, R> CriteriaWrapper lt(SerializableFunction<T, R> key, Object value) {
		list.add(Criteria.where(ReflectionUtil.getFieldName(key)).lt(value));
		return this;
	}

	/**
	 * 小于或等于
	 * 
	 * @value key 字段
	 * @value value 值
	 * @return CriteriaWrapper
	 */
	public <T, R> CriteriaWrapper lte(SerializableFunction<T, R> key, Object value) {
		list.add(Criteria.where(ReflectionUtil.getFieldName(key)).lte(value));
		return this;
	}

	/**
	 * 大于
	 * 
	 * @value key 字段
	 * @value value 值
	 * @return CriteriaWrapper
	 */
	public <T, R> CriteriaWrapper gt(SerializableFunction<T, R> key, Object value) {
		list.add(Criteria.where(ReflectionUtil.getFieldName(key)).gt(value));
		return this;
	}

	/**
	 * 大于或等于
	 * 
	 * @value key 字段
	 * @value value 值
	 * @return CriteriaWrapper
	 */
	public <T, R> CriteriaWrapper gte(SerializableFunction<T, R> key, Object value) {
		list.add(Criteria.where(ReflectionUtil.getFieldName(key)).gte(value));
		return this;
	}


	/**
	 *
	 * 某一个相等
	 *
	 * @value key 字段
	 * @value values 值
	 * @return CriteriaWrapper
	 */
	public <T, R> CriteriaWrapper any(SerializableFunction<T, R> key, Collection<?> values) {
		CriteriaOrWrapper criteriaOrWrapper = new CriteriaOrWrapper();
		for (Object value : values) {
			criteriaOrWrapper.is(key, value);
		}
		list.add(criteriaOrWrapper.build());
		return this;
	}


	/**
	 * 全部相等
	 * @value key 字段
	 * @value values 值
	 * @return CriteriaWrapper
	 */
	public <T, R> CriteriaWrapper all(SerializableFunction<T, R> key, Collection<?> values) {
		list.add(Criteria.where(ReflectionUtil.getFieldName(key)).all(values));
		return this;
	}


	/**
	 * 相似于
	 * 
	 * @value key 字段
	 * @value value 值
	 * @return CriteriaWrapper
	 */
	public <T, R> CriteriaWrapper like(SerializableFunction<T, R> key, String value) {
		Pattern pattern = Pattern.compile("^.*" + value + ".*$", Pattern.CASE_INSENSITIVE);
		list.add(Criteria.where(ReflectionUtil.getFieldName(key)).regex(pattern));
		return this;
	}

	/**
	 * 在其中
	 * 
	 * @value key 字段
	 * @value values 值
	 * @return CriteriaWrapper
	 */
	public <T, R> CriteriaWrapper in(SerializableFunction<T, R> key, Collection<?> values) {
		list.add(Criteria.where(ReflectionUtil.getFieldName(key)).in(values));
		return this;
	}


	/**
	 * 不在其中
	 * 
	 * @value key 字段
	 * @value values 值
	 * @return CriteriaWrapper
	 */
	public <T, R> CriteriaWrapper nin(SerializableFunction<T, R> key, Collection<?> values) {
		list.add(Criteria.where(ReflectionUtil.getFieldName(key)).nin(values));
		return this;
	}

	/**
	 * 为null
	 *
	 * @value key 字段
	 * @return CriteriaWrapper
	 */
	public <T, R> CriteriaWrapper isNull(SerializableFunction<T, R> key) {
		list.add(Criteria.where(ReflectionUtil.getFieldName(key)).is(null));
		return this;
	}

	/**
	 * 不为空
	 * 
	 *
	 * @value key 字段
	 * @return CriteriaWrapper
	 */
	public <T, R> CriteriaWrapper isNotNull(SerializableFunction<T, R> key) {
		list.add(Criteria.where(ReflectionUtil.getFieldName(key)).ne(null));
		return this;
	}

	/**
	 * 数组查询
	 * 例如：实体中有一集合，包含三个元素，A、B、C，实现至少含有其中一个元素就可以查询到此实体的时候就需要使用elemMatch来进行查询
	 *
	 * @return
	 * @value collectionKey 集合的字段名
	 * @value key 集合中的字段名
	 * @value value 字段值
	 */
	public <T, R, M, E> CriteriaWrapper findInCollection(SerializableFunction<T, R> collectionKey, SerializableFunction<M, E> key, Object value) {
		list.add(Criteria.where(ReflectionUtil.getFieldName(collectionKey)).elemMatch(Criteria.where(ReflectionUtil.getFieldName(key)).is(value)));
		return this;
	}

	/**
	 * 数组模糊查询
	 * 例如：实体中有一集合，包含三个元素，A、B、C，实现至少含有其中一个元素就可以查询到此实体的时候就需要使用elemMatch来进行查询
	 * @value collectionKey 集合的字段名
	 * @value key 集合中的字段名
	 * @value value 字段值
	 * @return
	 */
	public <T, R, M, E> CriteriaWrapper findInCollectionLike(SerializableFunction<T, R> collectionKey, SerializableFunction<M, E> key, String value){
		Pattern pattern = Pattern.compile("^.*" + value + ".*$", Pattern.CASE_INSENSITIVE);
		list.add(Criteria.where(ReflectionUtil.getFieldName(collectionKey)).elemMatch(Criteria.where(ReflectionUtil.getFieldName(key)).regex(pattern)));
		return this;
	}

}
