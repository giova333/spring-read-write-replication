package com.gladunalexander.readwritereplication.configuration;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "datasources")
class DataSourceProperties {

    private final Map<DataSourceType, DataSource> replicas = new LinkedHashMap<>();
    private final RoutingStrategy routingStrategy;

    public enum RoutingStrategy {
        ANNOTATION, TRANSACTION
    }

    DataSourceProperties(Map<DataSourceType, Map<String, String>> replicas,
                         RoutingStrategy routingStrategy) {
        this.routingStrategy = routingStrategy;
        replicas.forEach((k, v) -> this.replicas.put(k, convert(v)));
    }

    private DataSource convert(Map<String, String> source) {
        return DataSourceBuilder.create()
                .url(source.get("jdbcUrl"))
                .driverClassName(source.get("driverClassName"))
                .username(source.get("username"))
                .password(source.get("password"))
                .build();
    }
}
