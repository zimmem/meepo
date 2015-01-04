/**
 * 
 */
package com.zimmem.meepo;

import java.util.List;

import javax.sql.DataSource;

/**
 * @author zhaowen.zhuang
 *
 */
public class SingleSlaveStrategy implements SlaveChooseStrategy {

    @Override
    public DataSource choose(List<DataSource> dataSources) {
        return dataSources.get(0);
    }

}
