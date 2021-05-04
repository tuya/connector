package com.tuya.connector.api.handler;

import com.tuya.connector.api.annotations.Header;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/20 9:10 下午
 */
public class HeaderAnnotationHandler extends BaseAnnotationHandler<Header> {
    @Override
    public Object handle(Header srcHeader, CtClass ctClass, CtMethod ctMethod) {
        ConstPool constPool = ctClass.getClassFile().getConstPool();
        Annotation header = new Annotation(retrofit2.http.Header.class.getTypeName(), constPool);
        header.addMemberValue("value", new StringMemberValue(srcHeader.value(), constPool));

        return header;
    }
}
