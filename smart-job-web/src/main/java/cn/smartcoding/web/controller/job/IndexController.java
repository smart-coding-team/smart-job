

package cn.smartcoding.web.controller.job;

import cn.smartcoding.common.core.controller.BaseController;
import cn.smartcoding.common.core.domain.ResultModel;
import cn.smartcoding.schedule.service.XxlJobService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * index controller
 *
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
@RequestMapping("/api")
public class IndexController extends BaseController {

    @Resource
    private XxlJobService xxlJobService;


    //新的仪表盘接口
    @RequestMapping("/dashboardInfo")
    @ResponseBody
    public ResultModel dashboardInfo() {
        return ResultModel.success(xxlJobService.dashboardInfo());
    }

    @RequestMapping("/chartInfo")
    @ResponseBody
    public ResultModel chartInfo(Date startDate, Date endDate) {
        Map<String, Object> chartInfo = xxlJobService.chartInfo(startDate, endDate);

        return ResultModel.success(chartInfo);
    }


}
