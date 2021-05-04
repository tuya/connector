package com.tuya.connector.spring.export.handler;

import javassist.CtClass;
import javassist.CtMethod;

import java.lang.annotation.Annotation;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/20 9:43 下午
 */
public class PostAnnotationHandler extends BaseHttpAnnotationHandler {

    @Override
    public Object handle(Annotation src, CtClass ctClass, CtMethod ctMethod) {
        return super.handle(src, ctClass, ctMethod);
    }
}
