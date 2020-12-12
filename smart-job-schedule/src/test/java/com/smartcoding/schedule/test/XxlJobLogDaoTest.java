
//
//package com.smartcoding.schedule.test;
//
//import XxlJobLogMapper;
//import com.smartcoding.schedule.com.smartcoding.robot.core.model.XxlJobLog;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import javax.annotation.Resource;
//import java.util.Date;
//import java.util.List;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath*:spring/applicationcontext-*.xml")
//public class XxlJobLogDaoTest {
//
//    @Resource
//    private XxlJobLogMapper xxlJobLogDao;
//
//    @Test
//    public void test(){
//        List<XxlJobLog> list = xxlJobLogDao.pageList(0, 10, 1L, 1L,null, null,  null, null, 1);
//        int list_count = xxlJobLogDao.pageListCount(0, 10, 1L,1L, null, null, null, null, 1);
//
//        XxlJobLog log = new XxlJobLog();
//        log.setJobGroup(1L);
//        log.setJobId(1L);
//
//        int ret1 = xxlJobLogDao.updateByPrimaryKeySelective(log);
//        XxlJobLog dto = xxlJobLogDao.selectByPrimaryKey(log.getId());
//
//        log.setTriggerTime(new Date());
//        log.setTriggerCode(1);
//        log.setTriggerMsg("1");
//        log.setExecutorAddress("1");
//        log.setExecutorHandler("1");
//        log.setExecutorParam("1");
//        ret1 = xxlJobLogDao.updateTriggerInfo(log);
//        dto = xxlJobLogDao.selectByPrimaryKey(log.getId());
//
//
//        log.setHandleTime(new Date());
//        log.setHandleCode(2);
//        log.setHandleMsg("2");
//        ret1 = xxlJobLogDao.updateHandleInfo(log);
//        dto = xxlJobLogDao.selectByPrimaryKey(log.getId());
//
//
//        //List<Map<String, Object>> list2 = xxlJobLogDao.triggerCountByDay(new Date(new Date().getTime() + 30*24*60*60*1000), new Date());
//
//        int ret4 = xxlJobLogDao.clearLog(1L, 1L, new Date(), 100);
//
//        int ret2 = xxlJobLogDao.delete(log.getJobId());
//
//        int ret3 = xxlJobLogDao.triggerCountByHandleCode(-1);
//    }
//
//}
