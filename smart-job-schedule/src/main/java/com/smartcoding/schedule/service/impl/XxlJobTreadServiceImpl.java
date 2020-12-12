package com.smartcoding.schedule.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.smartcoding.common.utils.DateUtils;
import com.smartcoding.common.utils.ip.IpUtils;
import com.smartcoding.schedule.core.enums.ScheduleStatusEnum;
import com.smartcoding.schedule.core.enums.ThreadTypeEnum;
import com.smartcoding.schedule.core.model.XxlJobThread;
import com.smartcoding.schedule.mapper.XxlJobThreadMapper;
import com.smartcoding.schedule.service.IXxlJobTreadService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 任务调度Service业务层处理
 *
 * @author wuque
 * @date 2020-08-29
 */
@Service
@Slf4j
public class XxlJobTreadServiceImpl implements IXxlJobTreadService {
    @Resource
    private XxlJobThreadMapper xxlJobThreadMapper;

    @Value("${server.port}")
    private String serverPort;

    private LoadingCache<Integer, XxlJobThread> lineInfoCache;
    private static String address;
    private static String hostName;

    @PostConstruct
    public void init() {
        address = IpUtils.getHostIp() + ":" + serverPort;
        hostName = IpUtils.getHostName();
        List<Integer> codeList = ThreadTypeEnum.getCodeList();
        List<XxlJobThread> xxlJobThreadList = xxlJobThreadMapper.selectXxlJobThreadListByAddress(address, codeList);
        Map<Integer, String> map = CollectionUtil.isEmpty(xxlJobThreadList) ? new HashMap<>() : xxlJobThreadList.stream().collect(Collectors.toMap(XxlJobThread::getThreadType, XxlJobThread::getAddress));
        codeList.forEach(scheduleType -> {
            if (!map.containsKey(scheduleType)) {
                insertThread(scheduleType, address, hostName);
            }
        });
        lineInfoCache = CacheBuilder.newBuilder()
                .maximumSize(50)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build(new CacheLoader<Integer, XxlJobThread>() {
                    @Override
                    public XxlJobThread load(Integer threadType) throws Exception {
                        XxlJobThread xxlJobThread = xxlJobThreadMapper.selectXxlJobThreadByAddress(threadType, address);
                        if (xxlJobThread != null) {
                            updateThread(threadType, address);
                        } else {
                            xxlJobThread = insertThread(threadType, address, hostName);
                        }
                        return xxlJobThread;
                    }
                });
    }

    private XxlJobThread insertThread(Integer threadType, String address, String hostName) {
        XxlJobThread newXxlJobThread = new XxlJobThread();
        newXxlJobThread.setAddress(address);
        newXxlJobThread.setHostName(hostName);
        newXxlJobThread.setThreadType(threadType);
        newXxlJobThread.setThreadStatus(ScheduleStatusEnum.OPEN.getCode());
        newXxlJobThread.setLastOnlineTime(new Date());
        newXxlJobThread.setUpdateBy("系统");
        newXxlJobThread.setUpdateTime(new Date());
        newXxlJobThread.setGmtCreate(new Date());
        newXxlJobThread.setGmtModified(new Date());
        xxlJobThreadMapper.insertXxlJobThread(newXxlJobThread);
        return newXxlJobThread;
    }
    private int updateThread(Integer threadType, String address) {
        return xxlJobThreadMapper.updateXxlJobThreadOnlineTime(threadType,address);
    }

    @Override
    public boolean isOpenThread(ThreadTypeEnum threadTypeEnum) {
        try {
            Integer threadType = threadTypeEnum.getCode();
            XxlJobThread xxlJobThread = lineInfoCache.get(threadType);
            if (xxlJobThread == null) {
                xxlJobThread = xxlJobThreadMapper.selectXxlJobThreadByAddress(threadType, address);
                if (xxlJobThread == null) {
                    xxlJobThread = insertThread(threadType, address, hostName);
                    lineInfoCache.put(threadType, xxlJobThread);
                }
            }
            return ScheduleStatusEnum.OPEN.getCode().equals(xxlJobThread.getThreadStatus());
        } catch (ExecutionException e) {
            log.error("load local cache error,threadType:{},", threadTypeEnum, e);
        }
        return false;
    }

    @Override
    public boolean autoDeleteOpenThread(ThreadTypeEnum threadTypeEnum) {
        try {
            Integer threadType = threadTypeEnum.getCode();
            return xxlJobThreadMapper.deleteXxlJobThreadByAddress(threadType, address, ScheduleStatusEnum.OPEN.getCode()) > 0;
        } catch (Exception e) {
            log.error("delete open thread error,threadType:{},", threadTypeEnum, e);
        }
        return false;
    }

    /**
     * 查询任务调度
     *
     * @param id 任务调度ID
     * @return 任务调度
     */
    @Override
    public XxlJobThread selectXxlJobThreadById(Long id) {
        return xxlJobThreadMapper.selectXxlJobThreadById(id);
    }

    /**
     * 查询任务调度列表
     *
     * @param xxlJobThread 任务调度
     * @return 任务调度
     */
    @Override
    public List<XxlJobThread> selectXxlJobThreadList(XxlJobThread xxlJobThread) {
        return xxlJobThreadMapper.selectXxlJobThreadList(xxlJobThread);
    }

    @Override
    public PageInfo<XxlJobThread> selectXxlJobThreadList(int pageNum, int pageSize, XxlJobThread dictData) {
        PageHelper.startPage(pageNum, pageSize);
        List<XxlJobThread> list = selectXxlJobThreadList(dictData);
        return new PageInfo<>(list);
    }

    /**
     * 新增任务调度
     *
     * @param xxlJobThread 任务调度
     * @return 结果
     */
    @Override
    public int insertXxlJobThread(XxlJobThread xxlJobThread) {
        return xxlJobThreadMapper.insertXxlJobThread(xxlJobThread);
    }

    /**
     * 修改任务调度
     *
     * @param xxlJobThread 任务调度
     * @return 结果
     */
    @Override
    public int updateXxlJobThread(XxlJobThread xxlJobThread) {
        xxlJobThread.setUpdateTime(DateUtils.getNowDate());
        return xxlJobThreadMapper.updateXxlJobThread(xxlJobThread);
    }

    /**
     * 批量删除任务调度
     *
     * @param ids 需要删除的任务调度ID
     * @return 结果
     */
    @Override
    public int deleteXxlJobThreadByIds(Long[] ids) {
        return xxlJobThreadMapper.deleteXxlJobThreadByIds(ids);
    }

    /**
     * 删除任务调度信息
     *
     * @param id 任务调度ID
     * @return 结果
     */
    @Override
    public int deleteXxlJobScheduleById(Long id) {
        return xxlJobThreadMapper.deleteXxlJobThreadById(id);
    }

    @Override
    public boolean stop(Long id, String userName) {
        return startOrStop(id, ScheduleStatusEnum.CLOSE, userName);
    }

    @Override
    public boolean start(Long id, String userName) {
        return startOrStop(id, ScheduleStatusEnum.OPEN, userName);
    }

    private boolean startOrStop(Long id, ScheduleStatusEnum scheduleStatusEnum, String userName) {
        XxlJobThread xxlJobThread = new XxlJobThread();
        xxlJobThread.setId(id);
        xxlJobThread.setUpdateBy(userName);
        xxlJobThread.setUpdateTime(new Date());
        xxlJobThread.setThreadStatus(scheduleStatusEnum.getCode());
        boolean result = xxlJobThreadMapper.updateXxlJobThread(xxlJobThread) > 0;
        XxlJobThread newXxlJobThread = xxlJobThreadMapper.selectXxlJobThreadById(id);
        if (newXxlJobThread != null) {
            lineInfoCache.refresh(newXxlJobThread.getThreadType());
        }
        return result;
    }

    @Override
    public void changeStatus(Long id, Integer status, String username) {
        ScheduleStatusEnum scheduleStatusEnum = ScheduleStatusEnum.fromCode(status, ScheduleStatusEnum.CLOSE);
        startOrStop(id, scheduleStatusEnum, username);
    }
}
