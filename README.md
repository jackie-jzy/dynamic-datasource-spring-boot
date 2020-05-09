简体中文
## 介绍
dynamic-datasource-spring-boot-starter 多数据源配置

## 使用方法
- 下载源码 https://github.com/jackie-jzy/dynamic-datasource-spring-boot.git 执行 mvn install.
- 在`pom.xml`中引入依赖：
```xml
<dependency>
    <groupId>cn.jzyan</groupId>
    <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
    <version>1.0.0-RELEASE</version>
</dependency>
```

- 在`application.properties`配置数据源：
```properties
#默认数据源
spring.datasource.url=jdbc:mysql://localhost:3306/platform?useUnicode=true&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=12345678
spring.datasource.driver=com.mysql.cj.jdbc.Driver

#动态数据源
dynamic.datasource.names=ds1,ds2

dynamic.datasource.ds1.url=jdbc:mysql://localhost:3306/platform?useUnicode=true&characterEncoding=UTF-8
dynamic.datasource.ds1.username=root
dynamic.datasource.ds1.password=12345678
dynamic.datasource.ds1.driver=com.mysql.cj.jdbc.Driver

dynamic.datasource.ds2.url=jdbc:mysql://localhost:3306/platform?useUnicode=true&characterEncoding=UTF-8
dynamic.datasource.ds2.username=root
dynamic.datasource.ds2.password=12345678
dynamic.datasource.ds2.driver=com.mysql.cj.jdbc.Driver
```

- service/dao层的方法添加注解：
```java
@TargetDataSource("ds1")
public PageInfo pageTopic() {
    return new PageInfo();
}
```