

package com.smartcoding.schedule.service.impl;

import com.smartcoding.schedule.core.model.XxlJobLock;
import com.smartcoding.schedule.mapper.XxlJobLockMapper;
import com.smartcoding.schedule.service.XxlJobInitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service
@Slf4j
public class XxlJobInitServiceImpl implements XxlJobInitService {

    @Resource
    private XxlJobLockMapper xxlJobLockMapper;


    @Value("${xxl.job.admin.initUser:false}")
    private boolean initUser;

    @PostConstruct
    public void init() {
        if (initUser) {
        }
        initXxlJobLock();
    }


    /**
     * 初始化的xxl-job调度锁
     */
    @Override
    public void initXxlJobLock() {
        XxlJobLock scheduleLock = xxlJobLockMapper.selectByPrimaryKey("schedule_lock");
        if (scheduleLock != null) {
            return;
        }
        log.info(">>> xxl-job init xxl_job_lock,scheduleLock:schedule_lock");
        xxlJobLockMapper.insertLockName("schedule_lock");
    }
}
