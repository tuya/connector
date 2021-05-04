package com.tuya.connector.assist.controller.spring;

import com.tuya.connector.assist.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/4 6:05 下午
 */
@Slf4j
@RestController("ErrorProcessor4Register")
@RequestMapping("/test/spring/errorprocessor")
public class ErrorProcessorController {

    @GetMapping("/get")
    public Result<Boolean> get() {
        log.warn("/get");
        return Result.<Boolean>builder().result(true).build();
    }

}
