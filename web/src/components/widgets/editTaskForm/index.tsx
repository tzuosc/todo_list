// components/widgets/edit-task-form.tsx

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
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Checkbox } from "@/components/ui/checkbox";
import { Button } from "@/components/ui/button";
import { Check } from "lucide-react";
import { cn } from "@/utils";
import { toast } from "sonner";
import { updateTask } from "@/api/task";
 // 你需要实现这个接口

const formSchema = z.object({
    name: z.string().min(1, "任务名称不能为空"),
    status: z.boolean().optional(),
});

export interface EditTaskFormProps {
    taskId: number;
    defaultValues: {
        name: string;
        status: boolean;
    };
    onSuccess?: () => void;
}

export function EditTaskForm({ taskId, defaultValues, onSuccess }: EditTaskFormProps) {
    const [loading, setLoading] = useState(false);

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues,
    });

    const onSubmit = async (values: z.infer<typeof formSchema>) => {
        setLoading(true);

    };

    return (
        <Form {...form}>
            <form
                onSubmit={form.handleSubmit(onSubmit)}
                className={cn("flex flex-col gap-4")}
            >
                <FormField
                    control={form.control}
                    name="name"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>任务名称</FormLabel>
                            <FormControl>
                                <Input placeholder="请输入任务名称" {...field} />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />

                <FormField
                    control={form.control}
                    name="status"
                    render={({ field }) => (
                        <FormItem className="flex items-center gap-2">
                            <FormControl>
                                <Checkbox
                                    checked={field.value}
                                    onCheckedChange={field.onChange}
                                />
                            </FormControl>
                            <FormLabel className="m-0">已完成</FormLabel>
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
