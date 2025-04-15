## 模型设计

### USER

- **username** -> 用户名
- **password** -> 密码
- **avatar_url** -> 头像

### TASK

- **task_name** -> 任务名
- **task_description** -> 任务备注
- **deadline** -> 截止日期
- **status** -> 任务是否已经完成

### TODOLIST

- **category** -> 任务类型
- **task_list** -> 任务List

## 页面(路由)设计

```angular2html
/ (路由入口)
├── /auth
│   ├── login           # 登录页
│   └── register        # 注册页
├── /index              # 主控台
└── /settings           # 系统设置

流程: login -> index -> seeting
```

## 接口设计

### 关于 USER

- /register -> 注册接口
    - POST
    - 传入参数: username, password
    - 返回: 是否注册成功
- /login -> 登录接口
    - POST
    - 传入参数: username, password
    - 返回: 登录成功返回一个 token, 前端拿到后持久化存储下来

### 关于 TODOLIST

- /create_list -> 建立一个空列表
    - POST
    - 传入参数: category,
    - 返回:是否新建成功
- /delete_list -> 删除列表
    - DELETE
    - 传入参数: id
    - 返回:是否删除成功
- /change_list -> 修改列表类型
    - UPDATE
    - 传入参数: id, category
    - 返回: 是否删除成功
- /fetch_list -> 获取所有列表
    - GET
    - 返回: 一个列表 [[category1, todo_id_list1], [category2, todo_id_list2]]
    - 备注: 加载主界面的时候调用这个接口, 这个接口不会直接返回 todo 模型, 点击对应的todolist 需要调用todo接口获取

### 关于 TASK

- /create_task -> 新建一个任务
    - POST
    - 传入参数: category, task, remark(可以为空), dead_line(可以为空)
    - 返回: 是否创建成功
- /get_task -> 获取单个任务
    - GET
    - 传入: id
    - 返回: 一个任务
- /finish_task -> 完成一个任务
    - PATCH
    - 传入: id
- /not_finish_task -> 把任务重新标记为未完成
    - GET
    - 传入: id
- /delete_task -> 删除一个任务
    - DELETE
    - 传入: id
- /change_category -> 修改任务类型
    - UPDATE
    - 传入: id, category
- /change_deadline -> 修改截止日期
    - UPDATE
    - 传入: id, deadline

2
