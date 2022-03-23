package com.gitee.xiezengcheng.fluent.mongo.wraper;

import com.gitee.xiezengcheng.fluent.mongo.reflection.SerializableFunction;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Collection;

/**
 * 查询语句生成器 AND连接
 *
 */
public class CriteriaAndWrapper extends CriteriaWrapper {

	public CriteriaAndWrapper() {
		andLink = true;
	}

	public CriteriaAndWrapper and(Criteria criteria) {
		list.add(criteria);
		return this;
	}

	public CriteriaAndWrapper and(CriteriaWrapper criteriaWrapper) {
		list.add(criteriaWrapper.build());
		return this;
	}

	public CriteriaAndWrapper and() {
		return this;
	}


	/**
	 * 等于
	 * 
	 * @param key 字段
	 * @param value 值
	 * @return CriteriaWrapper
	 */
	@Override
	public <T, R> CriteriaAndWrapper is(SerializableFunction<T, R> key, Object value) {
		super.is(key, value);
		return this;
	}


	/**
	 * 不等于
	 * 
	 * @param key 字段
	 * @param value 值
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <T, R> CriteriaAndWrapper ne(SerializableFunction<T, R> key, Object value) {
		super.ne(key, value);
		return this;
	}


	/**
	 * 小于
	 * 
	 * @param key 字段
	 * @param value 值
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <T, R> CriteriaAndWrapper lt(SerializableFunction<T, R> key, Object value) {
		super.lt(key, value);
		return this;
	}


	/**
	 * 小于或等于
	 * 
	 * @param key 字段
	 * @param value 值
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <T, R> CriteriaAndWrapper lte(SerializableFunction<T, R> key, Object value) {
		super.lte(key, value);
		return this;
	}


	/**
	 * 大于
	 * 
	 * @param key 字段
	 * @param value 值
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <T, R> CriteriaAndWrapper gt(SerializableFunction<T, R> key, Object value) {
		super.gt(key, value);
		return this;
	}


	/**
	 * 大于或等于
	 * 
	 * @param key 字段
	 * @param value 值
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <T, R> CriteriaAndWrapper gte(SerializableFunction<T, R> key, Object value) {
		super.gte(key, value);
		return this;
	}



	/**
	 * 满足包含values中的某一个
	 * 
	 * @param key 字段
	 * @param values 值
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <T, R> CriteriaAndWrapper any(SerializableFunction<T, R> key, Collection<?> values) {
		super.any(key, values);
		return this;
	}



	/**
	 * 满足包含values中的全部
	 * 
	 * @param key 字段
	 * @param values 值
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <T, R> CriteriaAndWrapper all(SerializableFunction<T, R> key, Collection<?> values) {
		super.all(key, values);
		return this;
	}


	/**
	 * 相似于
	 * 
	 * @param key 字段
	 * @param value 值
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <T, R> CriteriaAndWrapper like(SerializableFunction<T, R> key, String value) {
		super.like(key, value);
		return this;
	}



	/**
	 * 在其中
	 * 
	 * @param key 字段
	 * @param values 值
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <T, R> CriteriaAndWrapper in(SerializableFunction<T, R> key, Collection<?> values) {
		super.in(key, values);
		return this;
	}



	/**
	 * 不在其中
	 * 
	 * @param key 字段
	 * @param values 值
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <T, R> CriteriaAndWrapper nin(SerializableFunction<T, R> key, Collection<?> values) {
		super.nin(key, values);
		return this;
	}



	/**
	 * 为空
	 * 
	 *
	 * @param key 字段
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <T, R> CriteriaAndWrapper isNull(SerializableFunction<T, R> key) {
		super.isNull(key);
		return this;
	}


	/**
	 * 不为空
	 * 
	 *
	 * @param key 字段
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <T, R> CriteriaAndWrapper isNotNull(SerializableFunction<T, R> key) {
		super.isNotNull(key);
		return this;
	}


	/**
	 * 数组查询
	 * 例如：实体中有一集合，包含三个元素，A、B、C，实现至少含有其中一个元素就可以查询到此实体的时候就需要使用elemMatch来进行查询
	 * @value collectionKey 集合的字段名
	 * @value key 集合中的字段名
	 * @value value 字段值
	 * @return
	 */
	@Override
	public <T, R, M, E> CriteriaAndWrapper findInCollection(SerializableFunction<T, R> collectionKey, SerializableFunction<M, E> key, Object value){
		super.findInCollection(collectionKey, key, value);
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
	@Override
	public <T, R, M, E> CriteriaAndWrapper findInCollectionLike(SerializableFunction<T, R> collectionKey, SerializableFunction<M, E> key, String value){
		super.findInCollectionLike(collectionKey, key, value);
		return this;
	}
}
