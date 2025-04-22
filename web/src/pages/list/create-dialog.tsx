import { useNavigate, useParams } from "react-router-dom";
import { z } from "zod";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { createTask } from "@/api/task";
import { toast } from "sonner";
import { Form, FormControl, FormField, FormItem, FormLabel } from "@/components/ui/form.tsx";
import { Input } from "@/components/ui/input.tsx";
import { Button } from "@/components/ui/button.tsx";
import { Dialog, DialogContent, DialogTitle, DialogTrigger } from "@/components/ui/dialog.tsx";

function CreateDialog() {
    const { category } = useParams<{ category: string }>();
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();


    const formSchema = z.object({
        name: z.string({ message: "请写任务名称" }),
        description: z.string().optional()
    });
    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
    });


    const [open ,setOpen] = useState(false)    /* 控制弹窗 点击按钮后弹窗会关闭 */
    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild>
                <Button variant="outline">Add Task</Button>
            </DialogTrigger>
            <DialogContent>
                <DialogTitle>Add Task</DialogTitle>
                <Form {...form}>
                    <form onSubmit={form.handleSubmit((values) => {
                        if (!category) {
                            toast.error("缺少任务分类信息");
                            return;
                        }

                        setLoading(true);

                        createTask({
                            ...values,
                            deadline: 1830268799,
                            status: false,
                            category,
                        })
                            .then((res) => {
                                if (res.code === 200) {
                                    toast.success("任务添加成功", {
                                        id: "add-success",
                                        description: "finish",
                                    });

                                    setOpen(false); // 关闭弹窗
                                    navigate(0);
                                } else {
                                    toast.error("任务添加失败", {
                                        id: "add-error",
                                        description: res.msg,
                                    });
                                }
                            })
                            .finally(() => setLoading(false));
                    })}
                    >
                        <div>
                            <FormField
                                control={form.control}
                                name="name"
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel>任务名称</FormLabel>
                                        <FormControl>
                                            <Input {...field} />
                                        </FormControl>
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
                                            <Input {...field} />
                                        </FormControl>
                                    </FormItem>
                                )}
                            />
                            <Button type="submit" variant="outline" disabled={loading}>
                                {loading ? "添加中..." : "Add"}
                            </Button>
                        </div>
                    </form>
                </Form>
            </DialogContent>
        </Dialog>
    );
}

export { CreateDialog };
