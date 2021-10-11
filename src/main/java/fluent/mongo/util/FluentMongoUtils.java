package fluent.mongo.util;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import fluent.mongo.builder.SortBuilder;
import fluent.mongo.common.FluentBean;
import fluent.mongo.common.FluentPage;
import fluent.mongo.reflection.ReflectionUtil;
import fluent.mongo.reflection.SerializableFunction;
import fluent.mongo.wraper.CriteriaAndWrapper;
import fluent.mongo.wraper.CriteriaWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;


public class FluentMongoUtils {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private final MongoTemplate mongoTemplate;

	public FluentMongoUtils(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}


	// 获取原生的mongoTemplate
	public MongoTemplate mongoTemplate() {
		return mongoTemplate;
	}

	/**
	 * 按查询条件获取Page
	 *
	 * @param fluentPage 分页
	 * @param clazz      类
	 * @param collectionName 集合名称 如果没有则取的是实体类注解 @Document
	 * @return Page 分页
	 */
	public <T> FluentPage<T> findPage(CriteriaWrapper criteriaWrapper, FluentPage<?> fluentPage, Class<T> clazz, String... collectionName) {
		SortBuilder sortBuilder = new SortBuilder(FluentBean::getId, Sort.Direction.DESC);
		return findPage(criteriaWrapper, sortBuilder, fluentPage, clazz, collectionName);
	}

	/**
	 * 按查询条件获取Page
	 *
	 * @param criteriaWrapper 查询
	 * @param sortBuilder     排序
	 * @param clazz           类
	 * @param collectionName 集合名称 如果没有则取的是实体类注解 @Document
	 * @return Page 分页
	 */
	public <T> FluentPage<T> findPage(CriteriaWrapper criteriaWrapper, SortBuilder sortBuilder, FluentPage<?> fluentPage, Class<T> clazz, String... collectionName) {

		FluentPage<T> fluentPageResp = new FluentPage<T>();
		fluentPageResp.setCurr(fluentPage.getCurr());
		fluentPageResp.setLimit(fluentPage.getLimit());

		long count;
		// 查询出总条数
		if (fluentPage.getQueryTotalCount()) {
			if (ArrayUtil.isNotEmpty(collectionName)) {
				count = findCountByQuery(criteriaWrapper, collectionName[0]);
			}else{
				count = findCountByQuery(criteriaWrapper, clazz);
			}

			fluentPageResp.setTotalCount(count);
		}

		// 查询List
		Query query = new Query(criteriaWrapper.build());
		query.with(sortBuilder.toSort());

		// 从那条记录开始
		query.skip((fluentPage.getCurr() - 1) * fluentPage.getLimit());

		// 取多少条记录
		query.limit(fluentPage.getLimit());

		List<T> list = new ArrayList<>();
		if (ArrayUtil.isNotEmpty(collectionName)) {
			list = mongoTemplate.find(query, clazz, collectionName[0]);
		} else {
			list = mongoTemplate.find(query, clazz);
		}

		fluentPageResp.setList(list);
		return fluentPageResp;
	}

	/**
	 * 按查询条件获取Page
	 * 
	 * @param sortBuilder     排序
	 * @param clazz    类
	 * @param collectionName 集合名称 如果没有则取的是实体类注解 @Document
	 * @return Page 分页
	 */
	public <T> FluentPage<T> findPage(SortBuilder sortBuilder, FluentPage<?> fluentPage, Class<T> clazz,String... collectionName) {
		return findPage(new CriteriaAndWrapper(), sortBuilder, fluentPage, clazz, collectionName);
	}

	/**
	 * 获取Page
	 * 
	 * @param fluentPage  分页
	 * @param clazz 类
	 * @param collectionName 集合名称 如果没有则取的是实体类注解 @Document
	 * @return Page 分页
	 */
	public <T> FluentPage<T> findPage(FluentPage<?> fluentPage, Class<T> clazz,String... collectionName) {
		return findPage(new CriteriaAndWrapper(), fluentPage, clazz, collectionName);
	}

	/**
	 * 根据id查找
	 * 
	 * @param id    id
	 * @param clazz 类
	 * @param collectionName 集合名称 如果没有则取的是实体类注解 @Document
	 * @return Optional<T> 对象
	 */
	public <T> Optional<T> findById(String id, Class<T> clazz,String... collectionName) {

		if (StrUtil.isEmpty(id)) {
			return Optional.empty();
		}
		T t;
		if (ArrayUtil.isNotEmpty(collectionName)) {
			t = (T) mongoTemplate.findById(id, clazz, collectionName[0]);
		} else {
			t = (T) mongoTemplate.findById(id, clazz);
		}

		return Optional.ofNullable(t);
	}

	/**
	 * 根据条件查找单个
	 *
	 * @param <T>      类型
	 * @param criteriaWrapper 条件
	 * @param clazz    类
	 * @param collectionName 集合名称 如果没有则取的是实体类注解 @Document
	 * @return Optional<T> 对象
	 */
	public <T> Optional<T> findOneByQuery(CriteriaWrapper criteriaWrapper, Class<T> clazz,String... collectionName) {
			SortBuilder sortBuilder = new SortBuilder(FluentBean::getId, Sort.Direction.DESC);
		return findOneByQuery(criteriaWrapper, sortBuilder, clazz, collectionName);
	}

	/**
	 * 根据条件查找单个
	 *
	 * @param criteriaWrapper 查询
	 * @param clazz 类
	 * @param collectionName 集合名称 如果没有则取的是实体类注解 @Document
	 * @return Optional<T> 对象
	 */
	public <T> Optional<T> findOneByQuery(CriteriaWrapper criteriaWrapper, SortBuilder sortBuilder, Class<T> clazz,String... collectionName) {

		Query query = new Query(criteriaWrapper.build());
		query.limit(1);
		query.with(sortBuilder.toSort());
		T t;
		if (ArrayUtil.isNotEmpty(collectionName)) {
			t = (T) mongoTemplate.findOne(query, clazz, collectionName[0]);
		} else {
			t = (T) mongoTemplate.findOne(query, clazz);
		}

		return Optional.ofNullable(t);

	}


	/**
	 * 根据条件查找单个
	 *
	 * @param clazz 类
	 * @param collectionName 集合名称 如果没有则取的是实体类注解 @Document
	 * @return Optional<T> 对象
	 */
	public <T> Optional<T> findOneByQuery(SortBuilder sortBuilder, Class<T> clazz,String... collectionName) {
		return findOneByQuery(new CriteriaAndWrapper(), sortBuilder, clazz, collectionName);
	}

	/**
	 * 根据条件查找List
	 *
	 * @param <T>      类型
	 * @param criteriaWrapper 查询
	 * @param clazz    类
	 * @param collectionName 集合名称 如果没有则取的是实体类注解 @Document
	 * @return List 列表
	 */
	public <T> List<T> findListByQuery(CriteriaWrapper criteriaWrapper, Class<T> clazz,String... collectionName) {
		SortBuilder sortBuilder = new SortBuilder().add(FluentBean::getId, Sort.Direction.DESC);
		return findListByQuery(criteriaWrapper, sortBuilder, clazz, collectionName);
	}

	/**
	 * 根据条件查找List
	 *
	 * @param <T>      类型
	 * @param criteriaWrapper 查询
	 * @param sortBuilder     排序
	 * @param clazz    类
	 * @param collectionName 集合名称 如果没有则取的是实体类注解 @Document
	 * @return List 列表
	 */
	public <T> List<T> findListByQuery(CriteriaWrapper criteriaWrapper, SortBuilder sortBuilder, Class<T> clazz,String... collectionName) {
		Query query = new Query(criteriaWrapper.build());
		query.with(sortBuilder.toSort());

		if (ArrayUtil.isNotEmpty(collectionName)) {
			return mongoTemplate.find(query, clazz, collectionName[0]);
		}
		return mongoTemplate.find(query, clazz);
	}

	/**
	 * 根据条件查找某个属性
	 *
	 * @param <T>             类型
	 * @param criteriaWrapper 查询
	 * @param documentClass   类
	 * @param property        属性
	 * @param propertyClass   属性类
	 * @param collectionName 集合名称 如果没有则取的是实体类注解 @Document
	 * @return List 列表
	 */
	public <T, R, E> List<T> findPropertiesByQuery(CriteriaWrapper criteriaWrapper, Class<?> documentClass, SerializableFunction<E, R> property, Class<T> propertyClass, String... collectionName) {
		Query query = new Query(criteriaWrapper.build());
		query.fields().include(ReflectionUtil.getFieldName(property));
		List<?> list = new ArrayList<>();
		if (ArrayUtil.isNotEmpty(collectionName)) {
			list = mongoTemplate.find(query, documentClass, collectionName[0]);
		} else {
			list = mongoTemplate.find(query, documentClass);
		}

		return extractProperty(list, ReflectionUtil.getFieldName(property), propertyClass);
	}

	/**
	 * 根据条件查找某个属性
	 *
	 * @param criteriaWrapper 查询
	 * @param documentClass   类
	 * @param property        属性
	 * @param collectionName 集合名称 如果没有则取的是实体类注解 @Document
	 * @return List 列表
	 */
	public <R, E> List<String> findPropertiesByQuery(CriteriaWrapper criteriaWrapper, Class<?> documentClass, SerializableFunction<E, R> property, String... collectionName) {
		return findPropertiesByQuery(criteriaWrapper, documentClass, property, String.class, collectionName);
	}

	/**
	 * 根据id查找某个属性
	 *
	 * @param ids      查询
	 * @param clazz 类
	 * @param property      属性
	 * @param collectionName 集合名称 如果没有则取的是实体类注解 @Document
	 * @return List 列表
	 */
	public <R, E> List<String> findPropertiesByIds(List<String> ids, Class<?> clazz, SerializableFunction<E, R> property, String... collectionName) {
		CriteriaAndWrapper criteriaAndWrapper = new CriteriaAndWrapper().in(FluentBean::getId, ids);
		return findPropertiesByQuery(criteriaAndWrapper, clazz, property, collectionName);
	}

	/**
	 * 根据条件查找id
	 *
	 * @param criteriaWrapper 查询
	 * @param clazz    类
	 * @param collectionName 集合名称 如果没有则取的是实体类注解 @Document
	 * @return List 列表
	 */
	public List<String> findIdsByQuery(CriteriaWrapper criteriaWrapper, Class<?> clazz, String... collectionName) {
		return findPropertiesByQuery(criteriaWrapper, clazz, FluentBean::getId, collectionName);
	}

	/**
	 * 根据id集合查找
	 *
	 * @param ids  ids id集合
	 * @param clazz 类
	 * @param collectionName 集合名称 如果没有则取的是实体类注解 @Document
	 * @return List 列表
	 */
	public <T> List<T> findListByIds(Collection<String> ids, Class<T> clazz, String... collectionName) {
		CriteriaAndWrapper criteriaAndWrapper = new CriteriaAndWrapper().in(FluentBean::getId, ids);
		return findListByQuery(criteriaAndWrapper, clazz, collectionName);
	}

	/**
	 * 根据id集合查找
	 *
	 * @param ids id集合
	 * @param clazz 类
	 * @param collectionName 集合名称 如果没有则取的是实体类注解 @Document
	 * @return List 列表
	 */
	public <T> List<T> findListByIds(Collection<String> ids, SortBuilder sortBuilder, Class<T> clazz, String... collectionName) {
		CriteriaAndWrapper criteriaAndWrapper = new CriteriaAndWrapper().in(FluentBean::getId, ids);
		return findListByQuery(criteriaAndWrapper, sortBuilder, clazz, collectionName);
	}

	/**
	 * 根据id集合查找
	 *
	 * @param ids id集合
	 * @param clazz 类
	 * @param collectionName 集合名称 如果没有则取的是实体类注解 @Document
	 * @return List 列表
	 */
	public <T> List<T> findListByIds(String[] ids, SortBuilder sortBuilder, Class<T> clazz, String... collectionName) {
		return findListByIds(Arrays.asList(ids), sortBuilder, clazz, collectionName);
	}

	/**
	 * 根据id集合查找
	 *
	 * @param ids id集合
	 * @param clazz 类
	 * @param collectionName 集合名称 如果没有则取的是实体类注解 @Document
	 * @return List 列表
	 */
	public <T> List<T> findListByIds(String[] ids, Class<T> clazz, String... collectionName) {
		SortBuilder sortBuilder = new SortBuilder(FluentBean::getId, Sort.Direction.DESC);
		return findListByIds(ids, sortBuilder, clazz, collectionName);
	}

	/**
	 * 查询全部
	 *
	 * @param <T>   类型
	 * @param clazz 类
	 * @param collectionName 集合名称 如果没有则取的是实体类注解 @Document
	 * @return List 列表
	 */
	public <T> List<T> findAll(Class<T> clazz, String... collectionName) {
		SortBuilder sortBuilder = new SortBuilder(FluentBean::getId, Sort.Direction.DESC);
		return findListByQuery(new CriteriaAndWrapper(), sortBuilder, clazz, collectionName);
	}

	/**
	 * 查询全部
	 *
	 * @param <T>   类型
	 * @param clazz 类
	 * @param collectionName 集合名称 如果没有则取的是实体类注解 @Document
	 * @return List 列表
	 */
	public <T> List<T> findAll(SortBuilder sortBuilder, Class<T> clazz, String... collectionName) {
		return findListByQuery(new CriteriaAndWrapper(), sortBuilder, clazz, collectionName);
	}

	/**
	 * 查找全部的id
	 *
	 * @param clazz 类
	 * @param collectionName 集合名称 如果没有则取的是实体类注解 @Document
	 * @return List 列表
	 */
	public List<String> findAllIds(Class<?> clazz, String... collectionName) {
		return findIdsByQuery(new CriteriaAndWrapper(), clazz, collectionName);
	}

	/**
	 * 查找数量
	 *
	 * @param criteriaWrapper 查询
	 * @param clazz    类
	 * @return Long 数量
	 */
	public long findCountByQuery(CriteriaWrapper criteriaWrapper, Class<?> clazz) {
		long count;
		Query query = new Query(criteriaWrapper.build());
		if (query.getQueryObject().isEmpty()) {
			count = mongoTemplate.getCollection(mongoTemplate.getCollectionName(clazz)).estimatedDocumentCount();
		} else {
			count = mongoTemplate.count(query, clazz);
		}
		return count;
	}

	/**
	 * 查找数量
	 *
	 * @param criteriaWrapper 查询
	 * @param collectionName  集合名称
	 * @return Long 数量
	 */
	public long findCountByQuery(CriteriaWrapper criteriaWrapper, String collectionName) {
		long count;
		Query query = new Query(criteriaWrapper.build());
		if (query.getQueryObject().isEmpty()) {
			count = mongoTemplate.getCollection(collectionName).estimatedDocumentCount();
		} else {
			count = mongoTemplate.count(query, collectionName);
		}
		return count;
	}

	/**
	 * 查找全部数量
	 * @param clazz 类
	 * @return Long 数量
	 */
	public long findTotalCount(Class<?> clazz) {
		return findCountByQuery(new CriteriaAndWrapper(), clazz);
	}

	/**
	 * 查找全部数量
	 * @param collectionName 集合名称
	 * @return Long 数量
	 */
	public long findTotalCount(String collectionName) {
		return findCountByQuery(new CriteriaAndWrapper(), collectionName);
	}

	/**
	 * 获取list中对象某个属性,组成新的list
	 *
	 * @param list     列表
	 * @param clazz    类
	 * @param property 属性
	 * @return List<T> 列表
	 */
	@SuppressWarnings("unchecked")
	private <T> List<T> extractProperty(List<?> list, String property, Class<T> clazz) {
		Set<T> rs = new HashSet<T>();
		for (Object object : list) {
			Object value = ReflectUtil.getFieldValue(object, property);
			if (value != null && value.getClass().equals(clazz)) {
				rs.add((T) value);
			}
		}

		return new ArrayList<T>(rs);
	}

}
