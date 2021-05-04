package com.tuya.connector.api.handler;

import com.tuya.connector.api.annotations.QueryMap;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.BooleanMemberValue;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/20 9:36 下午
 */
public class QueryMapAnnotationHandler extends BaseAnnotationHandler<QueryMap> {

    @Override
    public Object handle(QueryMap srcQueryMap, CtClass ctClass, CtMethod ctMethod) {
        ConstPool constPool = ctClass.getClassFile().getConstPool();
        Annotation queryMap = new Annotation(retrofit2.http.QueryMap.class.getTypeName(), constPool);
        queryMap.addMemberValue("encoded", new BooleanMemberValue(srcQueryMap.encoded(), constPool));

        return queryMap;
    }
}
