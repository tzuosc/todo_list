import {
    UserRound,
    Lock,
    Check,
    TypeIcon,
    HashIcon,
    HouseIcon,
} from "lucide-react";
import { cn } from "@/utils";
import { Input } from "@/components/ui/input";
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
import { Button } from "@/components/ui/button";
import { useState } from "react";
import { register } from "@/api/users";
import { toast } from "sonner";
import { useNavigate } from "react-router";

function RegisterForm() {
    const navigate = useNavigate();

    const [loading, setLoading] = useState<boolean>(false);

    const formSchema = z
        .object({
            login: z
                .string({
                    message: "请输入用户名",
                })
                .regex(/^[a-z]/, "用户名必须以小写字母开头")
                .regex(/^[a-z0-9]*$/, "用户名只能包含小写字母和数字"),
            name: z.string({
                message: "请输入昵称",
            }),
            age: z.number({
                message: "请输入年龄",
            }),
            address: z.string({
                message: "请输入地址",
            }),
            password: z
                .string({
                    message: "请输入密码",
                })
                .min(6, "密码最少需要 6 个字符"),
            confirm_password: z.string({
                message: "请重新输入新密码",
            }),
        })
        .refine((data) => data.password === data.confirm_password, {
            message: "新密码与确认密码不一致",
            path: ["confirm_password"],
        });

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
    });

    function onSubmit(values: z.infer<typeof formSchema>) {
        setLoading(true);
        register({
            ...values,
        })
            .then((res) => {
                if (res.code === 200) {
                    toast.success("注册成功", {
                        id: "register-success",
                        description: "注册成功，请登录",
                    });
                    navigate("/account/login");
                }

                if (res.code === 400) {
                    toast.success("注册失败", {
                        id: "register-error",
                        description: res.msg,
                    });
                }

                if (res.code === 409) {
                    toast.success("注册失败", {
                        id: "register-error",
                        description: "用户名重复",
                    });
                }
            })
            .finally(() => {
                setLoading(false);
            });
    }

    return (
        <Form {...form}>
            <form
                onSubmit={form.handleSubmit(onSubmit)}
                autoComplete={"off"}
                className={cn(["flex", "flex-col", "h-full", "gap-8"])}
            >
                <div className={cn("space-y-3", "flex-1")}>
                    <div className={cn(["flex", "gap-3", "items-center"])}>
                        <FormField
                            control={form.control}
                            name={"login"}
                            render={({ field }) => (
                                <FormItem className={cn(["flex-1"])}>
                                    <FormLabel>用户名</FormLabel>
                                    <FormControl>
                                        <Input icon={UserRound} {...field} />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name={"name"}
                            render={({ field }) => (
                                <FormItem className={cn(["flex-1"])}>
                                    <FormLabel>昵称</FormLabel>
                                    <FormControl>
                                        <Input icon={TypeIcon} {...field} />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                    </div>
                    <FormField
                        control={form.control}
                        name={"age"}
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>年龄</FormLabel>
                                <FormControl>
                                    <Input
                                        {...field}
                                        type={"number"}
                                        icon={HashIcon}
                                        value={field.value || 0}
                                        onChange={(e) =>
                                            field.onChange(
                                                Number(e.target.value)
                                            )
                                        }
                                    />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name={"address"}
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>地址</FormLabel>
                                <FormControl>
                                    <Input icon={HouseIcon} {...field} />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name={"password"}
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>密码</FormLabel>
                                <FormControl>
                                    <Input
                                        icon={Lock}
                                        type={"password"}
                                        {...field}
                                    />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name={"confirm_password"}
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>确认密码</FormLabel>
                                <FormControl>
                                    <Input
                                        icon={Lock}
                                        type={"password"}
                                        {...field}
                                    />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                </div>
                <Button
                    variant={"solid"}
                    level={"info"}
                    type={"submit"}
                    size={"lg"}
                    className={cn(["w-full"])}
                    icon={Check}
                    loading={loading}
                >
                    注册
                </Button>
            </form>
        </Form>
    );
}

export { RegisterForm };
