# fluent-mongo

#### 介绍
spring-data-mongodb增强工具包，简化 CRUD 操作，提供类fluent-mybatis的数据库操作。传统关系型数据库及围绕它们构建的orm在项目开发中有很多难用的痛点。
mongodb这种文档性数据库的出现，完美的解决了sql数据库在项目开发中的诸多痛点，在mongodb4.0以后支持了事务，已经可以完美的用于工程项目。
spring-data-mongodb已经对mongodb的操作做了一部分封装，但依然不够，Query Criteria Sort的操作依然有比较大的局限性，而且对于习惯sql操作的人来说，理解其使用法则依然稍显别扭。
fluent-mongo基于mongoHelper对spring-data-mongodb又进行了进一步封装，使其更易于使用，并添加了很多易于项目管理的功能。

#### 其他说明
本项目只适用于springBoot项目，基于mongoHelper，简化了其很多不必要的部分并加入了一些自己的想法。
项目依赖SpringBoot相关库，另外项目依赖了hutool提供的诸多Util工具，让代码更简洁。

mongoHelper：https://gitee.com/cym1102/mongoHelper

#### 安装教程

1.  git clone 到本地

```
    git clone https://gitee.com/xiezengcheng/fluent-mongo.git
```

2.  将依赖安装到本地maven库

```
    cd fluent-mongo
    mvn clean package install -Dmaven.test.skip=true
```


3.  新建Springboot工程,引入maven库

```
    <dependency>
	    <groupId>fluent.mongo</groupId>
		<artifactId>fluent-mongo</artifactId>
		<version>1.0</version>
	</dependency>

```

4.  完整pom.xml(依赖hutool和spring-boot-starter-data-mongodb)
```
    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    	<modelVersion>4.0.0</modelVersion>
    	<parent>
    		<groupId>org.springframework.boot</groupId>
    		<artifactId>spring-boot-starter-parent</artifactId>
    		<version>2.5.5</version>
    		<relativePath/> <!-- lookup parent from repository -->
    	</parent>
    	<groupId>fluent.mongo.demo</groupId>
    	<artifactId>fluent-mongo-demo</artifactId>
    	<version>0.0.1-SNAPSHOT</version>
    	<name>fluent-mongo-demo</name>
    	<description>Demo project for Spring Boot</description>
    	<properties>
    		<java.version>1.8</java.version>
    	</properties>
    	<dependencies>
    		<dependency>
    			<groupId>org.springframework.boot</groupId>
    			<artifactId>spring-boot-starter</artifactId>
    		</dependency>
    
    		<dependency>
    			<groupId>org.springframework.boot</groupId>
    			<artifactId>spring-boot-starter-test</artifactId>
    			<scope>test</scope>
    		</dependency>
    
    		<dependency>
    			<groupId>junit</groupId>
    			<artifactId>junit</artifactId>
    		</dependency>
    
    		<dependency>
    			<groupId>org.springframework.boot</groupId>
    			<artifactId>spring-boot-starter-data-mongodb</artifactId>
    		</dependency>
    
    		<dependency>
    			<groupId>cn.hutool</groupId>
    			<artifactId>hutool-all</artifactId>
    			<version>5.7.12</version>
    		</dependency>
    
    		<dependency>
    			<groupId>fluent.mongo</groupId>
    			<artifactId>fluent-mongo</artifactId>
    			<version>1.0</version>
    		</dependency>
    
    	</dependencies>
    
    	<build>
    		<plugins>
    			<plugin>
    				<groupId>org.springframework.boot</groupId>
    				<artifactId>spring-boot-maven-plugin</artifactId>
    			</plugin>
    		</plugins>
    	</build>
   
    </project>

```

5.  配置springBoot配置文件application.yml

```
    spring:
      data:
        mongodb:
          host: 127.0.0.1
          database: ${database}
          port: ${port}
```

6.  在合适的地方添加FluentMongoConfig配置类

```
    @Configuration
    public class FluentMongoConfig {
    
        @Bean
        public FluentMongoTemplate fluentMongoTemplate(MongoTemplate mongoTemplate) {
            return new FluentMongoTemplate(mongoTemplate);
        }
    
    }

```

7.  新建一个测试类进行测试
```
    @RunWith(SpringRunner.class)
    @SpringBootTest
    class FluentMongoDemoApplicationTests {
    
    	@Autowired
    	private FluentMongoTemplate fluentMongoTemplate;
    
    	@Test
    	void testFluentMongoTemplate() {
    
    		List<DbConfig> full = fluentMongoTemplate.findListByQuery(new CriteriaAndWrapper()
    				.eq(DbConfig::getDbVersion, 1)
    				.and(new CriteriaOrWrapper().like(DbConfig::getPath, "full").eq(DbConfig::getHost, "10.54.1.6"))
    				.eq(DbConfig::getPort, "21031"), new SortBuilder(DbConfig::getDbVersion, Sort.Direction.DESC), DbConfig.class);
    
    	}
    }
```


#### 使用说明
###### 1.  基本操作
    - 获取原生MongoTemplate：MongoTemplate mongoTemplate = fluentMongoTemplate.mongoTemplate();
    - 构建Criteria:
    ```
         MongoTemplate mongoTemplate = fluentMongoTemplate.mongoTemplate();
         Query query = new Query(new CriteriaAndWrapper().eq(DbConfig::getDbVersion, 1).build());
         mongoTemplate.find(query, DbConfig.class);
    ```
    - 增、删、改
    ```
        fluentMongoTemplate.insert(?);
        fluentMongoTemplate.deleteByQuery(?) -> fluentMongoTemplate.deleteByQuery(new CriteriaAndWrapper().eq(DbConfig::getId, 1), DbConfig.class);
        fluentMongoTemplate.update(?)
        
    ```
###### 2.   复杂查询

```
     List<DbConfig> full = fluentMongoTemplate.findListByQuery(new CriteriaAndWrapper()
        .eq(DbConfig::getDbVersion, 1)
        .and(new CriteriaOrWrapper().like(DbConfig::getPath, "full").eq(DbConfig::getHost, "10.54.1.6"))
        .eq(DbConfig::getPort, "21031"), new SortBuilder(DbConfig::getDbVersion, Sort.Direction.DESC), DbConfig.class);

    // select * from dbConfig where dbVersion=1 and (path like '%full%' or host = "10.54.1.6") and port = "21031" order by dbVersion desc
```   
###### 3.   分页查询

```
     FluentPage<Object> fluentPage = new FluentPage<>();
     fluentPage.setPageSize(20);
     FluentPage<DbConfig> page = fluentMongoTemplate.findPage(new CriteriaAndWrapper(),
     		new SortBuilder(DbConfig::getDbVersion, Sort.Direction.DESC),
     		fluentPage,
     		DbConfig.class);
     List<DbConfig> contents = page.getContents();
```   
###### 4.   构建自己的fluentMongoTemplate
```
    // 继承
    @Component
    public class MyMongoTemplate extends FluentMongoTemplate {
    
        public MyMongoTemplate(MongoTemplate mongoTemplate) {
            super(mongoTemplate);
        }
    
        public List<DbConfig> find(CriteriaWrapper criteriaWrapper) {
            List<DbConfig> dbConfigs = mongoTemplate().find(new Query(criteriaWrapper.build()), DbConfig.class);
            return dbConfigs;
        }
    }

    // 调用   
    @RunWith(SpringRunner.class)
    @SpringBootTest
    class FluentMongoDemoApplicationTests {
    
    	@Autowired
    	private FluentMongoTemplate fluentMongoTemplate;
    
    	@Autowired
    	private MyMongoTemplate myMongoTemplate;
    
    	@Test
    	void testFluentMongoTemplate() {
    		List<DbConfig> dbConfigs = myMongoTemplate.find(new CriteriaAndWrapper());
    	}
    
    }
```
### 交流群
```
    QQ群：321832726
```