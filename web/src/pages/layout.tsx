import { useNavigate } from "react-router-dom";
import globalRouter from "@/utils/global-router.ts";
import { cn } from "@/utils";
import { SidebarInset, SidebarProvider } from "@/components/ui/sidebar.tsx";
import AppSidebar from "@/components/widgets/sidebar";

export default function layout(){
    const navigate = useNavigate()
    globalRouter.navigate = navigate
    return(
        <div className={cn(["flex h-screen flex-col"])}>

            <div>
                <SidebarProvider>
                    <AppSidebar />
                    <SidebarInset>
                        <main
                            className={cn([
                                "flex flex-col " +
                                "min-h-full w-full " +
                                "p-4"
                            ])}
                        >
                        </main>
                    </SidebarInset>
                </SidebarProvider>
            </div>

        </div>
    )
}