/**
 * 
 */
package com.zimmem.meepo;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.slf4j.LoggerFactory;

import com.zimmem.meepo.MasterSlaveStrategy.Role;

/**
 * 
 * @author zhaowen.zhuang
 */
public class DataSourceWrapper implements DataSource {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(DataSourceWrapper.class);

    private DataSource masterDataSource;

    private List<DataSource> slaveDataSrouces;

    private SlaveChooseStrategy slaveChooseStrategy;

    private ThreadLocal<String> clearKey = new ThreadLocal<String>();

    public ThreadLocal<DataSource> dataSorceHolder = new ThreadLocal<DataSource>();

    void selectDatasource(String clearKey, MasterSlaveStrategy strategy) {
        if (dataSorceHolder.get() != null) {
            return;
        }
        Role role = strategy.select();
        if (MasterSlaveStrategy.Role.Master == role) {
            dataSorceHolder.set(this.masterDataSource);
            if (log.isDebugEnabled()) {
                log.debug("using master datasourc by strategy ({}) with clearKey {}", strategy
                        .getClass().getName(), clearKey);
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("using slave datasourc by strategy ({}) with clearKey {}", strategy
                        .getClass().getName(), clearKey);
            }
            dataSorceHolder.set(slaveChooseStrategy.choose(this.slaveDataSrouces));
        }
        this.clearKey.set(clearKey);
    }

    public void clear(String clearKey) {

        if (log.isTraceEnabled()) {
            log.trace("try to clear dataSorceHolder with clearKey:{}", clearKey);
        }

        if (clearKey != null && clearKey.equals(this.clearKey.get())) {

            this.dataSorceHolder.remove();
            this.clearKey.remove();
            if (log.isDebugEnabled()) {
                log.debug("clear dataSorceHolder with clearKey:{} success", clearKey);
            }
        }
    }

    private DataSource getDataSource() {
        return dataSorceHolder.get();
    }

    public PrintWriter getLogWriter() throws SQLException {
        throw new NotImplementedException();
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new NotImplementedException();

    }

    public void setLoginTimeout(int seconds) throws SQLException {
        throw new NotImplementedException();
    }

    public int getLoginTimeout() throws SQLException {
        throw new NotImplementedException();
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new NotImplementedException();
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new NotImplementedException();
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new NotImplementedException();
    }

    public Connection getConnection() throws SQLException {
        return this.getDataSource() == null ? this.masterDataSource.getConnection() : this
                .getDataSource().getConnection();
    }

    public Connection getConnection(String username, String password) throws SQLException {
        return this.getDataSource() == null ? this.masterDataSource.getConnection(username,
                password) : this.getDataSource().getConnection(username, password);
    }

    public DataSource getMasterDataSource() {
        return masterDataSource;
    }

    public void setMasterDataSource(DataSource masterDataSource) {
        this.masterDataSource = masterDataSource;
    }

    public List<DataSource> getSlaveDataSrouces() {
        return slaveDataSrouces;
    }

    public void setSlaveDataSrouces(List<DataSource> slaveDataSrouces) {
        this.slaveDataSrouces = slaveDataSrouces;
    }

    public SlaveChooseStrategy getSlaveChooseStrategy() {
        return slaveChooseStrategy;
    }

    public void setSlaveChooseStrategy(SlaveChooseStrategy slaveChooseStrategy) {
        this.slaveChooseStrategy = slaveChooseStrategy;
    }

}
