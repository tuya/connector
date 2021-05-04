package com.tuya.connector.api.handler;

import com.tuya.connector.api.annotations.Path;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/20 7:26 下午
 */
public class PathAnnotationHandler extends BaseAnnotationHandler<Path> {
    @Override
    public Object handle(Path srcPath, CtClass ctClass, CtMethod ctMethod) {
        ConstPool constPool = ctClass.getClassFile().getConstPool();
        Annotation path = new Annotation(retrofit2.http.Path.class.getTypeName(), constPool);
        path.addMemberValue("value", new StringMemberValue(srcPath.value(), constPool));

        return path;
    }
}
