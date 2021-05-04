package com.tuya.connector.spring.export.handler;

import javassist.CtClass;
import javassist.CtMethod;

import java.lang.annotation.Annotation;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/20 7:19 下午
 */
public class GetAnnotationHandler extends BaseHttpAnnotationHandler {

    @Override
    public Object handle(Annotation srcGet, CtClass ctClass, CtMethod ctMethod) {
        return super.handle(srcGet, ctClass, ctMethod);
    }


}