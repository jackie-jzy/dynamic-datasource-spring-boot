package cn.jzyan.datasource.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName : dynamic-datasource-spring-boot
 * @FileName : DynamicDataSourceRegister
 * @Version : 1.0
 * @Package : cn.jzyan.datasource
 * @Description : 注册数据源
 * @Author : jzyan
 * @CreateDate : 2020/01/13 15:18
 * @ModificationHistory Who        When      What
 * --------- ---------     ---------------------------
 */
@Slf4j
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    /**
     * 指定默认数据源(springboot2.0默认数据源是hikari如何想使用其他数据源可以自己配置)
     */
    private static final String DATASOURCE_TYPE_DEFAULT = "com.zaxxer.hikari.HikariDataSource";
    /**
     * 默认数据源
     */
    private DataSource defaultDataSource;
    /**
     * 用户自定义数据源
     */
    private Map<String, DataSource> dynamicDataSources = new HashMap<String, DataSource>();

    /**
     * Set the {@code Environment} that this component runs in.
     *
     * @param environment
     */
    public void setEnvironment(Environment environment) {
        this.initDefaultDataSource(environment);
        this.initDynamicDataSources(environment);
    }

    /**
     * 注册数据源
     *
     * @param importingClassMetadata
     * @param registry
     */
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        //添加默认数据源
        targetDataSources.put("dataSource", this.defaultDataSource);
        DynamicDataSourceContextHolder.dataSourceIds.add("dataSource");
        //添加其他数据源
        targetDataSources.putAll(dynamicDataSources);
        for (String key : dynamicDataSources.keySet()) {
            DynamicDataSourceContextHolder.dataSourceIds.add(key);
        }
        //创建DynamicDataSource
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        mpv.addPropertyValue("targetDataSources", targetDataSources);
        //注册 - BeanDefinitionRegistry
        registry.registerBeanDefinition("dataSource", beanDefinition);

        log.info("Dynamic DataSource Registry");
    }


    /**
     * 初始化默认数据源
     *
     * @param env
     */
    private void initDefaultDataSource(Environment env) {
        // 读取默认数据源
        Map<String, String> map = new HashMap(5);
        map.put("type", env.getProperty("spring.datasource.type"));
        map.put("driver", env.getProperty("spring.datasource.driver"));
        map.put("url", env.getProperty("spring.datasource.url"));
        map.put("username", env.getProperty("spring.datasource.username"));
        map.put("password", env.getProperty("spring.datasource.password"));
        defaultDataSource = buildDataSource(map);
    }

    /**
     * 初始化动态数据源
     *
     * @param env
     */
    private void initDynamicDataSources(Environment env) {
        // 读取配置文件获取更多数据源
        String prefixStr = env.getProperty("dynamic.datasource.names");
        if (prefixStr != null) {
            String[] prefixArray = prefixStr.split(",");
            Map<String, String> map = new HashMap(5);
            for (String prefix : prefixArray) {
                // 多个数据源
                map.put("type", env.getProperty("dynamic.datasource." + prefix + ".type"));
                map.put("driver", env.getProperty("dynamic.datasource." + prefix + ".driver"));
                map.put("url", env.getProperty("dynamic.datasource." + prefix + ".url"));
                map.put("username", env.getProperty("dynamic.datasource." + prefix + ".username"));
                map.put("password", env.getProperty("dynamic.datasource." + prefix + ".password"));
                DataSource ds = buildDataSource(map);
                dynamicDataSources.put(prefix, ds);
                map.clear();
            }
        } else {
            log.warn("InitDynamicDataSources Failure because property 'dynamic.datasource.names' is null");
        }
    }

    private DataSource buildDataSource(Map<String, String> dataSourceMap) {
        try {
            String type = dataSourceMap.get("type") == null ? DATASOURCE_TYPE_DEFAULT : dataSourceMap.get("type");
            Class<? extends DataSource> dataSourceType = (Class<? extends DataSource>) Class.forName(type);
            String driverClassName = dataSourceMap.get("driver");
            String url = dataSourceMap.get("url");
            String username = dataSourceMap.get("username");
            String password = dataSourceMap.get("password");
            // 自定义DataSource配置
            DataSourceBuilder factory = DataSourceBuilder.create().driverClassName(driverClassName).url(url)
                    .username(username).password(password).type(dataSourceType);
            return factory.build();
        } catch (ClassNotFoundException e) {
            log.info("DynamicDataSourceRegister buildDataSource Exception:{}", e);
        }
        return null;
    }
}
