package com.tuya.connector.assist.controller.restful;

import com.tuya.connector.assist.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/4 8:30 下午
 */
@Slf4j
@RestController
@RequestMapping("/test/autosetheader")
public class AutoSetHeaderController {

    @GetMapping("/enable")
    public Result<String> enable(@RequestHeader("autoheader") String autoheader) {
        log.warn("/enable");
        return Result.<String>builder().result(autoheader).build();
    }

}
