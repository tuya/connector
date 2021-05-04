package com.tuya.connector.api.handler;

import javassist.CtClass;
import javassist.CtMethod;

import java.lang.annotation.Annotation;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/20 9:30 下午
 */
public class UrlAnnotationHandler extends BaseNonValueParameterBaseAnnotationHandler {
    @Override
    public Object handle(Annotation annotation, CtClass ctClass, CtMethod ctMethod) {
        return super.handle(annotation, ctClass, ctMethod);
    }
}
