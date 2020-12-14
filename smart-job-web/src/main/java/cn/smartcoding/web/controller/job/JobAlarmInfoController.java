package cn.smartcoding.web.controller.job;

import cn.hutool.core.util.StrUtil;
import cn.smartcoding.common.annotation.Log;
import cn.smartcoding.common.core.controller.BaseController;
import cn.smartcoding.common.core.domain.ResultModel;
import cn.smartcoding.common.core.domain.util.ResultModelUtils;
import cn.smartcoding.common.enums.BusinessType;
import cn.smartcoding.common.utils.SecurityUtils;
import cn.smartcoding.schedule.core.alarm.AlarmResult;
import cn.smartcoding.schedule.core.model.XxlAlarmInfo;
import cn.smartcoding.schedule.core.model.bo.XxlShortAlarmInfoBO;
import cn.smartcoding.schedule.core.model.vo.TestAlarmParamVO;
import cn.smartcoding.schedule.service.IXxlJobAlarmInfoService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务告警方式配置Controller
 *
 * @author wuque
 * @date 2020-08-29
 */
@RestController
@RequestMapping("/api/alarm")
public class JobAlarmInfoController extends BaseController {
    @Autowired
    private IXxlJobAlarmInfoService xxlJobAlarmInfoService;

    /**
     * 查询任务告警方式配置列表
     */
    @PreAuthorize("@ss.hasPermi('job:alarmInfo:list')")
    @GetMapping("/list")
    public ResultModel list(@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
                            @RequestParam(value = "id", required = false) Integer id,
                            @RequestParam(value = "alarmStatus", required = false) Integer alarmStatus,
                            @RequestParam(value = "alarmName", required = false) String alarmName) {

        XxlAlarmInfo xxlAlarmInfo = new XxlAlarmInfo();
        xxlAlarmInfo.setId(id);
        xxlAlarmInfo.setAlarmName(alarmName);
        xxlAlarmInfo.setAlarmStatus(alarmStatus);
        PageInfo<XxlAlarmInfo> list = xxlJobAlarmInfoService.selectXxlJobAlarmWayList(pageNum, pageSize, xxlAlarmInfo);
        return ResultModel.success(list);
    }


    /**
     * 获取任务告警方式配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('job:alarmInfo:query')")
    @GetMapping(value = "/{id}")
    public ResultModel getInfo(@PathVariable("id") Long id) {
        return ResultModel.success(xxlJobAlarmInfoService.selectXxlJobAlarmInfoById(id));
    }

    /**
     * 新增任务告警方式配置
     */
    @PreAuthorize("@ss.hasPermi('job:alarmInfo:add')")
    @Log(title = "任务告警方式配置", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultModel add(@RequestBody XxlAlarmInfo xxlAlarmInfo) {
        xxlAlarmInfo.setCreateBy(SecurityUtils.getUsername());
        return ResultModelUtils.toAjax(xxlJobAlarmInfoService.insertXxlJobAlarmWay(xxlAlarmInfo));
    }

    /**
     * 修改任务告警方式配置
     */
    @PreAuthorize("@ss.hasPermi('job:alarmInfo:edit')")
    @Log(title = "任务告警方式配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultModel edit(@RequestBody XxlAlarmInfo xxlAlarmInfo) {
        xxlAlarmInfo.setUpdateBy(SecurityUtils.getUsername());
        return ResultModelUtils.toAjax(xxlJobAlarmInfoService.updateXxlJobAlarmWay(xxlAlarmInfo));
    }

    /**
     * 删除任务告警方式配置
     */
    @PreAuthorize("@ss.hasPermi('job:alarmInfo:remove')")
    @Log(title = "任务告警方式配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public ResultModel remove(@PathVariable Long id) {
        return ResultModelUtils.toAjax(xxlJobAlarmInfoService.deleteXxlJobAlarmWayById(id));
    }

    /**
     * 任务告警测试
     */
    @PreAuthorize("@ss.hasPermi('job:alarmInfo:testSend')")
    @Log(title = "任务告警测试", businessType = BusinessType.RUN)
    @PostMapping("/testSend")
    public ResultModel testSendAlarm(@RequestBody TestAlarmParamVO xxlAlarmInfo) {
        AlarmResult alarmResult = xxlJobAlarmInfoService.testSendAlarm(xxlAlarmInfo);
        return ResultModel.success(alarmResult);
    }

    @GetMapping("/querySearch")
    @ApiOperation("执行器列表")
    public ResultModel querySearch(@RequestParam(value = "queryStr", required = false) String searchContent, @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        List<XxlShortAlarmInfoBO> list = xxlJobAlarmInfoService.getXxlShortAlarmInfoBO(searchContent, pageSize);
        return ResultModel.success(list);
    }

    @GetMapping("/queryIds")
    @ApiOperation("执行器列表")
    public ResultModel queryIds(@RequestParam(value = "alarmIds") String alarmIds) {
        List<Integer> idList = Arrays.stream(StrUtil.splitToInt(alarmIds, ',')).boxed().collect(Collectors.toList());
        List<XxlShortAlarmInfoBO> list = xxlJobAlarmInfoService.queryIds(idList);
        return ResultModel.success(list);
    }
}
