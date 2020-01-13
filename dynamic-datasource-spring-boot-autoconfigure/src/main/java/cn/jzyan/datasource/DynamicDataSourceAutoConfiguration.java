package cn.jzyan.datasource;

import cn.jzyan.datasource.datasource.DynamicDataSourceRegister;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @ProjectName : dynamic-datasource-spring-boot
 * @FileName : DynamicDataSourceAutoConfiguration
 * @Version : 1.0
 * @Package : cn.jzyan.autoconfigure
 * @Description : 动态数据源 配置
 * @Author : jiangzhongyan
 * @Email : jiangzhongyan@juzifenqi.com
 * @CreateDate : 2020/01/13 16:23
 * @ModificationHistory Who        When      What
 * --------- ---------     ---------------------------
 */
@Configuration
@ComponentScan("cn.jzyan.datasource")
@Import(DynamicDataSourceRegister.class)
public class DynamicDataSourceAutoConfiguration {
}
