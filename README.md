# Fluent-Mongo

基于 Java8 的 @FunctionInterface 特性，简化 MongoTemplate 操作，支持查询、修改、删除、更新。

<p align="center">
    <a target="_blank" href="https://www.apache.org/licenses/LICENSE-2.0.txt">
		<img src="https://img.shields.io/:license-Apache2-blue.svg" alt="Apache 2" />
	</a>
    <a target="_blank" href="https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html">
		<img src="https://img.shields.io/badge/JDK-8-green.svg" alt="jdk-8" />
	</a>
    <a target="_blank" href="https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html">
		<img src="https://img.shields.io/badge/JDK-11-green.svg" alt="jdk-11" />
	</a>
    <br />
        <img src="https://img.shields.io/badge/SpringBoot-v2.6.x-blue">
    <br />
</p>

## 特征

#### 1、很轻量
> Fluent-Mongo 整个框架只依赖 spring-boot-starter-data-mongodb，再无其他任何第三方依赖。

#### 2、只增强
> Fluent-Mongo  支持 CRUD、分页查询、多表查询、批量操作，但不丢失 MongoTemplate 原有的任何功能。

#### 3、可读性
> Fluent-Mongo 采用Java8的@FunctionInterface特性，实现属性传递，避免大量字符串和魔法值的出现，大大提高可读性。

#### 4、更灵活
> Fluent-Mongo 支持分页查询、简单查询、批量更新、批量插入、排序、分页...等等等等。


## 快速安装

1.在pom.xml中引入依赖

```xml
<dependency>
    <groupId>com.gitee.xiezengcheng</groupId>
    <artifactId>fluent-mongo</artifactId>
    <version>1.1.2</version>
</dependency>
```

2.定义配置类，将 `FluentMongoTemplate` 注入 Spring 容器
```java
@Configuration
public class FluentMongoConfig {

    @Bean
    public FluentMongoTemplate fluentMongoTemplate(MongoTemplate mongoTemplate) {
        return new FluentMongoTemplate(mongoTemplate);
    }

}
```

## 快速上手

### 定义实体类

```java
@Data
@Document(collection = "tb_algorithm")
public class Algorithm {

    private String id;

    private String algorithmType;

}
```
### 查询

列表查询：
```java
List<Algorithm> algorithmList = fluentMongoTemplate.findListByQuery(CriteriaWrapper.andWrapper(),SortBuilder.builder().build(),Algorithm.class);
```

单个查询：
```java
CriteriaAndWrapper criteriaAndWrapper = CriteriaWrapper.andWrapper().is(Algorithm::getId, 1);
SortBuilder builder = SortBuilder.builder();
Optional<Algorithm> algorithmOptional = fluentMongoTemplate.findOneByQuery(criteriaAndWrapper, builder, Algorithm.class);
```

条件查询：
```java
CriteriaAndWrapper criteriaAndWrapper = CriteriaWrapper.andWrapper().is(Algorithm::getAlgorithmType, "平均值");
        
// 排序
SortBuilder sortBuilder = SortBuilder.builder().add(Algorithm::getId, Sort.Direction.DESC);

List<Algorithm> algorithmList = fluentMongoTemplate.findListByQuery(criteriaAndWrapper, sortBuilder, Algorithm.class);

```

分页查询：
```java
CriteriaAndWrapper criteriaAndWrapper = CriteriaWrapper.andWrapper().is(Algorithm::getAlgorithmType, "平均值");

FluentPageRequest fluentPageRequest = FluentPageRequest.of(1, 10, SortBuilder.builder());

FluentPageResponse<Algorithm> page = fluentMongoTemplate.findPage(Algorithm.class, criteriaAndWrapper, fluentPageRequest);

```

### 更新

批量更新：
```java
CriteriaAndWrapper criteriaAndWrapper = CriteriaWrapper.andWrapper().is(Algorithm::getAlgorithmType, "平均值");

UpdateBuilder updateBuilder = UpdateBuilder.builder().set(Algorithm::getAlgorithmType, "绝对值");

fluentMongoTemplate.updateMulti(criteriaAndWrapper,updateBuilder , Algorithm.class);
```
