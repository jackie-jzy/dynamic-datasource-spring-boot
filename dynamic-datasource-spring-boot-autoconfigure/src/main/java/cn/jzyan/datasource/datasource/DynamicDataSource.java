package cn.jzyan.datasource.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @ProjectName : dynamic-datasource-spring-boot
 * @FileName : DynamicDataSource
 * @Version : 1.0
 * @Package : cn.jzyan.datasource
 * @Description : 动态数据源
 * @Author : jzyan
 * @CreateDate : 2020/01/13 14:51
 * @ModificationHistory Who        When      What
 * --------- ---------     ---------------------------
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }
}
