package com.tuya.connector.api.handler;

import com.tuya.connector.api.annotations.Headers;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.StringMemberValue;

import java.util.Objects;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/20 9:08 下午
 */
public class HeadersAnnotationHandler extends BaseAnnotationHandler<Headers> {
    @Override
    public Object handle(Headers headers, CtClass ctClass, CtMethod ctMethod) {
        ConstPool constPool = ctClass.getClassFile().getConstPool();
        AnnotationsAttribute methodAttribute;
        AttributeInfo attribute = ctMethod.getMethodInfo().getAttribute(AnnotationsAttribute.visibleTag);
        if (Objects.nonNull(attribute)) {
            methodAttribute = (AnnotationsAttribute) attribute;
        } else {
            methodAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        }
        javassist.bytecode.annotation.Annotation ctAnnotation =
                new javassist.bytecode.annotation.Annotation(retrofit2.http.Headers.class.getTypeName(), constPool);
        String[] headerValues = headers.value();
        StringMemberValue[] stringMemberValues = new StringMemberValue[headerValues.length];
        for (int i = 0; i < headerValues.length; i++) {
            stringMemberValues[i] = new StringMemberValue(headerValues[i], constPool);
        }
        ArrayMemberValue arrayMemberValue = new ArrayMemberValue(constPool);
        arrayMemberValue.setValue(stringMemberValues);
        ctAnnotation.addMemberValue("value", arrayMemberValue);
        methodAttribute.addAnnotation(ctAnnotation);
        ctMethod.getMethodInfo().addAttribute(methodAttribute);

        return true;
    }
}
