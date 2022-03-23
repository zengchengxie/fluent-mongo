# fluent-mongo

#### 介绍
fluent-mongo是MongoTemplate增强工具包,简化查询操作,对条件查询、排序、分页进行了很好的封装。<br>

如果使用原生的Criteria构建查询条件，将会存在大量的魔法值。<br>

例如： Criteria.where("m_dev_id").is(devModelId)，其中 m_dev_id 就是魔法值。<br>

fluent-mongo可以有效的避免魔法值的出现。<br>

fluent-mongo利用Java8的@FunctionInterface，通过反射获取实体类的属性名称或者@Field注解的Value值<br>

优先级：@Field > 实体类属性名称<br>


#### 安装步骤

###### 1. 新建SpringBoot工程,在pom.xml在加入依赖

```
<dependency>
    <groupId>com.gitee.xiezengcheng</groupId>
    <artifactId>fluent-mongo</artifactId>
    <version>1.1.1</version>
</dependency>

```

###### 2. 新建FluentMongoConfig配置类

```
@Configuration
public class FluentMongoConfig {
 
    @Bean
    public FluentMongoTemplate fluentMongoTemplate(MongoTemplate mongoTemplate) {
        return new FluentMongoTemplate(mongoTemplate);
    }
}

```
###### 3. 在application.yml中配置mongodb连接信息
```
# application.yml

spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/test
```

###### 4. 新建实体类
```
@Document(collection = "tb_config")
public class DbConfig {

    @Id
    private Integer id;

    @Field("host")
    private String host;

    @Field("port")
    private Integer port;

    @Field("path")
    private String path;

    @Field("dbVersion")
    private Integer dbVersion;
    
    ......
}
```

#### 快速上手
###### 1.  查询全部
```
// 注入依赖
@Autowired
private FluentMongoTemplate fluentMongoTemplate;

List<DbConfig> configList = fluentMongoTemplate.findAll(DbConfig.class, Sort.unsorted());
```
###### 2.  条件查询

```
// select * from tb_config where id = 1
CriteriaAndWrapper criteriaAndWrapper = new CriteriaAndWrapper().is(DbConfig::getId, 1);

Optional<DbConfig> dbConfig = fluentMongoTemplate.findOneByQuery(criteriaAndWrapper, Sort.unsorted(), DbConfig.class);

// select * from tb_config where host is not null
CriteriaAndWrapper wrapper = new CriteriaAndWrapper().isNotNull(DbConfig::getHost);
List<DbConfig> configList = fluentMongoTemplate.findListByQuery(wrapper, Sort.unsorted(), DbConfig.class);

// select * from tb_config where host = '127.0.0.1'
CriteriaAndWrapper wrapper = new CriteriaAndWrapper().is(DbConfig::getHost,"127.0.0.1");
List<DbConfig> configList = fluentMongoTemplate.findListByQuery(wrapper, Sort.unsorted(), DbConfig.class);

// select * from tb_config where id = 1
fluentMongoTemplate.findById(1, DbConfig.class);

```   
###### 3.  分页查询

```
// select * from tb_config where host = "127.0.0.1" limit 0,10
CriteriaAndWrapper wrapper = new CriteriaAndWrapper().is(DbConfig::getHost,"127.0.0.1");
FluentPageRequest pageRequest = FluentPageRequest.of(1, 10);
FluentPageResponse<DbConfig> page = fluentMongoTemplate.findPage(DbConfig.class, wrapper, pageRequest);
```   
###### 4.  排序
```
// select * from tb_config where host = "127.0.0.1" order by host desc
CriteriaAndWrapper wrapper = new CriteriaAndWrapper().is(DbConfig::getHost, "127.0.0.1");
Sort sort = new SortBuilder().add(DbConfig::getPort, Sort.Direction.DESC).build();
List<DbConfig> configList = fluentMongoTemplate.findListByQuery(wrapper, sort, DbConfig.class);
 
```
###### 5.  获取原生MongoTemplate
```
MongoTemplate mongoTemplate = fluentMongoTemplate.mongoTemplate();

```
###### 6.  构建自己的查询
```
MongoTemplate mongoTemplate = fluentMongoTemplate.mongoTemplate();
// 构建查询条件
Query query = new CriteriaAndWrapper().is(DbConfig::getPort, 8080).buildQuery();
mongoTemplate.find(query, DbConfig.class);

```
###### 7.  复杂查询
```
// select * from tb_config where port = 8080 and (dbVersion = 1 or version = 2) order by dbVersion desc limit 0,10
		
Sort sort = new SortBuilder().add(DbConfig::getDbVersion, Sort.Direction.DESC).build();
FluentPageRequest pageRequest = FluentPageRequest.of(1, 10, sort);
CriteriaAndWrapper wrapper = new CriteriaAndWrapper()
			.is(DbConfig::getPort, 8080)
			.and(new CriteriaOrWrapper().is(DbConfig::getDbVersion, 1).or(new CriteriaOrWrapper().is(DbConfig::getDbVersion, 2)));

FluentPageResponse<DbConfig> page = fluentMongoTemplate.findPage(DbConfig.class, wrapper, pageRequest);

```
