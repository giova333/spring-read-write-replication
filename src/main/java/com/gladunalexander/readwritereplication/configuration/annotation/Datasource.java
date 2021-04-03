package com.gladunalexander.readwritereplication.configuration.annotation;

import com.gladunalexander.readwritereplication.configuration.DataSourceType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Datasource {

    DataSourceType type() default DataSourceType.WRITE;
}
