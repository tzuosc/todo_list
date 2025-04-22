import { Card } from "@/components/ui/card.tsx";
import { cn } from "@/utils";
import { TrashIcon } from "lucide-react";
import { Button } from "@/components/ui/button.tsx";
import { useSharedStore } from "@/storages/shared.ts";
import { useState } from "react";
import { deleteTask } from "@/api/task";
import { toast } from "sonner";

interface DeleteTaskDialogProps {
    taskId: number;
    taskName: string;
    onClose: () => void;
    onDelete?:() => void;
}
function DeleteTaskDialog({taskId,taskName,onClose,onDelete}:DeleteTaskDialogProps){
    const sharedStore = useSharedStore();
    const [loading, setLoading] = useState<boolean>(false);
    const handleDelete = () => {
        setLoading(true);
        deleteTask({ id: taskId })
            .then((res) => {
                if (res.code === 200) {
                    toast.success(`任务 ${taskName} 删除成功`);
                    sharedStore?.setRefresh?.(); // 刷新数据
                    onClose(); // 关闭弹窗
                } else {
                    toast.error("删除失败");
                }
            })
            .catch(() => {
                toast.error("请求出错，删除失败");
            })
            .finally(() => setLoading(false));
        onDelete?.()
    };
    return(
        <>
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
                    你确定要删除任务 {taskName} 吗？
                </p>
                <div className={cn(["flex", "justify-end"])}>
                    <Button
                        level={"error"}
                        variant={"tonal"}
                        size={"sm"}
                        onClick={()=>handleDelete()}
                    >
                        {loading ? "删除中..." : "确定"}
                    </Button>
                </div>
            </Card>

        </>
    )
}
export { DeleteTaskDialog }