import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getAllTodoLists, fetchByListId } from "@/api/todolist";
import { toast } from "sonner";
import { CreateDialog } from "@/pages/list/create-dialog.tsx";
import { Columns, TaskRow } from "@/pages/list/columns.tsx";
import { getTask } from "@/api/task";
import { cn } from "@/utils";

export default function ListDetailPage() {
    const { category } = useParams<{ category: string }>();
    const [loading, setLoading] = useState(true);
    const [tasks, setTasks] = useState<TaskRow[]>([]);

    // 更新任务列表
    const fetchTasks = async () => {
        if (!category) return; // 如果没有 category，不执行后续操作
        setLoading(true); // 开始加载

        try {
            // 获取所有的 todolist
            const res = await getAllTodoLists();
            const allLists = res.data || [];
            const matched = allLists.find((list) => list.category === category);

            if (!matched) {
                toast.error("找不到该分类");
                return; // 找不到对应分类，退出
            }

            // 获取匹配的列表的任务
            const res2 = await fetchByListId(matched.id);
            const todoList = res2.data;
            if (!todoList?.tasks || todoList.tasks.length === 0) {
                setTasks([]); // 如果没有任务，清空任务列表
                return;
            }

            // 使用 Promise.all 来并发获取任务详情
            const taskList = await Promise.all(
                todoList.tasks.map(async (id) => {
                    const taskRes = await getTask(id);
                    if (taskRes.code === 200 && taskRes.data?.name) {
                        return {
                            id,
                            name: taskRes.data.name,
                            status: taskRes.data.status ?? false,
                            category: category,
                            deadline: taskRes.data.deadline,
                        };
                    }
                    return null;
                })
            );

            // 过滤掉返回 null 的任务
            setTasks(taskList.filter((task) => task !== null) as TaskRow[]);
        } catch (error) {
            toast.error("获取任务列表失败");
            console.error(error); // 打印出错误以便调试
        } finally {
            setLoading(false); // 无论如何都设置加载完成
        }
    };

    useEffect(() => {
        fetchTasks(); // 当 `category` 变化时重新调用 fetchTasks
    }, [category]);

    return (
        <div className={cn(["flex", "flex-col", "items-center", "justify-center"])} >
            <div className={cn(["flex-col", "items-center", "justify-center"])} >
                <h1 className="text-2xl font-bold mb-4">{category} 分类任务</h1>
                <Columns tasks={tasks} loading={loading} onUpdated={fetchTasks} />
            </div>
            <CreateDialog />
        </div>
    );
}
