package com.smartcoding.schedule.core.model.vo;

import com.smartcoding.schedule.core.model.XxlAlarmInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TestAlarmParamVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private XxlAlarmInfo alarmInfo;
    private List<Variable> variableList;
    private NoticeUser noticeUser;





}
