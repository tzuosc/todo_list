import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getAllTodoLists, fetchByListId } from "@/api/todolist";

import { toast } from "sonner";
import { AddTask } from "@/components/widgets/addTask";
import { Column } from "@/pages/list/column.tsx";
import { getTask } from "@/api/task";
import { cn } from "@/utils";
import { Dialog, DialogContent, DialogTrigger } from "@/components/ui/dialog.tsx";
import { Button } from "@/components/ui/button.tsx";

export default function ListDetailPage() {
    const { category } = useParams<{ category: string }>(); /* 获取*/
    const [loading, setLoading] = useState(true);
    const [taskNames, setTaskNames] = useState<string[]>([]);

    useEffect(() => {
        if (!category) return;

        getAllTodoLists().then((res) => {
            const allLists = res.data || [];

            const matched = allLists.find((list) => list.category === category);
            if (!matched) {
                toast.error("找不到该分类");
                setLoading(false);
                return;
            }  //category不匹配

            fetchByListId(matched.id).then(async (res2) => {
                const todoList = res2.data;
                if (!todoList?.tasks) {
                    setTaskNames([]);
                    setLoading(false);
                    return;
                }

                const names: string[] = [];
                for (const id of todoList.tasks) {
                    const taskRes = await getTask(id);
                    if (taskRes.code === 200 && taskRes.data?.name) {
                        names.push(taskRes.data.name);
                    }
                }

                setTaskNames(names);
                setLoading(false);
            });
        });
    }, [category]);


    return (
        <div className={cn(["flex","flex-col","items-center", "justify-center"])}>
            <div className={cn(["flex-col"," items-center", "justify-center"])}>
                <h1 className="text-2xl font-bold mb-4">{category} 分类任务</h1>
                <Column tasks={taskNames} loading={loading} />
            </div>

            {/* 添加任务组件，传入 listId */}
            <Dialog>
                <DialogTrigger asChild>
                    <Button variant={"outline"}>Add Task</Button>
                </DialogTrigger>
                <DialogContent>
                    <AddTask  />
                </DialogContent>
            </Dialog>
        </div>
    );
}
