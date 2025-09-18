package com.example.pehd.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * checkuser数据库数据源配置
 */
@Configuration
@EnableJpaRepositories(
    basePackages = "com.example.pehd.repository.checkuser",
    entityManagerFactoryRef = "checkUserEntityManagerFactory",
    transactionManagerRef = "checkUserTransactionManager"
)
public class CheckUserDataSourceConfig {
    
    @Bean
    @ConfigurationProperties("app.datasource.checkuser")
    public DataSourceProperties checkUserDataSourceProperties() {
        return new DataSourceProperties();
    }
    
    @Bean
    @Qualifier("checkUserDataSource")
    public DataSource checkUserDataSource() {
        return checkUserDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }
    
    @Bean
    @Qualifier("checkUserEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean checkUserEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("checkUserDataSource") DataSource dataSource) {
        
        return builder
                .dataSource(dataSource)
                .packages("com.example.pehd.entity.checkuser")
                .persistenceUnit("checkuser")
                .properties(java.util.Map.of(
                    "hibernate.dialect", "org.hibernate.dialect.MySQLDialect",
                    "hibernate.hbm2ddl.auto", "validate",
                    "hibernate.show_sql", "true",
                    "hibernate.format_sql", "true"
                ))
                .build();
    }
    
    @Bean
    @Qualifier("checkUserTransactionManager")
    public PlatformTransactionManager checkUserTransactionManager(
            @Qualifier("checkUserEntityManagerFactory") LocalContainerEntityManagerFactoryBean checkUserEntityManagerFactory) {
        return new JpaTransactionManager(checkUserEntityManagerFactory.getObject());
    }
}
