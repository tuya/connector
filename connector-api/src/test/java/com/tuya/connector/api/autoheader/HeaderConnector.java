package com.tuya.connector.api.autoheader;

import com.tuya.connector.api.annotations.GET;
import com.tuya.connector.api.annotations.Path;

import java.util.Map;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/4 8:35 下午
 */
public interface HeaderConnector extends HeaderAbility{

    @Override
    @GET("/test/autosetheader/enable")
    String enable();

    @Override
    @GET("/test/annotations/plus-and-chinese-char/{plus}/{chinese_char}?param=xyz")
    Map<String, String> chineseAndPlusTest(@Path("plus") String plus, @Path(value="chinese_char") String chineseChar);

}
