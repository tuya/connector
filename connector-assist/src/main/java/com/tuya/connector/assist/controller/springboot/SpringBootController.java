package com.tuya.connector.assist.controller.springboot;

import com.alibaba.fastjson.JSON;
import com.tuya.connector.assist.model.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
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
    public Result<HashMap> deleteTest(@RequestBody HashMap map) {
        log.info(JSON.toJSONString(map));
        return Result.<HashMap>builder().success(true).t(System.currentTimeMillis()).result(map).build();
    }

    @DeleteMapping("/deleteNoBody")
    public Result<Object> deleteTestNoBody(Integer num) {
        log.info(num.toString() + "!!!!!!!!!!!!!!");
        return Result.builder().code(200).msg("ok").t(100L).result(num).build();
    }

    @GetMapping("/getWithBody")
    public Result<Object> getWithBodyTest() {
        DeviceStatus ggggg = new DeviceStatus("1111", 1);
        log.info(ggggg.toString());
        return Result.builder().result(1).build();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class DeviceStatus implements Serializable {

        private static final long serialVersionUID = -3591144314043292495L;
        private String code;

        private Object value;
    }


}
