package com.tuya.connector.spring.annotations.export;

import com.tuya.connector.api.annotations.*;
import com.tuya.connector.spring.annotations.Export;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/7 12:30 下午
 */
@Export
public interface ExportConnector {

    @GET("/test/annotations/get")
    Boolean get();

    @POST("/test/annotations/post")
    Boolean post();

    @PUT("/test/annotations/put")
    Boolean put();

    @DELETE("/test/annotations/delete")
    Boolean delete();

    @GET("/test/annotations/path/{path_param}")
    String path(@Path("path_param") String pathParam);

    @GET("/test/annotations/query")
    String query(@Query("param") String param);

    // @GET("/test/annotations/queryMap")
    // String queryMap(@QueryMap Map<String, Object> map);
    //
    // @GET
    // String url(@Url String url);
    //
    // @Headers("headerKey:headerValue")
    // @GET("/test/annotations/headers")
    // String headers();

    @GET("/test/annotations/header")
    String header(@Header("headerKey") String header);

    // @GET("/test/annotations/headerMap")
    // String headerMap(@HeaderMap Map<String, String> headerMap);

    @POST("/test/annotations/body")
    String body(@Body Object body);
}
