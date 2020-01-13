package cn.jzyan.datasource.aspect;

import cn.jzyan.datasource.annotation.TargetDataSource;
import cn.jzyan.datasource.datasource.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @ProjectName : dynamic-datasource-spring-boot
 * @FileName : DynamicDataSourceAspect
 * @Version : 1.0
 * @Package : cn.jzyan.aspect
 * @Description : 多数据源 aop
 * @Author : jzyan
 * @CreateDate : 2020/01/13 14:44
 * @ModificationHistory Who        When      What
 * --------- ---------     ---------------------------
 */
@Slf4j
@Aspect
@Order(-10) //保证该AOP在@Transactional之前执行
@Component
public class DynamicDataSourceAspect {


    @Before("@annotation(targetDataSource)")
    public void changeDataSource2(JoinPoint point, TargetDataSource targetDataSource) {
        //获取当前的指定的数据源;
        String dsId = targetDataSource.value();
        //如果不在我们注入的所有的数据源范围之内，那么输出警告信息，系统自动使用默认的数据源。
        if (!DynamicDataSourceContextHolder.containsDataSource(dsId)) {
            log.info("数据源[{}]不存在，使用默认数据源. > {}", dsId, point.getSignature());
        } else {
            log.info("Dynamic DataSource : {} > {}", dsId, point.getSignature());
            //找到的话，那么设置到动态数据源上下文中。
            DynamicDataSourceContextHolder.setDataSourceType(dsId);
        }
    }

    @After("@annotation(targetDataSource))")
    public void restoreDataSource2(JoinPoint point, TargetDataSource targetDataSource) {
        log.info("Revert DynamicDataSource : {} > {}", targetDataSource.value(), point.getSignature());
        //方法执行完毕之后，销毁当前数据源信息，进行垃圾回收。
        DynamicDataSourceContextHolder.clearDataSourceType();

    }
}
