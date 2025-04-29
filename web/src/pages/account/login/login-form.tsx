import { cn } from "@/utils";

import {toast} from "sonner";
import { Input } from "@/components/ui/input.tsx";

import { Button } from "@/components/ui/button.tsx";
import { useAuthStore } from "@/storages/auth.ts";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form.tsx";
import { login } from "@/api/user";

function LoginForm(){
    const authStore =useAuthStore()
    const navigate =useNavigate()
    const [loading,setLoading] = useState<boolean>(false)
    const formSchema =z.object({
        username:z.string({
            message:"请输入用户名",
        }),
        password:z.string({
            message:"请输入密码"
        })
    })
    const form = useForm<z.infer<typeof formSchema> >({
        resolver:zodResolver(formSchema)
    })

    function  onSubmit(values:z.infer<typeof formSchema>){
        setLoading(true)
        login({
            ...values
        })
            .then((res)=>{
            if(res.code===200){
                console.log(res.data)
                authStore.setUser(res.data)
            toast.success("success to login",{
                id:"login-success",
                description:`welcome back,${res.data?.username}`
                })
                navigate("/")
            }
            if (res.code===400){
                toast.error(
                    "fall to login",{
                        id:"login-error",
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
        <Form {...form}>
        <form className="px-6 md:px-8" onSubmit={form.handleSubmit(onSubmit)}>
            <div className="flex flex-col gap-6">
                <div className={cn("flex flex-col items-center text-center")}>
                    <h1 className="text-2xl font-bold">Welcome back</h1>
                    <p className="text-balance text-muted-foreground">Login to your Acme Inc account</p>
                </div>
                <FormField
                control={form.control}
                name={'username'}
                render={({field }) => (
                    <FormItem>
                        <FormLabel>用户名</FormLabel>
                        <FormControl>
                            <Input
                                placeholder={"Login Name"}
                                {...field}
                            />
                        </FormControl>
                        <FormMessage />
                    </FormItem>
                )}
                />
                <FormField
                    control={form.control}
                    name={"password"}
                    render={({field})=>(
                        <FormItem>
                            <FormLabel>密码</FormLabel>
                            <FormControl>
                                <Input
                                placeholder={"password"}
                                type={"password"}
                                {...field}
                                ></Input>
                            </FormControl>
                        </FormItem>
                    )}
                ></FormField>
                <Button
                    type="submit"
                    className={cn(["w-full",
                        "bg-green-600",
                        "text-white",
                        "text-lg",
                        "hover:bg-green-700",
                        "focus:ring-2"," focus:ring-green-500 ","focus:ring-offset-2"
                    ])}
                    loading={loading}
                    variant={"solid"}
                    level={"success"}>
                    登录
                </Button>


            </div>
        </form>
        </Form>
    )
}
export {LoginForm}