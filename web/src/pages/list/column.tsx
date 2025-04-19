// components/widgets/column.tsx
import { ColumnDef } from "@tanstack/react-table";
import { DataTable } from "@/components/ui/data-table";
import { Badge } from "@/components/ui/badge";
import { Checkbox } from "@/components/ui/checkbox";

export interface TaskRow {
    id: number;
    name: string;
    done: boolean;
}

interface ColumnProps {
    tasks: TaskRow[];
    loading?: boolean;
}

const columns: ColumnDef<TaskRow>[] = [
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
        id: "actions",
        header: "操作",
        cell: ({ row }) => (
            <Badge variant="outline">编辑</Badge> // 你可以换成下拉、按钮等
        ),
    },
];

export function Column({ tasks, loading }: ColumnProps) {
    if (loading) return <div className="p-4">加载中...</div>;

    return (
        <div className="container mx-auto py-4">
            <DataTable columns={columns} data={tasks} />
        </div>
    );
}
