package org.example.todo_list.service;


import lombok.RequiredArgsConstructor;
import org.example.todo_list.repository.jpa.TaskRepository;
import org.example.todo_list.repository.jpa.TodoListRepository;
import org.example.todo_list.repository.jpa.UserRepository;
import org.example.todo_list.security.JwtUtils;
import org.springframework.stereotype.Service;

@Service//表示该类是一个Spring服务组件，用于业务逻辑处理。
@RequiredArgsConstructor //自动生成包含所有final字段的构造函数
public class TodoListService {
    private final TodoListRepository todoListRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

//    public void create(String category, Long userId) {
//
//    }
 /* TODO 新建任务列表: 如果你没有对应的类别的 todolist 你需要新建一个 todolist
开始创建任务列表
└─→ 检查类别是否已存在?
   ├─→ 是 → 抛出列表重复异常 (含 getCode()返回错误码，getMessage()返回错误描述)（LIST_ALREADY_EXISTS）
   └─→ 否 → 查找用户
       ├─→ 存在 → 构建新任务列表并保存 → 将任务列表添加到用户
       └─→ 不存在 → 抛出用户不存在异常（USER_NULL）
*/

//    public void delete(Long id, Long userId) {
//
//    }
/* TODO 删除 todolist 如果你在模型没有设置级联删除, 那么你需要在这里一个个删除所有 task. 你需要处理 id 对应的 todolist 不存在的情况
开始删除TodoList
├─→ 根据id查找TodoList
│   ├─→ 存在 → 删除TodoList
│   │       └─→ 根据userId查找用户
│   │           ├─→ 存在 → 用户移除TodoList → 结束
│   │           └─→ 不存在 → 抛出（USER_NULL） → 结束
│   └─→ 不存在 → 抛出（LIST_NOT_FOUND） → 结束
*/

//    public void changeListCategory(Long id, String newCategory, Long userId) {
//    }
/*TODO 更新 todolist 的类别
开始更新类别
├─→ 检查新类别是否存在
│   ├─→ 存在 → 抛出 DUPLICATE_CATEGORY 异常 → 结束
│   └─→ 不存在 → 尝试更新类别
│           └─→ 检查更新结果是否为1
│               ├─→ 是 → 结束
│               └─→ 否 → 抛出 LIST_NOT_FOUND 异常 → 结束
*/

//    public List<GetListResponse> getAllLists(Long userId) {
//    }
/*TODO 获取所有 todolist,循环获取所有 list, 为此, 我准备了 taskRepository.findTaskIdsByCategory
该方法根据用户ID获取所有待办事项列表，并构建包含任务ID、列表ID和分类的响应数据返回。
开始获取待办列表
├─→ 调用 findTodoListByUserId 获取原始数据
├─→ 初始化空结果列表 res
├─→ 遍历每个 TodoList 条目
│   ├─→ 转换为 GetListResponse 对象（DTO模式）
│   ├─→ 提取任务ID列表、列表ID、分类信息（数据解耦）
│   └─→ 将响应对象加入 res（结果聚合）
└─→ 返回结果列表 res（响应标准化）
     */

//    public GetListResponse getListById(Long id) {
//    }
/*TODO 根据 id 获取 todolist, 你只需要判断 id 对应的 todolist 是否存在就行了
开始
└─ 通过 id 查找 todolist
    └─ 查找到的 todolist 是否存在
        └─ 是
            ├─ 查询关联的任务 id 列表
            ├─构建并返回 GetListResponse
        └─ 否
           └─ 抛出 LIST_NOT_FOUND 异常
 */
}

