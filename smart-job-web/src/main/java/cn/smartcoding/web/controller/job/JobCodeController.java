

package cn.smartcoding.web.controller.job;

import cn.smartcoding.common.core.domain.CommonErrorCode;
import cn.smartcoding.common.exception.CommonException;
import cn.smartcoding.common.core.domain.ResultModel;
import cn.smartcoding.schedule.core.model.XxlJobInfo;
import cn.smartcoding.schedule.core.model.XxlJobLogGlue;
import cn.smartcoding.schedule.core.util.I18nUtil;
import cn.smartcoding.schedule.mapper.XxlJobInfoMapper;
import cn.smartcoding.schedule.mapper.XxlJobLogGlueMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;

/**
 * job code controller
 *
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
@RequestMapping("/api/jobcode")
public class JobCodeController {

    @Resource
    private XxlJobInfoMapper xxlJobInfoDao;
    @Resource
    private XxlJobLogGlueMapper xxlJobLogGlueDao;

    @RequestMapping("/save")
    @PreAuthorize("@ss.hasPermi('job:jobcode:save')")
    @ResponseBody
    public ResultModel save(Model model, @RequestParam(value = "id") Long id, @RequestParam(value = "glueSource") String glueSource, @RequestParam(value = "glueRemark") String glueRemark) {
        // valid
        if (glueRemark == null) {
            throw new CommonException(CommonErrorCode.ERROR, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_glue_remark")));
        }
        if (glueRemark.length() < 4 || glueRemark.length() > 100) {
            throw new CommonException(CommonErrorCode.ERROR, I18nUtil.getString("jobinfo_glue_remark_limit"));
        }
        XxlJobInfo existsJobInfo = xxlJobInfoDao.selectByPrimaryKey(id);
        if (existsJobInfo == null) {
            throw new CommonException(CommonErrorCode.ERROR, I18nUtil.getString("jobinfo_glue_jobid_unvalid"));
        }

        // update new code
        existsJobInfo.setGlueSource(glueSource);
        existsJobInfo.setGlueRemark(glueRemark);
        existsJobInfo.setGlueUpdatetime(new Date());
        xxlJobInfoDao.updateByPrimaryKeySelective(existsJobInfo);

        // log old code
        XxlJobLogGlue xxlJobLogGlue = new XxlJobLogGlue();
        xxlJobLogGlue.setJobId(existsJobInfo.getId());
        xxlJobLogGlue.setGlueType(existsJobInfo.getGlueType());
        xxlJobLogGlue.setGlueSource(glueSource);
        xxlJobLogGlue.setGlueRemark(glueRemark);
        xxlJobLogGlueDao.save(xxlJobLogGlue);

        // remove code backup more than 30
        xxlJobLogGlueDao.removeOld(existsJobInfo.getId(), 30);

        return ResultModel.success();
    }

}
