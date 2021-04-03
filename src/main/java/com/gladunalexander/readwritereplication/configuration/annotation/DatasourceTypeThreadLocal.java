package com.gladunalexander.readwritereplication.configuration.annotation;

import com.gladunalexander.readwritereplication.configuration.DataSourceType;

class DatasourceTypeThreadLocal {

    private static final ThreadLocal<DataSourceType> DATA_SOURCE_TYPE_THREAD_LOCAL =
            ThreadLocal.withInitial(() -> DataSourceType.WRITE);

    static DataSourceType getDataSourceType() {
        return DATA_SOURCE_TYPE_THREAD_LOCAL.get();
    }

    static void setDataSourceType(DataSourceType type) {
        DATA_SOURCE_TYPE_THREAD_LOCAL.set(type);
    }
}
