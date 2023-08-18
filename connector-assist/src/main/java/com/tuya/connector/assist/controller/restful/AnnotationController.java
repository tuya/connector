package com.tuya.connector.assist.controller.restful;

import com.alibaba.fastjson.JSON;
import com.tuya.connector.assist.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/4 6:05 下午
 */
@Slf4j
@RestController
@RequestMapping("/test/annotations")
public class AnnotationController {

    @GetMapping("/get")
    public Result<Boolean> get() {
        log.warn("/get");
        return Result.<Boolean>builder().result(true).build();
    }

    @PostMapping("/post")
    public Result<Boolean> post() {
        log.warn("/post");
        return Result.<Boolean>builder().result(true).build();
    }

    @PutMapping("/put")
    public Result<Boolean> put() {
        log.warn("/put");
        return Result.<Boolean>builder().result(true).build();
    }

    @DeleteMapping("/delete")
    public Result<Boolean> delete() {
        log.warn("/delete");
        return Result.<Boolean>builder().result(true).build();
    }


    @RequestMapping("/path/{param}")
    public Result<String> path(@PathVariable("param") String param) {
        log.warn("/path/{param}");
        return Result.<String>builder().result(param).build();
    }

    @RequestMapping("/query")
    public Result<String> query(@RequestParam("param") String param) {
        log.warn("/query");
        return Result.<String>builder().result(param).build();
    }

    @RequestMapping("/queryMap")
    public Result<String> query(@RequestParam("param1") String param1, @RequestParam("param2") String param2) {
        log.warn("/queryMap");
        Map<String, Object> queryMap =  new HashMap<>(2);
        queryMap.put("param1", param1);
        queryMap.put("param2", param2);
        return Result.<String>builder().result(JSON.toJSONString(queryMap)).build();
    }

    @RequestMapping("/url")
    public Result<String> url() {
        log.warn("/url");
        return Result.<String>builder().result("/url").build();
    }

    @RequestMapping("/headers")
    public Result<String> headers(@RequestHeader(value = "headerKey") String headerKey) {
        log.warn("/headers");
        return Result.<String>builder().result(headerKey).build();
    }

    @RequestMapping("/header")
    public Result<String> header(@RequestHeader(value = "headerKey") String headerKey) {
        log.warn("/header");
        return Result.<String>builder().result(headerKey).build();
    }

    @RequestMapping("/headerMap")
    public Result<String> headerMap(@RequestHeader(value = "headerKey") String headerKey) {
        log.warn("/headerMap");
        return Result.<String>builder().result(headerKey).build();
    }

    @RequestMapping("/body")
    public Result<String> body(@RequestBody Object object) {
        log.warn("/body");
        return Result.<String>builder().result(JSON.toJSONString(object)).build();
    }

    @GetMapping("/url-get/{path_p}")
    public Result<String> urlGet(@PathVariable("path_p") String p1, @RequestParam("query_p") String p2) {
        log.warn("/url-get, param:{}, {}", p1, p2);
        Map<String, String> ret = new HashMap();
        ret.put("path_p", p1);
        ret.put("query_p", p2);
        return Result.<String>builder().result(JSON.toJSONString(ret)).build();
    }

    @PostMapping("/url-post")
    public Result<String> urlPost(@RequestBody Map<String, String> p) {
        log.warn("/url-post, param:{}", JSON.toJSONString(p));
        return Result.<String>builder().result(JSON.toJSONString(p)).build();
    }

    @GetMapping("/url-object")
    public Result<List<Object>> urlObject() {
        log.warn("/url-object");
        return Result.<List<Object>>builder().result(
            List.of(
                Map.of("abc", 1, "deFg", 2, "mn_pq", 3),
                Map.of("abc", 4, "deFg", 5, "mn_pq", 6)
            )).build();
    }

}
