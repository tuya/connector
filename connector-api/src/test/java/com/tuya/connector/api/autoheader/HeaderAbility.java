package com.tuya.connector.api.autoheader;

import java.util.Map;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/4 8:35 下午
 */
public interface HeaderAbility {

    String enable();

    Map<String, String> chineseAndPlusTest(String plus, String chineseChar);

}
