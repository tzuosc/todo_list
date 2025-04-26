import { cn } from "@/utils";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card.tsx";
import { LoginForm } from "@/pages/account/login/login-form.tsx";
import { Button } from "@/components/ui/button.tsx";
import { Link } from "react-router-dom";
import { UserRoundPlus } from "lucide-react";

export default function(){
    return(
        <div
        className={cn("flex flex-1" ,
            "items-center justify-center")}>
            <Card className={cn("overflow-hidden")}>
                <CardHeader>
                    <CardTitle>
                        Login In
                        <CardDescription>
                            Sign in to continue browsing
                        </CardDescription>
                    </CardTitle>
                </CardHeader>
                <CardContent className={cn(["grid p-0 md:grid-cols-2"])}>
                    {/*
                        两列的 grid
                        <LoginForm /> 是第一列，右边的 div 是第二列
                    */}
                    <LoginForm/>

                    <div className={cn(["relative flex flex-col justify-between px-2 md:px-4"
                    ])}
                    >
                        <div className={cn(["flex justify-center items-center"])}>
                            <img
                                alt="logo"
                                decoding={"async"}
                                src={"https://avatars.githubusercontent.com/u/149759599?v=4"}
                                draggable={false}
                                className={cn(["w-50 h-50 rounded-full mx-auto"
                                ])}
                            />

                        </div>
                        <div className={cn(["flex justify-center"])}>
                            <Button
                                asChild
                                className={cn("w-full")}
                                size={"lg"}
                                variant={"tonal"}
                                icon={UserRoundPlus}
                            >
                                <Link to={"/account/register"}>还没有账号？注册！</Link>
                            </Button>
                        </div>

                    </div>

                </CardContent>
            </Card>
        </div>
        )
}