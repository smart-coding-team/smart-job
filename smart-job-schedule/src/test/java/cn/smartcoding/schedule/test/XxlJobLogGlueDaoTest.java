
//
//package com.smartcoding.schedule.test;
//
//import com.smartcoding.schedule.com.smartcoding.robot.core.model.XxlJobLogGlue;
//import XxlJobLogGlueMapper;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import javax.annotation.Resource;
//import java.util.List;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath*:spring/applicationcontext-*.xml")
//public class XxlJobLogGlueDaoTest {
//
//    @Resource
//    private XxlJobLogGlueMapper xxlJobLogGlueDao;
//
//    @Test
//    public void test(){
//        XxlJobLogGlue logGlue = new XxlJobLogGlue();
//        logGlue.setJobId(1L);
//        logGlue.setGlueType("1");
//        logGlue.setGlueSource("1");
//        logGlue.setGlueRemark("1");
//        int ret = xxlJobLogGlueDao.save(logGlue);
//
//        List<XxlJobLogGlue> list = xxlJobLogGlueDao.findByJobId(1L);
//
//        int ret2 = xxlJobLogGlueDao.removeOld(1L, 1);
//
//        int ret3 =xxlJobLogGlueDao.deleteByJobId(1L);
//    }
//
//}
