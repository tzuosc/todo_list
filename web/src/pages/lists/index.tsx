import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import {
    flexRender,
    getCoreRowModel,
    getFilteredRowModel,
    useReactTable,
} from "@tanstack/react-table";
import { columns } from "@/pages/lists/columns";  // 导入 columns 配置

import { Dialog, DialogContent } from "@/components/ui/dialog.tsx";
import { CreateDialog } from "@/pages/lists/create-dialog.tsx";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table.tsx";
import { cn } from "@/utils";
import { Task } from "@/models/task.ts";
import { fetchByListId, getAllTodoLists } from "@/api/todolist";
import { toast } from "sonner";
import { getTask } from "@/api/task";
import { TaskRow } from "@/pages/list/columns.tsx";

export default function Index() {
    const [total, setTotal] = useState<number>(0);
    const [createDialogOpen, setCreateDialogOpen] = useState(false);
    const { category } = useParams<{ category: string }>();
    const [loading, setLoading] = useState(true);
    const [tasks, setTasks] = useState<Array<Task>>([])
    const table = useReactTable<Task>({
        data:tasks,
        columns,
        getCoreRowModel:getCoreRowModel(),
        rowCount:total,/*计算行*/
        manualFiltering:true,/*筛选*/
        getFilteredRowModel:getFilteredRowModel(),
        manualSorting:true,

    })
    const fetchTasks = async () => {
        if (!category) return;
        setLoading(true);
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
                setTasks([]);
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
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchTasks();
    }, [category]);
    return (
        <div className="flex flex-col items-center">
            <h1 className="text-2xl font-bold mb-4">{category} 分类任务</h1>
            <Table className={cn(["text-foreground"])}>
                <TableHeader>
                    {table.getHeaderGroups().map((headerGroup) => (
                        <TableRow key={headerGroup.id}>
                            {headerGroup.headers.map((header) => {
                                return (
                                    <TableHead key={header.id}>
                                        {!header.isPlaceholder &&
                                            flexRender(
                                                header.column.columnDef
                                                    .header,
                                                header.getContext()
                                            )}
                                    </TableHead>
                                );
                            })}
                        </TableRow>
                    ))}
                </TableHeader>
                <TableBody>
                    {table.getRowModel().rows?.length ? (
                        table.getRowModel().rows.map((row) => (
                            <TableRow
                                key={row.getValue("id")}
                                data-state={
                                    row.getIsSelected() && "selected"
                                }
                            >
                                {row.getVisibleCells().map((cell) => (
                                    <TableCell key={cell.id}>
                                        {flexRender(
                                            cell.column.columnDef.cell,
                                            cell.getContext()
                                        )}
                                    </TableCell>
                                ))}
                            </TableRow>
                        ))
                    ) : (
                        <TableRow>
                            <TableCell
                                colSpan={columns.length}
                                className="h-24 text-center"
                            >
                                哎呀，好像还没有Task。
                            </TableCell>
                        </TableRow>
                    )}
                </TableBody>
            </Table>
            {/*<div>
                <div>
                    {table.getFilteredRowModel().rows.length}/{total}
                </div>
                <div className={}>
                    <Select
                </div>
            </div>*/}
            <Dialog open={createDialogOpen} onOpenChange={setCreateDialogOpen}>
                <DialogContent>
                    <CreateDialog onClose={() => setCreateDialogOpen(false)} />
                </DialogContent>
            </Dialog>
        </div>
    );
}
