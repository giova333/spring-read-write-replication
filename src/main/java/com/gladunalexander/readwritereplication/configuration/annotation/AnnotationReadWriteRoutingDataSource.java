package com.gladunalexander.readwritereplication.configuration.annotation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Slf4j
public class AnnotationReadWriteRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        var dataSourceType = DatasourceTypeThreadLocal.getDataSourceType();
        log.debug("Executing operation on datasource {}", dataSourceType);
        return dataSourceType;
    }
}
