package com.gladunalexander.readwritereplication.configuration.transaction;

import com.gladunalexander.readwritereplication.configuration.DataSourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
public class TransactionReadWriteRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        var readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        log.debug("Serving readOnly={} transaction", readOnly);
        return readOnly ? DataSourceType.READ : DataSourceType.WRITE;
    }

}
