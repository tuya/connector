package com.tuya.connector.api.handler;

import javassist.CtClass;
import javassist.CtMethod;

import java.lang.annotation.Annotation;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/20 9:52 下午
 */
public abstract class BaseNonValueParameterBaseAnnotationHandler extends BaseAnnotationHandler<Annotation> {

    @Override
    public Object handle(Annotation annotation, CtClass ctClass, CtMethod ctMethod) {
        return new javassist.bytecode.annotation.Annotation(mappedTypeName(annotation), ctClass.getClassFile().getConstPool());
    }

}
