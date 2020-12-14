

package cn.smartcoding.schedule.core.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 无缺
 * @date 2019-07-13
 */
@Data
public class JobAddressGroupVO implements Serializable {
    private List<JobAddressVO> registryInfoList;
    private int onLineNum;
    private String appName;

    /**
     * VARCHAR(60) 必填
     * 执行器名称
     */
    private String title;

    public void addOnLineNum() {
        this.onLineNum = this.onLineNum + 1;
    }
}
