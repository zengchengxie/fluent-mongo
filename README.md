# fluent-mongo

#### 介绍
fluent-mongo是MongoTemplate增强工具包，简化 CRUD 操作,封装了许多通用的方法。同时，也提供自定义方法的方式。

#### 安装步骤

1.  执行 git clone 

```
  git clone https://gitee.com/xiezengcheng/fluent-mongo.git
```

2.  将依赖安装到本地maven库

```
  cd fluent-mongo
  mvn clean package install -Dmaven.test.skip=true
```


3.  新建SpringBoot工程,在pom.xml在加入依赖

```
  <dependency>
      <groupId>com.gitee.xiezengcheng</groupId>
      <artifactId>fluentmongo</artifactId>
      <version>1.1.0</version>
  </dependency>

```

4.  新建FluentMongoConfig配置类

```
    @Configuration
    public class FluentMongoConfig {
 
        @Bean
        public FluentMongoTemplate fluentMongoTemplate(MongoTemplate mongoTemplate) {
            return new FluentMongoTemplate(mongoTemplate);
        }
    }

```

#### 快速上手
###### 1.  查询全部
```
    List<DbConfig> configList = fluentMongoTemplate.findAll(DbConfig.class, Sort.unsorted());
```
###### 2.  条件查询

```
     CriteriaAndWrapper wrapper = new CriteriaAndWrapper().isNotNull(DbConfig::getHost);
     List<DbConfig> configList = fluentMongoTemplate.findListByQuery(wrapper, Sort.unsorted(), DbConfig.class);

     // select * from tb_config where host = '127.0.0.1'
     CriteriaAndWrapper wrapper = new CriteriaAndWrapper().is(DbConfig::getHost,"127.0.0.1");
     List<DbConfig> configList = fluentMongoTemplate.findListByQuery(wrapper, Sort.unsorted(), DbConfig.class);

     // select * from tb_config where id = 1
     CriteriaAndWrapper wrapper = new CriteriaAndWrapper().is(DbConfig::getId,1);
     List<DbConfig> configList = fluentMongoTemplate.findListByQuery(wrapper, Sort.unsorted(), DbConfig.class);
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
	Sort sort = new SortBuilder().add(DbConfig::getDbVersion, Sort.Direction.DESC).build();
    FluentPageRequest pageRequest = FluentPageRequest.of(1, 10, sort);
    		
    // select * from tb_config where port = 8080 and (version = 1 or version = 2) order by version desc limit 0,10
    CriteriaAndWrapper wrapper = new CriteriaAndWrapper()
    		.is(DbConfig::getPort, 8080)
    		.and(new CriteriaOrWrapper().is(DbConfig::getDbVersion, 1).or(new CriteriaOrWrapper().is(DbConfig::getDbVersion, 2)));
    
    FluentPageResponse<DbConfig> page = fluentMongoTemplate.findPage(DbConfig.class, wrapper, pageRequest);
```
