package com.tuya.connector.spring.export.handler;

import com.tuya.connector.api.annotations.DELETE;
import com.tuya.connector.api.annotations.GET;
import com.tuya.connector.api.annotations.POST;
import com.tuya.connector.api.annotations.PUT;
import com.tuya.connector.api.exceptions.ConnectorAnnotationException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/20 7:19 下午
 */
@Slf4j
public class BaseHttpAnnotationHandler extends BaseAnnotationHandler<Annotation> {

    @Override
    public Object handle(Annotation annotation, CtClass ctClass, CtMethod ctMethod) {
        String mappedTypeName = mappedTypeName(annotation);
        if (Objects.isNull(mappedTypeName)) {
            return false;
        }

        ConstPool constPool = ctClass.getClassFile().getConstPool();
        AnnotationsAttribute methodAttribute;
        AttributeInfo attribute = ctMethod.getMethodInfo().getAttribute(AnnotationsAttribute.visibleTag);
        if (Objects.nonNull(attribute)) {
            methodAttribute = (AnnotationsAttribute) attribute;
        } else {
            methodAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        }
        HttpValue httpValue = extractHttpValue(annotation);
        javassist.bytecode.annotation.Annotation ctAnnotation = new javassist.bytecode.annotation.Annotation(mappedTypeName, constPool);
        ArrayMemberValue arrayMemberValue = new ArrayMemberValue(constPool);
        StringMemberValue[] stringMemberValues = new StringMemberValue[1];
        arrayMemberValue.setValue(stringMemberValues);
        stringMemberValues[0] = new StringMemberValue(httpValue.value, constPool);
        ctAnnotation.addMemberValue("value", arrayMemberValue);

        methodAttribute.addAnnotation(ctAnnotation);
        ctMethod.getMethodInfo().addAttribute(methodAttribute);
        return true;
    }

    private HttpValue extractHttpValue(Annotation annotation) {
        if (annotation instanceof GET) {
            return HttpValue.builder().value(((GET) annotation).value()).build();
        } else if (annotation instanceof POST) {
            return HttpValue.builder().value(((POST) annotation).value()).build();
        } else if (annotation instanceof PUT) {
            return HttpValue.builder().value(((PUT) annotation).value()).build();
        } else if (annotation instanceof DELETE) {
            return HttpValue.builder().value(((DELETE) annotation).value()).build();
        }
        throw new ConnectorAnnotationException(String.format("Unknown annotation: %s", annotation.getClass().getName()));
    }

    @Builder
    private static class HttpValue {
        String value;
        String method;
        String path;
        boolean hasBody;
    }

}