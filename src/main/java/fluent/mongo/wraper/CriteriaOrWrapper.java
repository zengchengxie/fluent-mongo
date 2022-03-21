package fluent.mongo.wraper;

import fluent.mongo.reflection.ReflectionUtil;
import fluent.mongo.reflection.SerializableFunction;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Collection;

/**
 * 查询语句生成器 OR连接
 *
 */
public class CriteriaOrWrapper extends CriteriaWrapper {
	
	public CriteriaOrWrapper() {
		andLink = false;
	}
	
	public CriteriaOrWrapper or(Criteria criteria) {
		list.add(criteria);
		return this;
	}

	public CriteriaOrWrapper or(CriteriaWrapper criteriaWrapper) {
		list.add(criteriaWrapper.build());
		return this;
	}

	public CriteriaOrWrapper or() {
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
	public <T, R> CriteriaOrWrapper is(SerializableFunction<T, R> key, Object value) {
		super.is(key, value);
		return this;
	}


	/**
	 * 不等于
	 * 
	 * @param key 字段
	 * @param value 值
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <T, R> CriteriaOrWrapper ne(SerializableFunction<T, R> key, Object value) {
		super.ne(key, value);
		return this;
	}



	/**
	 * 小于
	 * 
	 * @param key 字段
	 * @param value 值
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <T, R> CriteriaOrWrapper lt(SerializableFunction<T, R> key, Object value) {
		super.lt(key, value);
		return this;
	}



	/**
	 * 小于或等于
	 * 
	 * @param key 字段
	 * @param value 值
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <T, R> CriteriaOrWrapper lte(SerializableFunction<T, R> key, Object value) {
		super.lte(key, value);
		return this;
	}



	/**
	 * 大于
	 * 
	 * @param key 字段
	 * @param value 值
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <T, R> CriteriaOrWrapper gt(SerializableFunction<T, R> key, Object value) {
		super.gt(key, value);
		return this;
	}



	/**
	 * 大于或等于
	 * 
	 * @param key 字段
	 * @param value 值
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <T, R> CriteriaOrWrapper gte(SerializableFunction<T, R> key, Object value) {
		super.gte(key, value);
		return this;
	}




	/**
	 * 满足values中某一个相等
	 * 
	 * @param key 字段
	 * @param values 值
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <T, R> CriteriaOrWrapper any(SerializableFunction<T, R> key, Collection<?> values) {
		super.any(key, values);
		return this;
	}




	/**
	 * values中全部相等
	 * 
	 * @param key 字段
	 * @param values 值
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <T, R> CriteriaOrWrapper all(SerializableFunction<T, R> key, Collection<?> values) {
		super.all(key, values);
		return this;
	}



	/**
	 * 相似于
	 * 
	 * @param key 字段
	 * @param value 值
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <T, R> CriteriaOrWrapper like(SerializableFunction<T, R> key, String value) {
		super.like(key, value);
		return this;
	}



	/**
	 * 在其中
	 * 
	 * @param key 字段
	 * @param values 值
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <T, R> CriteriaOrWrapper in(SerializableFunction<T, R> key, Collection<?> values) {
		super.in(key, values);
		return this;
	}


	/**
	 * 不在其中
	 * 
	 * @param key 字段
	 * @param values 值
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <T, R> CriteriaOrWrapper nin(SerializableFunction<T, R> key, Collection<?> values) {
		super.nin(key, values);
		return this;
	}



	/**
	 * 为空
	 * 
	 *
	 * @param key 字段
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <T, R> CriteriaOrWrapper isNull(SerializableFunction<T, R> key) {
		super.isNull(key);
		return this;
	}


	/**
	 * 不为空
	 * 
	 *
	 * @param key 字段
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <T, R> CriteriaOrWrapper isNotNull(SerializableFunction<T, R> key) {
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
	public <T, R, M, E> CriteriaOrWrapper findInCollection(SerializableFunction<T, R> collectionKey, SerializableFunction<M, E> key, Object value){
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
	public <T, R, M, E> CriteriaOrWrapper findInCollectionLike(SerializableFunction<T, R> collectionKey, SerializableFunction<M, E> key, String value){
		super.findInCollectionLike(collectionKey, key, value);
		return this;
	}

}
