package cn.jzyan.datasource.annotation;

import java.lang.annotation.*;

/**
 * @ProjectName : dynamic-datasource-spring-boot
 * @FileName : TargetDataSource
 * @Version : 1.0
 * @Package : cn.jzyan.annotation
 * @Description : 多数据源 注解
 * @Author : jzyan
 * @CreateDate : 2020/01/13 14:42
 * @ModificationHistory Who        When      What
 * --------- ---------     ---------------------------
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {

    String value();
}
