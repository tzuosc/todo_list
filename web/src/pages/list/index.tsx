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
            const res = await getAllTodoLists();
            const allLists = res.data || [];
            const matched = allLists.find((list) => list.category === category);
            if (!matched) {
                toast.error("找不到该分类");
                return;
            }
            const res2 = await fetchByListId(matched.id);
            const todoList = res2.data;
            if (!todoList?.tasks || todoList.tasks.length === 0) {
                setTasks([]); // 如果没有任务，清空任务列表
                return;
            }

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
            setTasks(taskList.filter((task) => task !== null) as TaskRow[]);
        } catch (error) {
            toast.error("获取任务列表失败");
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchTasks(); // 当 `category` 变化时重新调用 fetchTasks
    }, [category]);

    return (
        /*item-end */
        <div className={cn(["flex","w-full","h-full","justify-end"])}>
            <div className={cn([
                "flex", "flex-col",
                "h-full","w-4/5"
            ])} >
                <div className={cn(["w-full","items-start","basis-1/5"])}>
                    <p className="lg:text-2xl font-bold mb-4">{category}</p>
                </div>

                <div className={cn(["flex","w-full","flex-col" ,"basis-4/5"])} >

                    <div className={cn([ "flex","px-8","h-full","flex-col"])}>
                        <Columns tasks={tasks} loading={loading} onUpdated={fetchTasks} />
                    </div>

                    <div className={cn(["flex","h-1/3","justify-end","w-full"])}>
                        <CreateDialog/>
                    </div>
                </div>

            </div>
        </div>
    );
}
