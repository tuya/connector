package com.tuya.connector.spring.export.handler;

import javassist.CtClass;
import javassist.CtMethod;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/20 9:52 下午
 */
public abstract class BaseNonValueParameterBaseAnnotationHandler extends BaseAnnotationHandler<Annotation> {

    @Override
    public Object handle(Annotation annotation, CtClass ctClass, CtMethod ctMethod) {
        String mappedTypeName = mappedTypeName(annotation);
        if (Objects.isNull(mappedTypeName)) {
            return null;
        }

        return new javassist.bytecode.annotation.Annotation(mappedTypeName, ctClass.getClassFile().getConstPool());
    }

}
