# 项目介绍

本项目将要完成一个 TodoList 系统, 包含以下几个部分

1. 登录和注册
2. 任务的增删改查
3. 各种边界异常的处理
4. 权限的控制(只有登录和没有登录两种状态, 没有其他权限)

我将会保留注册相关流程的代码用于示例, 其他的代码需要同学们自己完成,
本项目采用 **Apifox** 测试项目完成度. 项目代码没有标准答案. 能够通过测试的代码都是标准答案. 选择任意你喜欢的方式完成项目.  

技术栈: mysql + spring boot + Maven + React (可选)

> [!Note] 
>
> **作者做了什么?** 
>
> 我为你们搭好了项目的框架, 异常处理, DTO设计, 模型设计还有一些配置类和工具类. 避免了繁琐的配置, 当然, 如果你愿意的话, 当然可以从头开始
>
> **你们需要做什么?**
>
> 在现有的项目基础上(没有业务代码) 实现 repository(可以使用 JPA 或者 mybatis), service, controller.  

# 项目设计

## 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── todo_list/
│   │           ├── controller/          // 控制器层 (REST API)
│   │           ├── service/             // 服务层 (业务逻辑)
│   │           ├── repository/          // 数据访问层 (DAO)
│   │           ├── model/               // 实体类 (JPA)
│   │           ├── dto/                 // 数据传输对象
│   │           ├── config/              // 配置类
│   │           ├── exception/           // 异常处理相关
│   │           ├── security/            // 安全处理相关
│   │           ├── utils/               // 工具类
│   │           └── Application.java     // 主启动类
│   ├── resources/
│   │   ├── application.yml              // 应用配置文件 (YAML 格式为 application.yml)
│   │   └── db.migration/                // flyway 数据库迁移相关文件
└── test/
    └── java/
        └── com/
            └── todo_list/               // 测试代码目录
web/                                     // 前端代码
```

## 模型设计

> 本项目共有三个模型. user(用户), task(任务), todolist(任务列表). 用户用于管理登录状态, task 用户管理所有已完成的,
> 未完成的任务. todolist 用于统一管理所有 task

### USER

- username -> 用户名 : String
- password -> 密码(需要加密) : String
- avatar_url -> 头像 : String

### TASK(多对一)

- task_name -> 任务名 : String
- task_description -> 任务备注 : String
- deadline -> 截止日期(使用时间戳) : Long
- status -> 任务是否已经完成 : boolean
- todo_list_id -> todolist 的外键 : Long

### TODOLIST(一对多)

- category -> 任务类型 : String
- user_id -> user 的外键 :Long

## 接口设计

### 关于 USER

- /user/register -> 注册
    - POST
    - 传入参数: username, password
    - 返回: 注册成功
- /user/login -> 登录
    - POST
    - 传入参数: username, password
    - 返回: 登录成功添加响应头 setCookie, 值为 token, 前端拿到后持久化存储下来
- /user/logout -> 登出
    - GET
    - 返回: 登出成功
- /user/{id} -> 更新用户
    - PATCH
    - 传入: username || password || avatarUrl (一个或多个都行)
    - 返回: 更新成功
-  /user/upload/{id} -> 上传头像(图片)
  - POST
  - 传入: 用户id, 图片
  - 返回: 图片地址

### 关于 TODOLIST

- /list/{category}-> 建立一个空列表
    - PUT
    - 传入参数: category,
    - 返回: 新建成功
- /list/{id} -> 删除列表
    - DELETE
    - 返回: 删除成功
- /list/change_category/{id} -> 修改列表类型
    - PATCH
    - 传入参数: id, category
    - 返回: 是否删除成功
- /list/ -> 获取所有列表
    - GET
    - 返回: 一个列表 [[id, category, tasks[...]], [id, category1, tasks[...]], ...]
    - 备注: 加载主界面的时候调用这个接口, 这个接口不会直接返回 todo 模型, 点击对应的todolist 需要调用todo接口获取
- /list/{id}
    - GET
    - 返回: 一个列表 [id, category , tasks[...]]

### 关于 TASK

- /task/ -> 新建一个任务
    - POST
    - 传入参数: category(必须), name(必须), status(必须为false), description, deadline
    - 返回: 创建成功
- /task/{id} -> 获取单个任务
    - GET
    - 返回: 一个 task
- /task/{id} -> 删除一个任务
    - DELETE
    - 返回: 删除成功
- /task/{id}
    - PATCH
    - 传入参数: category || name || status || description || deadline
    - 返回: 更新成功

## 异常处理

> 下面我列举的异常是需要在服务层处理的具体的业务逻辑异常, 我表示的方法是 异常名(异常代码)
> 你们遇到对应的异常需要抛出对应的异常代码和异常名, 例如遇到了传入非法用户名就需要抛出"非法用户名", 异常代码为 1001

### 关于 User

- 非法用户名(1001) 用户名必须是大小写字母或者数字或者下划线的组合, 并且长度大于2.
    - 例如 aaa_AAA, bb1122
    - 反例 ##, a, abcd$
- 用户名或者密码错误(1002)
- 用户名已存在(1003)
- 找不到用户(1005) 更新的时候传入了非法的 id, 导致到不到对应的用户
- 没有登录(1004) 这个异常我已经帮你们处理了

### 关于 Task

- 非法时间(2001) 传入截止日期超过了 2038 年, 这是时间戳的最大表示日期, 表示为时间戳他应该小于2147483647
- 没找到任务(2002) 获取, 删除, 更新的时候传入了非法的 id
- 不是将来的时间(2003) 传入截至日期但是传入了过去的时间
- 错误的状态(2004) 传入任务是否完成的状态但是传入了 true, 新建一个任务默认完成的状态应该是 false

### 关于 List

- 重复类型的任务(3001) 新建任务列表的时候传入了重复的类别
- 没找到任务列表(3002) 更新或者获取的时候传入非法 id

> [!warning]
>
> 注意: 如果你写的代码对边界情况考虑的不充分, 当产生了异常, 却没有被你捕获的时候, 代码会抛出 `500` 异常 并显示
> `系统繁忙，请稍后重试`.

# 准备工作

## maven 换源

新建一个 xml 文件, 如果没有的话, 路径是

```angular2html
C:\Users\你的用户名\.m2\settings.xml
```

填入

```angular2html

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

然后到 IDEA 运行

```angular2html
mvn clean install -T 1C
```

## 设置 jdk

![image-20250422145405621](./assets/image-20250422145405621.png)

![image-20250422145458973](./assets/image-20250422145458973.png)

然后选择并下载 `jdk21` , 确保你的项目的 jdk 版本是 21 否则可能无法启动项目.

## 连接数据库

确保你的 mysql 有这样一个数据库 `TodoList` , 如果没有的话就新建一个.

```sql
create database TodoList;
use TodoList;
```

到 `src/main/resources/application.yml` 可以看到我设置的数据库的账号和密码

![image-20250422150014976](./assets/image-20250422150014976.png)

我默认设置的是 root , root . 如果你的数据库的账号和密码不同, 请把这个地方的配置修改成你的用户名和密码.

然后接下来测试一下看看是否能连接

![image-20250422150309907](./assets/image-20250422150309907.png)

![image-20250422150339347](./assets/image-20250422150339347.png)

在 `1, 2` 的位置 填入你的账号密码. `3` 处如果你没有下载启动程序他会先要求你下载驱动, 点一下就行了. 如果测试连接失败请检查一下
mysql 有没有安装到你的电脑上, 然后检查一下自己的账号密码对不对.

## 前置知识

> 想要开始本项目, 你至少需要了解下面的相关知识.

- [ ] 什么是 `JWT` , 如何使用. (登录, 注册有用)
- [ ] 什么是网络拦截器, 它是如何阻止没有登陆的用户访问后端接口的. 以及什么是 Cookie
- [ ] 什么是 Java 异常处理. (本项目采用全局异常处理的方式, 这符合最佳实践)
- [ ] 什么是 JPA (spring boot 的持久层)
- [ ] 什么是 DTO, 如何设计一个合理的 DTO.
- [ ] 什么是接口文档(Swagger)
- [ ] 什么是 RESTful API 规范, 如何设计一个合理的接口

还有一些 java 的知识

- [ ] 什么是 枚举类, 纪录类

除此之外, 你还需要简单了解一下 spring boot 是如何进行测试的.

## 接口文档

> 如果上述步骤你都没有问题, 那就可以开始测试我留下来的第一个接口了. 这个接口我保留了注册全部的流程, 方便同学们更深刻的理解本项目.

先把项目跑起来. 到这个网址

``` url
http://localhost:8080/swagger-ui/index.html
```

![image-20250422153216867](./assets/image-20250422153216867.png)

![image-20250422153319116](./assets/image-20250422153319116.png)

如果显示注册成功, 那么项目的准备工作就到此为止, 可以开始编写自己的代码了.

# Apifox

> apifox 是一个用于测试接口和自动化测试的软件, 在这里下载 https://apifox.com/ 



## 导入配置和测试



点击进入 github 首页右侧的 release. 我把我已经配置好的 apifox 上传了. 你们需要下载下来. ![image-20250507195854443](./assets/image-20250507195854443.png)

然后导入到 apifox 中

###### ![PixPin_2025-05-07_20-05-17](./assets/PixPin_2025-05-07_20-05-17.gif)

然后到自动化测试页面中测试代码完成度.

![image-20250507200810108](./assets/image-20250507200810108.png) 

如果你的代码没有问题, 那么他就会通过测试

![image-20250507200901202](./assets/image-20250507200901202.png)  

任务相关的测试在 Task 目录下面, 列表相关的测试在 List 目录下面

如果你完成了所有代码, 可以批量运行测试代码, 最终项目展示的时候需要你展示通过率 

![image-20250507201021168](.\assets\image-20250507201021168.png)

当然, 仅仅批量运行用户相关代码也是可以的. 

# 保证数据库连通 

还有, 在测试之前请确保数据库是可以连接的

![image-20250507201238700](.\assets\image-20250507201238700.png)

选择开发环境. 如果连接失败, 那么请排查以下错误

- 是否已经下载 mysql 数据库
- mysql 数据库的账号和密码是否都是 root, 如果不是并且不想修改, 那么可以修改配置文件. 我将告诉你怎么做.
- 如果数据库可以联通, 确保有 TodoList 这个库. 可以通过 show databases; 查看.

## 修改配置文件.

> 如果你上面都没有问题, 那么这条可以略过. 

![image-20250507202805657](./assets/image-20250507202805657.png)

 把这里的数据库用户名和密码改成你的

![image-20250507202851737](./assets/image-20250507202851737.png)

回到代码, 把配置文件也改一下

![image-20250507202946145](./assets/image-20250507202946145.png)



# TODO



我预留了许多提示信息. 几乎在所有你需要写代码的地方, 你可以通过 `IDEA` 的 TODO 功能找到他们

![image-20250507205434219](./assets/image-20250507205434219.png)

![image-20250507205551366](./assets/image-20250507205551366.png)

双击即可跳转到对应的位置. 方便排查未完成的功能

我将会把需要实现的功能注释掉而你需要做的是把他解开然后实现. 选中这个方法(包括 @Operation 注解) 使用快捷键 `ctrl + /` 即可取消注释

![image-20250507205833967](./assets/image-20250507205833967.png)











