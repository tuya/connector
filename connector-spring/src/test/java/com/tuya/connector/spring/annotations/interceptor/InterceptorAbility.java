package com.tuya.connector.spring.annotations.interceptor;

import com.tuya.connector.api.model.Result;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/6 4:41 下午
 */
public interface InterceptorAbility {
    Boolean get();

    Boolean deleteNoBody(Integer num);

    Object deleteWithBody(Result result);

    Object getWithBody();
}
