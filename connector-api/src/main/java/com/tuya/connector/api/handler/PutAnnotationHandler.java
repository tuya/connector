package com.tuya.connector.api.handler;

import javassist.CtClass;
import javassist.CtMethod;

import java.lang.annotation.Annotation;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/20 7:19 下午
 */
public class PutAnnotationHandler extends BaseHttpAnnotationHandler {

    @Override
    public Object handle(Annotation srcPut, CtClass ctClass, CtMethod ctMethod) {
        return super.handle(srcPut, ctClass, ctMethod);
    }


}