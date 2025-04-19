import { useNavigate, useParams } from "react-router-dom";
import { z } from "zod";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { createTask } from "@/api/task";
import { toast } from "sonner";
import { Form, FormControl, FormField, FormItem, FormLabel } from "@/components/ui/form.tsx";
import { Input } from "@/components/ui/input.tsx";
import { Button } from "@/components/ui/button.tsx";
import { getAllTodoLists } from "@/api/todolist";


function AddTask() {
    const { category } = useParams<{ category: string }>(); /* 从url中获取 */
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate()
    const [todoListId, setTodoListId] = useState<number | null>(null);
    const formSchema = z.object({
        name: z.string({
            message: "请写任务名称"
        })//必写
        ,
        description: z.string().optional() //可选
    })
    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
    });

    useEffect(() => {
        if (!category) return;

        getAllTodoLists().then((res) => {
            const match = res.data?.find((list) => list.category === category);
            if (!match) {
                toast.error("未找到对应的任务列表");
                return;
            }
            setTodoListId(match.id);
        });
    }, [category]);  //通过category找id找对应的list


    function onSubmit(values: z.infer<typeof formSchema>) {
        if (!todoListId) {
            toast.error("任务列表加载失败");
            return;
        }
        setLoading(true)
        createTask({
            ...values, deadline: 0, status: false/* 等下写  */
        })
            .then((res) => {
                if (res.code === 200) {
                    toast.success("任务添加成功", {
                        id: "add-success",
                        description: "finish"
                    })
                    form.reset();
                    navigate(`/list/${category}`);
                }/*这些都要改，看后端报错*/
                if (res.code === 500) {
                    toast.error(
                        "任务添加失败", {
                            id: "add-error",
                            description: res.msg
                        }
                    )
                }
            })
            .finally(() => {
                setLoading(false)
            })
    }


    return (
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)}>
                <div>
                    <FormField
                        control={form.control}
                        name={'name'}
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>任务名称</FormLabel>
                                <FormControl>
                                    <Input
                                        placeholder={"..."}
                                        {...field}
                                    />
                                </FormControl>
                            </FormItem>
                        )} />
                    <FormField
                        control={form.control}
                        name={'description'}
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>任务描述</FormLabel>
                                <FormControl>
                                    <Input
                                        placeholder={"..."}
                                        {...field}
                                    />
                                </FormControl>
                            </FormItem>)} />
                    <Button type="submit" loading={loading} variant={"outline"}>Add</Button>
                </div>
            </form>
        </Form>
    )
}
export {AddTask}