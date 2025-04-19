import { useNavigate } from "react-router-dom";
import { z } from "zod";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { createTask } from "@/api/task";
import { toast } from "sonner";
import { Form, FormControl, FormField, FormItem, FormLabel } from "@/components/ui/form.tsx";
import { Input } from "@/components/ui/input.tsx";
import { Button } from "@/components/ui/button.tsx";

function AddTask(){

    const navigate = useNavigate()

    const formSchema = z.object({
        name:z.string({
            message:"请写任务名称"
        }),
        description:z.string({
            message:"请写任务描述"
        })

    })
    const [loading, setLoading] = useState(false);

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
    });
    function onSubmit(values:z.infer<typeof formSchema>){
        setLoading(true)
        createTask({
            ...values, deadline: 0, status: false,description:"" /* 等下写  */
        })
            .then((res)=>{
                if(res.code===200){
                    toast.success("任务添加成功",{
                        id:"add-success",
                        description:"finish"
                    })
                    form.reset();
                    navigate("/"); //目前没想好更新
                }/*这些都要改，看后端报错*/
                if (res.code===500){
                    toast.error(
                        "任务添加失败",{
                            id:"add-error",
                            description:res.msg
                        }
                    )
                }
            })
            .finally(()=>{
                setLoading(false)
            })
    }
    return(
        <div>
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)}>
                    <div>
                        <FormField
                            control={form.control}
                            name={'name'}
                            render={({field})=>(
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
                            render={({field})=>(
                                <FormItem>
                                    <FormLabel>任务描述</FormLabel>
                                    <FormControl>
                                        <Input
                                            placeholder={"..."}
                                            {...field}
                                        />
                                    </FormControl>
                                </FormItem>
                            )} />
                        <Button type="submit" loading={loading} variant={"outline"}>Add</Button>
                    </div>
                </form>
            </Form>
        </div>
    )
}
export {AddTask}