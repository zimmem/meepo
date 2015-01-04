/**
 * 
 */
package com.zimmem.meepo;

/**
 * @author zhaowen.zhuang
 *
 */
public interface MasterSlaveStrategy {
    public static enum Role {
        Master, Slave
    }

    Role select();
}
