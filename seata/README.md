# 示例说明

用户购买商品的业务逻辑。整个业务逻辑由3个微服务提供支持：

- 仓储服务：对给定的商品扣除仓储数量。

- 订单服务：根据采购需求创建订单。

- 帐户服务：从用户帐户中扣除余额。

![架构图](http://seata.io/img/architecture.png)

![SEATA 的分布式交易解决方案](http://seata.io/img/solution.png)

---

## 环境搭建

本示例环境搭建使用Docker搭建

[docker-compose.yml](../docker/docker-compose.yml)

```yaml
version: "3.9"
services:
  mysql:
    image: mysql
    ports:
      - 3306:3306
    environment:
      MYSQL_ROOT_PASSWORD: root
  nacos:
    image: nacos/nacos-server
    environment:
      - MODE=standalone
      - SPRING_DATASOURCE_PLATFORM=mysql
      - MYSQL_SERVICE_HOST=192.168.0.100
      - MYSQL_SERVICE_PORT=3306
      - MYSQL_SERVICE_DB_NAME=nacos
      - MYSQL_SERVICE_USER=root
      - MYSQL_SERVICE_PASSWORD=root
    ports:
      - 8848:8848
  seata:
    image: seataio/seata-server
    hostname: seata-server
    ports:
      - 8091:8091
    environment:
      - SEATA_IP=192.168.0.102
      - SEATA_PORT=8091
      - STORE_MODE=db
      - SEATA_CONFIG_NAME=file:/root/seata-config/registry
    volumes:
      - /seata/conf:/root/seata-config
```

### Mysql

- Version: 8.0.25

1. 初始化数据库，设置root账户密码`MYSQL_ROOT_PASSWORD`

2. 数据库初始化
    - 自动初始化本示例所需数据库，执行据库整合脚本文件[demo-all-in-one.sql](../../docker/seata/db/demo-all-in-one.sql)(
      包括了Nacos和Seata所需的数据库，后续无需任何操作)

[account.sql](../docker/seata/db/account.sql)

```sql
DROP DATABASE IF EXISTS db_account;
CREATE DATABASE db_account;
USE db_account;

CREATE TABLE `account_tbl`
(
    `id`      INT(11) NOT NULL AUTO_INCREMENT,
    `user_id` VARCHAR(255) DEFAULT NULL,
    `money`   INT(11)      DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO account_tbl (id, user_id, money)
VALUES (1, '1001', 10000);
INSERT INTO account_tbl (id, user_id, money)
VALUES (2, '1002', 10000);
```

[order.sql](../docker/seata/db/order.sql)

```sql
DROP DATABASE IF EXISTS db_order;
CREATE DATABASE db_order;
USE db_order;

CREATE TABLE `order_tbl`
(
    `id`             INT(11) NOT NULL AUTO_INCREMENT,
    `user_id`        VARCHAR(255) DEFAULT NULL,
    `commodity_code` VARCHAR(255) DEFAULT NULL,
    `count`          INT(11)      DEFAULT '0',
    `money`          INT(11)      DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
```

[storage.sql](../docker/seata/db/seata.sql)

```sql
DROP DATABASE IF EXISTS db_storage;
CREATE DATABASE db_storage;
USE db_storage;

CREATE TABLE `storage_tbl`
(
    `id`             INT(11) NOT NULL AUTO_INCREMENT,
    `commodity_code` VARCHAR(255) DEFAULT NULL,
    `count`          INT(11)      DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `commodity_code` (`commodity_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


INSERT INTO storage_tbl (id, commodity_code, count)
VALUES (1, '2001', 1000);
```

### Nacos

- Version: 2.0.2

1. 建立数据库,执行[nacos.sql](../docker/nacos/db/nacos.sql)数据库脚本文件。
2. 修改Docker容器启动参数:
    - `MYSQL_SERVICE_HOST`:数据库地址
    - `MYSQL_SERVICE_PORT`:端口
    - `MYSQL_SERVICE_USER`:用户名
    - `MYSQL_SERVICE_PASSWORD`:密码
3. 启动Nacos注册中心。

### Seata

- Version: 1.4.0

1. 建立数据库，执行[seata.sql](../docker/seata/db/seata.sql)数据库脚本。

2. 修改[registry.yml](../docker/seata/conf/registry.yml)
    - `registry.type`: 注册中心类型
    - `serverAddr`: 注册中心地址
    - `config.type`: 配置中心类型
    - `config.file.name`: 配置文件名

```yaml
registry:
  type: nacos
  nacos:
    application: seata-server
    serverAddr: 192.168.0.102:8848
    group: SEATA_GROUP
    namespace:
    cluster: default
    username:
    password:
config:
  type: file
  file:
    name: file:/root/seata-config/file.yml
```

3. 修改[file.yml](../docker/seata/conf/file.yml)
    - `datasource`:数据源类型
    - `dbType`:数据库类型
    - `driverClassName`:数据库驱动
    - `url`: 数据库地址
    - `user`: 用户名
    - `password`: 密码

```yaml
store:
  mode: db
  db:
    datasource: druid
    dbType: mysql
    driverClassName: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://192.168.0.100:3306/seata?useUnicode=true&rewriteBatchedStatements=true
    user: root
    password: root
```

4. 为Account,Order,Storage三个数据库数据创建`undo_log`表

```sql
CREATE TABLE IF NOT EXISTS `undo_log`

(
    `branch_id`     BIGINT       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11)      NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6)  NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6)  NOT NULL COMMENT 'modify datetime',
    UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table';
```

5. 修改Docker容器启动参数:
    - `SEATA_IP`: 指定服务在Nacos注册中心的IP
6. 启动Seata-Server服务.

## 微服务

```xml

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.seata</groupId>
            <artifactId>seata-spring-boot-starter</artifactId>
            <version>1.4.2</version>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.2.0</version>
        </dependency>
    </dependencies>
</dependencyManagement>
```

```xml

<dependencies>
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-actuator</artifactId>
    </dependency>
</dependencies>
```

每个微服务`application.yml`添加如下配置

- `server-addr`: 注册中心地址
- `tx-service-group`: 事务组
- `vgroup-mapping.xxx-group`: xxx-group必须与`tx-service-group`的值一致

```yaml
seata:
  registry:
    type: nacos
    nacos:
      server-addr: 192.168.0.102:8848
  tx-service-group: demo-group
  service:
    vgroup-mapping:
      demo-group: default
```

### Order

```xml

<dependencies>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
    </dependency>
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

1. 实体类

```java

@Data
public class Order {
    private Integer id;

    private String userId;

    private String commodityCode;

    private Integer count;

    private BigDecimal money;
}
```

2. 业务逻辑:

添加注解`@Transactional`

```java

@Service
public class OrderService {
    @Resource
    private OrderMapper orderMapper;

    @Resource
    private AccountClient accountClient;

    @Transactional(rollbackFor = Exception.class)
    public void create(String userId, String commodityCode, Integer count) {
        System.out.println("order XID " + RootContext.getXID());
        BigDecimal money = new BigDecimal(count).multiply(new BigDecimal(10));
        Order order = new Order();
        order.setUserId(userId);
        order.setCommodityCode(commodityCode);
        order.setCount(count);
        order.setMoney(money);
        orderMapper.insert(order);
        accountClient.debit(userId, money);
    }
}
```

3. 启动类添加如下注解:
    - `@SpringBootApplication`
    - `@EnableDiscoveryClient`
    - `@EnableFeignClients`
    - `@MapperScan`
    - `@EnableAutoDataSourceProxy`

### Storage

```xml

<dependencies>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```

1. 实体类

```java

@Data
public class Storage {
    private Integer id;

    private String commodityCode;

    private Integer count;
}
```

2. 业务逻辑:

```java

@Service
public class StorageService {
    @Resource
    private StorageMapper storageMapper;

    @Transactional(rollbackFor = Exception.class)
    public void deduct(String commodityCode, int count) {
        System.out.println("storage XID " + RootContext.getXID());
        Storage storage = storageMapper.findByCommodityCode(commodityCode);
        storage.setCount(storage.getCount() - count);
        storageMapper.updateById(storage);
    }
}
```

3. 启动类添加如下注解:
    - `@SpringBootApplication`
    - `@EnableDiscoveryClient`
    - `@MapperScan`
    - `@EnableAutoDataSourceProxy`

### Account

```xml

<dependencies>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```

1. 实体类

```java

@Data
public class Account {
    private Integer id;
    private String userId;
    private BigDecimal money;
}
   ```

2. 业务逻辑

```java

@Service
public class AccountService {
    @Resource
    private AccountMapper accountMapper;

    @Transactional(rollbackFor = Exception.class)
    public void debit(String userId, BigDecimal money) {
        System.out.println("account XID " + RootContext.getXID());
        Account account = accountMapper.selectByUserId(userId);
        account.setMoney(account.getMoney().subtract(money));
        accountMapper.updateById(account);
    }
}
```

3. 启动类添加如下注解:
    - `@SpringBootApplication`
    - `@EnableDiscoveryClient`
    - `@MapperScan`
    - `@EnableAutoDataSourceProxy`

### Business

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
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

1. 整合以上微服务，添加注解`@GlobalTransactional`

```java

@Service
@Slf4j
public class BusinessService {
    @Resource
    private OrderClient orderClient;

    @Resource
    private StorageClient storageClient;

    private final static String USERID = "1002";

    @GlobalTransactional(name = "business", rollbackFor = Exception.class)
    public void purchase(String userId, String commodityCode, int count) {
        System.out.println("business XID " + RootContext.getXID());
        orderClient.create(userId, commodityCode, count);
        storageClient.deduct(commodityCode, count);
        log.info("断点");
        if (userId.equals(USERID)) {
            throw new RuntimeException("Business Exception");
        }
    }
}
```

2. 启动类添加如下注解:
    - `@SpringBootApplication`
    - `@EnableDiscoveryClient`
    - `@EnableFeignClients`

3. 打上断点，Debug调试以上微服务。

4. 查看数据库中各数据库表的数据。