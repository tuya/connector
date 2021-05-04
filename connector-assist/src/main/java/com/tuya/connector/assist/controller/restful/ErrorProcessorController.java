package com.tuya.connector.assist.controller.restful;

import com.tuya.connector.assist.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/5 12:14 下午
 */

@Slf4j
@RestController
@RequestMapping("/test/errorprocessor")
public class ErrorProcessorController {

    @RequestMapping("/success")
    public Result<Boolean> success() {
        return Result.<Boolean>builder().result(true).build();
    }

    @RequestMapping("/error/{code}")
    public Result<Boolean> error(@PathVariable("code") Integer code) {
        return Result.<Boolean>builder().code(code).msg("error code : " + code).build();
    }

}
