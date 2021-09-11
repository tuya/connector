package com.tuya.connector.spring.annotations.interceptor;

import java.util.HashMap;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/6 4:41 下午
 */
public interface InterceptorAbility {
    Boolean get();

    Boolean deleteNoBody(Integer num);

    Boolean deleteWithBody(HashMap map);
}
