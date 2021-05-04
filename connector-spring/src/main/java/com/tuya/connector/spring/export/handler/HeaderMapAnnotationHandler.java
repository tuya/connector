package com.tuya.connector.spring.export.handler;

import javassist.CtClass;
import javassist.CtMethod;

import java.lang.annotation.Annotation;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/20 7:42 下午
 */
public class HeaderMapAnnotationHandler extends BaseNonValueParameterBaseAnnotationHandler {

    @Override
    public Object handle(Annotation annotation, CtClass ctClass, CtMethod ctMethod) {
        return super.handle(annotation, ctClass, ctMethod);
    }
}
