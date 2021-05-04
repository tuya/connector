package com.tuya.connector.spring.export.handler;

import com.tuya.connector.api.annotations.Path;
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
public class PathAnnotationHandler extends BaseAnnotationHandler<Path> {
    @Override
    public Object handle(Path srcPath, CtClass ctClass, CtMethod ctMethod) {
        String mappedTypeName = mappedTypeName(srcPath);
        if (Objects.isNull(mappedTypeName)) {
            return null;
        }
        ConstPool constPool = ctClass.getClassFile().getConstPool();
        Annotation path = new Annotation(mappedTypeName, constPool);
        path.addMemberValue("value", new StringMemberValue(srcPath.value(), constPool));

        return path;
    }
}
