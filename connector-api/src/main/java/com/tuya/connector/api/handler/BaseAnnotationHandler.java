package com.tuya.connector.api.handler;

import com.tuya.connector.api.annotations.*;
import com.tuya.connector.api.exceptions.ConnectorAnnotationException;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.annotation.Annotation;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/20 7:18 下午
 */
public abstract class BaseAnnotationHandler<Anno extends Annotation> {

    /**
     * handle different connector annotation
     * @param anno connector annotation
     * @param ctClass proxy service class
     * @param ctMethod proxy service method
     * @return return true or another annotation
     */
    public abstract Object handle(Anno anno, CtClass ctClass, CtMethod ctMethod);

    public String mappedTypeName(Annotation annotation) {
        if (annotation instanceof Body) {
            return retrofit2.http.Body.class.getTypeName();
        } else if (annotation instanceof DELETE) {
            return retrofit2.http.DELETE.class.getTypeName();
        } else if (annotation instanceof GET) {
            return retrofit2.http.GET.class.getTypeName();
        } else if (annotation instanceof Header) {
            return retrofit2.http.Header.class.getTypeName();
        } else if (annotation instanceof HeaderMap) {
            return retrofit2.http.HeaderMap.class.getTypeName();
        } else if (annotation instanceof Headers) {
            return retrofit2.http.Headers.class.getTypeName();
        } else if (annotation instanceof Path) {
            return retrofit2.http.Path.class.getTypeName();
        } else if (annotation instanceof POST) {
            return retrofit2.http.POST.class.getTypeName();
        }  else if (annotation instanceof PUT) {
            return retrofit2.http.PUT.class.getTypeName();
        } else if (annotation instanceof Query) {
            return retrofit2.http.Query.class.getTypeName();
        }  else if (annotation instanceof QueryMap) {
            return retrofit2.http.QueryMap.class.getTypeName();
        } else if (annotation instanceof Url) {
            return retrofit2.http.Url.class.getTypeName();
        }

        throw new ConnectorAnnotationException(String.format("Unknown annotation: %s", annotation.getClass().getName()));
    }
}
