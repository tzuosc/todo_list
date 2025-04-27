import { Outlet, useNavigate } from "react-router-dom";
import globalRouter from "@/utils/global-router.ts";
import { cn } from "@/utils";
import { SidebarInset, SidebarProvider } from "@/components/ui/sidebar.tsx";
import AppSidebar from "@/components/widgets/sidebar";
import { useAuthStore } from "@/storages/auth.ts";
import { useEffect } from "react";

export default function layout(){
    /*const navigate = useNavigate()
    const user = useAuthStore((state)=>state.user)
    useEffect(() => {
        if (!user) {
            navigate("/account/login", { replace: true });
        }
    }, [user, navigate]);

    globalRouter.navigate = navigate;
    globalRouter.navigate = navigate*/
    return(
        <div className={cn(["flex h-screen flex-col"])}>

            <div>
                <SidebarProvider>
                    <AppSidebar />
                    <SidebarInset>
                        <main
                            className={cn([

                                "flex", "flex-col", /*"min-h-[calc(100vh-64px)]",*/"p-4","justify-center","h-full"
                            ])}
                        >
                            <Outlet/>

                        </main>
                    </SidebarInset>
                </SidebarProvider>
            </div>
        </div>
    )
}