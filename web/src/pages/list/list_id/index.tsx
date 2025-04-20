import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getAllTodoLists, fetchByListId } from "@/api/todolist";
import { toast } from "sonner";
import { AddTask } from "@/components/widgets/addTask";
import { Column, TaskRow } from "@/pages/list/column.tsx";
import { getTask } from "@/api/task";
import { cn } from "@/utils";

export default function ListDetailPage() {
    const { category } = useParams<{ category: string }>();
    const [loading, setLoading] = useState(true);
    const [tasks, setTasks] = useState<TaskRow[]>([]);

    // 更新任务列表
    const fetchTasks = async () => {
        if (!category) return;
        setLoading(true);
        try {
            // 获取所有的 todo list
            const res = await getAllTodoLists();
            const allLists = res.data || [];
            const matched = allLists.find((list) => list.category === category);
            if (!matched) {
                toast.error("找不到该分类");
                setLoading(false);
                return;
            }

            // 获取匹配的列表的任务
            const res2 = await fetchByListId(matched.id);
            const todoList = res2.data;
            if (!todoList?.tasks) {
                setLoading(false);
                return;
            }

            const taskList: TaskRow[] = [];
            await Promise.all(todoList.tasks.map(async (id) => {
                const taskRes = await getTask(id);
                if (taskRes.code === 200 && taskRes.data?.name) {
                    taskList.push({
                        id,
                        name: taskRes.data.name,
                        status: taskRes.data.status ?? false,
                        category: category,
                    });
                }
            }));
            setTasks(taskList);
        } catch (error) {
            toast.error("获取任务列表失败");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchTasks();
    }, [category]);

    // 任务更新后的回调函数


    return (
        <div className={cn(["flex", "flex-col", "items-center", "justify-center"])} >
            <div className={cn(["flex-col", "items-center", "justify-center"])} >
                <h1 className="text-2xl font-bold mb-4">{category} 分类任务</h1>
                <Column tasks={tasks} loading={loading} onUpdated={fetchTasks} />
            </div>
            <AddTask />
        </div>
    );
}
