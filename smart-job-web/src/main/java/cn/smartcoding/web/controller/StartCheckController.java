

package cn.smartcoding.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/")
public class StartCheckController {
    private static final Logger logger = LoggerFactory.getLogger(StartCheckController.class);

    private boolean makeFail = false;

    /**
     * SLB状态检查和发布脚本自检都会用到该url
     *
     * @return
     */
    @GetMapping(value = "checkpreload.htm")
    public ResponseEntity<String> checkPreload() {
        if (makeFail) {
            return new ResponseEntity<>("fail", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /**
     * 系统关闭前调用这个接口
     *
     * @return
     */
    @GetMapping(value = "makeFail.htm")
    public String makeFail() {
        makeFail = true;
        return "success";
    }
}
