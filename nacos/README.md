# 示例说明

导入Web依赖

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>
</dependencies>
```

Nacos注册中心依赖

```xml

<dependencies>
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>
</dependencies>
```

Nacos配置中心依赖

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-bootstrap</artifactId>
    </dependency>
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
    </dependency>
</dependencies>
```

## 注册中心

### Provider

1. application.yml
    - `spring.cloud.nacos.discovery.server-addr` :Nacos注册中心地址

```yaml
spring:
  application:
    name: nacos-discovery-provider
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.0.104:18848
management:
  endpoints:
    web:
      exposure:
        include: '*'
```

2. 启动类添加注解`@EnableDiscoveryClient`

Application.class

```java

@SpringBootApplication
@EnableDiscoveryClient
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### Consumer

#### RestTemplate

1. 添加BeanConfig，RestTemplate通过服务IP调用

```java

@Configuration
public class BeanConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

2. 启动类添加注解`@EnableDiscoveryClient`

Application.class

```java

@SpringBootApplication
@EnableDiscoveryClient
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

#### Loadbalancer

1. 导入依赖

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-loadbalancer</artifactId>
    </dependency>
    <dependency>
        <groupId>com.github.ben-manes.caffeine</groupId>
        <artifactId>caffeine</artifactId>
    </dependency>
</dependencies>
```

2. 添加BeanConfig,添加注解`@LoadBalanced`:负载均衡,通过服务名称调用。

BeanConfig.class

```java

@Configuration
public class BeanConfig {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

3. application.yml

```yaml
server:
  port: 8084
spring:
  application:
    name: nacos-discovery-consumer-resttemplate
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.0.104:18848
```

4. 启动类添加注解`@EnableDiscoveryClient`

Application.class

```java

@SpringBootApplication
@EnableDiscoveryClient
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

#### OpenFeign

1. 导入依赖

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-loadbalancer</artifactId>
    </dependency>
    <dependency>
        <groupId>com.github.ben-manes.caffeine</groupId>
        <artifactId>caffeine</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
</dependencies>
```

2. 创建调用接口,添加注解`@Component`,`@FeignClient(name = "服务名")`

```java

@Component
@FeignClient(name = "nacos-discovery-provider")
public interface ConsumerService {
    @GetMapping("/")
    String getIndex();

    @GetMapping("/provider")
    String getProvider();

    @GetMapping("/exception")
    String getException();
}
```

3. application.yml

```yaml
server:
  port: 8084
spring:
  application:
    name: nacos-discovery-consumer-openfeign
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.0.104:18848
```

4. 启动类添加注解`@EnableDiscoveryClient`,`@EnableFeignClients(basePackages = "接口所在包名")`

Application.class

```java

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.demo.nacos.consumer.service")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

## 配置中心

通过Nacos配置中心，动态加载和刷新配置信息。

1. 导入依赖

```xml

<dependencies>
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-bootstrap</artifactId>
    </dependency>
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
    </dependency>
</dependencies>
```

2. 创建bootstrap.yml，指定配置
   - `namespace`: 命名空间
   - `file-extension`: 配置类型
   - 配置名DataId默认为spring.application.name.file-extension

```yaml
server:
  port: 8084
spring:
  application:
    name: nacos-config
  cloud:
    nacos:
      config:
        server-addr: 192.168.0.102:8848
        namespace: public
        file-extension: yaml
      discovery:
        server-addr: 192.168.0.102:8848
```

3. 启动类添加注解`@EnableDiscoveryClient`

Application.class

```java

@SpringBootApplication
@EnableDiscoveryClient
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```