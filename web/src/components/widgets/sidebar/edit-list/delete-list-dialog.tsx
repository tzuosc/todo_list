import { useState } from "react";
import { useSharedStore } from "@/storages/shared.ts";

import { deleteByListId } from "@/api/todolist";
/*import { Card } from "@/components/ui/card.tsx";*/
import { cn } from "@/utils";
import { TrashIcon } from "lucide-react";
import { Button } from "@/components/ui/button.tsx";

function DeleteListDialog(
    {listId, category,onClose}:{
        listId:number;
        category:string;
        onClose:() => void}
)
{
    const [loading, setLoading] = useState<boolean>(false);
    const sharedStore = useSharedStore();


    const handleDelete=()=>{
        if (!listId) return
        setLoading(true)
        deleteByListId(
            listId
        )
            .then((res)=>{
                    if (res.code===200){
                        console.log(`删除成功`) /*myself*/
                        sharedStore.setRefresh()
                        onClose() //关闭弹窗
                    }else {
                        console.log(("删除失败"))
                    }
                }).finally(()=>setLoading(false))
    }
    return(
        <>
            <form
                className={cn([
                    "p-0",
                    "flex",
                    "flex-col",
                    "p-5","m-1",
                    "min-h-32",
                    "w-full",
                    "gap-10",
                ])}>
                <div
                    className={cn([
                        "flex",
                        "gap-2",
                        "items-center",
                        "text-2xl",
                        "font-bold"
                    ])}
                >
                    <TrashIcon className={cn(["size-8"])} />
                    删除列表
                </div>
                {/*"opacity-50"*/}
                <p className={cn(["text-lg","font-medium","text-center"])}>
                    你确定要删除列表 {category} 吗？
                </p>
                <div className={cn(["flex", "justify-end"])}>
                    <Button
                        level={"error"}
                        variant={"tonal"}
                        size={"sm"}
                        onClick={()=>handleDelete()}
                    >
                        {loading ? "删除中..." : "确定"}
                    </Button>
                </div>
            </form>

        </>
    )
}
export {DeleteListDialog}