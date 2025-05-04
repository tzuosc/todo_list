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
import { cn } from "@/utils";
import { Plus } from "lucide-react";

function AddList({onAddSuccess}:{onAddSuccess:()=>void}){
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const formSchema = z.object({
        category:z.string({message:"请填写列表类型"})
    })
    const form = useForm<z.infer<typeof formSchema>>({
        resolver:zodResolver(formSchema)
    })
    const [open,setOpen] =useState(false)
    function onSubmit(values:z.infer<typeof formSchema>){
        setLoading(true);
        createTodoList(values.category)
            .then(
                (res) => {
                    if (res.code === 200) {
                        toast.success("列表添加成功", {
                            id: "add-success",
                            description: "finish"
                        })
                        navigate(`/list/${values.category}`)
                        setOpen(false)
                        onAddSuccess()
                    } else {
                        toast.error("列表添加失败", {
                            id: "add-error",
                            description: res.msg
                        })
                    }
                })
            .finally(()=>{
                setLoading(false)
            })
    }

    return(
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild className={cn([])}>
                {/*"bg-blue-500"*/}
                <Button
                    variant="ghost"
                    className={cn(["w-full","flex","justify-start","gap-3"])}
                >
                    <Plus/>
                    新建列表
                </Button>
            </DialogTrigger>
            <DialogContent>
                <DialogTitle>添加列表</DialogTitle>
                <Form {...form}>
                    <form onSubmit={form.handleSubmit(onSubmit)}>
                        <div>
                            <FormField
                                control={form.control}
                                name="category"
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel>列表名称</FormLabel>
                                        <FormControl>
                                            <Input placeholder=" " {...field} />
                                        </FormControl>
                                    </FormItem>
                                )}
                            />
                            <div className={cn(["mt-4","flex","justify-end"])}>
                                <Button type="submit" variant="outline" disabled={loading}className={cn(["w-1/4"])} >
                                    {loading ? "添加中..." : "添加"}
                                </Button>
                            </div>
                        </div>
                    </form>
                </Form>
            </DialogContent>
        </Dialog>
    )
}
export {AddList}