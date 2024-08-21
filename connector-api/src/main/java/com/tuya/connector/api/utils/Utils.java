package com.tuya.connector.api.utils;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/20 3:41 下午
 */
@Slf4j
public class Utils {

    public static final ClassPool CLASS_POOL =new ClassPool(true);

    private static final Set<String> IMPORTED_PACKAGE = new HashSet<>();

    public static void importPackages(Collection<String> pkgs) {
        if (pkgs == null || pkgs.size() <= 0) {
            return ;
        }
        for (String pkg : pkgs) {
            synchronized (IMPORTED_PACKAGE) {
                if (IMPORTED_PACKAGE.add(pkg)) {
                    CLASS_POOL.importPackage(pkg);
                }
            }
        }
    }

    @SneakyThrows
    public static <M> String classMethodSignature(M method) {
        String methodSignature = null, className = null;
        if (method instanceof Method) {
            Method m = (Method) method;
            methodSignature = String.format("%s(%s)", m.getName(),
                Arrays.stream(m.getParameterTypes()).map(Class::getName).collect(Collectors.joining(", ")));
            className = m.getDeclaringClass().getName();
        } else if (method instanceof CtMethod) {
            CtMethod ctM = (CtMethod) method;
            methodSignature = String.format("%s(%s)", ctM.getName(),
                Arrays.stream(ctM.getParameterTypes()).map(CtClass::getName).collect(Collectors.joining(", ")));
            className = ctM.getDeclaringClass().getName();
        } else {
            log.warn(String.format("Method signature only for Method or CtMethod type, current type : %s", method.getClass().getName()));
        }

        return String.format("%s.%s", className, methodSignature);
    }

    @SneakyThrows
    public static <M> String classMethodSignature(M method, Object[] args) {
        String methodSignature = null, className = null;
        if (method instanceof Method) {
            Method m = (Method) method;
            methodSignature = String.format("%s(%s), args: (%s)", m.getName(),
                Arrays.stream(m.getParameterTypes()).map(Class::getName).collect(Collectors.joining(", ")),
                Objects.isNull(args) ? "null" : Arrays.stream(args).map(arg -> Objects.nonNull(arg) ? arg.toString() : "null").collect(Collectors.joining(", ")));
            className = m.getDeclaringClass().getName();
        } else if (method instanceof CtMethod) {
            CtMethod ctM = (CtMethod) method;
            methodSignature = String.format("%s(%s)", ctM.getName(),
                Arrays.stream(ctM.getParameterTypes()).map(CtClass::getName).collect(Collectors.joining(", ")));
            className = ctM.getDeclaringClass().getName();
        } else {
            log.warn(String.format("Method signature only for Method or CtMethod type, current type : %s", method.getClass().getName()));
        }

        return String.format("%s.%s", className, methodSignature);
    }

    public static String className4Signature(Class<?> clz) {
        Objects.requireNonNull(clz, "Class: clz required not null");
        String name = clz.getName();
        return "L" + name.replaceAll("\\.", "/");
    }
}
