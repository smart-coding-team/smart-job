package com.smartcoding.web.controller.monitor;

import com.smartcoding.common.core.controller.BaseController;
import com.smartcoding.common.core.domain.ResultModel;
import com.smartcoding.framework.web.domain.Server;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务器监控
 *
 * @author wuque
 */
@RestController
@RequestMapping("/api/monitor/server")
public class ServerController extends BaseController {

    @PreAuthorize("@ss.hasPermi('monitor:server:list')")
    @GetMapping()
    public ResultModel getInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        return ResultModel.success(server);
    }
}
