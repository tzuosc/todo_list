import { Dialog, DialogContent, DialogTitle, DialogTrigger } from "@/components/ui/dialog.tsx";
import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useState } from "react";
import { createTodoList } from "@/api/todolist";
import { toast } from "sonner";
import { useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button.tsx";
import { Form, FormControl, FormField, FormItem, FormLabel } from "@/components/ui/form.tsx";
import { Input } from "@/components/ui/input.tsx";

function AddList(){
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const formSchema = z.object({
        category:z.string({message:"请填写任务类型"})
    })
    const form = useForm<z.infer<typeof formSchema>>({
        resolver:zodResolver(formSchema)
    })

    function onSubmit(values:z.infer<typeof formSchema>){
        setLoading(true);
        createTodoList(values.category)
            .then(
                (res) => {
                    if (res.code === 200) {
                        toast.success("列表添加成功", {
                            id: "add-success",
                            description: "finish"
                        });
                        navigate(`/list/${values.category}`);
                    } else {
                        toast.error("列表添加失败", {
                            id: "add-error",
                            description: res.msg
                        });
                    }
                })
            .finally(()=>{
                setLoading(false)
            })
    }
    return(
        <Dialog>
            <DialogTrigger asChild>
                <Button variant="outline">Add List</Button>
            </DialogTrigger>
            <DialogContent>
                <DialogTitle>Add List</DialogTitle>
                <Form {...form}>
                    <form onSubmit={form.handleSubmit(onSubmit)}>
                        <div>
                            <FormField
                                control={form.control}
                                name="category"
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel>任务名称</FormLabel>
                                        <FormControl>
                                            <Input placeholder="" {...field} />
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
    )
}
export {AddList}