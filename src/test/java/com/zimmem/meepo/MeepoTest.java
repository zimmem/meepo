package com.zimmem.meepo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zimmem.meepo.sample.SelectService;

/**
 * @author zhaowen.zhuang
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-context.xml")
public class MeepoTest {

    @Autowired
    private SelectService selectService;


    @Test
    public void testMethod() {

        String name = selectService.getFromSlave();
        System.out.println("select from slave, result = " + name);

        name = selectService.getFromMaster();
        System.out.println("select from master, result = " + name);

        name = selectService.saveFromMaster();
        System.out.println("save from master, result = " + name);


    }

}
