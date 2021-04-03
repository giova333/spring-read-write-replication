package com.gladunalexander.readwritereplication.configuration;

import com.gladunalexander.readwritereplication.configuration.annotation.AnnotationReadWriteRoutingDataSource;
import com.gladunalexander.readwritereplication.configuration.annotation.AnnotationRoutingDatasourceAspect;
import com.gladunalexander.readwritereplication.configuration.transaction.TransactionReadWriteRoutingDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import static com.gladunalexander.readwritereplication.configuration.DataSourceProperties.RoutingStrategy.ANNOTATION;

@Configuration
@EnableConfigurationProperties(value = DataSourceProperties.class)
class ReadWriteRoutingConfiguration {

    @Bean
    @ConditionalOnProperty(name = "datasources.routing-strategy", havingValue = "annotation")
    AnnotationRoutingDatasourceAspect annotationRoutingDatasourceAspect() {
        return new AnnotationRoutingDatasourceAspect();
    }

    @Bean
    DataSource dataSource(DataSourceProperties dataSourceProperties) {
        var dataSource = dataSourceProperties.getRoutingStrategy() == ANNOTATION
                ? new AnnotationReadWriteRoutingDataSource()
                : new TransactionReadWriteRoutingDataSource();

        Map<Object, Object> dataSourceMap = dataSourceProperties.getReplicas()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> connectionPoolDataSource(entry.getValue()), (a, b) -> b));
        dataSource.setTargetDataSources(dataSourceMap);
        return dataSource;
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setPersistenceUnitName(getClass().getSimpleName());
        entityManagerFactoryBean.setPersistenceProvider(new HibernatePersistenceProvider());
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan("com.gladunalexander.readwritereplication");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        HibernateJpaDialect jpaDialect = vendorAdapter.getJpaDialect();
        jpaDialect.setPrepareConnection(false);
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.provider_disables_autocommit", "true");
        entityManagerFactoryBean.setJpaProperties(properties);
        return entityManagerFactoryBean;
    }

    private HikariConfig hikariConfig(DataSource dataSource) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setMaximumPoolSize(20);
        hikariConfig.setDataSource(dataSource);
        hikariConfig.setAutoCommit(false);
        return hikariConfig;
    }

    private HikariDataSource connectionPoolDataSource(
            DataSource dataSource) {
        return new HikariDataSource(hikariConfig(dataSource));
    }

}
