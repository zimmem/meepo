package com.zimmem.meepo;

import java.util.UUID;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

/**
 * 
 * @author zhaowen.zhuang
 */
public class MeepoDataSourceTransactionManager extends DataSourceTransactionManager {

    private static final long serialVersionUID = -5398456201969702443L;


    private DataSourceWrapper dataSourceWrapper;

    private ThreadLocal<String> clearKeyHolder = new ThreadLocal<String>();


    @Override
    public void afterPropertiesSet() {

        if (this.dataSourceWrapper == null) {
            throw new IllegalArgumentException("Property 'dataSourceWrapper' is required");
        }
        super.setDataSource(dataSourceWrapper);
        super.afterPropertiesSet();
    }

    @Override
    protected void doBegin(Object transaction, final TransactionDefinition definition) {

        String uuid = clearKeyHolder.get();
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
            clearKeyHolder.set(uuid);
        }

        getDataSourceWrapper().selectDatasource(uuid, new MasterSlaveStrategy() {

            public Role select() {
                if (definition.isReadOnly()) {
                    return Role.Slave;
                } else {
                    return Role.Master;
                }
            }

        });

        try {
            super.doBegin(transaction, definition);
        } catch (Exception e) {
            getDataSourceWrapper().clear(uuid);
            clearKeyHolder.remove();
            throw e;

        }
    }

    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);
        String uuid = clearKeyHolder.get();
        getDataSourceWrapper().clear(uuid);
        clearKeyHolder.remove();
    }

    public DataSourceWrapper getDataSourceWrapper() {
        return dataSourceWrapper;
    }



    public void setDataSourceWrapper(DataSourceWrapper dataSourceWrapper) {
        this.dataSourceWrapper = dataSourceWrapper;
    }

}
