package com.tuya.connector.api.handler;

import com.tuya.connector.api.annotations.*;
import com.tuya.connector.api.exceptions.ConnectorAnnotationException;
import com.tuya.connector.api.utils.Utils;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ConstPool;
import lombok.SneakyThrows;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/20 7:55 下午
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ApiAnnotationHandlerDispatcher {
    private static final Map<Class<? extends Annotation>, BaseAnnotationHandler> KNOWN_HANDLERS = new HashMap<>();

    static {
        /** *********************  method annotation  ************************/
        KNOWN_HANDLERS.put(GET.class, new GetAnnotationHandler());
        KNOWN_HANDLERS.put(POST.class, new PostAnnotationHandler());
        KNOWN_HANDLERS.put(PUT.class, new PutAnnotationHandler());
        KNOWN_HANDLERS.put(DELETE.class, new DeleteAnnotationHandler());

        KNOWN_HANDLERS.put(Headers.class, new HeadersAnnotationHandler());

        /** *********************  parameter annotation  ************************/
        KNOWN_HANDLERS.put(Path.class, new PathAnnotationHandler());
        KNOWN_HANDLERS.put(HeaderMap.class, new HeaderMapAnnotationHandler());
        KNOWN_HANDLERS.put(Header.class, new HeaderAnnotationHandler());
        KNOWN_HANDLERS.put(Url.class, new UrlAnnotationHandler());
        KNOWN_HANDLERS.put(Body.class, new BodyAnnotationHandler());
        KNOWN_HANDLERS.put(Query.class, new QueryAnnotationHandler());
        KNOWN_HANDLERS.put(QueryMap.class, new QueryMapAnnotationHandler());
    }

    @SneakyThrows
    public static Object dispatchAndHandle(Annotation annotation, CtClass ctClass, CtMethod ctMethod) {
        try {
            BaseAnnotationHandler handler = KNOWN_HANDLERS.get(annotation.annotationType());
            if (Objects.isNull(handler)) {
                return otherAnnotations(annotation, ctClass, ctMethod);
            }
            return handler.handle(annotation, ctClass, ctMethod);
        } catch (Exception e) {
            throw new ConnectorAnnotationException(
                String.format("Error handle annotation for @%s_%s",
                    annotation.annotationType().getSimpleName(), Utils.classMethodSignature(ctMethod)),
                e
            );
        }
    }

    /**
     * 非 HTTP 相关直接返回原注解
     * @param annotation
     * @param ctClass
     * @param ctMethod
     * @return
     */
    private static Object otherAnnotations(Annotation annotation, CtClass ctClass, CtMethod ctMethod) {
        ConstPool constPool = ctClass.getClassFile().getConstPool();
        AnnotationsAttribute methodAttribute;
        AttributeInfo attribute = ctMethod.getMethodInfo().getAttribute(AnnotationsAttribute.visibleTag);
        if (Objects.nonNull(attribute)) {
            methodAttribute = (AnnotationsAttribute) attribute;
        } else {
            methodAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        }
        javassist.bytecode.annotation.Annotation ctAnnotation = new javassist.bytecode.annotation.Annotation(annotation.annotationType().getTypeName(), constPool);
        methodAttribute.addAnnotation(ctAnnotation);
        ctMethod.getMethodInfo().addAttribute(methodAttribute);
        return true;
    }
}
