/**
 * 
 */
package com.zimmem.meepo;

import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * @author zhaowen.zhuang
 *
 */
public class EnforceMasterStrategy implements MasterSlaveStrategy {

    private DataSourceWrapper dataSourceWrapper;

    public Role select() {
        return Role.Master;
    }

    public Object invoke(ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        if (signature.getMethod().getAnnotation(EnforceMaster.class) == null) {
            return pjp.proceed();
        }

        String uuid = UUID.randomUUID().toString();
        getDataSourceWrapper().selectDatasource(uuid, this);
        try {
            return pjp.proceed();
        } finally {
            getDataSourceWrapper().clear(uuid);;
        }
    }

    public DataSourceWrapper getDataSourceWrapper() {
        return dataSourceWrapper;
    }

    public void setDataSourceWrapper(DataSourceWrapper dataSourceWrapper) {
        this.dataSourceWrapper = dataSourceWrapper;
    }
}
