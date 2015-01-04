/**
 * 
 */
package com.zimmem.meepo.sample;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zimmem.meepo.EnforceMaster;

/**
 * @author zhaowen.zhuang
 *
 */
public class SelectService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @EnforceMaster
    public String getFromMaster() {
        return execute();

    }



    public String getFromSlave() {
        return execute();

    }

    public String saveFromMaster() {
        return execute();

    }

    /**
     * @return
     */
    private String execute() {
        Map<String, Object> map = jdbcTemplate.queryForMap("select * from test_table");
        return (String) map.get("NAME");
    }

}
