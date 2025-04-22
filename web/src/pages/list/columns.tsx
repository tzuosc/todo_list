import { useMemo, useState } from "react";
import { ColumnDef } from "@tanstack/react-table";
import { DataTable } from "@/components/ui/data-table";
import { Checkbox } from "@/components/ui/checkbox";
import { EditTaskButton } from "@/components/widgets/editTask/editTaskButton/editTaskButton.tsx";
import { deleteTask, updateTask } from "@/api/task";
import { toast } from "sonner";
import { Switch } from "@/components/ui/switch.tsx";
import { ArrowDown, ArrowUp, ArrowUpDown, EditIcon, TrashIcon } from "lucide-react";
import { cn } from "@/utils";
import { Button } from "@/components/ui/button.tsx";
import { useSharedStore } from "@/storages/shared.ts";
import { Dialog, DialogContent } from "@/components/ui/dialog.tsx";
import { UpdateTaskDialog } from "@/pages/lists/list_id/updateTaskDialog.tsx";
import { Card } from "@/components/ui/card.tsx";

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


export function Columns({ tasks, loading,onUpdated }: ColumnProps) {
    // 表格列定义
    const [createDialogOpen, setCreateDialogOpen] = useState<boolean>(false);
    const columns: ColumnDef<TaskRow>[] = useMemo(() => [
        {
            accessorKey:"status",
            id:"status",
            header:"状态",
            cell:({row})=>{
                const  status = row.getValue<boolean>("status")
                const name =row.getValue<string>("name")
                const id = row.getValue<number>("id")
                const [checked,setChecked] = useState(status)
                function handleTaskChange(){
                    const newValue = !checked;
                    setChecked(newValue);

                    /* 调用之前封装好的跟新Task接口*/
                    updateTask({
                        id,
                        status: newValue,
                    }).then((res) => {
                        if (res.code === 200) {
                            toast.success(
                                `更新任务 ${name} 的完成度: ${newValue ? "完成" : "未完成"}`,
                                {
                                    id: "is_finished",
                                }
                            );
                        }
                    })
                }
                return(
                    <Switch
                        checked={checked}
                        onCheckedChange={handleTaskChange}
                        aria-label={"开关"}
                    />
                )
            }
        },
        {
            accessorKey: "name",
            header: "任务名称",
            cell: ({ row }) => {
                const name = row.getValue("name") as string
                return name || "-"
            }
        },
        {
            accessorKey:"deadline",
            id:"deadline",
            header:({column})=>{
                const Icon = useMemo(() => {
                    switch (column.getIsSorted()) {
                        case "asc":
                            return ArrowUp;
                        case "desc":
                            return ArrowDown;
                        case false:
                        default:
                            return ArrowUpDown;
                    }
                },[column.getIsSorted()])
                return(
                    <div className={cn(["flex", "gap-1", "items-center"])}>
                        截止于
                        <Button
                            icon={Icon}
                            square
                            size={"sm"}
                            onClick={()=>column.toggleSorting()}
                        />
                    </div>
                )
            },
            cell:({row})=>{
                return new Date(
                    row.getValue<number>("deadline")*1000
                ).toLocaleDateString()

            }
        },
        {
            id:"action",
            header:()=><div className={cn(["justify-self-center"])}>操作</div>,
            cell:({row})=>{
                const id = row.getValue<number>("id")
                const name = row.getValue<string>("name")
                const sharedStore = useSharedStore()
                const [deleteDialogOpen, setDeleteDialogOpen] =
                    useState<boolean>(false);

                /* 调用删除接口 */
                function  handleDelete(){
                    deleteTask({
                        id
                    }).then((res)=>{
                        if(res.code===200){
                            toast.success(`任务 ${name} 删除成功`);
                            setDeleteDialogOpen(false);
                        }
                    }).finally(() => {
                        sharedStore?.setRefresh();
                    })
                }

                return(
                    <div
                        className={cn([
                            "flex",
                            "items-center",
                            "justify-center",
                            "gap-2",
                        ])}
                    >
                        <Button
                            variant={"ghost"}
                            size={"sm"}
                            square
                            icon={EditIcon}
                            onClick={()=>setCreateDialogOpen(true)}
                        />
                        <Dialog
                            open={createDialogOpen}
                            onOpenChange={setCreateDialogOpen}
                        >
                            <DialogContent>
                                <UpdateTaskDialog taskId={row.original.id}></UpdateTaskDialog>
                            </DialogContent>
                        </Dialog>



                        <Button
                            level={"error"}
                            variant={"ghost"}
                            size={"sm"}
                            square
                            icon={TrashIcon}
                            onClick={() => setDeleteDialogOpen(true)}
                        />
                        <Dialog
                            open={deleteDialogOpen}
                            onOpenChange={setDeleteDialogOpen}
                        >
                            <DialogContent>
                                <Card
                                    className={cn([
                                        "flex",
                                        "flex-col",
                                        "p-5",
                                        "min-h-32",
                                        "w-72",
                                        "gap-5",
                                    ])}
                                >
                                    <div
                                        className={cn([
                                            "flex",
                                            "gap-2",
                                            "items-center",
                                            "text-sm",
                                        ])}
                                    >
                                        <TrashIcon className={cn(["size-4"])} />
                                        删除任务
                                    </div>
                                    <p className={cn(["text-sm"])}>
                                        你确定要删除任务 {name} 吗？
                                    </p>
                                    <div className={cn(["flex", "justify-end"])}>
                                        <Button
                                            level={"error"}
                                            variant={"tonal"}
                                            size={"sm"}
                                            onClick={handleDelete}
                                        >
                                            确定
                                        </Button>
                                    </div>
                                </Card>
                            </DialogContent>
                        </Dialog>
                    </div>
                )
            }
        }
    ], [onUpdated]);

    if (loading) return <div className="p-4">加载中...</div>;

    return (
        <div className="container mx-auto py-4">
            <DataTable columns={columns} data={tasks} />
        </div>
    );
}
