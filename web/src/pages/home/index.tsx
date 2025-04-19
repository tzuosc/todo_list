import { Dialog, DialogContent, DialogTrigger } from "@/components/ui/dialog.tsx";
import { Button } from "@/components/ui/button.tsx";
import { AddTask } from "@/pages/home/task/addtask.tsx";
import { cn } from "@/utils";
import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { GetTaskResponse } from "@/api/task";

export default function Home() {
    const {id} = useParams() /* 获取当前list的url里面的id */
    const todoListId =Number(id) /* 转为number属性 */
    const [task,setTask] = useState<GetTaskResponse>() /* 查找所以 task 的api*/
    /*const loadTask = async  () =>{
        const res = await fetch
    }*/

    useEffect(() => {

    }, [todoListId]);
    return (

        <div className={cn(["flex","flex-1","items-center", "justify-center"])}>
            <Dialog>
                <DialogTrigger asChild>
                    <Button variant={"outline"}>Add Task</Button>
                </DialogTrigger>
                <DialogContent>
                    <AddTask />
                </DialogContent>
            </Dialog>
        </div>
    );
}