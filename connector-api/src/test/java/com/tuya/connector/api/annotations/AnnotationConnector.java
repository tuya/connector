package com.tuya.connector.api.annotations;

import java.util.Map;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/4 6:45 下午
 */
public interface AnnotationConnector extends AnnotationAbility{

    @Override
    @GET("/test/annotations/get")
    Boolean get();

    @Override
    @POST("/test/annotations/post")
    Boolean post();

    @Override
    @PUT("/test/annotations/put")
    Boolean put();

    @Override
    @DELETE("/test/annotations/delete")
    Boolean delete();

    @Override
    @GET("/test/annotations/path/{path_param}")
    String path(@Path("path_param") String pathParam);

    @Override
    @GET("/test/annotations/query")
    String query(@Query("param") String param);

    @Override
    @GET("/test/annotations/queryMap")
    String queryMap(@QueryMap Map<String, Object> map);

    @Override
    @GET
    String url(@Url String url);

    @Override
    @Headers("headerKey:headerValue")
    @GET("/test/annotations/headers")
    String headers();

    @Override
    @GET("/test/annotations/header")
    String header(@Header("headerKey") String header);

    @Override
    @GET("/test/annotations/headerMap")
    String headerMap(@HeaderMap Map<String, String> headerMap);

    @Override
    @POST("/test/annotations/body")
    String body(@Body Object body);

    @Override
    @GET
    String urlGet(@Url String url);

    @Override
    @POST
    String urlPost(@Url String url, @Body Map<String, String> param);
}
