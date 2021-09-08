package com.tuya.connector.api.handler;

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
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import lombok.Builder;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/20 7:19 下午
 */
public class BaseHttpAnnotationHandler extends BaseAnnotationHandler<Annotation> {

    @Override
    public Object handle(Annotation annotation, CtClass ctClass, CtMethod ctMethod) {
        ConstPool constPool = ctClass.getClassFile().getConstPool();
        AnnotationsAttribute methodAttribute;
        AttributeInfo attribute = ctMethod.getMethodInfo().getAttribute(AnnotationsAttribute.visibleTag);
        if (Objects.nonNull(attribute)) {
            methodAttribute = (AnnotationsAttribute) attribute;
        } else {
            methodAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        }
        HttpValue httpValue = extractHttpValue(annotation);
        javassist.bytecode.annotation.Annotation ctAnnotation = new javassist.bytecode.annotation.Annotation(mappedTypeName(annotation), constPool);
        MemberValue getValue = new StringMemberValue(httpValue.value, constPool);
        ctAnnotation.addMemberValue("value", getValue);
        if (annotation instanceof DELETE) {
            MemberValue getMethod = new StringMemberValue("DELETE", constPool);
            MemberValue getPath = new StringMemberValue(httpValue.value, constPool);
            MemberValue getHasBody = new BooleanMemberValue(true, constPool);
            ctAnnotation.addMemberValue("method", getMethod);
            ctAnnotation.addMemberValue("path", getPath);
            ctAnnotation.addMemberValue("hasBody", getHasBody);
        }
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