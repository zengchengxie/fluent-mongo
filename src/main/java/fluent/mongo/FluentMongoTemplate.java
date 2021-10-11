package fluent.mongo;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import fluent.mongo.builder.SortBuilder;
import fluent.mongo.builder.UpdateBuilder;
import fluent.mongo.common.FluentPage;
import fluent.mongo.reflection.ReflectionUtil;
import fluent.mongo.reflection.SerializableFunction;
import fluent.mongo.wraper.CriteriaAndWrapper;
import fluent.mongo.wraper.CriteriaWrapper;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.lang.reflect.Field;
import java.util.*;

public class FluentMongoTemplate {

	private final MongoTemplate mongoTemplate;

	public FluentMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}


	// 获取原生的mongoTemplate
	public MongoTemplate mongoTemplate() {
		return mongoTemplate;
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
		fluentPageResp.setPageNum(fluentPage.getPageNum());
		fluentPageResp.setPageSize(fluentPage.getPageSize());

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
		query.skip((fluentPage.getPageNum() - 1) * fluentPage.getPageSize());

		// 取多少条记录
		query.limit(fluentPage.getPageSize());

		List<T> list = new ArrayList<>();
		if (ArrayUtil.isNotEmpty(collectionName)) {
			list = mongoTemplate.find(query, clazz, collectionName[0]);
		} else {
			list = mongoTemplate.find(query, clazz);
		}

		fluentPageResp.setContents(list);
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
	 * 根据id查找
	 * 
	 * @param id    id
	 * @param clazz 类
	 * @param collectionName 集合名称 如果没有则取的是实体类注解 @Document
	 * @return Optional<T> 对象
	 */
	public <T> Optional<T> findById(Object id, Class<T> clazz,String... collectionName) {

		if (null == id) {
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

		return extractProperty(list, ReflectionUtil.getField(property), propertyClass);
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
	private <T> List<T> extractProperty(List<?> list, Field property, Class<T> clazz) {
		Set<T> rs = new HashSet<T>();
		for (Object object : list) {
			Object value = ReflectUtil.getFieldValue(object, property);
			if (value != null && value.getClass().equals(clazz)) {
				rs.add((T) value);
			}
		}

		return new ArrayList<T>(rs);
	}


	/**
	 * 插入文档对象
	 *
	 * @param document
	 * @param <T>
	 * @param collectionName 集合名称 可选
	 * @return
	 */
	public <T> T insert(T document, String... collectionName) {

		Field[] fields = ReflectUtil.getFields(document.getClass());

		// 将@ID标识字段设为null
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				ReflectUtil.setFieldValue(document, field, null);
			}
		}

		if (ArrayUtil.isNotEmpty(collectionName)) {
			return mongoTemplate.save(document, collectionName[0]);
		}

		return mongoTemplate.save(document);
	}


	/**
	 * 更新文档对象
	 *
	 * @param document
	 * @param <T>
	 * @param collectionName 集合名称 可选
	 * @return
	 */
	public <T> T update(T document, String... collectionName) {

		Field[] fields = ReflectUtil.getFields(document.getClass());

		// 不存在返回null，代表更新失败
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				if (!findById(ReflectUtil.getFieldValue(document, field), document.getClass(), collectionName).isPresent()) {
					return null;
				}
			}
		}

		if (ArrayUtil.isNotEmpty(collectionName)) {
			return mongoTemplate.save(document, collectionName[0]);
		}

		return mongoTemplate.save(document);
	}


	/**
	 * 批量插入对象
	 *
	 * @param list documentList集合
	 *
	 */
	public <T> List<T> insertAll(List<T> list) {
		if (ArrayUtil.isEmpty(list)) {
			return new ArrayList<>();
		}

		for (T document : list) {
			Field[] fields = ReflectUtil.getFields(document.getClass());

			// 将@ID标识字段设为null
			for (Field field : fields) {
				if (field.isAnnotationPresent(Id.class)) {
					ReflectUtil.setFieldValue(document, field, null);
				}
			}
		}

		return (List<T>) mongoTemplate.insertAll(list);
	}





	/**
	 * 更新查到的全部文档
	 *
	 * @param criteriaWrapper 查询条件
	 * @param updateBuilder   更新
	 * @param clazz           类
	 */
	public UpdateResult updateMulti(CriteriaWrapper criteriaWrapper, UpdateBuilder updateBuilder, Class<?> clazz, String... collectionName) {
		Query query = new Query(criteriaWrapper.build());
		if (ArrayUtil.isNotEmpty(collectionName)) {
			return mongoTemplate.updateMulti(new Query(criteriaWrapper.build()), updateBuilder.toUpdate(), collectionName[0]);
		} else {
			return mongoTemplate.updateMulti(new Query(criteriaWrapper.build()), updateBuilder.toUpdate(), clazz);
		}
	}


	/**
	 * 根据条件删除
	 *  @param criteriaWrapper 查询
	 * @param clazz           类
	 * @return
	 */
	public DeleteResult deleteByQuery(CriteriaWrapper criteriaWrapper, Class<?> clazz, String... collectionName) {
		Query query = new Query(criteriaWrapper.build());
		if (ArrayUtil.isNotEmpty(collectionName)) {
			return mongoTemplate.remove(query, collectionName[0]);
		}
		return mongoTemplate.remove(query, clazz);
	}


	/**
	 * 累加某一个字段的数量,原子操作
	 * @param criteriaWrapper
	 * @param property
	 * @param count
	 * @param clazz
	 */
	public <R, E> void inc(CriteriaWrapper criteriaWrapper, SerializableFunction<E, R> property, Number count, Class<?> clazz,String... collectionName) {
		UpdateBuilder updateBuilder = new UpdateBuilder().inc(property, count);
		updateMulti(criteriaWrapper, updateBuilder, clazz, collectionName);
	}


}
