[![license: Apache 2](https://img.shields.io/badge/license-Apache%202-green)](https://github.com/tuya/tuya-connector/blob/master/LICENSE 'License')
![QQ群: ***](https://img.shields.io/badge/chat-QQ%E7%BE%A4%3A***-orange)
![Version: 1.0.0](https://img.shields.io/badge/version-1.0.0-blue)

`connector`框架通过简单的配置和灵活的扩展机制，将云端API映射成本地API，订阅云端消息分发为本地事件，使得开发者在云云对接（OpenAPI或者消息订阅）项目开发过程中，不需要花费过多精力关注云环境连接和请求处理过程，从而帮助开发者更好的聚焦在自身的业务逻辑上。

### 快速开始（涂鸦云云对接推荐 [tuya-spring-boot-starter](https://github.com/tuya/tuya-connector) ）

#### SpringBoot集成（推荐）

1. 配置API数据源
```
connector.api.base-url=https://www.xxx.com
```

2. 定义`Connector`接口，添加扫描路径，直接注入`Connector`进行调用
```java
@ConnectorScan(basePackages = "com.xxx.connector")
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

public interface Connector {
    @GET("/test/{s}")
    String test(@Path("s") String s);
}

@Service
public class Service {
    @Autowired
    private Connector connector;
    
    public String test(String s) {
    	return connector.test(s);	
    }
}
```

#### 普通Java项目配置
根据数据源配置创建`ConnectorFactory`，通过`ConnectorFactory`加载`Connector`得到`Connector`代理，通过`Connector`代理类进行API调用。
```java
public interface Connector {
    @GET("/test/{s}")
    String test(@Path("s") String s);
}

public class Demo {
	public static void main(String[] args) {
        // initialize configuration
        Configuration config = new Configuration();
        ApiDataSource dataSource = ApiDataSource.DEFAULT_BUILDER.build();
        dataSource.setBaseUrl("https://www.xxx.com");
        config.setApiDataSource(dataSource);

        // create ConnectorFactory
        ConnectorFactory connectorFactory = new DefaultConnectorFactory(config);

        // Load connector and create connector proxy
        connector = connectorFactory.loadConnector(Connector.class);

        // call API
        String result = connector.test("hello");
    }
}
```


### 主要特性
- 统一设置Header
- 自动获取和刷新token
- 返回值兼容驼峰和下划线格式
- 返回值兼容泛型模式（支持返回Result<T>，也支持直接返回T）
- 错误码处理
- 拦截器扩展
- 云端restful API导出为本地restful API
- 消息事件分发

### 设计思路
1. 云端Restful API映射成本地`Connector`接口，本地`Connector`接口通过HTTP相关注解进行声明，框架在运行时通过创建`Connector`接口的代理完成真实的Restful API的请求处理过程。
2. 借鉴了retrofit2项目，通过接口和注解方式访问云端API，目前底层直接委托给retrofit2实现请求处理
3. 灵活的扩展机制：
    1. 上下文管理器（ContextManager）
    2. Token管理器（TokenManager）
    3. Header处理器（HeaderProcessor）
    4. 错误码处理器（ErrorProcessor）
    5. 请求拦截器（ConnectorInterceptor）
4. 顺序订阅云端统一消息模型，解析消息数据，识别并构建具体消息类型事件进行本地事件分发处理

### 框架整体架构
![整体架构](src/main/resources/architect.jpg)
![集成&扩展](src/main/resources/integration&extension.jpg)

### 核心模块设计

#### API连接器模型
![连接器领域模型](src/main/resources/ddd.png)

- **Config**

框架透出的配置项和集成扩展点，比如URL连接、AK/SK、超时、连接池、日志、Token管理器、Header处理器、Context管理器

- **Core**

框架云云对接的实现模块，负责Connector的实现，即实际连接到云端RestfulAPI的请求响应处理逻辑

- **Annotations**

云云对接Restful风格API注解和解析，目前提供的注解有：GET、POST、PUT、DELETE、Header、HeaderMap、Headers、Body、Query、QueryMap、Path、Url

| **注解** | **描述** | **示例** |
| --- | --- | --- |
| GET | HTTP GET 请求 | @GET(**"/test/annotations/get"**)<br />Boolean get(); |
| POST | HTTP POST 请求 | @POST(**"/test/annotations/post"**)<br />Boolean post(); |
| PUT | HTTP PUT 请求 | @PUT(**"/test/annotations/put"**)<br />Boolean put(); |
| DELETE | HTTP DELETE 请求 | @DELETE(**"/test/annotations/delete"**)<br />Boolean delete(); |
| Path | 请求路径参数映射 | @GET(**"/test/annotations/path/{path_param}"**)<br />String path(@Path(**"path_param"**) String pathParam); |
| Query | 方法参数映射到请求url的query上 | @GET(**"/test/annotations/query"**)<br />String query(@Query(**"param"**) String param); |
| QueryMap | 方法参数映射到请求url的query上 | @GET(**"/test/annotations/queryMap"**)<br />String queryMap(@QueryMap  Map<String, Object> map);  |
| Header | 方法参数映射到请求头 | @GET(**"/test/annotations/header"**)<br />String header(@Header(**"headerKey"**) String header); |
| Headers | 注解参数映射到请求头 | @Headers(**"headerKey:headerValue"**)<br />@GET(**"/test/annotations/headers"**)<br />String headers(); |
| HeaderMap | 方法参数映射到请求头 | @GET(**"/test/annotations/headerMap"**)<br />String headerMap(@HeaderMap  Map<String, String> headerMap);  |
| Url | 方法参数映射成请求url | @GET <br />String url(@Url String url);  |
| Body | 方法参数映射到请求体 | @POST(**"/test/annotations/body"**)<br />String body(@Body Object body);  |

- Interceptor&Extension

框架扩展点，包括请求拦截器、错误码处理器。

#### Messaging连接器模型
![image.png](https://cdn.nlark.com/yuque/0/2021/png/130426/1617203258286-90a4e4df-e720-471d-bb27-d2dad8717954.png#align=left&display=inline&height=211&margin=%5Bobject%20Object%5D&name=image.png&originHeight=422&originWidth=746&size=18514&status=done&style=none&width=373)

- MessageDataSource
  消息数据源配置类，包含需要订阅的消息的地址、ak和sk。

- MessageDispatcher
  消息分发器，不同的消息服务通过实现此消息分发器监听云端消息进行本地事件分发。

- MessageEvent
  消息事件类型，每个消息都通过type唯一标识，通过继承维护各自的数据结构。

### 模块
- connector-api: 连接云端RestfulAPI
- connector-messaging: 订阅云端消息
- connector-spring: 与Spring集成
- connector-spring-boot: 与SpringBoot集成
- connector-assist: 辅助模块，提供单元测试环境

### 计划
- 请求参数兼容驼峰和下划线格式
- 提供Mock机制
- 多数据源
- 多语言
- 熔断/降级
- 缓存
- 异步
- 配合插件基于OpenAPI一键生成本地代码
- 其他开发语言实现