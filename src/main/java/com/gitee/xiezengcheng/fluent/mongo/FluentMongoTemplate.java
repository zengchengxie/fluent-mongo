package com.gitee.xiezengcheng.fluent.mongo;

import cn.hutool.core.util.ReflectUtil;
import com.gitee.xiezengcheng.fluent.mongo.builder.UpdateBuilder;
import com.gitee.xiezengcheng.fluent.mongo.common.FluentPageRequest;
import com.gitee.xiezengcheng.fluent.mongo.common.FluentPageResponse;
import com.gitee.xiezengcheng.fluent.mongo.reflection.SerializableFunction;
import com.gitee.xiezengcheng.fluent.mongo.wraper.CriteriaAndWrapper;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.gitee.xiezengcheng.fluent.mongo.wraper.CriteriaWrapper;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

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
	 * @param collectionName 集合名称 如果没有则取的是实体类注解 @Document
	 * @param clazz 类
	 * @param criteriaWrapper 查询
	 * @param fluentPageRequest 分页和排序信息
	 * @return Page 分页
	 */
	public <T> FluentPageResponse<T> findPage(
											String collectionName,
											Class<T> clazz,
											CriteriaWrapper criteriaWrapper,
											FluentPageRequest fluentPageRequest) {

		FluentPageResponse<T> fluentPageResponseResp = new FluentPageResponse<T>();
		fluentPageResponseResp.setPageNum(fluentPageRequest.getPage());
		fluentPageResponseResp.setPageSize(fluentPageRequest.getSize());

		long count;

		// 查询出总条数
		if (fluentPageRequest.isQueryTotalCount()) {
			count = findCountByQuery(criteriaWrapper, collectionName);
			fluentPageResponseResp.setTotalCount(count);
		}

		// 查询List
		Query query = new Query(criteriaWrapper.build());
		query.with(fluentPageRequest.getSort());

		// 从哪条记录开始
		query.skip((fluentPageRequest.getPage() - 1) * fluentPageRequest.getSize());

		// 取多少条记录
		query.limit(fluentPageRequest.getSize());

		List<T> list = mongoTemplate.find(query, clazz, collectionName);

		fluentPageResponseResp.setContents(list);
		return fluentPageResponseResp;
	}


	/**
	 * 按查询条件获取Page
	 *
	 * @param clazz           类
	 * @param criteriaWrapper 查询
	 * @param fluentPageRequest   分页排序信息
	 * @return Page 分页数据
	 */
	public <T> FluentPageResponse<T> findPage(Class<T> clazz,
											  CriteriaWrapper criteriaWrapper,
											  FluentPageRequest fluentPageRequest) {

		FluentPageResponse<T> fluentPageResponseResp = new FluentPageResponse<T>();
		fluentPageResponseResp.setPageNum(fluentPageRequest.getPage());
		fluentPageResponseResp.setPageSize(fluentPageRequest.getSize());

		long count;

		// 查询出总条数
		if (fluentPageRequest.isQueryTotalCount()) {
			count = findCountByQuery(criteriaWrapper, clazz);
			fluentPageResponseResp.setTotalCount(count);
		}

		// 查询List
		Query query = new Query(criteriaWrapper.build());
		query.with(fluentPageRequest.getSort());

		// 从哪条记录开始
		query.skip((fluentPageRequest.getPage() - 1) * fluentPageRequest.getSize());

		// 取多少条记录
		query.limit(fluentPageRequest.getSize());

		List<T> list = mongoTemplate.find(query, clazz);

		fluentPageResponseResp.setContents(list);
		return fluentPageResponseResp;
	}


	/**
	 * 根据id查找
	 * 
	 * @param id    id
	 * @param clazz 类
	 * @return Optional<T> 对象
	 */
	public <T> Optional<T> findById(Object id, Class<T> clazz) {

		if (null == id) {
			return Optional.empty();
		}
		T t = mongoTemplate.findById(id, clazz);
		return Optional.ofNullable(t);
	}


	/**
	 * 根据id查找
	 *
	 * @param id    id
	 * @param clazz 类
	 * @param collectionName 集合名称 如果没有则取的是实体类注解 @Document
	 * @return Optional<T> 对象
	 */
	public <T> Optional<T> findById(Object id, Class<T> clazz,String collectionName) {

		if (null == id) {
			return Optional.empty();
		}

		T t = mongoTemplate.findById(id, clazz, collectionName);

		return Optional.ofNullable(t);
	}


	/**
	 * 根据条件查找单个
	 *
	 * @param criteriaWrapper 查询
	 * @param clazz 类
	 * @return Optional<T> 对象
	 */
	public <T> Optional<T> findOneByQuery(CriteriaWrapper criteriaWrapper, Sort sort, Class<T> clazz) {

		Query query = new Query(criteriaWrapper.build());
		query.limit(1);
		query.with(sort);
		T t = mongoTemplate.findOne(query, clazz);
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
	public <T> Optional<T> findOneByQuery(CriteriaWrapper criteriaWrapper, Sort sort, Class<T> clazz,String collectionName) {

		Query query = new Query(criteriaWrapper.build());
		query.limit(1);
		query.with(sort);
		T t =  mongoTemplate.findOne(query, clazz, collectionName);
		return Optional.ofNullable(t);

	}


	/**
	 * 根据条件查找List
	 *
	 * @param <T>             类型
	 * @param criteriaWrapper 查询
	 * @param sort     排序
	 * @param clazz           类
	 * @param collectionName  集合名称 如果没有则取的是实体类注解 @Document
	 * @return List 列表
	 */
	public <T> List<T> findListByQuery(CriteriaWrapper criteriaWrapper, Sort sort, Class<T> clazz, String collectionName) {
		Query query = new Query(criteriaWrapper.build());
		query.with(sort);
		return mongoTemplate.find(query, clazz, collectionName);
	}


	/**
	 * 根据条件查找List
	 *
	 * @param <T>      类型
	 * @param criteriaWrapper 查询
	 * @param sort     排序
	 * @param clazz    类
	 * @return List 列表
	 */
	public <T> List<T> findListByQuery(CriteriaWrapper criteriaWrapper, Sort sort, Class<T> clazz) {
		Query query = new Query(criteriaWrapper.build());
		query.with(sort);
		return mongoTemplate.find(query, clazz);
	}


	/**
	 * 查询全部
	 *
	 * @param <T>            类型
	 * @param clazz          类
	 * @param collectionName 集合名称 如果没有则取的是实体类注解 @Document
	 * @param sort           排序
	 * @return List 列表
	 */
	public <T> List<T> findAll(Class<T> clazz, String collectionName, Sort sort) {
		return findListByQuery(new CriteriaAndWrapper(), sort, clazz, collectionName);
	}

	/**
	 * 查询全部
	 *
	 * @param <T>   类型
	 * @param clazz 类
	 * @param sort 排序
	 * @return List 列表
	 */
	public <T> List<T> findAll(Class<T> clazz,Sort sort) {
		return findListByQuery(new CriteriaAndWrapper(), sort, clazz);
	}


	/**
	 * 统计数量
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
	 * 保存，不区分新增和修改
	 *
	 * @param document
	 * @param <T>
	 * @return
	 */
	public <T> T save(T document) {
		return mongoTemplate.save(document);
	}

	/**
	 * 保存，不区分新增和修改
	 *
	 * @param document
	 * @param collectionName
	 * @param <T>
	 * @return
	 */
	public <T> T save(T document, String collectionName) {
		return mongoTemplate.save(document, collectionName);
	}


	/**
	 * 更新文档对象
	 *
	 * @param document
	 * @param <T>
	 * @param collectionName 集合名称
	 * @return
	 */
	public <T> T update(T document, String collectionName) {

		Field[] fields = ReflectUtil.getFields(document.getClass());

		// 不存在返回null，代表更新失败
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				if (!findById(ReflectUtil.getFieldValue(document, field), document.getClass(), collectionName).isPresent()) {
					return null;
				}
			}
		}

		return mongoTemplate.save(document, collectionName);
	}


	/**
	 * 更新文档对象
	 *
	 * @param document
	 * @param <T>
	 * @return
	 */
	public <T> T update(T document) {

		Field[] fields = ReflectUtil.getFields(document.getClass());

		// 不存在返回null，代表更新失败
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				if (!findById(ReflectUtil.getFieldValue(document, field), document.getClass()).isPresent()) {
					return null;
				}
			}
		}

		return mongoTemplate.save(document);
	}


	/**
	 * 更新查到的全部文档
	 *
	 * @param criteriaWrapper 查询条件
	 * @param update   更新
	 * @param collectionName 集合名称
	 * @param clazz           类
	 */
	public UpdateResult updateMulti(CriteriaWrapper criteriaWrapper, Update update, Class<?> clazz, String collectionName) {
		Query query = new Query(criteriaWrapper.build());
		return mongoTemplate.updateMulti(new Query(criteriaWrapper.build()), update, clazz, collectionName);
	}


	/**
	 * 更新查到的全部文档
	 *
	 * @param criteriaWrapper 查询条件
	 * @param update   更新
	 * @param clazz           类
	 */
	public UpdateResult updateMulti(CriteriaWrapper criteriaWrapper, Update update, Class<?> clazz) {
		Query query = new Query(criteriaWrapper.build());
		return mongoTemplate.updateMulti(new Query(criteriaWrapper.build()), update, clazz);
	}


	/**
	 * 根据条件删除
	 *  @param criteriaWrapper 查询
	 * @param collectionName
	 * @return
	 */
	public DeleteResult deleteByQuery(CriteriaWrapper criteriaWrapper,String collectionName) {
		Query query = new Query(criteriaWrapper.build());
		return mongoTemplate.remove(query,collectionName);
	}

	/**
	 * 根据条件删除
	 *  @param criteriaWrapper 查询
	 * @return
	 */
	public DeleteResult deleteByQuery(CriteriaWrapper criteriaWrapper, Class<?> clazz) {
		Query query = new Query(criteriaWrapper.build());
		return mongoTemplate.remove(query, clazz);
	}
	

	/**
	 * 累加某一个字段的数量,原子操作
	 * @param criteriaWrapper
	 * @param property
	 * @param count
	 * @param clazz
	 * @param collectionName
	 */
	public <T, R> void inc(CriteriaWrapper criteriaWrapper, SerializableFunction<T, R> property, Number count, Class<?> clazz, String collectionName) {
		UpdateBuilder updateBuilder = new UpdateBuilder().inc(property, count);
		updateMulti(criteriaWrapper, updateBuilder.build(), clazz, collectionName);
	}

	/**
	 * 累加某一个字段的数量,原子操作
	 * @param criteriaWrapper
	 * @param property
	 * @param count
	 * @param clazz
	 */
	public <T, R> void inc(CriteriaWrapper criteriaWrapper, SerializableFunction<T, R> property, Number count, Class<?> clazz) {
		UpdateBuilder updateBuilder = new UpdateBuilder().inc(property, count);
		updateMulti(criteriaWrapper, updateBuilder.build(), clazz);
	}


}
