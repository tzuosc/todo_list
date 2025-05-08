# 项目介绍

本项目将实现一个具备完整功能的 TodoList 系统，主要功能模块包括：

✅ **用户认证模块**

- 用户注册与登录
- 登出功能
- 用户信息更新
- 头像上传 

✅ **任务管理模块**

- 任务的增删改查(CRUD)
- 任务分类管理
- 状态与截止时间管理

✅ **安全控制模块** (作者已经完成)

- JWT 鉴权机制
- 全局异常处理
- 接口权限控制（登录/未登录状态）

📦 **技术栈**

- 后端：Spring Boot + Maven + MySQL
- 可选前端：React, Vue
- 接口测试：Apifox

> [!NOTE]
> **作者已实现部分**
> - 基础项目框架搭建
> - 全局异常处理机制
> - DTO 数据传输对象设计
> - JWT 鉴权配置
> - 常用工具类封装
>
> **需要实现部分**
> - 数据持久层（Repository）可以使用 JPA 或者 MyBatis. 接口文件都在 `/src/repository` 中, MayBatis 的 `xml` 文件都在 `/resources/mapper`
> - 业务逻辑层（Service）
> - 接口控制层（Controller）

---

# 环境准备

## 1. Maven 换源配置

创建配置文件（路径：`C:\Users\<你的用户名>\.m2\settings.xml`）：

```xml

<settings>
    <mirrors>
        <mirror>
            <id>aliyunmaven</id>
            <name>阿里云公共仓库</name>
            <url>https://maven.aliyun.com/repository/public</url>
            <mirrorOf>central</mirrorOf>
        </mirror>
    </mirrors>
</settings>
```

执行依赖安装：

```bash
mvn clean install -T 1C
```

## 2. JDK 配置

![](./assets/image-20250422145405621.png)

![image-20250422145458973](./assets/image-20250422145458973.png)

![image-20250508110832882](./assets/image-20250508110832882.png)

要求：

- 必须使用 JDK 21
- 配置项目SDK为21版本

## 3. 数据库配置

1. 创建数据库：

```sql
CREATE
DATABASE TodoList;
USE
TodoList;
```

2. 修改配置文件（application.yml）：

![image-20250507202946145](./assets/image-20250507202946145.png)

```yaml
spring:
    datasource:
        url: jdbc:mysql://localhost:3306/TodoList
        username: root  # 修改为你的数据库账号
        password: root  # 修改为你的数据库密码
```

3. 测试数据库连接：
   ![](./assets/image-20250422150309907.png)![image-20250422150339347](./assets/image-20250422150339347.png)

------

# 接口测试指南

## Apifox 使用说明

1. 下载测试套件：
   ![](./assets/image-20250507195854443.png)

2. 导入测试配置：
   ![](./assets/PixPin_2025-05-07_20-05-17.gif)

3. 修改前置 url. 修改为 `http://127.0.0.1:8080`

   ![image-20250508113353411](./assets/image-20250508113353411.png)

4. 执行自动化测试：
   ![](./assets/image-20250507200810108.png)

✅ 成功测试示例：
![](./assets/image-20250507200901202.png)

------

# 开发提示

### 1. 使用 IDEA 的 TODO 功能定位待实现代码：

![](./assets/image-20250507205434219.png)

### 2. 启用被注释的方法（Ctrl+/）：
![](./assets/image-20250507205833967.png)

### 3. 数据库连通性检查：
![](./assets/image-20250507201238700.png)

### 4. 配置文件修改示例(修改成你的mysql的用户名和密码) ：
![]( ./assets/image-20250507202851737.png)



### 5. 通过 Swagger 测试注册接口：

```url
http://localhost:8080/swagger-ui/index.html
```

![](./assets/image-20250422153319116.png)

### 6. 前置知识

> 想要始本项目, 你至少需要了解下面的相关知识.

-  什么是 `JWT` , 如何使用. (登录, 注册有用)
-  什么是网络拦截器, 它是如何阻止没有登陆的用户访问后端接口的. 以及什么是 Cookie
-  什么是 Java 异常处理. (本项目采用全局异常处理的方式, 这符合最佳实践)
-  什么是 JPA (spring boot 的持久层)
-  什么是 DTO, 如何设计一个合理的 DTO.
-  什么是接口文档(Swagger)
-  什么是 RESTful API 规范, 如何设计一个合理的接口
- 什么是 枚举类, 纪录类
- java 文件读写 

### 7. 如何使用 Mybatis 替换 JPA(可选)

> 本项目默认采用 JPA 对数据库操作. 如果你一定要使用 Mybatis. 那么需要修改一些配置文件.   

在 `application.yml` 添加 

```yml
mybatis:
  mapper-locations: classpath:mapper/*.xml  # XML映射文件路径
  type-aliases-package: com.todo_list.model  # 实体类包路径
  configuration:
    map-underscore-to-camel-case: true      # 自动转换字段名（下划线→驼峰）
```

然后把 JPA 配置删除 (重要) ![image-20250508134534014](./assets/image-20250508134534014.png)

-----------



# 项目设计

## 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── todo_list/
│   │           ├── controller/    # REST API 接口层
│   │           ├── service/       # 业务逻辑层
│   │           ├── repository/    # 数据访问层(DAO)
│   │           ├── model/         # JPA 实体类
│   │           ├── dto/           # 数据传输对象
│   │           ├── config/        # 配置类
│   │           ├── exception/     # 异常处理
│   │           ├── security/      # 安全配置
│   │           ├── utils/         # 工具类
│   │           └── Application.java
│   ├── resources/
│   │   ├── application.yml       # YAML 配置文件
│   │   └── db.migration/         # Flyway 迁移脚本
└── test/                         # 测试代码
```

## 数据模型设计

### 👤 User 用户模型

| 字段         | 类型     | 说明        |
|------------|--------|-----------|
| username   | String | 用户名（唯一标识） |
| password   | String | 加密后的密码    |
| avatar_url | String | 头像存储地址    |

### 📝 Task 任务模型（多对一关系）

| 字段               | 类型      | 说明     |
|------------------|---------|--------|
| task_name        | String  | 任务名称   |
| task_description | String  | 任务描述   |
| deadline         | Long    | 截止时间戳  |
| status           | Boolean | 完成状态   |
| todo_list_id     | Long    | 所属清单ID |

### 📂 TodoList 任务清单（一对多关系）

| 字段       | 类型     | 说明     |
|----------|--------|--------|
| category | String | 清单分类   |
| user_id  | Long   | 所属用户ID |

------

## 接口规范

### 👤 用户相关接口

| 端点                  | 方法    | 参数                          | 说明             |
|---------------------|-------|-----------------------------|----------------|
| `/user/register`    | POST  | username, password          | 用户注册           |
| `/user/login`       | POST  | username, password          | 用户登录（返回Cookie） |
| `/user/logout`      | GET   | -                           | 用户登出           |
| `/user/{id}`        | PATCH | username/password/avatarUrl | 更新用户信息         |
| `/user/upload/{id}` | POST  | 图片文件                        | 上传用户头像         |

### 📂 任务清单接口

| 端点                           | 方法     | 参数       | 说明       |
|------------------------------|--------|----------|----------|
| `/list/{category}`           | PUT    | category | 创建新任务清单  |
| `/list/{id}`                 | DELETE | -        | 删除指定清单   |
| `/list/change_category/{id}` | PATCH  | category | 修改清单分类   |
| `/list/`                     | GET    | -        | 获取所有清单   |
| `/list/{id}`                 | GET    | -        | 获取指定清单详情 |

### 📝 任务管理接口

| 端点           | 方法     | 参数                           | 说明     |
|--------------|--------|------------------------------|--------|
| `/task/`     | POST   | category, name, status=false | 创建新任务  |
| `/task/{id}` | GET    | -                            | 获取任务详情 |
| `/task/{id}` | DELETE | -                            | 删除任务   |
| `/task/{id}` | PATCH  | 任意任务字段                       | 更新任务信息 |

------

## 异常处理规范

### 👤 用户相关异常

| 异常描述     | 错误码  | 触发场景             |
|----------|------|------------------|
| 非法用户名    | 1001 | 用户名不符合格式规范       |
| 用户名或密码错误 | 1002 | 登录验证失败           |
| 用户名已存在   | 1003 | 重复注册             |
| 未登录访问    | 1004 | 无有效Cookie访问受保护接口 |
| 用户不存在    | 1005 | 更新不存在的用户信息       |

### 📝 任务相关异常

| 异常描述   | 错误码  | 触发场景             |
|--------|------|------------------|
| 非法时间参数 | 2001 | 超过2038年的时间戳      |
| 任务不存在  | 2002 | 操作不存在任务          |
| 过去时间设置 | 2003 | 设置过去时间为截止时间      |
| 非法状态参数 | 2004 | 新建任务时status=true |

### 📂 清单相关异常

| 异常描述   | 错误码  | 触发场景      |
|--------|------|-----------|
| 重复清单分类 | 3001 | 创建重复分类的清单 |
| 清单不存在  | 3002 | 操作不存在清单   |

> [!WARNING] 注意
> 未处理的异常将返回500错误：
>
> ```json
>{
>"code": 500,
>"message": "系统繁忙，请稍后重试"
> }
> ```
