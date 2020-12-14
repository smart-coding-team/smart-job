
//
//package com.smartcoding.schedule.test;
//
//import XxlJobRegistryMapper;
//import com.smartcoding.schedule.com.smartcoding.robot.core.model.XxlJobRegistry;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.annotation.Resource;
//import java.util.Arrays;
//import java.util.List;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class XxlJobRegistryDaoTest {
//
//    @Resource
//    private XxlJobRegistryMapper xxlJobRegistryDao;
//
//    @Test
//    public void test(){
//        int ret = xxlJobRegistryDao.registryUpdate("g1", "k1", "v1");
//        if (ret < 1) {
//            ret = xxlJobRegistryDao.registrySave("g1", "k1", "v1");
//        }
//
//        List<XxlJobRegistry> list = xxlJobRegistryDao.findAll(1);
//
//        int ret2 = xxlJobRegistryDao.removeDead(Arrays.asList(1));
//
//    }
//
//}
