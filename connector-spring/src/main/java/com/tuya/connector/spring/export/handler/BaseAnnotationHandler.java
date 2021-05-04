package com.tuya.connector.spring.export.handler;

import com.tuya.connector.api.annotations.*;
import com.tuya.connector.api.exceptions.ConnectorAnnotationException;
import javassist.CtClass;
import javassist.CtMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/20 7:18 下午
 */
@Slf4j
public abstract class BaseAnnotationHandler<Anno extends Annotation> {

    /**
     * handle different connector annotation
     * @param anno connector annotation
     * @param ctClass controller class
     * @param ctMethod controller method
     * @return return true or another annotation
     */
    public abstract Object handle(Anno anno, CtClass ctClass, CtMethod ctMethod);

    public String mappedTypeName(Annotation annotation) {
        if (annotation instanceof Body) {
            return RequestBody.class.getTypeName();
        } else if (annotation instanceof DELETE) {
            return DeleteMapping.class.getTypeName();
        } else if (annotation instanceof GET) {
            return GetMapping.class.getTypeName();
        } else if (annotation instanceof Header) {
            return RequestHeader.class.getTypeName();
        } else if (annotation instanceof HeaderMap) {
            // TODO
            log.warn("no mapping type for annotation: {}", annotation.annotationType().getName());
            return null;
        } else if (annotation instanceof Headers) {
            // TODO
            log.warn("no mapping type for annotation: {}", annotation.annotationType().getName());
            return null;
        } else if (annotation instanceof Path) {
            return PathVariable.class.getTypeName();
        } else if (annotation instanceof POST) {
            return PostMapping.class.getTypeName();
        }  else if (annotation instanceof PUT) {
            return PutMapping.class.getTypeName();
        } else if (annotation instanceof Query) {
            return RequestParam.class.getTypeName();
        }  else if (annotation instanceof QueryMap) {
            // TODO
            log.warn("no mapping type for annotation: {}", annotation.annotationType().getName());
            return null;
        } else if (annotation instanceof Url) {
            // TODO
            log.warn("no mapping type for annotation: {}", annotation.annotationType().getName());
            return null;
        }

        throw new ConnectorAnnotationException(String.format("Unknown export annotation: %s", annotation.getClass().getName()));
    }
}
