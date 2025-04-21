// components/widgets/column.tsx
import { useMemo } from "react";
import { ColumnDef } from "@tanstack/react-table";
import { DataTable } from "@/components/ui/data-table";
import { Checkbox } from "@/components/ui/checkbox";
import { EditTaskButton } from "@/components/widgets/editTask/editTaskButton/editTaskButton.tsx";

// 删除不需要的状态声明，直接使用 props 传递过来的值
export interface TaskRow{
    id: number;        // 任务 ID
    name: string;      // 任务名称
    status: boolean;   // 任务状态，可能是完成或未完成
    deadline:number,
    category: string;  // 任务所属分类
}
interface ColumnProps {
    tasks: TaskRow[];  // 添加 tasks 属性
    loading: boolean;  // 添加 loading 属性
    onUpdated?:()=>void;
}


export function Column({ tasks, loading,onUpdated }: ColumnProps) {
    // 表格列定义
    const columns: ColumnDef<TaskRow>[] = useMemo(() => [
        {
            accessorKey: "status",
            header: "状态",
            cell: ({ row }) => (
                <Checkbox checked={row.getValue("status")} disabled />
            ),
        },
        {
            accessorKey: "name",
            header: "任务名称",
            cell: ({ row }) => (
                <span className="font-medium">{row.getValue("name")}</span>
            ),
        },
        {
            accessorKey: "deadline",
            header: "截止时间",
            cell: ({ row }) => {
                const timestamp = row.getValue<number>("deadline");
                const date = new Date(timestamp);
                const formatted = date.toLocaleDateString("zh-CN", {
                    year: "numeric", //
                    month: "2-digit", //
                    day: "2-digit", //
                });
                return <span>{formatted}</span>;
            },
        },
        {
            id: "actions",
            header: "操作",
            cell: ({ row }) => (
                <EditTaskButton
                    taskId={row.original.id}
                    category={row.original.category}
                    deadline={row.original.deadline}
                    onUpdated = {onUpdated}
                />
            ),
        }
    ], [onUpdated]);

    if (loading) return <div className="p-4">加载中...</div>;

    return (
        <div className="container mx-auto py-4">
            <DataTable columns={columns} data={tasks} />
        </div>
    );
}
