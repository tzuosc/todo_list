import { useState } from "react";
import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "@/components/ui/form.tsx";
import { Input } from "@/components/ui/input.tsx";
import { Textarea } from "@/components/ui/textarea.tsx";
import { Button } from "@/components/ui/button.tsx";
import { CalendarIcon, Check } from "lucide-react";
import { cn } from "@/utils";
import { toast } from "sonner";
import { updateTask } from "@/api/task";
import { format } from "date-fns";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover.tsx";
import { Calendar } from "@/components/ui/calendar.tsx";
import { zhCN } from "date-fns/locale";

const formSchema = z.object({
    name: z.string().min(1, "任务名称不能为空"),
    description: z.string().optional(),
    deadline: z.date().optional(),
});

export interface EditTaskFormProps {
    taskId: number;
    category: string;
    deadline?: number; // timestamp
    defaultValues: {
        name: string;
        description?: string;
    };
    onSuccess?: () => void;
}

export function EditTaskForm({ taskId, category, deadline, defaultValues, onSuccess }: EditTaskFormProps) {
    const [loading, setLoading] = useState(false);

    // 将 defaultValues 中的 deadline 设置为 Date 对象
    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            name: defaultValues.name,
            description: defaultValues.description ?? "",
            deadline: deadline ? new Date(deadline) : undefined,  // 恢复此行
        },
    });

    const onSubmit = async (values: z.infer<typeof formSchema>) => {
        setLoading(true);
        try {
            const res = await updateTask(taskId, {
                id: taskId,
                name: values.name,
                description: values.description,
                // 将表单中的 deadline 转换成时间戳
                deadline, // 使用 getTime() 将 Date 转换为 timestamp
                category,
            });

            if (res.code === 200) {
                toast.success("任务更新成功");
                onSuccess?.();  // 调用 onSuccess 来更新父组件
            } else {
                toast.error("更新失败", { description: res.msg });
            }
        } catch (e) {
            toast.error("请求出错");
        } finally {
            setLoading(false);
        }
    };

    return (
        <Form {...form}>
            <form
                onSubmit={form.handleSubmit(onSubmit)}
                className="flex flex-col gap-4"
            >
                <FormField
                    control={form.control}
                    name="name"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>任务名称</FormLabel>
                            <FormControl>
                                <Input placeholder="输入任务名" {...field} />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />

                <FormField
                    control={form.control}
                    name="description"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>任务描述</FormLabel>
                            <FormControl>
                                <Textarea placeholder="请输入描述（可选）" {...field} />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />

                <FormField
                    control={form.control}
                    name="deadline"
                    render={({ field }) => (
                        <FormItem className="flex flex-col">
                            <FormLabel>截止日期</FormLabel>
                            <Popover>
                                <PopoverTrigger asChild>
                                    <Button
                                        variant="outline"
                                        className={cn(
                                            "justify-start text-left font-normal",
                                            !field.value && "text-muted-foreground"
                                        )}
                                    >
                                        <CalendarIcon className="mr-2 h-4 w-4" />
                                        {field.value ? format(field.value, "yyyy-MM-dd") : "选择日期"}
                                    </Button>
                                </PopoverTrigger>
                                <PopoverContent align="start" className="w-auto p-0">
                                    <Calendar
                                        mode="single"
                                        selected={field.value}
                                        onSelect={field.onChange}
                                        locale={zhCN}
                                        initialFocus
                                    />
                                </PopoverContent>
                            </Popover>
                            <FormMessage />
                        </FormItem>
                    )}
                />

                <Button
                    type="submit"
                    icon={Check}
                    loading={loading}
                    className="w-full"
                >
                    保存修改
                </Button>
            </form>
        </Form>
    );
}
