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

	/**
	 * 等于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaWrapper
	 */
	@Override
	public <E, R> CriteriaOrWrapper eq(SerializableFunction<E, R> column, Object params) {
		super.eq(column, params);
		return this;
	}


	/**
	 * 不等于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <E, R> CriteriaOrWrapper ne(SerializableFunction<E, R> column, Object params) {
		super.ne(column, params);
		return this;
	}



	/**
	 * 小于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <E, R> CriteriaOrWrapper lt(SerializableFunction<E, R> column, Object params) {
		super.lt(column, params);
		return this;
	}



	/**
	 * 小于或等于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <E, R> CriteriaOrWrapper lte(SerializableFunction<E, R> column, Object params) {
		super.lte(column, params);
		return this;
	}



	/**
	 * 大于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <E, R> CriteriaOrWrapper gt(SerializableFunction<E, R> column, Object params) {
		super.gt(column, params);
		return this;
	}



	/**
	 * 大于或等于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <E, R> CriteriaOrWrapper gte(SerializableFunction<E, R> column, Object params) {
		super.gte(column, params);
		return this;
	}



	/**
	 * 包含
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <E, R> CriteriaOrWrapper contain(SerializableFunction<E, R> column, Object params) {
		super.contain(column, params);
		return this;
	}



	/**
	 * 包含,以或连接
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <E, R> CriteriaOrWrapper containOr(SerializableFunction<E, R> column, Collection<?> params) {
		super.containOr(column, params);
		return this;
	}



	/**
	 * 包含,以或连接
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <E, R> CriteriaOrWrapper containOr(SerializableFunction<E, R> column, Object[] params) {
		super.containOr(column, params);
		return this;
	}



	/**
	 * 包含,以且连接
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <E, R> CriteriaOrWrapper containAnd(SerializableFunction<E, R> column, Collection<?> params) {
		super.containAnd(column, params);
		return this;
	}



	/**
	 * 包含,以且连接
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <E, R> CriteriaOrWrapper containAnd(SerializableFunction<E, R> column, Object[] params) {
		super.containAnd(column, params);
		return this;
	}



	/**
	 * 相似于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <E, R> CriteriaOrWrapper like(SerializableFunction<E, R> column, Object params) {
		super.like(column, params);
		return this;
	}



	/**
	 * 在其中
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <E, R> CriteriaOrWrapper in(SerializableFunction<E, R> column, Collection<?> params) {
		super.in(column, params);
		return this;
	}

	/**
	 * 在其中
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <E, R> CriteriaOrWrapper in(SerializableFunction<E, R> column, Object[] params) {
		super.in(column, params);
		return this;
	}



	/**
	 * 不在其中
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <E, R> CriteriaOrWrapper nin(SerializableFunction<E, R> column, Collection<?> params) {
		super.nin(column, params);
		return this;
	}



	/**
	 * 不在其中
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <E, R> CriteriaOrWrapper nin(SerializableFunction<E, R> column, Object[] params) {
		super.nin(column, params);
		return this;
	}


	/**
	 * 为空
	 * 
	 *
	 * @param column 字段
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <E, R> CriteriaOrWrapper isNull(SerializableFunction<E, R> column) {
		super.isNull(column);
		return this;
	}


	/**
	 * 不为空
	 * 
	 *
	 * @param column 字段
	 * @return CriteriaOrWrapper
	 */
	@Override
	public <E, R> CriteriaOrWrapper isNotNull(SerializableFunction<E, R> column) {
		super.isNotNull(column);
		return this;
	}


	/**
	 * 数组查询
	 * 例如：实体中有一集合，包含三个元素，A、B、C，实现至少含有其中一个元素就可以查询到此实体的时候就需要使用elemMatch来进行查询
	 * 
	 * @param arr    数组名
	 * @param column 字段名
	 * @param param  字段值
	 * @return
	 */
	public <E, R> CriteriaOrWrapper findArray(SerializableFunction<E, R> arr, SerializableFunction<E, R> column, Object param) {
		super.findArray(ReflectionUtil.getFieldName(arr), column, param);
		return this;
	}


	/**
	 * 数组模糊查询
	 * 例如：实体中有一集合，包含三个元素，A、B、C，实现至少含有其中一个元素就可以查询到此实体的时候就需要使用elemMatch来进行查询
	 * 
	 * @param arr    数组名
	 * @param column 字段名
	 * @param param  字段值
	 * @return
	 */
	public <E, R> CriteriaOrWrapper findArrayLike(SerializableFunction<E, R> arr, SerializableFunction<E, R> column, Object param) {
		super.findArrayLike(ReflectionUtil.getFieldName(arr), column, param);
		return this;
	}
}
