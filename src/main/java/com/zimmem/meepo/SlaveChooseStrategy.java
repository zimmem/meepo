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
public interface SlaveChooseStrategy {
    DataSource choose(List<DataSource> dataSources);
}
