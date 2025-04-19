import { Dialog, DialogContent, DialogTrigger } from "@/components/ui/dialog.tsx";
import { Button } from "@/components/ui/button.tsx";
import { AddTask } from "@/components/widgets/addTask";
import { cn } from "@/utils";
import { useParams } from "react-router-dom";


export default function Home() {
    /* 获取当前list的url里面的id */
    return (

        <div className={cn(["flex","flex-1","items-center", "justify-center"])}>
            <Dialog>
                <DialogTrigger asChild>
                    <Button variant={"outline"}>Add Task</Button>
                </DialogTrigger>
                <DialogContent>
                    <AddTask  />
                </DialogContent>
            </Dialog>
        </div>
    );
}