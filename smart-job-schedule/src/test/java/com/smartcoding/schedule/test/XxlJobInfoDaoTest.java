
//
//package com.smartcoding.schedule.test;
//
//import com.smartcoding.schedule.com.smartcoding.robot.core.model.XxlJobInfo;
//import XxlJobInfoMapper;
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
//public class XxlJobInfoDaoTest {
//
//	@Resource
//	private XxlJobInfoMapper xxlJobInfoDao;
//
//	@Test
//	public void pageList(){
//		List<XxlJobInfo> list = xxlJobInfoDao.pageList(0, 20, 0L, null, -1, null, null, null);
//		int list_count = xxlJobInfoDao.pageListCount(0, 20, 0L, null, -1, null, null, null);
//
//		System.out.println(list);
//		System.out.println(list_count);
//
//		List<XxlJobInfo> list2 = xxlJobInfoDao.getJobsByGroup(1L);
//	}
//
//	@Test
//	public void save_load(){
//		XxlJobInfo info = new XxlJobInfo();
//		info.setJobGroup(1L);
//		info.setJobCron("jobCron");
//		info.setJobName("wuque");
//		info.setAuthor("setAuthor");
//		info.setAlarmUserIds("setAlarmUserIds");
//		info.setExecutorRouteStrategy("setExecutorRouteStrategy");
//		info.setExecutorHandler("setExecutorHandler");
//		info.setExecutorParam("setExecutorParam");
//		info.setExecutorBlockStrategy("setExecutorBlockStrategy");
//		info.setGlueType("setGlueType");
//		info.setGlueSource("setGlueSource");
//		info.setGlueRemark("setGlueRemark");
//		info.setChildJobId("1");
//
//		int count = xxlJobInfoDao.insertSelective(info);
//
//		XxlJobInfo info2 = xxlJobInfoDao.selectByPrimaryKey(info.getId());
//		info2.setJobCron("jobCron2");
//		info.setJobName("wuque");
//		info2.setAuthor("setAuthor2");
//		info2.setAlarmUserIds("setAlarmEmail2");
//		info2.setExecutorRouteStrategy("setExecutorRouteStrategy2");
//		info2.setExecutorHandler("setExecutorHandler2");
//		info2.setExecutorParam("setExecutorParam2");
//		info2.setExecutorBlockStrategy("setExecutorBlockStrategy2");
//		info2.setGlueType("setGlueType2");
//		info2.setGlueSource("setGlueSource2");
//		info2.setGlueRemark("setGlueRemark2");
//		info2.setGlueUpdatetime(new Date());
//		info2.setChildJobId("1");
//
//		int item2 = xxlJobInfoDao.update(info2);
//
//		xxlJobInfoDao.deleteByPrimaryKey(info2.getId());
//
//		List<XxlJobInfo> list2 = xxlJobInfoDao.getJobsByGroup(1L);
//
//		int ret3 = xxlJobInfoDao.findAllCount();
//
//	}
//
//}
