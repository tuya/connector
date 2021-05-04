package com.tuya.connector.spring.annotations.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/7 12:30 下午
 */
@RestController
public class ExportController {
    @Autowired
    ExportConnector connector;

    @Autowired
    HttpServletRequest request;

    // @GET("/test/annotations/get")
    @GetMapping("/test/annotations/get")
    Boolean get() {
         return connector.get();
    }

    @PostMapping("/test/annotations/post")
    Boolean post() {
        return connector.post();
    }

    @PutMapping("/test/annotations/put")
    Boolean put() {
        return connector.put();
    }

    @DeleteMapping("/test/annotations/delete")
    Boolean delete() {
        return connector.delete();
    }

    @GetMapping("/test/annotations/path/{path_param}")
    String path(@PathVariable("path_param") String pathParam) {
        return connector.path(pathParam);
    }

    @GetMapping("/test/annotations/query")
    String query(@RequestParam("param") String param) {
        return connector.query(param);
    }

    // @GetMapping("/test/annotations/queryMap")
    // String queryMap(@QueryMap Map<String, Object> map) {// TODO
    //     return connector.queryMap(request.getParameterMap());
    // }
    //
    // // TODO 单例 URL Mapping 接收
    // @GET
    // String url(@Url String url) {
    //     return connector.url(url);
    // }
    //
    // @Headers("headerKey:headerValue") // TODO
    // @GetMapping("/test/annotations/headers")
    // String headers() {
    //     return connector.headers();
    // }

    @GetMapping("/test/annotations/header")
    String header(@RequestHeader("headerKey") String header) {
        return connector.header(header);
    }

    // @GetMapping("/test/annotations/headerMap")
    // String headerMap(@HeaderMap Map<String, String> headerMap) { // TODO
    //     return connector.headerMap(headerMap);
    // }

    @PostMapping("/test/annotations/body")
    String body(@RequestBody Object body) {
        return connector.body(body);
    }
}
