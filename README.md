# 项目介绍

本项目将要完成一个 TodoList 系统, 包含以下几个部分

1. 登录和注册
2. 任务的增删改查
3. 各种边界异常的处理
4. 权限的控制(只有登录和没有登录两种状态, 没有其他权限)

我将会保留注册相关流程的代码用于示例, 其他的代码需要同学们自己完成,
本项目采用**测试类**来检验项目的**完成度**, 同学们在完成某个功能的代码之后可以自己先测试
一下自己的接口对不对, 然后再到测试类测试一下边界情况是否考虑到了, 本项目没有**标准答案**,
所有可以通过测试类的代码都是标准答案.

技术栈: mysql + spring boot + Maven + React(可选)

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
- /list/change -> 修改列表类型
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
>
> 为了方便教学, 所有业务逻辑异常的状态码统一为 400.

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

![image-20250422145405621](assets/image-20250422145405621.png)

![image-20250422145458973](assets/image-20250422145458973.png)

然后选择并下载 `jdk21` , 确保你的项目的 jdk 版本是 21 否则可能无法启动项目.

## 连接数据库

确保你的 mysql 有这样一个数据库 `TodoList` , 如果没有的话就新建一个.

```sql
create database TodoList;
use TodoList;
```

到 `src/main/resources/application.yml` 可以看到我设置的数据库的账号和密码

![image-20250422150014976](assets/image-20250422150014976.png)

我默认设置的是 root , root . 如果你的数据库的账号和密码不同, 请把这个地方的配置修改成你的用户名和密码.

然后接下来测试一下看看是否能连接

![image-20250422150309907](assets/image-20250422150309907.png)

![image-20250422150339347](assets/image-20250422150339347.png)

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

## 第一个接口

> 如果上述步骤你都没有问题, 那就可以开始测试我留下来的第一个接口了. 这个接口我保留了全部的流程, 方便同学们更深刻的理解本项目.

先把项目跑起来. 到这个网址

``` url
http://localhost:8080/swagger-ui/index.html
```

![image-20250422153216867](assets/image-20250422153216867.png)

![image-20250422153319116](assets/image-20250422153319116.png)

如果显示注册成功, 那么项目的准备工作就到此为止, 可以开始编写自己的代码了.



# 前端项目设计

## 项目前置准备

本项目基于 **[Vite](https://vitejs.dev/)** + **React** + **TypeScript**，使用了 **[Tailwind CSS](https://tailwindcss.com/)** 作为原子化 CSS 工具，并集成了 **shadcn/ui** 组件库。使用之前请完成以下前置工作：

+ **安装Vite脚手架**

  ```powershell
  npm create vite@latest
  pnpm create vite (推荐)
  ```

  (pnpm下载得基于npm,没有安装pnpm去 [pnpm中文文档](https://www.pnpm.cn/installation) 自己找，换源什么的不多赘述)

+ **配置Tailwind CSS**
  📄 Tailwind CSS 官网文档：
   👉 https://tailwindcss.com/docs/installation

+ **配置shadcn/ui组件**
  📄 shadcn/ui 官网配置文档：
   👉 https://ui.shadcn.dev/docs/installation


  **(建议先去配置shadcn/ui,再去看Tailwind CSS。shadcn配置文档会教你如何配置Tailwind CSS。使用 shadcn 组件前，Tailwind 必须已正确配置)**

## 项目结构

```markdown
web/
├── node_modules/            // 项目依赖文件夹（自动生成）
├── public/                  // 公共资源目录
├── src/                     // 源代码主目录
│   ├── api/                 // 接口请求封装（如 axios 实例、API 方法等）
│   ├── assets/              // 静态资源目录
│   ├── components/          // 通用组件库（按钮、模态框等）
│   ├── lib/                 // 第三方库封装或工具库
│   ├── models/              // 类型模型定义（如接口响应结构等）
│   ├── pages/               // 页面级组件（路由组件）
│   ├── router/              // 路由配置文件（如 react-router-dom 配置）
│   ├── storages/            // 本地存储封装（localStorage 等）
│   ├── styles/              // 全局样式文件（如 Tailwind、全局 CSS）   介绍
│   ├── types/               // 全局类型定义
│   ├── utils/               // 工具函数（如日期处理、加密等）
│   ├── App.tsx              // 应用主组件
│   ├── main.tsx             // 应用入口文件
│   └── vite-env.d.ts        // Vite 的环境类型定义
├── .gitignore               // Git 忽略文件配置
├── .prettierrc              // Prettier 代码格式化配置  介绍
├── components.json          // 组件库配置（可选）
├── eslint.config.js         // ESLint 配置文件
├── index.html               // HTML 入口文件
├── package.json             // 项目信息和依赖列表
├── package-lock.json        // 锁定依赖版本
├── README.md                // 项目说明文档
├── tsconfig.json            // TypeScript 基础配置
├── tsconfig.app.json        // 应用专用的 TS 配置
├── tsconfig.node.json       // Node 环境的 TS 配置
└── vite.config.ts           // Vite 配置文件

```
## 🧩 前端通用工具与类型定义说明

本文件主要说明了项目中 `types/`、`utils/` 目录下的工具函数、类型定义与全局配置用途及使用方式

### 📁 `types/index.tsx`

#### `WebResponse<T>`

统一定义接口响应结构，适用于所有后端 API 通信的泛型封装。

```ts
export interface WebResponse<T> {
  code: number;        // 状态码，如 200 表示成功，1004 表示未登录等
  data?: T;            // 返回的泛型数据
  msg?: string;        // 错误或成功提示信息
  ts: number;          // 时间戳
  total?: number;      // 数据总量（用于分页）
}
```



### 📁 `utils/index.ts`

🧩 `cn(...)` 工具函数

```ts
import { clsx, type ClassValue } from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}
```

##### ✅ 功能说明

- 用于合并多个类名字符串，自动解决 Tailwind CSS 的类冲突。
- 类似 `classnames`，但增强了 Tailwind 专用的合并逻辑。

### 📁 `utils/alova.ts`

#### 🌐 网络请求封装

使用 alova 配合 fetch 构建统一请求器。

```ts
import { createAlova } from "alova";
import adapterFetch from "alova/fetch";

export const alova = createAlova({
  baseURL: "/api",
  requestAdapter: adapterFetch(),
  timeout: 5000,
  shareRequest: true,
  statesHook: ReactHook,
  responded: {
    onSuccess: async (response, _method) => {
      const res = await response.json();
      // 通用拦截逻辑
    }
  }
});
```

##### 🔐 通用拦截逻辑

- `1004`: 未登录 → 跳转登录页并清除用户状态。
- `502`: 后端挂了 → 弹出错误提示。

### 📁 `utils/global-router.ts`

#### 🌍 全局路由跳转支持

```ts
const globalRouter = { navigate: undefined } as {
  navigate?: NavigateFunction;
};

export default globalRouter;
```

##### 🧭 使用方式 (具体的使用在项目代码里写了注释)

在 `layout.tsx` 中初始化：

```tsx
globalRouter.navigate = useNavigate();
```



## 📦 状态管理模块：`storages/`

### 1. `auth.ts` - 用户认证状态管理

**路径**：`storages/auth.ts`
 **依赖**：[Zustand](https://github.com/pmndrs/zustand)、`User` 模型

```ts
// storages/auth.ts

import { User } from "@/models/user";
import { create } from "zustand";
import { createJSONStorage, persist } from "zustand/middleware";

/**
 * ✅ 说明：
 * 全局用户认证状态管理
 * - 使用 zustand 管理用户登录信息
 * - 支持持久化存储（localStorage），实现刷新页面后仍保持登录
 *
 * ✅ 为什么使用 zustand？
 * - 简洁轻量
 * - 使用简单但功能强大(persist)
 */

// 用户状态接口
interface AuthState {
    user?: User;                      // 当前登录用户（包括用户名、头像等）
    setUser: (user?: User) => void;  // 设置用户信息（登录后使用）
    clear: () => void;               // 清空用户信息（登出时使用）
}

// 创建状态管理：useAuthStore 包含 user, setUser, clear
export const useAuthStore = create<AuthState>()(
    persist(
        (set, _get) => ({
            setUser: (user?: User) => set({ user }),     // 设置用户
            clear: () => set({ user: undefined }),       // 清空用户
        }),
        {
            name: "auth",                                 // localStorage 的 key 名称
            storage: createJSONStorage(() => localStorage), // 使用 localStorage 存储
        }
    )
);

```



#### ✅ 功能说明

`auth.ts` 使用 `zustand` 管理全局的用户认证状态。它负责：

- 存储当前登录用户的信息（如用户名、头像等）
- 在用户登录、退出时更新状态
- 利用 `zustand/middleware/persist` 插件将状态持久化到 `localStorage`，支持页面刷新后的状态保持

#### 📘 使用场景

- 判断用户是否已登录
- 获取当前用户信息用于展示（如头像、昵称）
- 实现退出登录时的状态清除
- 页面刷新后自动保持登录状态

通过 `useAuthStore()` 可访问：

- `user`: 当前用户对象（可为空）
- `setUser(user?: User)`: 设置用户信息
- `clear()`: 清除用户信息（登出时调用）

### 2. `shared.ts` - 通用刷新状态管理

**路径**：`storages/shared.ts`
 **依赖**：仅依赖 `zustand`

```ts
// storages/shared.ts

import { create } from "zustand";

/**
 * ✅ 说明：
 * 用于组件之间共享状态的全局 store
 * 当前仅用于提供页面/组件刷新信号
 *
 * 使用场景示例：
 * - 用户更新信息后，需要其他组件重新拉取数据
 * - 点击某个操作，想通知多个组件重新加载
 */

// 共享状态接口
interface SharedState {
    refresh: number;        // 每次更新都会自增，触发依赖该值的组件重新渲染
    setRefresh: () => void; // 执行一次刷新：refresh + 1
}

// 创建共享状态管理：useSharedStore 包含 refresh 和 setRefresh
export const useSharedStore = create<SharedState>()((set, get) => ({
    refresh: 0,
    setRefresh: () => set({ refresh: get().refresh + 1 }),
}));

```



#### ✅ 功能说明

`shared.ts` 提供一个简单的全局状态 `refresh`，用于跨组件传递“刷新信号”。

- 通过递增的 `refresh` 数值，实现依赖组件的响应式刷新
- 适合用在“某个地方数据变了，其他地方需要感知并刷新”这种场景中

#### 📘 使用场景 (在update-list-dialog中的例子)

```tsx
const sharedStore = useSharedStore()
const onSubmit = form.handleSubmit(async (values) => {
        setLoading(true)
        try {
            ...
            if (res.code === 200) {
                sharedStore.setRefresh()// 在操作成功后手动触发一次全局刷新信号，侧边栏会出现新的list名字
                onClose()
                navigate(`/list/${values.category}`)//然后跳转到更新的list页面
            } ...
        } catch (err) {
           ...
        } finally {
            ...
        }
    })
return (
    ...
    <Form{...form}>
    	...
        <form onSubmit={onSubmit}...>
        	<Button
            ...
            type={"submit"}>
        	...
        	</Button>
        </form>
    </Form>
```



通过 `useSharedStore()` 可访问：

- `refresh`: 数值，每次调用 `setRefresh()` 都会 +1
- `setRefresh()`: 用于手动触发刷新



## 📁 `models/user.ts` 模块说明文档

### ✅ 作用

定义项目中与「用户（User）」相关的数据结构，即 `User` 类型接口。它用于标识、约束用户对象包含的字段类型，是整个项目在登录、注册、更新用户信息等操作中统一使用的用户模型。

### 📦 User 接口字段说明

```ts
export interface User {
    id?: number; // 用户 ID
    username?: string; // 用户名
    password?: string; // 密码（登录或注册时用）
    confirm_password?: string; // 确认密码（注册时使用）
    avatarUrl?: string; // 用户头像 URL
}
```

> 所有字段均为 ?（`表示可选`），以方便在表单处理、局部更新时使用。

### 🧠 使用场景

- 注册表单中，使用 `User` 模型进行类型推导；
- 登录响应中，服务端返回的用户数据统一使用该结构；
- 在全局状态 `auth.ts` 中存储的 `user` 类型也是该接口。

## 📁 `api/user/index.ts` 模块说明文档

### ✅ 作用

封装与「用户」相关的所有 API 请求方法，所有请求都通过 `alova` 请求实例发出，并统一使用 `WebResponse` 类型封装响应结果。

### 📡 API 一览表

| 方法名         | 请求路径         | 请求方式 | 用途         |
| -------------- | ---------------- | -------- | ------------ |
| `login`        | `/user/login`    | POST     | 登录         |
| `logout`       | `/user/logout`   | GET      | 注销登录     |
| `register`     | `/user/register` | POST     | 注册账号     |
| `updateUser`   | `/user`          | PATCH    | 修改用户信息 |
| `uploadAvatar` | `/user/upload`   | POST     | 上传用户头像 |



### 🧩 请求类型定义

```ts
// 登录请求体
export interface UserLoginRequest {
    username: string;
    password: string;
}

// 注册请求体
export interface UserRegisterRequest {
    username: string;
    password: string;
    confirm_password: string;
}

// 更新用户信息请求体
export interface UserUpdateRequest {
    id: number;
    username?: string;
    password?: string;
}
```



### 🔄 响应格式统一封装

所有 API 响应都使用如下通用格式封装：

```ts
interface WebResponse<T> {
    code: number;
    data?: T;
    msg?: string;
    ts: number;
    total?: number;
}
```

例如：

- 登录返回：`WebResponse<User>`
- 上传头像返回：`WebResponse<string>`（返回头像地址）
