package com.tuya.connector.spring.export;


import com.tuya.connector.api.utils.Utils;
import com.tuya.connector.spring.export.handler.ExportAnnotationHandlerDispatcher;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.ParameterAnnotationsAttribute;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/7 11:41 上午
 */
public class ExportProxyFactory {

    private static final String RETURN_BODY = "{return this.connector.%s(%s);}";
    private static final String VOID_BODY = "{this.connector.%s(%s);}";

    @SneakyThrows
    public static Class<?> build(Class<?> connectorInterface) {
        ClassPool pool = Utils.CLASS_POOL;

        String connectorName = connectorInterface.getName();
        CtClass connector = pool.getCtClass(connectorName);

        Collection<String> refClasses = connector.getRefClasses();
        Collection<String> refCls = new HashSet<>(refClasses);
        refCls.add(connectorName);
        addSpringWebRef(refCls);
        Utils.importPackages(refCls);

        String packageName = connector.getPackageName();
        String controllerName = "$" + connectorInterface.getSimpleName() + "Controller";
        controllerName = Objects.nonNull(packageName) ? packageName + "." + controllerName : controllerName;
        if (Objects.isNull(pool.getOrNull(controllerName))) {
            pool.makeClass(controllerName);
        }

        CtClass controller = pool.getCtClass(controllerName);
        ConstPool constPool = controller.getClassFile().getConstPool();

        // add class annotation: @RestController
        javassist.bytecode.annotation.Annotation controllerAnno = new javassist.bytecode.annotation.Annotation(RestController.class.getTypeName(), constPool);
        AnnotationsAttribute classAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        classAttribute.setAnnotation(controllerAnno);
        controller.getClassFile().addAttribute(classAttribute);

        // add @Autowired connector
        CtField ctField = new CtField(connector, "connector", controller);
        AnnotationsAttribute connectorFieldAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        javassist.bytecode.annotation.Annotation autowiredAnno = new javassist.bytecode.annotation.Annotation(Autowired.class.getTypeName(), constPool);
        connectorFieldAttribute.setAnnotation(autowiredAnno);
        ctField.getFieldInfo().addAttribute(connectorFieldAttribute);
        controller.addField(ctField);

        // add @Autowired servlet
        CtClass servletClass = pool.getCtClass(HttpServletRequest.class.getName());
        CtField servletField = new CtField(servletClass, "servlet", controller);
        servletField.getFieldInfo().addAttribute(connectorFieldAttribute);
        controller.addField(servletField);

        CtMethod[] declaredMethods = connector.getDeclaredMethods();
        for (CtMethod connectorMethod : declaredMethods) {
            CtClass[] parameterTypes = connectorMethod.getParameterTypes();
            int paramLength = parameterTypes.length;
            String paramBody = "";
            if (paramLength > 0) {
                paramBody = IntStream.rangeClosed(1, paramLength).mapToObj(value -> "$" + value).collect(Collectors.joining(","));
            }
            String methodName = connectorMethod.getName();
            CtClass returnType = connectorMethod.getReturnType();
            boolean isVoid = Void.TYPE.getSimpleName().equalsIgnoreCase(returnType.getName());
            CtMethod controllerMethod = CtNewMethod.make(returnType, methodName,
                parameterTypes, connectorMethod.getExceptionTypes(), String.format(isVoid ? VOID_BODY : RETURN_BODY, methodName, paramBody), controller);

            // 方法
            Object[] annotations = connectorMethod.getAnnotations();
            for (Object annotation : annotations) {
                ExportAnnotationHandlerDispatcher.dispatchAndHandle((Annotation) annotation, controller, controllerMethod);
            }

            // 参数
            Object[][] parameterAnnotationsArray = connectorMethod.getParameterAnnotations();
            int paramAnnotationsLength = parameterAnnotationsArray.length;
            if (paramAnnotationsLength > 0) {
                javassist.bytecode.annotation.Annotation[][] paramAnnotations = new javassist.bytecode.annotation.Annotation[paramAnnotationsLength][parameterAnnotationsArray[0].length];
                for (int i = 0; i < paramAnnotationsLength; i++) {
                    for (int j = 0; j < parameterAnnotationsArray[i].length; j++) {
                        Object exportAnnotation = ExportAnnotationHandlerDispatcher.dispatchAndHandle((Annotation) parameterAnnotationsArray[i][j], controller, controllerMethod);
                        if (Objects.nonNull(exportAnnotation)) {
                            paramAnnotations[i][j] = (javassist.bytecode.annotation.Annotation) exportAnnotation;
                        }
                    }
                }
                ParameterAnnotationsAttribute paramAttr = new ParameterAnnotationsAttribute(constPool, ParameterAnnotationsAttribute.visibleTag);
                paramAttr.setAnnotations(paramAnnotations);
                controllerMethod.getMethodInfo().addAttribute(paramAttr);
            }

            controller.addMethod(controllerMethod);
        }

        controller.writeFile(".");
        return controller.toClass();
    }

    /**
     * 全量 import spring mvc 依赖的类
     * TODO 按需 import
     * @param refCls
     */
    private static void addSpringWebRef(Collection<String> refCls) {
        refCls.add(RestController.class.getTypeName());
        refCls.add(Autowired.class.getTypeName());
        refCls.add(GetMapping.class.getTypeName());
        refCls.add(PostMapping.class.getTypeName());
        refCls.add(DeleteMapping.class.getTypeName());
        refCls.add(PutMapping.class.getTypeName());
        refCls.add(PathVariable.class.getTypeName());
        refCls.add(RequestParam.class.getTypeName());
        refCls.add(RequestHeader.class.getTypeName());
        refCls.add(RequestBody.class.getTypeName());
        refCls.add(HttpServletRequest.class.getTypeName());
    }

}
