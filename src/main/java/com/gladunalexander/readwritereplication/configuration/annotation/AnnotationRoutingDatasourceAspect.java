package com.gladunalexander.readwritereplication.configuration.annotation;

import com.gladunalexander.readwritereplication.configuration.DataSourceType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class AnnotationRoutingDatasourceAspect {

    @Around("@annotation(com.gladunalexander.readwritereplication.configuration.annotation.Datasource)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            var annotation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(Datasource.class);
            DatasourceTypeThreadLocal.setDataSourceType(annotation.type());
            return joinPoint.proceed();
        } finally {
            DatasourceTypeThreadLocal.setDataSourceType(DataSourceType.WRITE);
        }
    }
}
