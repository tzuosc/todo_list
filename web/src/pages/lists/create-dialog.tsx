import { useSharedStore } from "@/storages/shared.ts";
import { useState } from "react";
import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { createTask } from "@/api/task";
import { toast } from "sonner";
import { useParams } from "react-router-dom";
import { Card } from "@/components/ui/card.tsx";
import { cn } from "@/utils";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form.tsx";
import { Input } from "@/components/ui/input.tsx";
import { CalendarIcon, CheckIcon,  FolderDot, TypeIcon } from "lucide-react";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover.tsx";
import { Button } from "@/components/ui/button.tsx";
import { format } from "date-fns";
import { Calendar } from "@/components/ui/calendar.tsx";
import { zhCN } from "date-fns/locale";

interface CreateDialogProps {
    onClose:() => void
}

function CreateDialog(props:CreateDialogProps){
    const {category} =useParams< { category:string }>()
    const {onClose} = props
    const sharedStore = useSharedStore()
    const [loading, setLoading] = useState<boolean>(false);
    const formSchema = z.object({
        name: z.string({
            message: "请写任务名称"
        }),
        description: z.string({
            message:"请写详细描述"
        }).optional(),
        deadline: z.string({
            message:"请写截止时间"
        }).optional()
    })

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: { },/* */
    });

    function onSubmit(values: z.infer<typeof formSchema>) {

        setLoading(true);
        createTask({
            ...values,
            status: false,
            deadline:1830268799,
            category:category!
        })
            .then((res) => {
                if (res.code === 200) {
                    toast.success(`任务 ${res?.data?.name} 创建成功`);
                    onClose();
                }
            })
            .finally(() => {
                sharedStore.setRefresh();
                setLoading(false);
            });
    }
    return(
        <Card
            className={cn([
                "flex", "gap-3", "items-center", "text-md"
            ])}
        >
            <h3 className={cn(["flex", "gap-3", "items-center", "text-md"])}>
                <FolderDot className={cn(["size-4"])} />
                创建任务
            </h3>
            <Form {...form}>
                <form
                    onSubmit={form.handleSubmit(onSubmit)}
                    autoComplete={"off"}
                    className={cn(["flex", "flex-col", "flex-1", "gap-5"])}
                >
                    <FormField
                        control={form.control}
                        name="name"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>任务名称</FormLabel>
                                <FormControl>
                                    <Input
                                        {...field}
                                        icon={TypeIcon}
                                        size={"sm"}
                                        placeholder={"任务名称"}
                                        value={field.value || ""}
                                        onChange={field.onChange}
                                    />
                                </FormControl>
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name={"deadline"}
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>截止时间</FormLabel>
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
                                            selected={field.value? new Date(field.value) : undefined}
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
                    type={"submit"}
                    icon={CheckIcon}
                    level={"success"}
                    loading={loading}
                    >
                        保存修改
                    </Button>
                </form>

            </Form>

        </Card>
    )
}
export {CreateDialog}