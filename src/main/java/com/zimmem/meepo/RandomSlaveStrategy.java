/**
 * 
 */
package com.zimmem.meepo;

import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

/**
 * @author zhaowen.zhuang
 *
 */
public class RandomSlaveStrategy implements SlaveChooseStrategy {

    private Random random = new Random();

    @Override
    public DataSource choose(List<DataSource> dataSources) {
        return dataSources.get(random.nextInt(dataSources.size()));
    }

}
