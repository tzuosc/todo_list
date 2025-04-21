
import { useState } from "react";
import { Button } from "@/components/ui/button.tsx";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog.tsx";
import { EditTaskForm } from "@/components/widgets/editTask/editTaskForm";
import { getTask } from "@/api/task";
import { Loader2 } from "lucide-react";

interface EditTaskButtonProps {
    taskId: number;
    category: string;
    deadline:number
    onUpdated?:() => void;
}

function EditTaskButton({ taskId, category ,deadline,onUpdated }: EditTaskButtonProps) {
    const [open, setOpen] = useState(false);
    const [loading, setLoading] = useState(true);
    const [task, setTask] = useState<any>(null);
    const fetchTask = async () => {
        setLoading(true);
        const res = await getTask(taskId);
        if (res.code === 200) {
            setTask(res.data)
        }
        setLoading(false);
    };

    const handleOpenChange = (value: boolean) => {
        setOpen(value)
        if (value) {
            fetchTask();
        }
    };

    return (
        <Dialog open={open} onOpenChange={handleOpenChange}>
            <DialogTrigger asChild>
                <Button variant="outline" size="sm">
                    编辑
                </Button>
            </DialogTrigger>
            <DialogContent className="max-w-md">
                <DialogHeader>
                    <DialogTitle>编辑任务</DialogTitle>
                </DialogHeader>
                {loading ? (
                    <div className="flex items-center justify-center py-10">
                        <Loader2 className="animate-spin w-6 h-6" />
                    </div>
                ) : (
                    <EditTaskForm
                        taskId={taskId}
                        category={category}
                        deadline={deadline}
                        defaultValues={{
                            name: task.name,
                            description: task.description,
                        }}
                        onSuccess={() => {
                            setOpen(false)
                            onUpdated?.()
                        }}
                    />
                )}
            </DialogContent>
        </Dialog>
    );
}
export {EditTaskButton}
