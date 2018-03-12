/**  
 * Project Name:Springboothelloworld  
 * File Name:DataSourceConfig.java  
 * Package Name:com.study.config  
 * Date:2017年12月14日下午3:00:55  
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.  
 *  
*/  
  
package com.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**  
 * ClassName:DataSourceConfig <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2017年12月14日 下午3:00:55 <br/>  
 * @author   Administrator  
 * @version    
 * @since    JDK 1.6  
 * @see        
 */
@Configuration  
@EnableAutoConfiguration  
@MapperScan("com.dao")  
public class DataSourceConfig {  
      
    @Bean  
    @ConfigurationProperties(prefix="spring.datasource")  
    public ComboPooledDataSource dataSource() {  
        return new ComboPooledDataSource();  
    }  
  
    @Bean  
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {  
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();  
        ComboPooledDataSource dataSource  = dataSource();  
        sqlSessionFactoryBean.setDataSource(dataSource);  
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();  
        sqlSessionFactoryBean.setConfigLocation(resolver.getResource("classpath:/mybatis-config.xml"));  
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mapper/*.xml"));  
        return sqlSessionFactoryBean.getObject();  
    }  
      
    @Bean  
    public DataSourceTransactionManager transactionManager() {  
        return new DataSourceTransactionManager(dataSource());  
    }  
     
}  
  
