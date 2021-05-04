package com.tuya.connector.spring.export.handler;

import com.tuya.connector.api.annotations.Query;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;

import java.util.Objects;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/20 7:26 下午
 */
public class QueryAnnotationHandler extends BaseAnnotationHandler<Query> {
    @Override
    public Object handle(Query srcQuery, CtClass ctClass, CtMethod ctMethod) {
        String mappedTypeName = mappedTypeName(srcQuery);
        if (Objects.isNull(mappedTypeName)) {
            return null;
        }
        ConstPool constPool = ctClass.getClassFile().getConstPool();
        Annotation query = new Annotation(mappedTypeName, constPool);
        query.addMemberValue("value", new StringMemberValue(srcQuery.value(), constPool));

        return query;
    }
}
