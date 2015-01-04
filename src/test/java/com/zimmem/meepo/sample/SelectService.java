/**
 * 
 */
package com.zimmem.meepo.sample;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author zhaowen.zhuang
 *
 */
public class SelectService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String getFromMaster() {
        Map<String, Object> map = jdbcTemplate.queryForMap("select * from test_table");
        return (String) map.get("NAME");

    }

    public String getFromSlave() {
        Map<String, Object> map = jdbcTemplate.queryForMap("select * from test_table");
        return (String) map.get("NAME");

    }

    public String saveFromMaster() {
        Map<String, Object> map = jdbcTemplate.queryForMap("select * from test_table");
        return (String) map.get("NAME");

    }


}
