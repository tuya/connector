package com.tuya.connector.assist.controller.springboot;

import com.alibaba.fastjson.JSON;
import com.tuya.connector.assist.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/6 5:44 下午
 */

@Slf4j
@RestController
@RequestMapping("/test/springboot")
public class SpringBootController {

    @GetMapping("/echo/{s}")
    public Result<String> get(@PathVariable String s) {
        log.warn("/echo");
        return Result.<String>builder().result(s).build();
    }

    @RequestMapping("/export")
    public Result<Boolean> export() {
        log.warn("/export");
        return Result.<Boolean>builder().result(true).build();
    }

    @RequestMapping("/export/test/void")
    public Result<Void> exportVoid() {
        log.warn("/export/test/void");
        return Result.<Void>builder().result(null).build();
    }

    @RequestMapping("/export/test/query")
    public Result<String> exportQuery(@RequestParam("a") String a) {
        log.warn("/export/test/query");
        return Result.<String>builder().result(a).build();
    }

    @RequestMapping("/export/test/body")
    public Result<Object> exportQuery(@RequestBody Object body) {
        log.warn("/export/test/body");
        return Result.builder().result(JSON.toJSONString(body)).build();
    }

    @DeleteMapping("/deleteWithBody")
    public Result<Object> deleteTest(@RequestBody HashMap map) {
        log.info(JSON.toJSONString(map));
        return Result.builder().result(true).build();
    }

    @DeleteMapping("/deleteNoBody")
    public Result<Object> deleteTestNoBody(Integer num) {
        log.info(num.toString() + "!!!!!!!!!!!!!!");
        return Result.builder().result(true).build();
    }

}
