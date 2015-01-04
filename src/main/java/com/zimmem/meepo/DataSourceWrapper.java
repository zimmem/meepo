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

import com.zimmem.meepo.MasterSlaveStrategy.Role;

/**
 * 
 * @author zhaowen.zhuang
 */
public class DataSourceWrapper implements DataSource {

    private DataSource masterDataSource;

    private List<DataSource> slaveDataSrouces;

    private ThreadLocal<String> clearKey = new ThreadLocal<String>();

    public ThreadLocal<DataSource> dataSorceHolder = new ThreadLocal<DataSource>();

    void selectDatasource(String clearKey, MasterSlaveStrategy strategy) {
        if (dataSorceHolder.get() != null) {
            return;
        }
        Role role = strategy.select();
        if (MasterSlaveStrategy.Role.Master == role) {
            dataSorceHolder.set(this.masterDataSource);
        } else {
            dataSorceHolder.set(this.slaveDataSrouces.get(0));
        }
        this.clearKey.set(clearKey);
    }

    void clear(String clearKey) {
        if (clearKey != null && clearKey.equals(this.clearKey.get())) {
            this.dataSorceHolder.remove();
            this.clearKey.remove();
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

}
