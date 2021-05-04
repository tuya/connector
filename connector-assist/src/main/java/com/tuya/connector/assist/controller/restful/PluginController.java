package com.tuya.connector.assist.controller.restful;

import com.tuya.connector.assist.model.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/5 10:22 上午
 */
@RestController
@RequestMapping("/test/plugin")
public class PluginController {

    @RequestMapping("/hello")
    public Result<Boolean> plugin() {
        return Result.<Boolean>builder().result(true).build();
    }
}
