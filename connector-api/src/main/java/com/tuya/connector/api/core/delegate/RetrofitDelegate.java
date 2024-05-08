package com.tuya.connector.api.core.delegate;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.tuya.connector.api.config.*;
import com.tuya.connector.api.core.convert.FastJsonConverterFactory;
import com.tuya.connector.api.error.ErrorContext;
import com.tuya.connector.api.error.ErrorInfo;
import com.tuya.connector.api.exceptions.ConnectorDelegateException;
import com.tuya.connector.api.exceptions.ConnectorResultException;
import com.tuya.connector.api.exceptions.ExceptionFactory;
import com.tuya.connector.api.handler.ApiAnnotationHandlerDispatcher;
import com.tuya.connector.api.header.DefaultHeaderInterceptor;
import com.tuya.connector.api.header.HeaderProcessor;
import com.tuya.connector.api.logging.HttpLoggingFactory;
import com.tuya.connector.api.model.Result;
import com.tuya.connector.api.utils.Utils;
import javassist.*;
import javassist.bytecode.ConstPool;
import javassist.bytecode.ParameterAnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/21 10:22 上午
 */
@Slf4j
public class RetrofitDelegate implements ProxyDelegate {

    private static final String CALL_SIG_PREFIX = Utils.className4Signature(Call.class);
    private static final String RESULT_SIG_PREFIX = Utils.className4Signature(Result.class);
    private static final String CALL_CLASS = Call.class.getName();

    private final Configuration configuration;
    private final Class<?> connector;

    private volatile Class<?> derivedConnector;
    private volatile Object service;

    private static volatile Retrofit retrofitClient;

    /**
     * store connector classes which has created retrofit service class
     */
    private static Map<Class, Class> retrofitConnectors = new ConcurrentHashMap<>();

    public RetrofitDelegate(@NonNull Configuration configuration, @NonNull Class<?> connector) {
        this.configuration = configuration;
        this.connector = connector;
        Class savedDerivedConnector = retrofitConnectors.get(connector);
        if (savedDerivedConnector == null) {
            synchronized (connector) {
                savedDerivedConnector = retrofitConnectors.get(connector);
                if (savedDerivedConnector == null) {
                    savedDerivedConnector = deriveFrom(connector);
                    retrofitConnectors.put(connector, savedDerivedConnector);
                }
            }
        }
        derivedConnector = savedDerivedConnector;
        Retrofit retrofit = getGlobalRetrofit();
        service = retrofit.create(derivedConnector);

    }

    @SuppressWarnings({"rawtypes", "unchecked", "ConstantConditions"})
    @SneakyThrows
    @Override
    public Object execute(Method method, Object[] args) {
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Method delegatedMethod = service.getClass().getDeclaredMethod(methodName, parameterTypes);
        if (Objects.isNull(delegatedMethod)) {
            throw new ConnectorDelegateException(String.format("Delegated method is null for %s", Utils.classMethodSignature(method)));
        }

        Class<?> returnType = delegatedMethod.getReturnType();
        Object invokeObject = null;
        try {
            invokeObject = delegatedMethod.invoke(service, args);
        } catch (InvocationTargetException invocationTargetException) {
            throw invocationTargetException.getCause();
        }
        if (returnType == Call.class) {
            Call<Result> call = (Call<Result>) invokeObject;
            Response<Result> response = call.execute();
            if (!response.isSuccessful()) {
                int responseCode = response.code();
                log.error("Response(code={}) is not successful for requeset: {}", responseCode, Utils.classMethodSignature(method, args));
                throw new ConnectorResultException(String.format("Response(code=%s) is not successful for requeset: %s", responseCode, Utils.classMethodSignature(method, args)));
            }
            Result body = response.body();
            Integer retCode = body.getCode();
            String retMsg = body.getMsg();
            if (Objects.nonNull(retCode)) {
                log.warn("Result(code={},msg={},t={}) is not successful for requeset: {}", retCode, retMsg, body.getT(), Utils.classMethodSignature(method, args));
                ErrorContext.set(new ErrorInfo(retCode + "", retMsg));
                throw ExceptionFactory.ofCode(retCode, retMsg, body.getT());
            }
            Class<?> srcReturnType = method.getReturnType();
            if (srcReturnType == Result.class) {
                return body;
            } else {
                return body.getResult();
            }
        } else {
            throw new ConnectorDelegateException("Derived connector method's return type currently just support retrofit2.Call");
        }
    }

    /**
     * 根据Connector接口派生出的Connector接口，用于Retrofit创建代理service时的接口
     * 约定增加前缀: $
     *
     * @param connectorInterface
     * @return
     */
    @SneakyThrows
    private Class<?> deriveFrom(Class<?> connectorInterface) {
        ClassPool pool = Utils.CLASS_POOL;

        String connectorName = connectorInterface.getName();
        ClassClassPath classPath = new ClassClassPath(connectorInterface);
        pool.insertClassPath(classPath);
        CtClass connector = pool.getCtClass(connectorName);

        Collection<String> refClasses = connector.getRefClasses();
        log.debug("Classes referenced by connector({}) : {}", connectorName, refClasses);
        Utils.importPackages(refClasses);

        String packageName = connector.getPackageName();
        String delegatedConnectorName = "$" + connectorInterface.getSimpleName();
        String serviceName = Objects.nonNull(packageName) ? packageName + "." + delegatedConnectorName : delegatedConnectorName;
        if (Objects.isNull(pool.getOrNull(serviceName))) {
            pool.makeInterface(serviceName);
        }
        CtClass service = pool.getCtClass(serviceName);
        if (service.isFrozen()) {
            log.warn("delegate class[{}] is frozen, defrost it", serviceName);
            service.defrost();
        }
        ConstPool serviceConstPool = service.getClassFile().getConstPool();
        CtClass call = pool.getCtClass(CALL_CLASS);

        CtMethod[] methods = connector.getDeclaredMethods();
        log.debug("Methods declared by connector({}) : {}", connectorName,
                Arrays.stream(methods).map(CtMethod::getName).collect(Collectors.joining(",")));

        for (CtMethod connectorMethod : methods) {
            CtMethod serviceMethod = CtNewMethod.abstractMethod(call, connectorMethod.getName(), connectorMethod.getParameterTypes(),
                    connectorMethod.getExceptionTypes(), service);

            // 方法返回值和参数泛型
            String serviceMethodGenericSignature = buildMethodGenericSignature(connectorMethod);
            serviceMethod.setGenericSignature(serviceMethodGenericSignature);

            // 方法注解
            Object[] annotations = connectorMethod.getAnnotations();
            for (Object annotation : annotations) {
                ApiAnnotationHandlerDispatcher.dispatchAndHandle((java.lang.annotation.Annotation) annotation, service, serviceMethod);
            }

            // 参数注解
            Object[][] parameterAnnotationsArray = connectorMethod.getParameterAnnotations();
            int paramAnnotationsLength = parameterAnnotationsArray.length;
            if (paramAnnotationsLength > 0) {
                Annotation[][] paramAnnotations = new Annotation[paramAnnotationsLength][parameterAnnotationsArray[0].length];
                for (int i = 0; i < paramAnnotationsLength; i++) {
                    for (int j = 0; j < parameterAnnotationsArray[i].length; j++) {
                        paramAnnotations[i][j] = (Annotation) ApiAnnotationHandlerDispatcher.dispatchAndHandle(
                                (java.lang.annotation.Annotation) parameterAnnotationsArray[i][j], service, serviceMethod
                        );
                    }
                }
                ParameterAnnotationsAttribute paramAttr = new ParameterAnnotationsAttribute(serviceConstPool, ParameterAnnotationsAttribute.visibleTag);
                paramAttr.setAnnotations(paramAnnotations);
                serviceMethod.getMethodInfo().addAttribute(paramAttr);
            }

            service.addMethod(serviceMethod);
        }
        return service.toClass(configuration.getClass().getClassLoader(), null);
    }

    /**
     * T: (Ljava/lang/String;Ljava/util/Map<Ljava/lang/String;Ljava/lang/Object;>;)Lmodel/Device;
     * Result: (Ljava/lang/String;Ljava/util/Map<Ljava/lang/String;Ljava/lang/Object;>;)Lmodel/Result;
     * Result<T>: (Ljava/lang/String;Ljava/util/Map<Ljava/lang/String;Ljava/lang/Object;>;)Lmodel/Result<Lmodel/Device;>;
     * <p>
     * Call: (Ljava/lang/String;Ljava/util/Map<Ljava/lang/String;Ljava/lang/Object;>;)Lretrofit2/Call;
     * Call<Result>: (Ljava/lang/String;Ljava/util/Map<Ljava/lang/String;Ljava/lang/Object;>;)Lretrofit2/Call<Lmodel/Result;>;
     * Call<Result<T>>>: (Ljava/lang/String;Ljava/util/Map<Ljava/lang/String;Ljava/lang/Object;>;)Lretrofit2/Call<Lmodel/Result<Lmodel/Device;>;>;
     * <p>
     * void: (Ljava/lang/String;Ljava/util/Map<Ljava/lang/String;Ljava/lang/Object;>;)V
     * Void: (Ljava/lang/String;Ljava/util/Map<Ljava/lang/String;Ljava/lang/Object;>;)Ljava/lang/Void;
     * int: (Ljava/lang/String;Ljava/util/Map<Ljava/lang/String;Ljava/lang/Object;>;)I
     * <p>
     * TODO 算法解析、直接返回Call的方法
     *
     * @param srcCtMethod
     * @return
     */
    @SneakyThrows
    private static String buildMethodGenericSignature(CtMethod srcCtMethod) {
        String srcSginature = srcCtMethod.getGenericSignature();
        if (Objects.isNull(srcSginature)) {
            srcSginature = srcCtMethod.getSignature();
        }
        String destGenericSignature;
        String[] sigs = srcSginature.split("\\)");
        String returnSig = sigs[1];
        if (returnSig.length() == 1) {
            // return primitive
            destGenericSignature = sigs[0] + ")" + CALL_SIG_PREFIX + "<" + RESULT_SIG_PREFIX + ";>;";
        } else {
            if (returnSig.startsWith(CALL_SIG_PREFIX)) {
                throw new ConnectorDelegateException(
                        String.format("Connector method's return type currently not support retrofit2.Call directly for %s",
                                Utils.classMethodSignature(srcCtMethod))
                );
                // if (returnSig.equals(CALL_SIG_PREFIX + ";")) {
                //     // return Call
                //     return sigs[0] + ")" + "Lretrofit2/Call<Lmodel/Result;>;";
                // } else {
                //     // return Call<T>
                //     return srcSginature;
                // }
            } else {
                if (returnSig.startsWith(RESULT_SIG_PREFIX)) {
                    // return Result or Result<T>
                    destGenericSignature = sigs[0] + ")" + CALL_SIG_PREFIX + "<" + returnSig + ">;";
                } else {
                    // return T
                    destGenericSignature = sigs[0] + ")" + CALL_SIG_PREFIX + "<" + RESULT_SIG_PREFIX + "<" + returnSig + ">;>;";
                }
            }
        }
        log.debug("Method signature identify from {} to {}", srcSginature, destGenericSignature);
        return destGenericSignature;
    }


    private Retrofit getGlobalRetrofit() {
        // TODO get client from target dataSource
        if (Objects.isNull(retrofitClient)) {
            synchronized (this) {
                if (Objects.isNull(retrofitClient)) {
                    ApiDataSource apiDataSource = configuration.getApiDataSource();
                    OkHttpClient.Builder okHttpBuilder;
                    OkHttpClient specificClient = apiDataSource.getSpecificClient();
                    if (Objects.nonNull(specificClient)) {
                        log.info("Use specific specific client: {}", specificClient);
                        okHttpBuilder = specificClient.newBuilder();
                    } else {
                        okHttpBuilder = new OkHttpClient.Builder();
                        ConnectionPool connectionPool = apiDataSource.getConnectionPool();
                        Timeout timeout = apiDataSource.getTimeout();
                        boolean retryOnConnectionFailure = configuration.isRetryOnConnectionFailure();
                        Logging.Level loggingLevel = apiDataSource.getLoggingLevel();
                        Logging.Strategy loggingStrategy = apiDataSource.getLoggingStrategy();
                        okHttpBuilder.connectionPool(new okhttp3.ConnectionPool(connectionPool.getMaxIdleConnections(),
                                connectionPool.getKeepAliveSecond(), TimeUnit.SECONDS))
                            .callTimeout(timeout.getCallTimeout(), TimeUnit.SECONDS)
                            .connectTimeout(timeout.getConnectTimeout(), TimeUnit.SECONDS)
                            .readTimeout(timeout.getReadTimeout(), TimeUnit.SECONDS)
                            .writeTimeout(timeout.getWriteTimeout(), TimeUnit.SECONDS)
                            .retryOnConnectionFailure(retryOnConnectionFailure)
                            .addInterceptor(HttpLoggingFactory.createHttpLoggingInterceptor(loggingLevel, loggingStrategy));
                    }
                    boolean autoSetHeader = configuration.getApiDataSource().isAutoSetHeader();
                    HeaderProcessor headerProcessor = apiDataSource.getHeaderProcessor();
                    boolean validateEagerly = configuration.isValidateEagerly();
                    if (autoSetHeader) {
                        Objects.requireNonNull(headerProcessor, "HeaderProcessor must not be null when autoSetHeader is enabled");
                        okHttpBuilder.addInterceptor(new DefaultHeaderInterceptor(headerProcessor, apiDataSource.getContextManager()));
                    }

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

                    retrofitClient = new Retrofit.Builder()
                            .baseUrl(apiDataSource.getBaseUrl())
                            .validateEagerly(validateEagerly)
                            .client(okHttpBuilder.build())
                            .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                            .build();
                }
            }
        }
        return retrofitClient;
    }

}
