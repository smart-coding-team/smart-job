package cn.smartcoding.web.controller.system;

import cn.smartcoding.common.annotation.Log;
import cn.smartcoding.common.core.controller.BaseController;
import cn.smartcoding.common.core.domain.ResultModel;
import cn.smartcoding.common.enums.BusinessType;
import cn.smartcoding.common.utils.SecurityUtils;
import cn.smartcoding.system.domain.SysNotice;
import cn.smartcoding.system.service.ISysNoticeService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.smartcoding.common.core.domain.util.ResultModelUtils.toAjax;

/**
 * 公告 信息操作处理
 *
 * @author wuque
 */
@RestController
@RequestMapping("/api/system/notice")
public class SysNoticeController extends BaseController {
    @Autowired
    private ISysNoticeService noticeService;

    /**
     * 获取通知公告列表
     */
    @PreAuthorize("@ss.hasPermi('system:notice:list')")
    @GetMapping("/list")
    public ResultModel list(@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
                            @RequestParam(value = "noticeTitle", required = false) String noticeTitle,
                            @RequestParam(value = "noticeType", required = false) String noticeType,
                            @RequestParam(value = "createBy", required = false) String createBy) {

        SysNotice dictData = new SysNotice();
        dictData.setNoticeTitle(noticeTitle);
        dictData.setNoticeTitle(noticeType);
        dictData.setCreateBy(createBy);
        PageInfo<SysNotice> list = noticeService.selectNoticeList(pageNum, pageSize, dictData);
        return ResultModel.success(list);
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:notice:query')")
    @GetMapping(value = "/{noticeId}")
    public ResultModel getInfo(@PathVariable Long noticeId) {
        return ResultModel.success(noticeService.selectNoticeById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:add')")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultModel add(@Validated @RequestBody SysNotice notice) {
        notice.setCreateBy(SecurityUtils.getUsername());
        return toAjax(noticeService.insertNotice(notice));
    }

    /**
     * 修改通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultModel edit(@Validated @RequestBody SysNotice notice) {
        notice.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(noticeService.updateNotice(notice));
    }

    /**
     * 删除通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:remove')")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public ResultModel remove(@PathVariable Long[] noticeIds) {
        return toAjax(noticeService.deleteNoticeByIds(noticeIds));
    }
}
