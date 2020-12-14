package cn.smartcoding.schedule.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ClearLogDto implements Serializable {
    private Long jobGroup;
    private Long jobId;
    private Integer type;
    private Integer num;

}
